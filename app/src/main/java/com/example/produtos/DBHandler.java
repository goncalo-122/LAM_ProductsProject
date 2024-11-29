package com.example.produtos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "produtosDB";
    private static final String TABELA_PRODUTO = "produto";

    private static final String PRODUTO_ID = "id";
    private static final String PRODUTO_DESCR = "descr";
    private static final String PRODUTO_QTD = "qtd";
    private static final String PRODUTO_NOCARRINHO = "noCarrinho";

    private static final String TAG = DBHandler.class.getSimpleName();

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUTOS_TABLE = "CREATE TABLE " + TABELA_PRODUTO + "("
                + PRODUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PRODUTO_DESCR + " TEXT NOT NULL, "
                + PRODUTO_QTD + " INTEGER NOT NULL, "
                + PRODUTO_NOCARRINHO + " INTEGER NOT NULL)";
        db.execSQL(CREATE_PRODUTOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_PRODUTO);
        onCreate(db);
    }

    /**
     * Insere um produto na base de dados
     * @param product
     */
    public void insertProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUTO_ID, product.getId());
        values.put(PRODUTO_DESCR, product.getDescr());
        values.put(PRODUTO_QTD, product.getQtd());
        values.put(PRODUTO_NOCARRINHO, product.isInCart() ? 1 : 0);

        db.insert(TABELA_PRODUTO, null, values);
        db.close();
    }

    /**
     * Altera produto na base de dados
     * @param id
     * @param newQtd
     * @param isInCar
     * @return true se atualizção é bem sucedida e false se existir erro na atualização
     */
    public boolean updateProduct(int id, int newQtd, boolean isInCar) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PRODUTO_QTD, newQtd);
            contentValues.put(PRODUTO_NOCARRINHO, isInCar ? 1 : 0);

            String whereClause = PRODUTO_ID + " = ?";
            String[] whereArgs = new String[]{String.valueOf(id)};

            int rowsAffected = db.update(TABELA_PRODUTO, contentValues, whereClause, whereArgs);

            // Verifica se a atualização foi bem-sucedida.
            if (rowsAffected > 0) {
                Log.d(TAG, "Produto com ID " + id + " atualizado com sucesso.");
                return true;
            } else {
                Log.d(TAG, "Erro ao atualizar o produto com ID " + id);
                return false;
            }
        } finally {
            if (db != null && db.isOpen()) db.close();
        }
    }

    /**
     * Lê todos os produtos na base de dados
     * @return products lista de todos os produtos
     */
    public List<Product> getAllProducts () {
        List <Product> products = new ArrayList<>();
        String query = "SELECT * FROM " + TABELA_PRODUTO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setDescr(cursor.getString(1));
                product.setQtd(cursor.getInt(2));
                product.setInCart(cursor.getInt(3) == 1);
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }

    /**
     * Lê da base de dados todos os produtos que estão no carrinho
     * @return products lista de produtos no carrinho
     */
    public List<Product> getProductsInCart() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM " + TABELA_PRODUTO + " WHERE " + PRODUTO_NOCARRINHO + " = 1 AND " + PRODUTO_QTD + " > 0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setDescr(cursor.getString(1));
                product.setQtd(cursor.getInt(2));
                product.setInCart(true);
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }

    /**
     * Lê da base de dados todos os produtos que não estão no carrinho
     * @return products lista de produtos que não estão no carrinho
     */
    public List<Product> getProductsNotInCart() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM " + TABELA_PRODUTO + " WHERE " + PRODUTO_NOCARRINHO + " = 0 AND " + PRODUTO_QTD + " > 0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setDescr(cursor.getString(1));
                product.setQtd(cursor.getInt(2));
                product.setInCart(false);
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }

    /**
     * Lê da base de dados todos os produtos por ordem alfabetica
     * @return products lista de todos os produtos por ordem alfabetica
     */
    public List<Product> getSortedProducts() {
        List<Product> products = getAllProducts();

        // Lidar com acentos especificos da língua portuguesa durante a ordenação
        Collator collator = Collator.getInstance(new Locale("pt", "PT"));
        collator.setStrength(Collator.PRIMARY);

        products.sort((product1, product2) ->
                collator.compare(product1.getDescr(), product2.getDescr()));

        return products;
    }

    /**
     * Limpa da base de dados todos os produtos
     */
    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABELA_PRODUTO);
        db.close();
    }

}
