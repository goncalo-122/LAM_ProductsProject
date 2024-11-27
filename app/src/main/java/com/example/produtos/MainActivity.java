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

        listarProd();
    }
    private void listarProd() {
        Prod();
        List<Product> products = db.listProducts();
        myAdapter = new MyAdapter(products);
        recyclerView.setAdapter(myAdapter);
    }

    private void Prod() {
           db.adicionarListaInicialDeProdutos(url);
    }
//    private void addDetalhesProd() {
//        List<Product> products = db.listProducts();
//        for(Product p:products){
//            db.updateProduto(p.getId(),p.getQtd(),p.isInChart());
//        }
//        db.updateProduto();
//    }

    public void listAll(MenuItem item) {
        List<Product> contactos = db.listProducts();
        myAdapter = new MyAdapter(contactos);
        recyclerView.setAdapter(myAdapter);
    }
    public void listNotChart(MenuItem item) {

    }

    public void listChart(MenuItem item) {

    }

    public void orderProducts(MenuItem item) {

    }

    public void refresh(MenuItem item) {
        listarProd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();  // Fechar o ExecutorService quando a activity for destruída
        }
    }
}
