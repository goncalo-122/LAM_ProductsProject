package com.example.produtos;

public class Produto {

        int id;
        boolean noCarrinho;
        String nomeP;
        int qtd;

        public Produto(int id, String nomeP, int qtd,boolean noCarrinho) {
            this.id=id;
            this.noCarrinho = noCarrinho;
            this.nomeP = nomeP;
            this.qtd = qtd;
        }
        public boolean isNoCarrinho() {
            return noCarrinho;
        }

        public void setNoCarrinho(boolean noCarrinho) {
            this.noCarrinho = noCarrinho;
        }

        public String getNomeP() {
            return nomeP;
        }

        public void setNomeP(String nomeP) {
            this.nomeP = nomeP;
        }

        public int getQtd() {
            return qtd;
        }

        public void setQtd(int qtd) {
            this.qtd = qtd;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
}
