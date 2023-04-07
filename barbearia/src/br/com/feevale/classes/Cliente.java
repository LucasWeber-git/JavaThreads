package br.com.feevale.classes;

import static br.com.feevale.domain.StatusCliente.ESPERANDO_CORTE;

import br.com.feevale.domain.StatusCliente;

public class Cliente {

    private final String nome;

    private StatusCliente status;

    public Cliente(final String nome) {
        this.nome = nome;
        this.status = ESPERANDO_CORTE;
    }

    public String getNome() {
        return nome;
    }

    public StatusCliente getStatus() {
        return status;
    }

    public void setStatus(final StatusCliente status) {
        this.status = status;
    }

}
