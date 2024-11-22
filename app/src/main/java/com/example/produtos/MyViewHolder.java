package com.example.produtos;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView productTxt;
    EditText qtdTxt;
    CheckBox inChart;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        productTxt = itemView.findViewById(R.id.productNameTextView);
        qtdTxt = itemView.findViewById(R.id.productQtdEditText);
        inChart = itemView.findViewById(R.id.checkBox);
    }
}


