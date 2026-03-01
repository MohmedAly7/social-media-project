package com.socialmedia.views;

import com.socialmedia.Main;
import com.socialmedia.dao.FriendDAO;
import com.socialmedia.utils.Constants;
import com.socialmedia.db.DatabaseConnection;
import com.socialmedia.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendsView {

    private Stage stage;
    private FriendDAO friendDAO;

    public FriendsView(Stage stage) {
        this.stage = stage;
        this.friendDAO = new FriendDAO();
    }

    public void show() {
        if (Main.currentUser == null)
            return;

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        HBox headerBox = new HBox(15);
        Label titleLabel = new Label("Friends & Search");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Button feedBtn = new Button("Back to Feed");
        feedBtn.setOnAction(e -> new FeedView(stage).show());
        headerBox.getChildren().addAll(titleLabel, feedBtn);

        // Search Area
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
                List<User> found = searchUsers(q);
                for (User u : found) {
                    if (u.getId() == Main.currentUser.getId())
                        continue; // Skip self
                    HBox userRow = new HBox(10);
                    Label nameLbl = new Label(u.getName() + " (" + u.getEmail() + ")");
                    Button addBtn = new Button("Add Friend");
                    addBtn.setOnAction(ev -> {
                        friendDAO.addFriend(Main.currentUser.getId(), u.getId());
                        addBtn.setText("Added!");
                        addBtn.setDisable(true);
                    });
                    userRow.getChildren().addAll(nameLbl, addBtn);
                    resultsBox.getChildren().add(userRow);
                }
            }
        });

        Label friendsLabel = new Label("My Friends");
        friendsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 20 0 0 0;");
        VBox myFriendsBox = new VBox(5);

        List<User> list = friendDAO.getFriends(Main.currentUser.getId());
        if (list.isEmpty()) {
            myFriendsBox.getChildren().add(new Label("No friends yet. Search and add some above."));
        } else {
            for (User f : list) {
                HBox friendRow = new HBox(10);
                Label fLbl = new Label("- " + f.getName() + " (" + f.getEmail() + ")");
                Button msgBtn = new Button("Message");
                msgBtn.setOnAction(e -> new ChatView(stage, f).show());
                friendRow.getChildren().addAll(fLbl, msgBtn);
                myFriendsBox.getChildren().add(friendRow);
            }
        }

        root.getChildren().addAll(headerBox, searchBox, resultsBox, friendsLabel, myFriendsBox);

        Scene scene = new Scene(root, Constants.FRIENDS_WINDOW_WIDTH, Constants.FRIENDS_WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        stage.setTitle("Social Media App - Friends");
        stage.setScene(scene);
        stage.show();
    }

    private List<User> searchUsers(String query) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE name LIKE ? LIMIT 20";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + query + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            null,
                            rs.getTimestamp("created_at")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Search error: " + e.getMessage());
        }
        return users;
    }
}
