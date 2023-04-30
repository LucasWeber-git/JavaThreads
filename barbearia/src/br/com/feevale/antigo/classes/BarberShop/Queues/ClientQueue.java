package br.com.feevale.antigo.classes.BarberShop.Queues;

import br.com.feevale.antigo.classes.BarberShop.BarberShop;
import br.com.feevale.antigo.classes.Client.Client;

import java.util.LinkedList;
import java.util.Queue;

public class ClientQueue {
    private Queue<Client> queue = new LinkedList<>();
    private int maxLength;
    public String name;
    public BarberShop barberShop;

    public ClientQueue(int maxLength, String name, BarberShop barberShop) {
        this.maxLength = maxLength;
        this.name = name;
        this.barberShop = barberShop;
    }

    public synchronized boolean addToQueue(Client c) {
        if(isQueueFull()) return false;

        queue.add(c);
        messageQueue("Client " + c.id + " get into " + name + " line ");
        return true;
    }

    public synchronized boolean removeFromQueue() {
        if(queue != null && queue.size() <= 0) return false;

        Client c = queue.poll();
        messageQueue("Client " + c.id + " got out of " + name + " line ");
        return true;
    }

    public boolean isFirstClientinLine(int id) { return queue != null && id == queue.peek().id; }

    public synchronized boolean isQueueFull() { return queue != null && queue.size() >= maxLength; }

    public synchronized int getQueueSize() { return queue != null ? queue.size() : 0; }

    public synchronized boolean hasAnyClientInQueue() { return queue != null && queue.size() > 0; }

    public synchronized void messageQueue(String msg) { System.out.println( msg + name + " => " + queue.size()); }
    public synchronized Queue<Client> getQueue() {return queue; }
}
