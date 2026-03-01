package com.socialmedia.views;

import com.socialmedia.Main;
import com.socialmedia.dao.UserDAO;
import com.socialmedia.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class SearchView {

    private Stage stage;
    private UserDAO userDAO;

    public SearchView(Stage stage) {
        this.stage = stage;
        this.userDAO = new UserDAO();
    }

    public void show() {
        if (Main.currentUser == null)
            return;

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        HBox headerBox = new HBox(15);
        Label titleLabel = new Label("Search Users");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Button backBtn = new Button("Back to Feed");
        backBtn.setOnAction(e -> new FeedView(stage).show());
        headerBox.getChildren().addAll(titleLabel, backBtn);

        HBox searchBox = new HBox(10);
        TextField searchField = new TextField();
        searchField.setPromptText("Search by name...");
        Button searchBtn = new Button("Search");
        searchBox.getChildren().addAll(searchField, searchBtn);

        VBox resultsBox = new VBox(10);

        searchBtn.setOnAction(e -> {
            resultsBox.getChildren().clear();
            String q = searchField.getText().trim();
            if (!q.isEmpty()) {
                List<User> found = userDAO.searchUsers(q);
                if (found.isEmpty()) {
                    resultsBox.getChildren().add(new Label("No users found matching your query."));
                } else {
                    for (User u : found) {
                        HBox userRow = new HBox(10);
                        Label nameLbl = new Label(u.getName() + " (" + u.getEmail() + ")");
                        Button profileBtn = new Button("View Profile");
                        profileBtn.setOnAction(ev -> {
                            // Can add navigation to another user's profile view here in the future
                            // For now, simple alert
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("User Profile");
                            alert.setHeaderText(u.getName());
                            alert.setContentText("Email: " + u.getEmail() + "\nJoined: " + u.getCreatedAt());
                            alert.showAndWait();
                        });
                        userRow.getChildren().addAll(nameLbl, profileBtn);
                        resultsBox.getChildren().add(userRow);
                    }
                }
            }
        });

        root.getChildren().addAll(headerBox, searchBox, resultsBox);

        Scene scene = new Scene(root, 500, 600);
        stage.setTitle("Social Media App - Search");
        stage.setScene(scene);
        stage.show();
    }
}
