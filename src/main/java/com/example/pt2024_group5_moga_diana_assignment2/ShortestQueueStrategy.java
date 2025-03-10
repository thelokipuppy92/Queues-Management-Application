package com.example.pt2024_group5_moga_diana_assignment2;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {

    @Override
    public Queue addClientToQueue(List<Queue> queues, Client client) {
        // Find the first empty queue
        for (Queue queue : queues) {
            if (queue.isEmpty()) {
                queue.addClient(client);
                return queue;
            }
        }
        // If all queues are occupied, find the queue with the fewest clients
        Queue shortestQueue = null;
        int minQueueSize = Integer.MAX_VALUE;

        for (Queue queue : queues) {
            int queueSize = queue.getSize();
            if (queueSize < minQueueSize) {
                shortestQueue = queue;
                minQueueSize = queueSize;
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

