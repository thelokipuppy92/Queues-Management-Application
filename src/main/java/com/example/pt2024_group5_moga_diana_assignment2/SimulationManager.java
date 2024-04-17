package com.example.pt2024_group5_moga_diana_assignment2;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationManager implements Runnable {
    private Scheduler scheduler;
    private List<Client> clients;
    private List<Client> originalClients;
    private Logger logger;
    private int maxSimulationTime;
    private int maxNumberPerQueue = 10;
    private int currentTime = 0;
    private Controller controller;
    private TextArea simulatorTextArea;
    private double totalServiceTime = 0.0;
    private Map<Integer, List<Integer>> clientsInQueues;
    private double totalWaitingTime = 0.0;
    public void setSimulatorTextArea(TextArea simulatorTextArea) {
        this.simulatorTextArea = simulatorTextArea;
    }


    public Scheduler getScheduler() {
        return scheduler;
    }

    public SimulationManager(int numClients, int minArrivalTime, int maxArrivalTime,
                             int minServiceTime, int maxServiceTime, int numOfQueues,
                             int maxSimulationTime) {
        this.scheduler = new Scheduler(numOfQueues, maxNumberPerQueue);
        this.maxSimulationTime = maxSimulationTime;

        this.clientsInQueues = new HashMap<>();

        this.originalClients=Client.generateRandomClients(numClients, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime);
        this.clients = new ArrayList<>(originalClients);
    }

    private void stopQueues() {
        for (Queue q : scheduler.getQueues()) {
            q.stop();
        }
    }


    @Override
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(scheduler.getNumOfQueues());
        try {
            for (Queue q : scheduler.getQueues()) {
                executor.execute(q);
            }

            logInitialSimulationState();

            while (currentTime < maxSimulationTime) {
                processClientsForCurrentTimeStep();
                logEvent("Time " + currentTime);
                System.out.println("Time " + currentTime);

                logWaitingClients();
                logQueueStatus();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                currentTime++;
            }

            double averageServiceTime = calculateAverageServiceTime();
            int peakHour = findPeakHour();
            double averageWaitingTime = calculateAverageWaitingTime();

            // Log the calculated values
            logEvent("Average Waiting Time: " + averageWaitingTime);
            logEvent("Average Service Time: " + averageServiceTime);
            logEvent("Peak Hour: " + peakHour);

            // Write the calculated values to simulation.txt
            logger.writeLogToFile("simulation.txt");
            logger.logEvent("Average Waiting Time: " + averageWaitingTime);
            logger.logEvent("Average Service Time: " + averageServiceTime);
            logger.logEvent("Peak Hour: " + peakHour);


            System.out.println("DONE");
        } finally {
            executor.shutdown();

            stopQueues();
        }
    }


    private void logInitialSimulationState() {
        logEvent("Simulation started.");
    }


    private void processClientsForCurrentTimeStep() {
        Iterator<Client> iterator = clients.iterator();
        while (iterator.hasNext()) {
            Client client = iterator.next();
            if (client.getArrivalTime() == currentTime) {
                scheduler.dispatchClient(client);
                totalServiceTime += client.getServiceTime();
                iterator.remove();
            }
        }
    }


    private void logEvent(String event) {
        // Check if the event contains "Time"
        if (event.matches(".*Time \\d+.*")) {
            // Clear the text area before logging information for the current time step
            Platform.runLater(() -> simulatorTextArea.clear());
        }

        if (logger != null) {
            logger.logEvent(event);
        }
        if (controller != null) {
            controller.updateSimulatorTextArea(event);
        }
        Platform.runLater(() -> simulatorTextArea.appendText(event + "\n"));
    }


    private void logQueueStatus() {
        for (Queue queue : scheduler.getQueues()) {
            int queueId = queue.getId();
            List<Client> clients = queue.getClients();
            int currentCount = clients.size(); // Get the current number of clients in the queue
            List<Integer> clientsCountList = clientsInQueues.getOrDefault(queueId, new ArrayList<>());
            clientsCountList.add(currentTime, currentCount); // Add the current count at the current time step
            clientsInQueues.put(queueId, clientsCountList);

            StringBuilder queueStatus = new StringBuilder("Queue " + queueId + ": ");
            if (!clients.isEmpty()) {
                for (Client client : clients) {
                    int positionInQueue = clients.indexOf(client);

                    // Calculate the time when service starts for the client based on their position in the queue
                    int timeServiceStarted = 0;
                    if (positionInQueue == 0) {
                        timeServiceStarted = currentTime;
                    } else if (positionInQueue > 0) {
                        // If the client is not at the front, calculate the remaining service times of clients ahead
                        int remainingServiceTime = 0;
                        for (int i = 0; i < positionInQueue; i++) {
                            remainingServiceTime += clients.get(i).getServiceTime();
                        }
                        // Service starts when all the clients ahead finish their service
                        timeServiceStarted = currentTime + remainingServiceTime;
                    }

                    // Calculate the waiting time for the client
                    int waitingTime = timeServiceStarted - client.getArrivalTime();
                    totalWaitingTime += waitingTime;

                    queueStatus.append("(").append(client.getId()).append(",").append(client.getArrivalTime()).append(",").append(client.getServiceTime()).append("); ");
                }
            } else {
                queueStatus.append("closed");
            }
            logEvent(queueStatus.toString());
            System.out.println(queueStatus.toString());
        }
        System.out.println("Clients in Queues: " + clientsInQueues);
    }



    private void logWaitingClients() {
        StringBuilder waitingClients = new StringBuilder("Waiting clients: ");
        for (Client client : clients) {
            waitingClients.append("(").append(client.getId()).append(",").append(client.getArrivalTime()).append(",").append(client.getServiceTime()).append("); ");
        }
        logEvent(waitingClients.toString());
        System.out.println(waitingClients.toString());
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public List<Client> getClients() {
        return clients;
    }

    public double calculateAverageWaitingTime() {
        int totalClients = originalClients.size();
        double averageWaitingTime = totalWaitingTime / totalClients;
        return averageWaitingTime;
    }

    public double calculateAverageServiceTime() {
        int totalClients = originalClients.size();
        double averageServiceTime = totalServiceTime / totalClients;
        return averageServiceTime;
    }


    public int findPeakHour() {
        int peakHour = 0;
        int maxTotalClients = 0;

        for (int hour = 0; hour <= maxSimulationTime; hour++) {
            int totalClients = 0;

            for (List<Integer> queueClients : clientsInQueues.values()) {
                if (hour < queueClients.size()) {
                    totalClients += queueClients.get(hour);
                }
            }

            if (totalClients > maxTotalClients) {
                maxTotalClients = totalClients;
                peakHour = hour;
            }
        }

        return peakHour;
    }




}

