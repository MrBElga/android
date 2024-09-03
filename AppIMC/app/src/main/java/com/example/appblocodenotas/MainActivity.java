package com.example.appblocodenotas;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button btFechar;
    private EditText etNote;

    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        btFechar = findViewById(R.id.btFechar);
        etNote = findViewById(R.id.etNote);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // salvando arquivo ao clicar no botão salvar
        btFechar.setOnClickListener(e -> {salvarDados();});
    }

    @Override
    protected void onStart() {
        recuperarDados();
        super.onStart();

    }


    public File getFilePublic(String filename)
    {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), filename);
        return file;
    }



    private void salvarDados()  {
        try {
            File arq = getFilePublic("notes.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(arq,false);
            fileOutputStream.write(etNote.getText().toString().getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            Toast.makeText(this, "erro: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private  void recuperarDados(){
        try {
            File arq = getFilePublic("notes.txt");
            FileInputStream fileInputStream = new FileInputStream(arq);
            byte[] bytes = new byte[(int)arq.length()];
            fileInputStream.read(bytes);
            etNote.setText(new String(bytes));
        } catch (IOException e) {
            Toast.makeText(this, "erro: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
}