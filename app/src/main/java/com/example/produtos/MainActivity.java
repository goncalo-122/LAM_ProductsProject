package com.example.produtos;

import static android.content.ContentValues.TAG;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    public static final String SELECTED_OPTION = "SELECTED_OPTION";
    String url = "https://hostingalunos.upt.pt/~dam/produtos.html";
    ArrayList<Product> listOfProducts = new ArrayList<>();
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    DB_handler db;
    ExecutorService executorService;
    MyViewHolder vw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myAdapter = new MyAdapter(listOfProducts);
        db = new DB_handler(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(myAdapter);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        executorService = Executors.newCachedThreadPool();

        loadProducts();
    }


//private void addProduct(){
//    if (vw.inChart.isChecked()){
//        db.adicionarProduct(url,Integer.parseInt(String.valueOf(vw.qtdTxt.getText())),vw.inChart.isChecked());
//    }
//    else{
//        db.adicionarProduct(url,Integer.parseInt(String.valueOf(vw.qtdTxt.getText())),vw.inChart.isEnabled());
//    }
//
//}

    private void loadProducts() {
        executorService.execute(() -> {
            List<Product> products = db.listProducts();
            Log.e(TAG,"MainActivity"+products);
            runOnUiThread(() -> {
                if (products != null && !products.isEmpty()) {
                    listOfProducts.clear();
                    listOfProducts.addAll(products);
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Produtos carregados com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("loadProducts", "Nenhum produto encontrado.");
                    Toast.makeText(MainActivity.this, "Nenhum produto encontrado.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void updateProductList(List<Product> products) {
        listOfProducts.clear();
        listOfProducts.addAll(products);
        myAdapter.notifyDataSetChanged();
    }

    private List<Product> filterProductsByChart(List<Product> products, boolean inChart) {
        List<Product> filtered = new ArrayList<>();
        for (Product product : products) {
            if (product.isInChart() == inChart) {
                filtered.add(product);
            }
        }
        return filtered;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_layout, menu);
        return true;
    }

    public void listProd(MenuItem item) {
        loadProducts();
    }

    public void listNotChart(MenuItem item) {
        executorService.execute(() -> {
            List<Product> products = db.listProducts();
            List<Product> notInChart = filterProductsByChart(products, false);
            runOnUiThread(() -> {
                updateProductList(notInChart);
                Log.d("listNotChart", "Exibindo produtos fora do carrinho.");
            });
        });
    }

    public void listChart(MenuItem item) {
        executorService.execute(() -> {
            List<Product> products = db.listProducts();
            List<Product> inChart = filterProductsByChart(products, true);
            runOnUiThread(() -> {
                updateProductList(inChart);
                Log.d("listChart", "Exibindo produtos no carrinho.");
            });
        });
    }

    public void orderProducts(MenuItem item) {
        executorService.execute(() -> {
            List<Product> products = db.listProducts();
            Collections.sort(products, (p1, p2) -> p1.getDescr().compareToIgnoreCase(p2.getDescr()));
            runOnUiThread(() -> {
                updateProductList(products);
                Log.d("orderProducts", "Produtos ordenados por nome.");
            });
        });
    }

    public void refresh(MenuItem item) {
        loadProducts();
        Log.d("refresh", "Lista de produtos atualizada.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();  // Fechar o ExecutorService quando a activity for destruída
        }
    }
}
