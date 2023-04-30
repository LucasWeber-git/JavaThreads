package br.com.feevale.novo.classes;

import static java.lang.String.format;
import static java.util.stream.Collectors.toCollection;

import java.util.LinkedList;
import java.util.stream.Stream;

public class BarberThread extends Thread {

    private static final int WORK_TIME = 5000;

    private final int id;
    private final BarberShop barberShop;

    public BarberThread(int id, BarberShop BarberShop) {
        super("Barber" + id);

        this.id = id;
        this.barberShop = BarberShop;
    }

    @Override
    public void run() {
        while (true) {
            try {
                work();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    private void work() throws InterruptedException {
        ClientThread client;

        synchronized (barberShop.busy) {
            client = getClientpaying();
            if (client != null) {
                System.out.printf("%s is receiving payment from %s\n", this, client);
                addBusy(client.id);
            } else {
                client = getClientWaitingToCut();
                if (client != null) {
                    System.out.printf("%s is cutting the hair of %s\n", this, client);
                    addBusy(client.id);
                }
            }
        }

        if (client != null) {
            doWork(client);
        } else {
            synchronized (barberShop) {
                barberShop.wait();
            }
        }
    }

    private ClientThread getClientpaying() {
        synchronized (barberShop.paying) {
            Stream<ClientThread> clients = barberShop.paying.stream()
                .filter(client -> client.alreadyCut && !client.alreadyPaid);

            return getNotBusy(clients);
        }
    }

    private ClientThread getClientWaitingToCut() {
        synchronized (barberShop.chairs) {
            Stream<ClientThread> clients = barberShop.chairs.stream()
                .filter(client -> !client.alreadyCut);

            return getNotBusy(clients);
        }
    }

    private ClientThread getNotBusy(Stream<ClientThread> clients) {
        return clients.filter(client -> !barberShop.busy.containsKey(client.id))
            .collect(toCollection(LinkedList::new))
            .peek();
    }

    private void doWork(ClientThread client) {
        try {
            Thread.sleep((int) (Math.random() * WORK_TIME));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (client.alreadyCut) {
            System.out.printf("%s received payment from %s\n", this, client);

            client.alreadyPaid = true;
            removeBusy(client.id);
            synchronized (barberShop.leaving) {
                barberShop.leaving.notify();
            }
        } else {
            System.out.printf("%s finished the haircut of %s\n", this, client);

            client.alreadyCut = true;
            removeBusy(client.id);
            synchronized (barberShop.paying) {
                barberShop.paying.notify();
            }
        }
    }

    private void addBusy(Integer clientId) {
        synchronized (barberShop.busy) {
            barberShop.busy.put(clientId, id);
        }
    }

    private void removeBusy(Integer clientId) {
        synchronized (barberShop.busy) {
            barberShop.busy.remove(clientId);
        }
    }

    @Override
    public String toString() {
        return format("Barber %s", id);
    }

}
