package com.example.produtos;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final List<Product> productList;

    public ProductAdapter(List <Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvDscr.setText(product.getDescr());
        holder.etQtd.setText(String.valueOf(product.getQtd()));
        holder.cbInCart.setChecked(product.isInCart());

        // Remover TextWatcher´s existentes para evitar listeners repetidos
        if (holder.etQtd.getTag() instanceof TextWatcher) {
            holder.etQtd.removeTextChangedListener((TextWatcher) holder.etQtd.getTag());
        }

        // Criar listener para editText da quantidade
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int quantity = Integer.parseInt(s.toString());
                    product.setQtd(quantity); // Atualizar quantidade
                } catch (NumberFormatException e) {
                    product.setQtd(0); // Se input é inválido qtd fica a 0 por defeito
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        // Adicionar listener à editText da quantidade e guardá-lo como uma tag
        holder.etQtd.addTextChangedListener(textWatcher);
        holder.etQtd.setTag(textWatcher);

        // Criar listener para checkBox noCarrinho
        holder.cbInCart.setOnCheckedChangeListener(null); // Remover listeners existentes
        holder.cbInCart.setChecked(product.isInCart()); // Definir estado da checkbox
        holder.cbInCart.setOnCheckedChangeListener((buttonView, isChecked) -> product.setInCart(isChecked)); // Definir listener da checkbox
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
