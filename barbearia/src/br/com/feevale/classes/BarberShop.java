package br.com.feevale.classes;

import java.util.*;

public class BarberShop {
    public Queue<Client> standing = new LinkedList<>();
    public Queue<Client> sofa = new LinkedList<>();
    public Queue<Client> paing = new LinkedList<>();
    public List chairs = new ArrayList<Client>();

    public boolean isBarberShopFull() { return standing.size() + sofa.size() + paing.size() + chairs.size() == 20; }
    public boolean isAnyoneWaitingInSofa() { return sofa.size() > 0; }
    public boolean isSofaFull() { return sofa.size() == 4; }
    public boolean isChairsFull() { return chairs.size() == 3; }
    public boolean isBarberAvailable() { return false; }

    public void openBarberShop() {
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


}
