package com.socialmedia.views;

import com.socialmedia.Main;
import com.socialmedia.dao.NotificationDAO;
import com.socialmedia.models.Notification;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class NotificationView {

    private Stage stage;
    private NotificationDAO notificationDAO;

    public NotificationView(Stage stage) {
        this.stage = stage;
        this.notificationDAO = new NotificationDAO();
    }

    public void show() {
        if (Main.currentUser == null)
            return;

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        HBox headerBox = new HBox(15);
        Label titleLabel = new Label("Notifications");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Button backBtn = new Button("Back to Feed");
        backBtn.setOnAction(e -> new FeedView(stage).show());
        headerBox.getChildren().addAll(titleLabel, backBtn);

        VBox notifBox = new VBox(10);

        List<Notification> unread = notificationDAO.getUnreadNotifications(Main.currentUser.getId());

        if (unread.isEmpty()) {
            notifBox.getChildren().add(new Label("You're all caught up! No new notifications."));
        } else {
            for (Notification n : unread) {
                HBox row = new HBox(10);
                row.setStyle("-fx-border-color: #ddd; -fx-padding: 10; -fx-background-color: #f9f9f9;");

                VBox textVBox = new VBox(5);
                Label typeLbl = new Label("[" + n.getType() + "]");
                typeLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: gray; -fx-font-size: 10px;");
                Label msgLbl = new Label(n.getMessage());
                Label dateLbl = new Label(n.getCreatedAt().toString());
                dateLbl.setStyle("-fx-font-size: 10px; -fx-text-fill: lightgray;");
                textVBox.getChildren().addAll(typeLbl, msgLbl, dateLbl);

                Button dismissBtn = new Button("Mark as Read");
                dismissBtn.setOnAction(e -> {
                    notificationDAO.markAsRead(n.getId());
                    show(); // Refresh
                });

                row.getChildren().addAll(textVBox, dismissBtn);
                notifBox.getChildren().add(row);
            }
        }

        root.getChildren().addAll(headerBox, notifBox);

        Scene scene = new Scene(root, 500, 600);
        stage.setTitle("Social Media App - Notifications");
        stage.setScene(scene);
        stage.show();
    }
}
