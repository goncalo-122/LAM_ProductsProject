package com.example.produtos;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    List <Product> products = new ArrayList<>();

    private DBHandler dbHandler;
    private ProductAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHandler = new DBHandler(this);

        adapter = new ProductAdapter(products);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        updateRecyclerView("T");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all:
                updateProducts("T"); // guardar alterações feitas anteriormente pelo utilizador
                Toast.makeText(this, "Todos os produtos", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.cart:
                updateProducts("C"); // guardar alterações feitas anteriormente pelo utilizador
                Toast.makeText(this, "Produtos no carrinho", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.notCart:
                updateProducts("F"); // guardar alterações feitas anteriormente pelo utilizador
                Toast.makeText(this, "Produtos em falta", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.order:
                updateProducts("O"); // guardar alterações feitas anteriormente pelo utilizador
                Toast.makeText(this, "Produtos ordenados alfabeticamente", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.refresh:
                refresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Quando for premido um botão “Refrescar”, o método limpa a base de dados,
     * lê uma lista de produtos do servidor usando HTTP, e insere-a na base de dados.
     */
    public void refresh() {
        new Thread(() -> {
            HttpHandler httpHandler = new HttpHandler();
            String url = "https://hostingalunos.upt.pt/~dam/produtos.html";
            String result = httpHandler.readInfo(url);

            if (result != null) {
                try {
                    String[] rows = result.split("\n");

                    dbHandler.clearDatabase(); // Limpar base de dados

                    // Ler lista de produtos do servidor HTTP
                    int i = 0;
                    for (String row : rows) {
                        String name = row.trim();
                        String cleanData = name.replaceAll("<br\\s*/?>", "\n");
                        if (!name.isEmpty()) {
                            Product product = new Product(i, cleanData, 0, false);
                            dbHandler.insertProduct(product);
                            i++;
                        }
                    }
                    runOnUiThread(() -> {
                        updateRecyclerView("T");
                        Toast.makeText(this, "Produtos refrescados com sucesso!", Toast.LENGTH_SHORT).show();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Erro ao processar produtos.", Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Erro ao encontrar produtos.", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    /**
     * @param viewOption tipo de vista que o utilizador selecionou no menu (T - Todos | C - Carrinho | F - Falta | O - Ordenar)
     * Atualiza na base de dados os novos valores inseridos de quantidade e noCarrinho e atualiza a recyclerView de modo a refletir essas alterações
     */
    public void updateProducts(String viewOption) {
        new Thread(() -> {
            boolean allUpdated = true;

            // Atualizar produtos na base de dados
            for (Product product : products) {
                Log.d(TAG, "Updating product: " + product.getId());
                boolean updated = dbHandler.updateProduct(product.getId(), product.getQtd(), product.isInCart());
                if (!updated) {
                    allUpdated = false;  // Se algum produto não for atualizado, marcar como falha
                    break;
                }
            }

            boolean finalAllUpdated = allUpdated;
            runOnUiThread(() -> {
                updateRecyclerView(viewOption); // Atualizar recyclerView
                if (!finalAllUpdated) {
                    Toast.makeText(MainActivity.this, "Erro ao atualizar produtos.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    /**
     * @param viewOption tipo de vista que o utilizador selecionou no menu (T - Todos | C - Carrinho | F - Falta | O - Ordenar)
     * Atualiza recyclerView consoante vista selecionada pelo utilizador no menu
     */
    private void updateRecyclerView(String viewOption) {
        switch (viewOption){
            case "T":
                products.clear();
                products.addAll(dbHandler.getAllProducts());
                adapter.notifyDataSetChanged();
                break;
            case"C":
                products.clear();
                products.addAll(dbHandler.getProductsInCart());
                adapter.notifyDataSetChanged();
                break;
            case"F":
                products.clear();
                products.addAll(dbHandler.getProductsNotInCart());
                adapter.notifyDataSetChanged();
                break;
            case"O":
                products.clear();
                products.addAll(dbHandler.getSortedProducts());
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
