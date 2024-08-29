package com.example.appemprestimo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appemprestimo.util.Price;
//par ao cabecalho criar um xml para cabecalho e no primeiro item ao inves de ter
// um item ter o cabecalho e logo apos ter os itens
// parcela do meio deve estar pintada de outra cor
public class MainActivity extends AppCompatActivity {
    private EditText etValor;
    private SeekBar sbPrazo, sbJuros;
    private TextView tvPrazo, tvJuros, tvParcela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //vinculando os componentes
        etValor = findViewById(R.id.etValor);
        etValor.setText("1000.00");
        sbPrazo = findViewById(R.id.sbPrazo);
        tvPrazo = findViewById(R.id.tvPrazo);
        sbJuros = findViewById(R.id.sbJuros);
        tvJuros = findViewById(R.id.tvJuros);
        tvParcela = findViewById(R.id.tvParcela);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //criando os eventos
        sbPrazo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvPrazo.setText("" + i);
                ajustarParcela();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbJuros.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvJuros.setText(String.format("%.2f", i / 100.0));
                ajustarParcela();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        etValor.setOnKeyListener((view, i, keyEvent) -> {
            ajustarParcela();
            return false;
        });
        etValor.setOnClickListener(view -> etValor.selectAll());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //recuperando dados do sharedpreferences
        SharedPreferences sharedPreferences = getSharedPreferences("dados.cfg", MODE_PRIVATE);
        double valor = Double.parseDouble(sharedPreferences.getString("valor", "1000"));
        int prazo = Integer.parseInt(sharedPreferences.getString("prazo", "12"));
        int juros = Integer.parseInt(sharedPreferences.getString("juros", "250"));
        etValor.setText("" + valor);
        sbPrazo.setProgress(prazo);
        sbJuros.setProgress(juros);
        ajustarParcela();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //armazenando os dados informados pelo usuário
        SharedPreferences sharedPreferences = getSharedPreferences("dados.cfg", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("valor", etValor.getText().toString());
        editor.putString("juros", "" + sbJuros.getProgress());
        editor.putString("prazo", "" + sbPrazo.getProgress());
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itParcelas) {
            //Toast.makeText(this,"Opção ainda não disponível",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, PlanilhaActivity.class);
            intent.putExtra("valor", etValor.getText().toString());
            intent.putExtra("taxa", tvJuros.getText().toString());
            intent.putExtra("meses", sbPrazo.getProgress());
            startActivity(intent);
        }
        if (item.getItemId() == R.id.itFechar) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void ajustarParcela() {
        if (!etValor.getText().toString().isEmpty()) {
            double parcela = Price.calcParcela(Double.parseDouble(etValor.getText().toString()),
                    sbJuros.getProgress() / 100.0, sbPrazo.getProgress());
            tvParcela.setText(String.format("%.2f", parcela));
        }
    }

}