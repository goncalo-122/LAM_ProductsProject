package com.example.produtos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Product> mDataset;

    public MyAdapter(List<Product> mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.produto_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        Product product = mDataset.get(position);
        holder.nomePT.setText(product.getId());
        holder.qtdT.setText(String.valueOf(product.getQtd()));
        holder.noCarrinho.setChecked(product.isInChart());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nomePT;
        EditText qtdT;
        CheckBox noCarrinho;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomePT = itemView.findViewById(R.id.productNameTextView);
            qtdT = itemView.findViewById(R.id.productQtdEditText);
            noCarrinho = itemView.findViewById(R.id.checkBox);
        }
    }
}
