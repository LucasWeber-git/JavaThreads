package br.com.feevale.novo.classes;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class BarberShop {

    private static final int TIME_BETWEEN_CLIENTS = 2000;

    public final Queue<ClientThread> standing = new LinkedList<>();
    public final Queue<ClientThread> sofa = new LinkedList<>();
    public final Queue<ClientThread> chairs = new LinkedList<>();
    public final Queue<ClientThread> paying = new LinkedList<>();
    public final Map<Integer, Integer> busy = new HashMap<>();
    public final Object leaving = new Object();

    private final int barbersQty;
    private final int capacity;

    public BarberShop(int barbersQty, int capacity) {
        this.barbersQty = barbersQty;
        this.capacity = capacity;
    }

    public boolean isSpaceAvailable() {
        return getTotalSize() < capacity;
    }

    public boolean isSofaUnavailable() {
        return sofa.size() >= 4;
    }

    public boolean isChairUnavailable() {
        return chairs.size() >= 3;
    }

    public boolean isPaymentUnavailable() {
        return paying.size() >= 1;
    }

    public void openBarberShop() {
        generateBarbers();
        generateClients();
    }

    private void generateBarbers() {
        for (int i = 1; i <= barbersQty; i++) {
            BarberThread barber = new BarberThread(i, this);
            barber.start();
        }
    }

    private void generateClients() {
        int i = 1;
        while (true) {
            if (isSpaceAvailable()) {
                ClientThread client = new ClientThread(i++, this);
                client.start();
            }
            System.out.println(this);

            try {
                Thread.sleep((int) (Math.random() * TIME_BETWEEN_CLIENTS));
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    private int getTotalSize() {
        synchronized (standing) {
            synchronized (sofa) {
                synchronized (chairs) {
                    synchronized (paying) {
                        return standing.size() + sofa.size() + chairs.size() + paying.size();
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        synchronized (standing) {
            synchronized (sofa) {
                synchronized (chairs) {
                    synchronized (paying) {
                        return format("Standing: %s, sofa: %s, chairs: %s, paying: %s, total: %s",
                            standing, sofa, chairs, paying, getTotalSize());
                    }
                }
            }
        }
    }

}
