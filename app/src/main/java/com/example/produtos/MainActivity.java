package com.example.produtos;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String SELECTED_OPTION = "SELECTED_OPTION";

    ArrayList<Product> listOfProducts;
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myAdapter = new MyAdapter(listOfProducts);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(myAdapter);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_layout, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
////        Intent intent;
//
////        switch (item.getItemId()) {
////            case R.id.option1:
////            case R.id.option2:
////            case R.id.option3:
//////                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
//////
//////                intent = new Intent(this, MainActivity2.class);
//////                intent.putExtra(SELECTED_OPTION, item.getTitle());  // Passa o título como extra
//////                startActivity(intent);
////                return true;
////
////            default:
////                return super.onOptionsItemSelected(item);
////        }
//    }
}