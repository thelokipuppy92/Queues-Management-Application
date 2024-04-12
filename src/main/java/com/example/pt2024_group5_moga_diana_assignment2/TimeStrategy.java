package com.example.pt2024_group5_moga_diana_assignment2;

import java.util.List;

public class TimeStrategy implements Strategy {

    @Override
    public Queue addClientToQueue(List<Queue> queues, Client client) {
        Queue earliestQueue = null;
        int minArrivalTime = Integer.MAX_VALUE;

        for (Queue queue : queues) {
            Client nextClient = queue.getNextClient();
            if (nextClient != null && nextClient.getArrivalTime() < minArrivalTime) {
                earliestQueue = queue;
                minArrivalTime = nextClient.getArrivalTime();
            }
        }

        if (earliestQueue != null) {
            earliestQueue.addClient(client);
            return earliestQueue;
        } else {
            System.out.println("No clients available in queues.");
            return null;
        }
    }

}