package com.example.pt2024_group5_moga_diana_assignment2;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Queue> queues;
    private int maxNumQueues;
    private int maxClientsPerQueue;
    private Strategy strategy;

    public Scheduler(int maxNumQueues, int maxClientsPerQueue) {
        this.maxNumQueues = maxNumQueues;
        this.maxClientsPerQueue = maxClientsPerQueue;
        this.queues = new ArrayList<>();
        // Create queues
        for (int i = 0; i < maxNumQueues; i++) {
            queues.add(new Queue());
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
        } else if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new TimeStrategy();
        }
    }

public void dispatchClient(Client client) {
    if (strategy != null) {
        Queue selectedQueue = strategy.addClientToQueue(queues, client);
        if (selectedQueue != null) {
            System.out.println("Client " + client.getId() + " added to Queue " + selectedQueue.getId());
        } else {
            System.out.println("No queues available.");
        }
    }
}

    public List<Queue> getQueues() {
        return queues;
    }

    public boolean allQueuesEmpty() {
        for (Queue queue : queues) {
            if (!queue.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public Strategy getStrategy() {
        return strategy;
    }


    public Queue findShortestQueue() {
        Queue shortestQueue = null;
        int shortestQueueSize = Integer.MAX_VALUE;
        for (Queue queue : queues) {
            if (queue.getSize() < shortestQueueSize) {
                shortestQueue = queue;
                shortestQueueSize = queue.getSize();
            }
        }
        return shortestQueue;
    }

    public int getNumOfQueues() {
        return maxNumQueues;
    }
}
