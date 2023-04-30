package br.com.feevale.antigo.classes.BarberShop.Queues;

import br.com.feevale.antigo.classes.BarberShop.BarberShop;
import br.com.feevale.antigo.classes.Client.Client;

public class StandingQueue extends ClientQueue {
    public StandingQueue(int maxLength, String name, BarberShop barberShop) {
        super(maxLength, name, barberShop);
    }

    @Override
    public synchronized boolean addToQueue(Client c) {
        if (this.barberShop.isBarberShopFull()) return false;
        return super.addToQueue(c);
    }
}
