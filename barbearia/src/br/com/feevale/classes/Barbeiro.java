package br.com.feevale.classes;

import static br.com.feevale.domain.StatusCliente.CORTANDO;
import static br.com.feevale.domain.StatusCliente.ESPERANDO_CORTE;
import static br.com.feevale.domain.StatusCliente.ESPERANDO_PAGAMENTO;
import static br.com.feevale.domain.StatusCliente.PAGANDO;
import static java.util.Objects.isNull;

public class Barbeiro extends Thread {

    private final String nome;
    private final Barbearia barbearia;

    public Barbeiro(final Barbearia barbearia, final String nome) {
        super(nome);
        this.nome = nome;
        this.barbearia = barbearia;
    }

    @Override
    public void run() {
        while (true) {
            atender();
        }
    }

    public String getNome() {
        return nome;
    }

    private void atender() {
        final Cliente cliente = barbearia.atender();

        if (isNull(cliente)) {
            dormir();
        } else if (cliente.getStatus() == ESPERANDO_CORTE) {
            cliente.setStatus(CORTANDO);
            System.out.printf("%s está cortando com %s.\n", cliente.getNome(), this.getNome());

            trabalhar();
            cliente.setStatus(ESPERANDO_PAGAMENTO);
        } else {
            cliente.setStatus(PAGANDO);
            System.out.printf("%s está pagando com %s.\n", cliente.getNome(), this.getNome());

            trabalhar();
            barbearia.liberar(cliente);
        }
    }

    private void trabalhar() {
        try {
            sleep((int) (Math.random() * 5000));
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void dormir() {
        try {
            System.out.printf("%s está dormindo.\n", this.getNome());
            synchronized (barbearia) {
                barbearia.wait();
            }
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

}
