package com.example.pt2024_group5_moga_diana_assignment2;

import java.util.List;

public class ShortestTimeStrategy implements Strategy {
    @Override
    public Queue addClientToQueue(List<Queue> queues, Client client) {
        Queue shortestQueue = null;
        int minServiceTime = Integer.MAX_VALUE;

        for (Queue queue : queues) {
            // Get the service time of the client in the queue
            int serviceTime = queue.getServiceTimeBeforeClient(client);
            // If the service time in the queue is less than the minimum so far, update the shortest queue
            if (serviceTime < minServiceTime) {
                shortestQueue = queue;
                minServiceTime = serviceTime;
            }
        }
        if (shortestQueue != null) {
            shortestQueue.addClient(client);
            return shortestQueue;
        } else {
            System.out.println("No clients available in queues.");
            return null;
        }
    }
}

