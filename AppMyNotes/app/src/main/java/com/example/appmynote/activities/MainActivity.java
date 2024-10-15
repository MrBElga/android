package com.example.appmynote.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appmynote.R;
import com.example.appmynote.adapters.NoteAdapter;
import com.example.appmynote.entidades.Note;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    private ListView listView;
    private NoteAdapter noteAdapter;
    private ArrayList<Note> notes;
    private ActivityResultLauncher<Intent> addNoteLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inserirDadosIniciais();
        listView = findViewById(R.id.listViewNotes);
        notes = new ArrayList<>();
        //inserirDados();
        // arrumei aq estava duplicado o isnerir do addnoteactivity
        addNoteLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        result.getData().getStringExtra("titulo");
                        result.getData().getStringExtra("prioridade");
                        result.getData().getStringExtra("conteudo");
                        result.getData().getStringExtra("caminho");
                        // atualizando a listview por aq
                        listarDados();

                        Toast.makeText(MainActivity.this, "Nota adicionada!", Toast.LENGTH_SHORT).show();
                    }
                }
        );


        criarBancoDados();
        listarDados();
    }

    public void criarBancoDados() {
        try {
            bancoDados = openOrCreateDatabase("notasapp", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS notas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "titulo VARCHAR, " +
                    "prioridade VARCHAR, " +
                    "conteudo VARCHAR," +
                    "caminho VARCHAR)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void listarDados() {
       // limpa a lista antes de adicionar novas notas
        notes.clear();
        try {
            bancoDados = openOrCreateDatabase("notasapp", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id, titulo, prioridade, conteudo, caminho FROM notas", null);

            if (cursor.moveToFirst()) {
                do {
                    String titulo = cursor.getString(1);
                    String prioridade = cursor.getString(2);
                    String conteudo = cursor.getString(3);
                    String caminho = cursor.getString(4);
                    notes.add(new Note(titulo, prioridade, conteudo, caminho));
                } while (cursor.moveToNext());
            }
            cursor.close();
            bancoDados.close();

            // atualiza adapter após adicionar
            if (noteAdapter == null) {
                noteAdapter = new NoteAdapter(this, notes);
                listView.setAdapter(noteAdapter);
            } else {
                noteAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add_note) {
            Intent intent = new Intent(this, AddNoteActivity.class);
            addNoteLauncher.launch(intent);
            return true;
        } else if (id == R.id.menu_sort_priority) {
            ordenarPorPrioridade();
            return true;
        } else if (id == R.id.menu_sort_alpha) {
            ordenarPorOrdem();
            return true;
        } else if (id == R.id.menu_close) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void ordenarPorPrioridade() {
        notes.sort((n1, n2) -> getPriorityValue(n1.getPrioridade()) - getPriorityValue(n2.getPrioridade()));
        noteAdapter.notifyDataSetChanged();
    }

    private void ordenarPorOrdem() {
        notes.sort((n1, n2) -> n1.getTitulo().compareToIgnoreCase(n2.getTitulo()));
        noteAdapter.notifyDataSetChanged();
    }

    private int getPriorityValue(String priority) {
        switch (priority) {
            case "Alta":
                return 1;
            case "Normal":
                return 2;
            case "Baixa":
                return 3;
            default:
                return 99;
        }
    }

    private void inserirDadosIniciais() {
        try {
            bancoDados = openOrCreateDatabase("notasapp", MODE_PRIVATE, null);

            Cursor cursor = bancoDados.rawQuery("SELECT COUNT(*) FROM notas", null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();


            if (count == 0) {
                String sql = "INSERT INTO notas (titulo, prioridade, conteudo, caminho) VALUES (?, ?, ?, ?)";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);

                // Exemplo de notas iniciais
                stmt.bindString(1, "Trabalho de ferro");
                stmt.bindString(2, "Alta");
                stmt.bindString(3, "Fazero o app myNote em android...");
                stmt.bindString(4, "");
                stmt.executeInsert();

                stmt.bindString(1, "Limpar o carro");
                stmt.bindString(2, "Normal");
                stmt.bindString(3, "Limpar o carro até sexta");
                stmt.bindString(4, "");
                stmt.executeInsert();

                stmt.bindString(1, "Trabalho do siscoutto de Redes");
                stmt.bindString(2, "Baixa");
                stmt.bindString(3, "Muito texto preguiça");
                stmt.bindString(4, "");
                stmt.executeInsert();

                stmt.close();
            }

            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
