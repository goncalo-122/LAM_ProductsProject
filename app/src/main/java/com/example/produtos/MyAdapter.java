package com.example.produtos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.produtos.Product;
import com.example.produtos.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Product> mDataset;

    // Construtor com proteção contra nulo
    public MyAdapter(List<Product> mDataset) {
        this.mDataset = (mDataset != null) ? mDataset : new ArrayList<>();
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout do item
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.produto_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        // Obtém o produto da posição
        Product product = mDataset.get(position);

        // Configura os dados no ViewHolder
        holder.nomePT.setText(product.getDescr());  // Alterado para getDescr() se for o nome do produto
        holder.qtdT.setText(String.valueOf(product.getQtd()));
        holder.qtdT.setFocusable(false);  // Desabilita a edição do EditText
        holder.qtdT.setClickable(false);
        holder.noCarrinho.setChecked(product.isInChart());

        // Lida com a alteração no CheckBox
        holder.noCarrinho.setOnCheckedChangeListener((buttonView, isChecked) -> {
            product.setInChart(isChecked);  // Atualiza o estado do produto no carrinho
        });
    }

    @Override
    public int getItemCount() {
        return (mDataset != null && !mDataset.isEmpty()) ? mDataset.size() : 0;
    }

    // Classe ViewHolder para vincular os elementos do layout
    public static class MyViewHolder extends RecyclerView.ViewHolder {
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

    // Método para atualizar a lista de produtos dinamicamente
    public void updateDataset(List<Product> newDataset) {
        if (newDataset != null && !newDataset.equals(mDataset)) {
            this.mDataset = newDataset;
            notifyDataSetChanged();
        }
    }
}
