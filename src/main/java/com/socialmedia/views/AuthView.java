package com.socialmedia.views;

import com.socialmedia.Main;
import com.socialmedia.dao.UserDAO;
import com.socialmedia.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AuthView {

    private Stage stage;
    private UserDAO userDAO;

    public AuthView(Stage stage) {
        this.stage = stage;
        this.userDAO = new UserDAO();
    }

    public void show() {
        TabPane tabPane = new TabPane();
        Tab loginTab = new Tab("Login");
        loginTab.setContent(createLoginTab());
        loginTab.setClosable(false);

        Tab registerTab = new Tab("Register");
        registerTab.setContent(createRegisterTab());
        registerTab.setClosable(false);

        tabPane.getTabs().addAll(loginTab, registerTab);

        Scene scene = new Scene(tabPane, 400, 350);
        stage.setTitle("Social Media App - Authentication");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createLoginTab() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Login to your account");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginBtn = new Button("Login");
        Label messageLabel = new Label();

        loginBtn.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            User user = userDAO.loginUser(email, password);
            if (user != null) {
                messageLabel.setText("Login successful!");
                messageLabel.setStyle("-fx-text-fill: green;");
                Main.currentUser = user;
                new ProfileView(stage).show();
            } else {
                messageLabel.setText("Invalid credentials!");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        vbox.getChildren().addAll(titleLabel, emailField, passwordField, loginBtn, messageLabel);
        return vbox;
    }

    private VBox createRegisterTab() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Create a new account");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button registerBtn = new Button("Register");
        Label messageLabel = new Label();

        registerBtn.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                messageLabel.setText("All fields are required!");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            boolean success = userDAO.registerUser(name, email, password);
            if (success) {
                messageLabel.setText("Registration successful! Please login.");
                messageLabel.setStyle("-fx-text-fill: green;");
                nameField.clear();
                emailField.clear();
                passwordField.clear();
            } else {
                messageLabel.setText("Registration failed. Email might be in use.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        vbox.getChildren().addAll(titleLabel, nameField, emailField, passwordField, registerBtn, messageLabel);
        return vbox;
    }
}
