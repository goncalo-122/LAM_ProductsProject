package com.example.produtos;

public class Product {

    private int id;
    private String descr;
    private int qtd;
    private boolean isInCart;

    public Product (){

    }

    public Product(int id, String descr, int qtd, boolean isInCart) {
        this.id = id;
        this.descr = descr;
        this.qtd = qtd;
        this.isInCart = isInCart;
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

    public boolean isInCart() {
        return isInCart;
    }

    public void setInCart(boolean inCart) {
        this.isInCart = inCart;
    }
}
