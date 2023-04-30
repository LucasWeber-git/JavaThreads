package br.com.feevale.novo.classes;

import static java.lang.String.format;

public class ClientThread extends Thread {

    public int id;
    public boolean alreadyCut = false;
    public boolean alreadyPaid = false;

    private final BarberShop barberShop;

    public ClientThread(int id, BarberShop barberShop) {
        super("Client" + id);

        this.id = id;
        this.barberShop = barberShop;
    }

    @Override
    public void run() {
        try {
            enter();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    private void enter() throws InterruptedException {
        stand();
        sitOnSofa();
        sitOnChair();
        pay();
        leave();
    }

    private void stand() {
        synchronized (barberShop.standing) {
            barberShop.standing.add(this);
            System.out.printf("%s is standing\n", this);
        }
    }

    private void sitOnSofa() throws InterruptedException {
        synchronized (barberShop.sofa) {
            while (barberShop.isSofaUnavailable()) {
                barberShop.sofa.wait();
            }
            barberShop.sofa.add(this);
            System.out.printf("%s seated on sofa\n", this);
            synchronized (barberShop.standing) {
                barberShop.standing.remove(this);
            }
        }
    }

    private void sitOnChair() throws InterruptedException {
        synchronized (barberShop.chairs) {
            while (barberShop.isChairUnavailable()) {
                barberShop.chairs.wait();
            }
            barberShop.chairs.add(this);
            System.out.printf("%s seated on chair\n", this);
            synchronized (barberShop.sofa) {
                barberShop.sofa.remove(this);
                barberShop.sofa.notify();
            }
            synchronized (barberShop) {
                barberShop.notify();
            }
        }
        while (!alreadyCut) {
            synchronized (barberShop.paying) {
                barberShop.paying.wait();
            }
        }
    }

    private void pay() throws InterruptedException {
        synchronized (barberShop.paying) {
            while (barberShop.isPaymentUnavailable()) {
                barberShop.paying.wait();
            }
            barberShop.paying.add(this);
            synchronized (barberShop.chairs) {
                barberShop.chairs.remove(this);
                barberShop.chairs.notify();
            }
        }
        while (!alreadyPaid) {
            synchronized (barberShop.leaving) {
                barberShop.leaving.wait();
            }
        }
    }

    private void leave() {
        synchronized (barberShop.paying) {
            barberShop.paying.remove(this);
            barberShop.paying.notify();
            System.out.printf("%s left\n", this);
        }
    }

    @Override
    public String toString() {
        return format("Client %s", id);
    }

}
