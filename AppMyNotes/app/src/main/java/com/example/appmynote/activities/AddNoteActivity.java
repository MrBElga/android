package com.example.appmynote.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appmynote.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;

public class AddNoteActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PHOTO = 10000;
    private ImageView imageView;
    private EditText editTextTitle;
    private RadioGroup radioGroupPriority;
    private TextInputLayout textConteudo;
    private Bitmap bitmap=null;
    private String caminhoImagem = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextTitle = findViewById(R.id.editTextTitle);
        radioGroupPriority = findViewById(R.id.radioGroupPriority);
        Button buttonSave = findViewById(R.id.buttonSave);
        textConteudo = findViewById(R.id.textConteudo);
        imageView = findViewById(R.id.imageView);

        FloatingActionButton flutuante = findViewById(R.id.floatingActionButton);
        flutuante.setOnClickListener(e-> tirarFoto());
        buttonSave.setOnClickListener(v -> {
            String titulo = editTextTitle.getText().toString().trim();
            String conteudo = textConteudo.getEditText() != null ? textConteudo.getEditText().getText().toString().trim() : "";


            if (titulo.isEmpty()) {
                Toast.makeText(AddNoteActivity.this, "O título não pode ser vazio", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedPriorityId = radioGroupPriority.getCheckedRadioButtonId();
            if (selectedPriorityId == -1) {
                Toast.makeText(AddNoteActivity.this, "Por favor, selecione uma prioridade", Toast.LENGTH_SHORT).show();
                return;
            }

            if (conteudo.isEmpty()) {
                Toast.makeText(AddNoteActivity.this, "O conteúdo não pode ser vazio", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedPriority = findViewById(selectedPriorityId);
            String prioridade = selectedPriority.getText().toString();

            // insere no banco de dados <<<<<<<<<<<
            try {
                SQLiteDatabase bancoDados = openOrCreateDatabase("notasapp", MODE_PRIVATE, null);
                String sql = "INSERT INTO notas (titulo, prioridade, conteudo, caminho) VALUES (?, ?, ?, ?)";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);
                stmt.bindString(1, titulo);
                stmt.bindString(2, prioridade);
                stmt.bindString(3, conteudo);
                stmt.bindString(4, caminhoImagem);
                stmt.executeInsert();
                stmt.close();
                bancoDados.close();

                Toast.makeText(AddNoteActivity.this, "Nota salva com sucesso!", Toast.LENGTH_SHORT).show();


                Intent returnIntent = new Intent();
                returnIntent.putExtra("titulo", titulo);
                returnIntent.putExtra("prioridade", prioridade);
                returnIntent.putExtra("conteudo", conteudo);
                returnIntent.putExtra("caminho", caminhoImagem);
                setResult(RESULT_OK, returnIntent);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(AddNoteActivity.this, "Erro ao salvar a nota", Toast.LENGTH_SHORT).show();
            }


            // retorna para  a mainActivity
            finish();
        });

    }

    private void salvarFoto() {
        File file = getFilePublic("foto" + System.currentTimeMillis() + ".jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fos);
            fos.flush();
            fos.close();
            caminhoImagem = file.getAbsolutePath();
            Intent novaIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file));
            sendBroadcast(novaIntent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Erro " + e, Toast.LENGTH_LONG).show();
        }
    }


    public File getFilePublic(String filename)
    {

        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), filename);
    }

    private void tirarFoto()
    {	 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {       super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_PHOTO)
        {   if (resultCode == Activity.RESULT_OK)
            {   Bundle extras = data.getExtras();
                assert extras != null;
                bitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(bitmap);
                salvarFoto();
            }
            else  // Cancelou a foto
            {
                imageView.setImageResource(R.drawable.ic_camera);
            }
        }
    }
}
