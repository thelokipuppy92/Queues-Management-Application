package com.example.pt2024_group5_moga_diana_assignment2;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class Controller {

    @FXML
    private TextField maxSimulationTimeField;

    @FXML
    private TextField numOfQueuesField;

    @FXML
    private TextArea clientTextArea;

    @FXML
    private Button generateButton;

    @FXML
    private TextField numClientsField;

    @FXML
    private TextField minArrivalTimeField;

    @FXML
    private TextField maxArrivalTimeField;

    @FXML
    private TextField minServiceTimeField;

    @FXML
    private TextField maxServiceTimeField;

    private SimulationManager simulationManager;

    @FXML
    private TextArea simulatorTextArea;

    @FXML
    private ComboBox<String> strategyComboBox;


    @FXML
    public static void changeScene() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("controller.fxml"));
        Scene scene = new Scene(fxmlLoader.load()); // scene

        Controller controller = fxmlLoader.getController();

        Stage stage = HelloApplication.getPrimaryStage();
        stage.hide();
        stage.setTitle("Controller");
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        strategyComboBox.getItems().addAll("SHORTEST_TIME_STRATEGY", "SHORTEST_QUEUE_STRATEGY");
    }


    @FXML
    private void handleGenerateClientsButtonClick() {
        try {
            int numClients = Integer.parseInt(numClientsField.getText());
            int minArrivalTime = Integer.parseInt(minArrivalTimeField.getText());
            int maxArrivalTime = Integer.parseInt(maxArrivalTimeField.getText());
            int minServiceTime = Integer.parseInt(minServiceTimeField.getText());
            int maxServiceTime = Integer.parseInt(maxServiceTimeField.getText());
            int numOfQueues = Integer.parseInt(numOfQueuesField.getText());
            int maxSimulationTime = Integer.parseInt(maxSimulationTimeField.getText());

            clientTextArea.clear();

            SimulationManager simulationManager = new SimulationManager(numClients, minArrivalTime, maxArrivalTime,
                    minServiceTime, maxServiceTime, numOfQueues,
                    maxSimulationTime);

            Logger logger = new Logger();

            simulationManager.setLogger(logger);

            simulationManager.setSimulatorTextArea(simulatorTextArea);

            // Strategy selection
            SelectionPolicy policy = SelectionPolicy.valueOf(strategyComboBox.getValue());
            simulationManager.getScheduler().changeStrategy(policy);

            List<Client> clients = simulationManager.getClients();
            for (Client client : clients) {
                clientTextArea.appendText("Client ID: " + client.getId() + ", Arrival Time: " + client.getArrivalTime() + ", Service Time: " + client.getServiceTime() + "\n");
            }

            setSimulationManager(simulationManager);

            Thread simulationThread = new Thread(() -> simulationManager.run());
            simulationThread.start();

        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid input. Please enter numeric values in the text fields.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Invalid selection policy.");
        }
    }


    public void setSimulationManager(SimulationManager simulationManager) {
        this.simulationManager = simulationManager;
    }

    public void updateSimulatorTextArea(String event) {
        Platform.runLater(() -> simulatorTextArea.appendText(event + "\n"));
    }



}

