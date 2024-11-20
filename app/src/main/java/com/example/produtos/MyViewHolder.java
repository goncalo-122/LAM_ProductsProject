package com.example.produtos;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView produtoTxt;
    EditText qtdTxt;
    CheckBox inCarrinho;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        produtoTxt = itemView.findViewById(R.id.nomePtxt);
        qtdTxt = itemView.findViewById(R.id.qtdTxt);
        inCarrinho = itemView.findViewById(R.id.checkBox);
    }
}


