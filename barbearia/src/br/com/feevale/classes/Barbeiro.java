package br.com.feevale.classes;

import br.com.feevale.domain.ClientStatus;

public class Barbeiro extends Thread {

    private final String nome;
    private final BarberShop barberShop;

    public Barbeiro(final BarberShop BarberShop, final String nome) {
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

        // final Client Client = barberShop.atender();

        // if (isNull(Client)) {
        // dormir();
        // } else if (Client.getStatus() == WAITING_BARBER) {
        // Client.setStatus(CUTTING_HAIR);
        // System.out.printf("%s está CUTTING_HAIR com %s.\n", Client.getNome(),
        // this.getNome());

        // trabalhar();
        // Client.setStatus(WAITING_BARBER);
        // } else {
        // Client.setStatus(PAYNG);
        // System.out.printf("%s está PAYNG com %s.\n", Client.getNome(),
        // this.getNome());

        // trabalhar();
        // barberShop.liberar(Client);
        // }
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
