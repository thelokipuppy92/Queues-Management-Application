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
        strategy =new ShortestTimeStrategy();
    }


    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_TIME_STRATEGY) {
            strategy = new ShortestTimeStrategy();
        } else if (policy == SelectionPolicy.SHORTEST_QUEUE_STRATEGY) {
            strategy = new ShortestQueueStrategy();
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


    public int getNumOfQueues() {
        return maxNumQueues;
    }


}
