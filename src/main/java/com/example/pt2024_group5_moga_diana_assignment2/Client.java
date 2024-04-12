package com.example.pt2024_group5_moga_diana_assignment2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Client {
    int id;
    int arrivalTime;
    int serviceTime;
    int initialServiceTime;

    private boolean hasBeenInQueue;

    // Constructor
    public Client(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.initialServiceTime =serviceTime;
        this.hasBeenInQueue=false;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    // Method to decrease service time
    public void decreaseServiceTime() {
        if (serviceTime > 0) {
            serviceTime--;
        }
    }

    // Random client generator method
    public static List<Client> generateRandomClients(int numClients, int minArrivalTime, int maxArrivalTime,
                                                     int minServiceTime, int maxServiceTime) {
        List<Client> clients = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numClients; i++) {
            int id = i + 1; // IDs start from 1
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int serviceTime = random.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;
            clients.add(new Client(id, arrivalTime, serviceTime));
        }

        return clients;
    }


    public void setInitialServiceTime() {
        this.serviceTime = initialServiceTime; // Reset service time to initial value
    }

    public int getInitialServiceTime() {
        return initialServiceTime;
    }

    public boolean hasDecrementedServiceTime() {
        return serviceTime < initialServiceTime;
    }

    public int getMaxServiceTime() {
        return initialServiceTime;
    }

    public boolean hasBeenInQueue() {
        return hasBeenInQueue;
    }

    public void setHasBeenInQueue(boolean hasBeenInQueue) {
        this.hasBeenInQueue = hasBeenInQueue;
    }

}
