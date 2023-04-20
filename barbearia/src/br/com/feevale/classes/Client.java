package br.com.feevale.classes;

import br.com.feevale.domain.ClientStatus;

public class Client extends Thread {
    private int id;
    private ClientStatus stauts;
    private BarberShop barberShop;
    private int sleepTime = 1000;

    public Client(int id, BarberShop barberShop) {
        this.id = id;
        this.barberShop = barberShop;
        locateClientInBarberShop();
    }
    @Override
    public void run() {
        super.run();
        while(stauts != ClientStatus.LEAVING) {
            locateClientInBarberShop();
            waitToRelocate();
        }
    }

    private void locateClientInBarberShop() {
        if(!barberShop.isBarberShopFull()) {
            if(stauts == null) {
                if (!barberShop.isAnyoneWaitingInSofa() && !barberShop.isChairsFull()) goToChair();
                else if (!barberShop.isSofaFull()) goToSofa();
                else standingUp();
            }
            else if (stauts == ClientStatus.STANDING && !barberShop.isSofaFull() && barberShop.standing.peek().id == id) goToSofa();
            else if (stauts == ClientStatus.SEATED_IN_SOFA && !barberShop.isChairsFull() && barberShop.sofa.peek().id == id) goToChair();
            else if (stauts == ClientStatus.WAITING_BARBER && barberShop.isBarberAvailable()) cuttingHair();
            else if (stauts == ClientStatus.CUTTING_HAIR) goPay();
            else if (stauts == ClientStatus.PAYNG && barberShop.paing.peek().id == id) leave();
        };
    }

    private void goToChair() {
        if((stauts != null && stauts == ClientStatus.SEATED_IN_SOFA)) barberShop.sofa.poll();
        stauts = ClientStatus.WAITING_BARBER;
        barberShop.chairs.add(this);
        System.out.println("Client: " + id + " is waiting to cut hair");
    }
    private void cuttingHair() {
        stauts = ClientStatus.CUTTING_HAIR;
        System.out.println("Client: " + id + " getting a hair cut");
        sleepTime = 2000;
    }
    private void goToSofa() {
        if((stauts != null && stauts == ClientStatus.STANDING)) barberShop.standing.poll();
        stauts = ClientStatus.SEATED_IN_SOFA;
        System.out.println("Client " + id + " sat on the sofa");
        barberShop.sofa.add(this);
    }
    private void standingUp() {
        stauts = ClientStatus.STANDING;
        barberShop.standing.add(this);
        System.out.println("Client " + id + " is standing");
    }
    private void goPay() {
        sleepTime = 1000;
        stauts = ClientStatus.PAYNG;
        barberShop.chairs.remove(this);
        barberShop.paing.add(this);
        System.out.println("Client " + id + " waitting to pay");
    }
    private void leave() {
        stauts = ClientStatus.LEAVING;
        System.out.println("Client " + id + " leaved happy with the new cut");
        barberShop.paing.poll();
    }
    private void waitToRelocate() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
