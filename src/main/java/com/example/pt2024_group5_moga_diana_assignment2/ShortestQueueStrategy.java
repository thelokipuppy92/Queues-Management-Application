package com.example.pt2024_group5_moga_diana_assignment2;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {
    @Override
    public Queue addClientToQueue(List<Queue> queues, Client client) {
        Queue shortestQueue = null;
        int minQueueSize = Integer.MAX_VALUE;

        // Find the queue with the fewest clients
        for (Queue queue : queues) {
            int queueSize = queue.getSize();
            if (queueSize < minQueueSize) {
                shortestQueue = queue;
                minQueueSize = queueSize;
            }
        }
        // Add the client to the shortest queue
        if (shortestQueue != null) {
            shortestQueue.addClient(client);
            return shortestQueue;
        } else {
            System.out.println("No queues available.");
            return null;
        }
    }
}
