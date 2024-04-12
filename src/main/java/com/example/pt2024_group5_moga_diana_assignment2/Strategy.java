package com.example.pt2024_group5_moga_diana_assignment2;

import java.util.List;

public interface Strategy {
    Queue addClientToQueue(List<Queue> queues, Client client);
}
