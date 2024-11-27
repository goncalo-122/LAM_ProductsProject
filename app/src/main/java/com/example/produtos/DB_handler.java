package com.example.produtos;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class DB_handler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PRODUTOS";
    private static final String TABLE_NAME = "produto";

    private static final String ID = "id";
    private static final String DESCR = "descr";
    private static final String QTD = "qtd";
    private static final String NOCARRINHO = "noCarrinho";

    public DB_handler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUTOS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DESCR + " TEXT NOT NULL, "
                + QTD + " INTEGER DEFAULT 0 NOT NULL, "
                + NOCARRINHO + " INTEGER DEFAULT 0 NOT NULL)";

        db.execSQL(CREATE_PRODUTOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }



        public List<Product> listProducts() {
            List<Product> products = new ArrayList<>();
            SQLiteDatabase db = null;
            Cursor cursor = null;

            try {
                db = this.getReadableDatabase();
                String query = "SELECT * FROM " + TABLE_NAME;
                cursor = db.rawQuery(query, null);

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        Product product = new Product(
                                cursor.getInt(cursor.getColumnIndexOrThrow(ID)), // ID do produto
                                cursor.getString(cursor.getColumnIndexOrThrow(DESCR)), // Nome do produto
                                cursor.getInt(cursor.getColumnIndexOrThrow(QTD)), // Quantidade
                                cursor.getInt(cursor.getColumnIndexOrThrow(NOCARRINHO)) == 1 // Verifica se está no carrinho
                        );
                        products.add(product); // Adiciona o produto à lista
                    } while (cursor.moveToNext());
                }
            } finally {
                if (cursor != null) cursor.close();
                if (db != null && db.isOpen()) db.close();
            }
            return products;
        }


    public boolean updateProduto(int id, int novaQtd,boolean noCarrinho) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(QTD, novaQtd);
            contentValues.put(NOCARRINHO, noCarrinho);

            String whereClause = ID + " = ?";
            String[] whereArgs = new String[]{String.valueOf(id)};
            int rowsAffected = db.update(TABLE_NAME, contentValues, whereClause, whereArgs);
            return rowsAffected > 0;
        } finally {
            if (db != null && db.isOpen()) db.close();
        }
    }


        public void adicionarListaInicialDeProdutos(String url) {
            HttpHandler httpHandler = new HttpHandler();
            String productsList = httpHandler.lerInformacao(url);

            SQLiteDatabase db = null;

            try {
                db = this.getWritableDatabase();
                BufferedReader br = new BufferedReader(new StringReader(productsList));



                String nomeProduto;
                ContentValues values;
                long result;
                while((nomeProduto = br.readLine()) != null) {

                    values = new ContentValues();
                    values.put(DESCR, nomeProduto);
                    result = db.insert(TABLE_NAME, null, values);

                    if (result != -1) {
                        Log.d(TAG, "Produto adicionado com sucesso. ID: " + result);
                    } else {
                        Log.e(TAG, "Erro ao adicionar produto ao banco de dados.");
                    }

                }


            } catch (Exception e) {
                Log.e(TAG, "Erro durante a inserção de lista inicial de produtos: ", e);
            } finally {
                if (db != null && db.isOpen()) {
                    db.close();
                }
            }
        }



}
