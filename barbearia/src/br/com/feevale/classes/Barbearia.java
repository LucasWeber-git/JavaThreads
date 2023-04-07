package br.com.feevale.classes;

import java.util.ArrayList;
import java.util.List;

public class Barbearia {

    private final List<Cliente> clientes;

    public Barbearia() {
        clientes = new ArrayList<>();
    }

    public void addEspera(final Cliente cliente) {
        clientes.add(cliente);
        System.out.printf("%s chegou na barbearia.\n", cliente.getNome());

        synchronized (this) {
            this.notifyAll();
        }
    }

    public Cliente atender() {
        return !clientes.isEmpty() ? clientes.get(0) : null;
    }

    public void liberar(final Cliente cliente) {
        clientes.remove(cliente);
        System.out.printf("%s saiu da barbearia.\n", cliente.getNome());
    }

}
