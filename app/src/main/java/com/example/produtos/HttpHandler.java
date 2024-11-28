package com.example.produtos;


import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();

    String readInfo(String urlParam) {
        String resultado = null;
        URL url;
        HttpURLConnection conn;
        InputStream in;

        try {
            url = new URL(urlParam);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            in = new BufferedInputStream(conn.getInputStream());
            resultado = convertStreamToString(in);
            Log.e(TAG, "res: " + resultado);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return resultado;
    }

    private String convertStreamToString(InputStream is) {
        String linha;
        BufferedReader reader;
        StringBuilder sb;

        reader = new BufferedReader(new InputStreamReader(is));
        sb = new StringBuilder();

        try {
            while ((linha = reader.readLine()) != null) {
                sb.append(linha).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}

