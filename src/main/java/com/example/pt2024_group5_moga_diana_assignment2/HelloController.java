package com.example.pt2024_group5_moga_diana_assignment2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class HelloController {
    @FXML
    protected void change() throws IOException {
        Controller.changeScene();
    }

}