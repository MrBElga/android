package com.example.appemprestimo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appemprestimo.entidades.Parcela;
import com.example.appemprestimo.util.Price;

import java.util.ArrayList;
import java.util.List;

public class PlanilhaActivity extends AppCompatActivity {
    private ImageButton btVoltar;
    private TextView tvValor, tvTaxa;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_planilha);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btVoltar = findViewById(R.id.btVoltar);
        tvValor = findViewById(R.id.tvValor);
        tvTaxa = findViewById(R.id.tvTaxa);
        listView = findViewById(R.id.listView);
        btVoltar.setOnClickListener(e -> finish());
        Intent intent = getIntent();
        double valor = Double.parseDouble(intent.getStringExtra("valor"));
        tvValor.setText(String.format("%.2f", valor));
        double taxa = Double.parseDouble(intent.getStringExtra("taxa"));
        tvTaxa.setText(String.format("%.2f", taxa));
        int meses = intent.getIntExtra("meses", 1);
        // aqui adicionei o header antes da lista
        View headerView = getLayoutInflater().inflate(R.layout.header_listview, null);
        listView.addHeaderView(headerView);
        // gera a lista de parcelas
        List<Parcela> list = gerarPlanilha(valor, taxa, meses);
        // adpter surgindo depois do cabecalho
        listView.setAdapter(new EmprestimoAdapter(this, R.layout.item_listview, list));

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Parcela parcela = list.get(i - 1); // Ajuste para ignorar o header
            Toast.makeText(PlanilhaActivity.this,
                    "Falta R$ " + String.format("%.2f", parcela.getSdDevedor()) + " para quitar emprestimo",
                    Toast.LENGTH_LONG).show();
        });
    }

    private List<Parcela> gerarPlanilha(double valor, double taxa, int meses) {
        List<Parcela> list = new ArrayList<>();
        double valorParcela = Price.calcParcela(valor, taxa, meses);
        for (int i = 1; i <= meses; i++) {
            list.add(new Parcela(i, valorParcela, valor * (taxa / 100),
                    valorParcela - valor * (taxa / 100),
                    valor - (valorParcela - valor * (taxa / 100))));
            valor -= (valorParcela - valor * (taxa / 100));
        }
        return list;
    }
}