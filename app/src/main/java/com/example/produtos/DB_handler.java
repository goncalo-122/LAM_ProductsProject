package com.example.produtos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DB_handler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PRODUTOS";
    private static final String TABLE_NAME = "produto";

    private static final String ID = "id";
    private static final String QTD = "qtd";
    private static final String NOCARRINHO = "noCarrinho";

    public DB_handler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUTOS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nomeP TEXT NOT NULL, "
                + QTD + " INTEGER NOT NULL, "
                + NOCARRINHO + " INTEGER NOT NULL)";
        db.execSQL(CREATE_PRODUTOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public String listNomeProducts(String url) {
        String productName = null;
        HttpHandler httpHandler = new HttpHandler();

        try {

            String jsonResponse = httpHandler.lerInformacao(url);

            if (jsonResponse != null && !jsonResponse.trim().isEmpty()) {

                JSONArray jsonArray = new JSONArray(jsonResponse);


                if (jsonArray.length() > 0) {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    productName = jsonObject.optString("nomeP", "Nome não encontrado");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return productName;
    }


    public List<Product> listProducts(String url) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String nomeProduto = listNomeProducts(url);

        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME;
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Product product = new Product(
                            cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                            nomeProduto,
                            cursor.getInt(cursor.getColumnIndexOrThrow(QTD)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(NOCARRINHO)) == 1
                    );
                    products.add(product);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }
        return products;
    }
}
