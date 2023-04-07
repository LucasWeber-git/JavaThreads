package br.com.feevale.classes;

import static java.lang.Math.random;
import static java.lang.String.format;

public class GeradorCliente extends Thread {

    private Integer contador = 1;
    private final Barbearia barbearia;

    public GeradorCliente(final Barbearia barbearia, final String nome) {
        super(nome);
        this.barbearia = barbearia;
    }

    @Override
    public void run() {
        while (true) {
            gerar();
        }
    }

    private void gerar() {
        try {
            final Cliente cliente = new Cliente(format("Cliente#%s", contador++));
            barbearia.addEspera(cliente);
            sleep((int) (random() * 5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
