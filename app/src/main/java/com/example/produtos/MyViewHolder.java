package com.example.produtos;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView tvDscr;
    EditText etQtd;
    CheckBox cbInCart;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        tvDscr = itemView.findViewById(R.id.tvDscr);
        etQtd = itemView.findViewById(R.id.etQtd);
        cbInCart = itemView.findViewById(R.id.checkBox);
    }
}


