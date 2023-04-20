//package br.com.feevale.classes;
//
//import java.util.AbstractQueue;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Queue;
//
//public class Barbearia {
//
//    private final Queue<Cliente> sofa;
//    private final Queue<Cliente> standingUp;
//    private final Queue<Cliente> chairs;
//    private final Queue<Cliente> paing;
//    public static final int MAX_SOFA = 4;
//    public static final int MAX_CHAIRS = 3;
//    public static final int MAX_STANDING_UP = 13;
//
//    public Barbearia() {
//        sofa = new AbstractQueue<Cliente>() {
//        }
//    }
//
//    public void addEspera(final Cliente cliente) {
//        clientes.add(cliente);
//        System.out.printf("%s chegou na barbearia.\n", cliente.getNome());
//
//        synchronized (this) {
//            this.notifyAll();
//        }
//    }
//
//    public Cliente atender() {
//        return !clientes.isEmpty() ? clientes.get(0) : null;
//    }
//
//    public void liberar(final Cliente cliente) {
//        clientes.remove(cliente);
//        System.out.printf("%s saiu da barbearia.\n", cliente.getNome());
//    }
//
//    public boolean isBarberShopFull() {
//        return sofa + standingUp + chairs == MAX_SOFA + MAX_STANDING_UP + MAX_CHAIRS;
//    }
//    public boolean isSofaFull() {
//        return sofa == MAX_SOFA;
//    }
//    public boolean isChairsFull() {
//        return chairs == MAX_CHAIRS;
//    }
//
//}
