package com.example.appmynote.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmynote.R;
import com.example.appmynote.activities.NoteDetailActivity;
import com.example.appmynote.entidades.Note;

import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Note> notes;

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        }


        Note currentNote = (Note) getItem(position);

        TextView title = convertView.findViewById(R.id.textViewTitle);
        LinearLayout noteLayout = convertView.findViewById(R.id.noteLayout);

        title.setText(currentNote.getTitulo());

        // colocando cor
        switch (currentNote.getPrioridade()) {
            case "Baixa":
                noteLayout.setBackgroundResource(android.R.color.holo_orange_light);
                break;
            case "Normal":
                noteLayout.setBackgroundResource(android.R.color.holo_blue_light);
                break;
            case "Alta":
                noteLayout.setBackgroundResource(android.R.color.holo_red_light);
                break;
        }

        // click curto
        noteLayout.setOnClickListener(v -> {
            Note note = notes.get(position);
            Intent intent = new Intent(context, NoteDetailActivity.class);
            intent.putExtra("titulo", note.getTitulo());
            intent.putExtra("prioridade", note.getPrioridade());
            intent.putExtra("conteudo", note.getConteudo());
            intent.putExtra("caminho", note.getCaminho()); // Adiciona o caminho aqui
            context.startActivity(intent);
        });

        // click longo
        noteLayout.setOnLongClickListener(v -> {
            Note note = notes.get(position);
            excluirNota(note.getTitulo());
            notes.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Nota excluída", Toast.LENGTH_SHORT).show();
            return true;
        });

        return convertView;
    }

    private void excluirNota(String titulo) {
        try {

            SQLiteDatabase bancoDados = context.openOrCreateDatabase("notasapp", Context.MODE_PRIVATE, null);
            bancoDados.delete("notas", "titulo = ?", new String[]{titulo});
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
