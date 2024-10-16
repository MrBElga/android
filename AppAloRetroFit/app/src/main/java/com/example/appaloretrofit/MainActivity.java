package com.example.appaloretrofit;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import com.example.appaloretrofit.Endereco;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;


import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button btPesquisar;
    private TextView tvRes;
    private TextInputLayout txCep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btPesquisar = findViewById(R.id.button);
        tvRes = findViewById(R.id.tvRes);
        txCep = findViewById(R.id.txCep);
        btPesquisar.setOnClickListener(e->{pesquisarCep();});


    }
    private void pesquisarCep() {
        String cep = Objects.requireNonNull(txCep.getEditText()).getText().toString();
        if (cep.isEmpty()) {
            tvRes.setText("Por favor, insira um CEP válido.");
            return;
        }
        Call<Endereco> call = new RetrofitConfig().getCEPService().buscarCep(cep);
        call.enqueue(new Callback<Endereco>() {
            @Override
            public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Endereco endereco = response.body();
                    tvRes.setText("Bairro: " + endereco.getBairro() + " Cidade: " + endereco.getLocalidade());
                } else {
                    tvRes.setText("Erro ao buscar o CEP");
                }
            }

            @Override
            public void onFailure(Call<Endereco> call, Throwable t) {
                tvRes.setText("Falha na conexão: " + t.getMessage());
            }
        });
    }

}