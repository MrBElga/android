package com.example.appemprestimo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appemprestimo.entidades.Parcela;

import java.util.List;

public class EmprestimoAdapter extends ArrayAdapter<Parcela> {

    private int layout;

    public EmprestimoAdapter(@NonNull Context context, int resource, @NonNull List<Parcela> parcelas) {
        super(context, resource, parcelas);
        this.layout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //convertView representa o lazout do item
        //parent representa o listView
        //position é a posição do elemento da lista

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(this.layout, parent, false);
        }
        //deve mexer aqui para as alterações
        TextView tvNum = (TextView) convertView.findViewById(R.id.tvParcela);
        TextView tvValorParcela = (TextView) convertView.findViewById(R.id.tvValorParcela);
        TextView tvJuros2 = (TextView) convertView.findViewById(R.id.tvJuros2);
        TextView tvAmort = (TextView) convertView.findViewById(R.id.tvAmort);

        Parcela parcela = getItem(position);
        tvNum.setText("" + parcela.getNum());
        tvValorParcela.setText(String.format("%.2f", parcela.getJuros()));
        tvJuros2.setText(String.format("%.2f", parcela.getJuros()));
        tvAmort.setText(String.format("%.2f", parcela.getAmort()));
        return convertView;
    }
}
