
package com.example.pt2024_group5_moga_diana_assignment2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Queue implements Runnable {
    private static int nextId = 1;
    private int id; // Queue ID
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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while adding client to queue: " + e.getMessage());
        }
    }

    public Client getNextClient() {
        return clients.peek();
    }

    public void processNextClient() {
        Client client = clients.poll();
        if (client != null) {
            client.decreaseServiceTime();
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
        while (running) {
            if (!isEmpty()) {
                processNextClient();
                if (!isEmpty()) {
                    StringBuilder queueStatus = new StringBuilder("Queue " + getId() + ": ");
                    for (Client client : getClients()) {
                        queueStatus.append("(")
                                .append(client.getId())
                                .append(",")
                                .append(client.getArrivalTime())
                                .append(",")
                                .append(client.getServiceTime())
                                .append("); ");
                    }
                    logEvent(queueStatus.toString());
                } else {
                    logEvent("Queue " + getId() + ": Empty");
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Interrupted while sleeping: " + e.getMessage());
                }
            }
        }
    }

    private void logEvent(String event) {
        System.out.println(event);
    }

}
