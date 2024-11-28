package com.example.produtos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABELA_PRODUTO);
        db.close();
    }


//    public ArrayList<Product> listaProdutos() {
//        ArrayList<Product> produtos = new ArrayList<>();
//        Product p;
//        int produtoId,produtoQtd, noCarrinho;
//        String produtoDscr;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = null;
//        try {
//            String query = "SELECT * FROM " + TABELA_PRODUTO;
//            cursor = db.rawQuery(query, null);
//
//            if (cursor.moveToFirst()) {
//                do {
//                    produtoId = cursor.getInt(cursor.getColumnIndexOrThrow(PRODUTO_ID));
//                    produtoDscr = cursor.getString(cursor.getColumnIndexOrThrow(PRODUTO_DESCR));
//                    produtoQtd = cursor.getInt(cursor.getColumnIndexOrThrow(PRODUTO_QTD));
//                    noCarrinho = cursor.getInt(cursor.getColumnIndexOrThrow(PRODUTO_NOCARRINHO));
//                    p = new Product(produtoId, produtoDscr,produtoQtd, noCarrinho == 1);
//                    produtos.add(p);
//                } while (cursor.moveToNext());
//            }
//        } finally {
//            if (cursor != null) cursor.close();
//            db.close();
//        }
//        return produtos;
//    }
//
//
//    public void adicionarListaInicialDeProdutos(String url) {
//        HttpHandler httpHandler = new HttpHandler();
//        String listaProdutosHttp = httpHandler.lerInformacao(url);
//        Log.e(TAG, "Lista de produtos: " + listaProdutosHttp);
//
//        SQLiteDatabase db = null;
//
//        try {
//            db = this.getWritableDatabase();
//            BufferedReader br = new BufferedReader(new StringReader(listaProdutosHttp));
//            String nomeProduto;
//            ContentValues values;
//            long result;
//            while((nomeProduto = br.readLine()) != null) {
//
//                values = new ContentValues();
//                values.put(PRODUTO_DESCR, nomeProduto);
//                result = db.insert(TABELA_PRODUTO, null, values);
//
//                if (result != -1) {
//                    Log.d(TAG, "Produto adicionado com sucesso. ID: " + result);
//                } else {
//                    Log.e(TAG, "Erro ao adicionar produto ao base de dados.");
//                }
//
//            }
//
//        } catch (Exception e) {
//            Log.e(TAG, "Erro durante a inserção de lista inicial de produtos: ", e);
//        } finally {
//            if (db != null && db.isOpen()) {
//                db.close();
//            }
//        }
//    }

}
