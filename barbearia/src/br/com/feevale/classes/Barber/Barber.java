package br.com.feevale.classes.Barber;

import br.com.feevale.classes.BarberShop.BarberShop;
import br.com.feevale.classes.Client.Client;
import br.com.feevale.domain.ClientStatus;

public class Barber extends Thread {

    private final String nome;
    private final BarberShop barberShop;

    public Barber(final BarberShop BarberShop, final String nome) {
        super(nome);
        this.nome = nome;
        this.barberShop = BarberShop;
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
        barberShop.setBarberAvailable(true);

        Client client = null;
        synchronized (barberShop.getChairs()) {
            if (!barberShop.getChairs().isEmpty()) {
                client = barberShop.getChairs().stream()
                        .filter(c -> c.getStatus().equals(ClientStatus.WAITING_BARBER))
                        .findFirst()
                        .orElse(null);
            }
        }

        if (client != null) {
            cut(client);
        } else {
            synchronized (barberShop.getPaying()) {
                if (!barberShop.getPaying().isEmpty()) {
                    client = barberShop.getPaying().peek();
                }
            }
        }

        if (client != null) {
            pay(client);
        }
    }

    private void trabalhar() {
        try {
            sleep((int) (Math.random() * 1000));
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void cut(Client client) {
        System.out.println(this.nome + " is cutting client " + client.id);
        trabalhar();
        client.goPay();
    }

    private synchronized void pay(Client client) {
        System.out.println(this.nome + " is recwiving payment client " + client.id);
        trabalhar();
        client.leave();
    }

    private void dormir() {
        try {
            System.out.printf("%s está dormindo.\n", this.getNome());
            synchronized (barberShop) {
                barberShop.wait();
            }
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

}
