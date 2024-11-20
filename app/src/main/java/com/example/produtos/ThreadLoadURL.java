package com.example.produtos;
import android.os.Handler;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ThreadLoadURL {
    private ExecutorService executor;
    private Handler handler;
    private TextView textView;

    public ThreadLoadURL(TextView textView) {
        this.executor = Executors.newSingleThreadExecutor();
        this.handler = new Handler();
        this.textView = textView;
    }
    public void carregarDados(String urlString) {
        executor.execute(() -> {
            String resultado = fetchDataFromURL(urlString);
            if (resultado != null) {
                // Atualiza a TextView na thread principal
                handler.post(() -> textView.setText(resultado));
            } else {
                // Trata erros de forma amigável
                handler.post(() -> textView.setText("Erro ao carregar dados."));
            }
        });
    }

    private String fetchDataFromURL(String urlString) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                reader.close();
            } else {
                return "Erro: Código HTTP " + responseCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result.toString();
    }
}
