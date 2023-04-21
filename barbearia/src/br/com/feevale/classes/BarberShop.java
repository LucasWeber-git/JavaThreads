package br.com.feevale.classes;

import java.util.*;

public class BarberShop {

    private boolean barberAvailable = false;

    public Queue<Client> standing = new LinkedList<>();
    public Queue<Client> sofa = new LinkedList<>();
    public Queue<Client> paying = new LinkedList<>();
    public List<Client> chairs = new ArrayList<Client>();

    public boolean isBarberShopFull() {
        return standing.size() + sofa.size() + paying.size() + chairs.size() == 20;
    }

    public boolean isAnyoneWaitingInSofa() {
        return sofa.size() > 0;
    }

    public boolean isSofaFull() {
        return sofa.size() == 4;
    }

    public boolean isChairsFull() {
        return chairs.size() == 3;
    }

    public boolean isBarberAvailable() {
        return barberAvailable;
    }

    public void openBarberShop() {
        for (int i = 0; i < 3; i++) {
            Barbeiro barbeiro = new Barbeiro(this, "jorge"+i);
            barbeiro.start();
        }

        List<Thread> clients = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            if (!isBarberShopFull()) {
                Client client = new Client(i, this);
                clients.add(client);
            }
        }
        for (Thread client : clients) {
            client.start();
        }
    }

    public void setBarberAvailable(boolean barberAvailable) {
        this.barberAvailable = barberAvailable;
    }
    
    public synchronized List<Client> getChairs() {
        return chairs;
    }

    public synchronized Queue<Client> getPaying() {
        return paying;
    }

}
