package com.example.appbank;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbank.databinding.ActivitySaldoBinding;

public class SaldoActivity extends AppCompatActivity {
    private ActivitySaldoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySaldoBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());


        getSupportActionBar().hide();
        binding.toolbarSaldo.setNavigationOnClickListener(view -> finish());
    }
}