package com.example.pt2024_group5_moga_diana_assignment2;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationManager implements Runnable {
    private Scheduler scheduler;
    private List<Client> clients;
    private Logger logger;
    private int maxSimulationTime;
    private int maxNumberPerQueue = 10;
    private int currentTime = 0;

    public SimulationManager(int numClients, int minArrivalTime, int maxArrivalTime,
                             int minServiceTime, int maxServiceTime, int numOfQueues,
                             int maxSimulationTime) {
        this.scheduler = new Scheduler(numOfQueues, maxNumberPerQueue);
        this.maxSimulationTime = maxSimulationTime;

        this.clients = Client.generateRandomClients(numClients, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime);
    }

    @Override
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(scheduler.getNumOfQueues());

        logInitialSimulationState();

        while (currentTime < maxSimulationTime || !clients.isEmpty()) {
            logEvent("Time " + currentTime);

            logWaitingClients();

            logQueueStatus();

            processClientsForCurrentTimeStep();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            currentTime++;
        }

        executor.shutdown();

        logger.writeLogToFile("simulation.txt");
    }

    private void logInitialSimulationState() {
        // Log initial state
        logEvent("Simulation started.");
    }

    private void processClientsForCurrentTimeStep() {
        Iterator<Client> iterator = clients.iterator();
        while (iterator.hasNext()) {
            Client client = iterator.next();
            if (client.getArrivalTime() <= currentTime) {
                processClient(client);
                iterator.remove();
            }
        }
    }


    private void processClient(Client client) {
        ShortestQueueStrategy shortestQueueStrategy = new ShortestQueueStrategy();
        if (client.getArrivalTime() == currentTime) {
            Queue queue = shortestQueueStrategy.addClientToQueue(scheduler.getQueues(), client);
            if (queue != null) {
                for (Client c : queue.getClients()) {
                    if (c.getArrivalTime() <= currentTime) {
                        c.decreaseServiceTime();
                    }
                }
            }
        }
    }


    private void logEvent(String event) {
        logger.logEvent(event);
    }

    private void logQueueStatus() {
        for (Queue queue : scheduler.getQueues()) {
            StringBuilder queueStatus = new StringBuilder("Queue " + queue.getId() + ": ");
            List<Client> clients = queue.getClients();
            if (!clients.isEmpty()) {
                for (Client client : clients) {
                    queueStatus.append("(").append(client.getId()).append(",").append(client.getArrivalTime()).append(",").append(client.getServiceTime()).append("); ");
                }
            } else {
                queueStatus.append("closed");
            }
            logEvent(queueStatus.toString());
        }
    }

    private void logWaitingClients() {
        StringBuilder waitingClients = new StringBuilder("Waiting clients: ");
        for (Client client : clients) {
            waitingClients.append("(").append(client.getId()).append(",").append(client.getArrivalTime()).append(",").append(client.getServiceTime()).append("); ");
        }
        logEvent(waitingClients.toString());
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public List<Client> getClients() {
        return clients;
    }
}
