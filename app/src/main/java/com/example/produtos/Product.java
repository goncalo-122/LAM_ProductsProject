package com.example.produtos;

public class Product {

    private int id;
    private String descr;
    private int qtd;
    private boolean isInChart;

    public Product(int id, String descr, int qtd, boolean isInChart) {
        this.id = id;
        this.descr = descr;
        this.qtd = qtd;
        this.isInChart = isInChart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public boolean isInChart() {
        return isInChart;
    }

    public void setInChart(boolean inChart) {
        isInChart = inChart;
    }
}
