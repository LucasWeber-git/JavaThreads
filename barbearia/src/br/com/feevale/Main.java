package br.com.feevale;

import br.com.feevale.classes.Barbearia;
import br.com.feevale.classes.Barbeiro;
import br.com.feevale.classes.GeradorCliente;

public class Main {

    public static void main(String[] args) {
        final Barbearia barbearia = new Barbearia();
        final Barbeiro barbeiro = new Barbeiro(barbearia, "Barbeiro#1");
        final GeradorCliente geradorCliente = new GeradorCliente(barbearia, "gerador");

        barbeiro.start();
        geradorCliente.start();
    }

}
