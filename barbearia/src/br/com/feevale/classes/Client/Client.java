package br.com.feevale.classes.Client;

import br.com.feevale.classes.BarberShop.BarberShop;
import br.com.feevale.domain.ClientStatus;

public class Client extends Thread {
    public int id;
    private ClientStatus status;
    private BarberShop barberShop;
    private int sleepTime = 1000;

    public Client(int id, BarberShop barberShop) {
        this.id = id;
        this.barberShop = barberShop;

        if (!barberShop.isBarberShopFull()) {
            locateClientInBarberShop();
        }
    }

    @Override
    public void run() {
        super.run();
        while (status != ClientStatus.LEAVING) {
            locateClientInBarberShop();
            waitToRelocate();
        }
    }

    private void locateClientInBarberShop() {
        if (status == null) {
//             if (!barberShop.couch.hasAnyClientInQueue() && !barberShop.isChairsFull()) goToChair();
//             else if (!barberShop.couch.isQueueFull()) goToSofa();
//             else
            standingUp();
        } else if (status == ClientStatus.STANDING && !barberShop.couch.isQueueFull() && barberShop.standing.isFirstClientinLine(this.id))
            goToSofa();
        else if (status == ClientStatus.SEATED_IN_SOFA && !barberShop.isChairsFull() && barberShop.couch.isFirstClientinLine(id))
            goToChair();
    }

    private void goToChair() {
        if ((status != null && status == ClientStatus.SEATED_IN_SOFA)) {
            barberShop.couch.removeFromQueue();
        }

        status = ClientStatus.WAITING_BARBER;

        synchronized (barberShop.chairs) {
            barberShop.chairs.add(this);
            System.out.println("Client: " + id + " is waiting to cut hair");
        }
    }

    private synchronized void goToSofa() {
//        if (status == null || (status != null && status == ClientStatus.SEATED_IN_SOFA)) {
        barberShop.standing.removeFromQueue();
//        }
        status = ClientStatus.SEATED_IN_SOFA;
        barberShop.couch.addToQueue(this);
    }

    private void standingUp() {
        status = ClientStatus.STANDING;
        barberShop.standing.addToQueue(this);
    }

    public void goPay() {
        status = ClientStatus.PAYNG;
        synchronized (barberShop.chairs) {
            barberShop.chairs.remove(this);
        }
        barberShop.paying.addToQueue(this);
    }

    public void leave() {
        status = ClientStatus.LEAVING;
        System.out.println("Client " + id + " leaved happy with the new cut");
        barberShop.paying.removeFromQueue();
    }

    private void waitToRelocate() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ClientStatus getStatus() {
        return status;
    }

}
