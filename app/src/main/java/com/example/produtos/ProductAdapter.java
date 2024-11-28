package com.example.produtos;

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
        holder.tvQtd.setText(String.valueOf(product.getQtd()));
        holder.cbInCart.setChecked(product.isInCart());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
