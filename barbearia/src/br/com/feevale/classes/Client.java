package br.com.feevale.classes;

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
            // if (!barberShop.isAnyoneWaitingInSofa() && !barberShop.isChairsFull())
            // goToChair();
            // else if (!barberShop.isSofaFull()) goToSofa();
            standingUp();
        } else if (status == ClientStatus.STANDING && !barberShop.isSofaFull() && barberShop.standing.peek().id == id)
            goToSofa();
        else if (status == ClientStatus.SEATED_IN_SOFA && !barberShop.isChairsFull() && barberShop.sofa.peek().id == id)
            goToChair();
        // else if (status == ClientStatus.WAITING_BARBER)
        // cuttingHair();
        // else if (status == ClientStatus.PAYNG)
        // goPay();
        // else if (status == ClientStatus.PAYNG && barberShop.paying.peek().id == id)
        // leave();
    }

    private void goToChair() {
        if ((status != null && status == ClientStatus.SEATED_IN_SOFA)) {
            synchronized (barberShop.sofa) {
                barberShop.sofa.poll();
            }
        }

        status = ClientStatus.WAITING_BARBER;

        synchronized (barberShop.chairs) {
            barberShop.chairs.add(this);
        }
        System.out.println("Client: " + id + " is waiting to cut hair");
    }

    // public void cuttingHair() {
    // status = ClientStatus.CUTTING_HAIR;
    // System.out.println("Client: " + id + " getting a hair cut");
    // sleepTime = 2000;
    // }

    private void goToSofa() {
        if ((status != null && status == ClientStatus.STANDING)) {
            synchronized (barberShop.standing) {
                barberShop.standing.poll();
            }
        }

        status = ClientStatus.SEATED_IN_SOFA;

        synchronized (barberShop.sofa) {
            System.out.println("Client " + id + " sat on the sofa. Total: " + barberShop.sofa.size());
            barberShop.sofa.add(this);
        }
    }

    private void standingUp() {
        status = ClientStatus.STANDING;
        synchronized (barberShop.standing) {
            barberShop.standing.add(this);
        }
        System.out.println("Client " + id + " is standing");
    }

    public void goPay() {
        sleepTime = 1000;
        status = ClientStatus.PAYNG;
        synchronized (barberShop.chairs) {
            barberShop.chairs.remove(this);
        }
        synchronized (barberShop.paying) {
            barberShop.paying.add(this);
        }
        System.out.println("Client " + id + " waitting to pay");
    }

    public void leave() {
        status = ClientStatus.LEAVING;
        System.out.println("Client " + id + " leaved happy with the new cut");
        synchronized (barberShop.paying) {
            barberShop.paying.poll();
        }
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
