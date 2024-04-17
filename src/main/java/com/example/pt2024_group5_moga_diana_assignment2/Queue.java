
package com.example.pt2024_group5_moga_diana_assignment2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Queue implements Runnable {
    private static int nextId = 1;
    private int id;
    private BlockingQueue<Client> clients;
    private volatile boolean running;
    private boolean clientsWithDecreasedServiceTime;

    public Queue() {
        clients = new LinkedBlockingQueue<>();
        this.id = nextId++;
        this.running = true;
        this.clientsWithDecreasedServiceTime = false;
    }

    public void addClient(Client client) {
        try {
            clients.put(client);
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while adding client to queue: " + e.getMessage());
        }
    }


    public void processNextClient() {
        Client client = clients.peek();
        if(client != null) {
            if(client.serviceTime == 1) {
                clients.remove(client);
            } else{
                client.decreaseServiceTime();
            }
        }
    }

    public boolean isEmpty() {
        return clients.isEmpty();
    }

    public int getSize() {
        return clients.size();
    }

    public int getId() {
        return id;
    }

    public List<Client> getClients() {
        List<Client> clientList = new ArrayList<>();
        clients.forEach(clientList::add);
        return clientList;
    }

    @Override
    public void run() {
        while(running) {
            processNextClient();
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Interrupted while sleeping: " + e.getMessage());

            }
        }
    }

    public void stop() {
        running = false;
    }


    public int getServiceTimeBeforeClient(Client client) {
        int serviceTimeBeforeClient = 0;
        for (Client c : clients) {
            if (c != client) {
                serviceTimeBeforeClient += c.getServiceTime();
            } else {
                break;
            }
        }
        return serviceTimeBeforeClient;
    }
}
