package br.com.feevale.classes.BarberShop;

import br.com.feevale.classes.Barber.Barber;
import br.com.feevale.classes.BarberShop.Queues.ClientQueue;
import br.com.feevale.classes.BarberShop.Queues.StandingQueue;
import br.com.feevale.classes.Client.Client;

import java.util.*;

public class BarberShop {

    private boolean barberAvailable = false;
    public ClientQueue standing = new StandingQueue(13, "Standing", this);
    public ClientQueue couch = new ClientQueue(4, "Couch", this);
    public ClientQueue paying = new ClientQueue(20, "Paying", this);
    public List<Client> chairs = new ArrayList<Client>();

    public synchronized boolean isBarberShopFull() {
        return standing.getQueueSize() + couch.getQueueSize() + paying.getQueueSize() + chairs.size() == 20;
    }

    public synchronized boolean isChairsFull() {
        return chairs.size() == 3;
    }

    public void openBarberShop() {
        for (int i = 0; i < 3; i++) {
            Barber barber = new Barber(this, "jorge "+i);
            barber.start();
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

    public synchronized Queue<Client> getPaying() { return paying.getQueue(); }

}
