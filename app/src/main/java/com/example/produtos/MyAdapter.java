package com.example.produtos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Produto> mDataset;

    public MyAdapter(List<Produto> mDataset) {
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
        Produto produto = mDataset.get(position);
        holder.nomePT.setText(produto.getNomeP());
        holder.qtdT.setText(String.valueOf(produto.getQtd()));
        holder.noCarrinho.setChecked(produto.isNoCarrinho());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nomePT;
        TextView qtdT;
        CheckBox noCarrinho;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomePT = itemView.findViewById(R.id.nomePtxt);
            qtdT = itemView.findViewById(R.id.qtdTxt);
            noCarrinho = itemView.findViewById(R.id.checkBox);
        }
    }
}
