package com.example.produtos;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DB_handler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PRODUTOS";
    private static final String TABLE_NAME = "produto";

    private static final String ID = "id";
    private static final String NOMEP = "nomeP";
    private static final String QTD = "qtd";
    private static final String NOCARRINHO = "noCarrinho";

    public DB_handler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUTOS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NOMEP + " TEXT NOT NULL, "
                + QTD + " INTEGER NOT NULL, "
                + NOCARRINHO + " INTEGER NOT NULL)";
        db.execSQL(CREATE_PRODUTOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean adicionarProduto(Produto produto) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NOMEP, produto.getNomeP());
            values.put(QTD, produto.getQtd());
            values.put(NOCARRINHO, produto.isNoCarrinho() ? 1 : 0);

            long result = db.insert(TABLE_NAME, null, values);
            return result != -1; // Retorna true se a inserção foi bem-sucedida
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    // Obter um único produto pelo ID
    public Produto getProduto(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.query(TABLE_NAME,
                    new String[]{ID, NOMEP, QTD, NOCARRINHO},
                    ID + " = ?",
                    new String[]{String.valueOf(id)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                return new Produto(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NOMEP)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(QTD)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(NOCARRINHO)) == 1
                );
            }
            return null; // Retorna null se o produto não foi encontrado
        } finally {
            if (cursor != null) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }
    }

    // Obter lista de produtos
    public List<Produto> listaProdutos() {
        List<Produto> produtos = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + NOMEP + " ASC";
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Produto produto = new Produto(
                            cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(NOMEP)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(QTD)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(NOCARRINHO)) == 1
                    );
                    produtos.add(produto);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }
        return produtos;
    }

    // Atualizar produto
    public boolean updateProduto(int id, String novoNome, int novaQtd, boolean noCarrinho) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(NOMEP, novoNome);
            contentValues.put(QTD, novaQtd);
            contentValues.put(NOCARRINHO, noCarrinho ? 1 : 0);

            int rowsAffected = db.update(TABLE_NAME, contentValues, ID + " = ?", new String[]{String.valueOf(id)});
            return rowsAffected > 0;
        } finally {
            if (db != null && db.isOpen()) db.close();
        }
    }

    // Deletar produto
    public boolean deleteProduto(int id) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            int rowsDeleted = db.delete(TABLE_NAME, ID + " = ?", new String[]{String.valueOf(id)});
            return rowsDeleted > 0;
        } finally {
            if (db != null && db.isOpen()) db.close();
        }
    }

    // Verificar se o banco de dados existe
    public boolean doesDatabaseExist(Context context) {
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            return db != null;
        } catch (Exception e) {
            return false;
        } finally {
            if (db != null && db.isOpen()) db.close();
        }
    }
}
