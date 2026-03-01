package com.socialmedia.views;

import com.socialmedia.Main;
import com.socialmedia.dao.MessageDAO;
import com.socialmedia.models.Message;
import com.socialmedia.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ChatView {

    private Stage stage;
    private User chatPartner;
    private MessageDAO messageDAO;
    private VBox messageBox;

    public ChatView(Stage stage, User chatPartner) {
        this.stage = stage;
        this.chatPartner = chatPartner;
        this.messageDAO = new MessageDAO();
    }

    public void show() {
        if (Main.currentUser == null || chatPartner == null)
            return;

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        HBox headerBox = new HBox(15);
        Label titleLabel = new Label("Chat with " + chatPartner.getName());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Button backBtn = new Button("Back to Friends");
        backBtn.setOnAction(e -> new FriendsView(stage).show());
        headerBox.getChildren().addAll(backBtn, titleLabel);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(450);
        messageBox = new VBox(10);
        messageBox.setPadding(new Insets(10));
        scrollPane.setContent(messageBox);

        HBox inputBox = new HBox(10);
        TextField messageField = new TextField();
        messageField.setPromptText("Type a message...");
        messageField.setPrefWidth(350);
        Button sendBtn = new Button("Send");
        sendBtn.setOnAction(e -> {
            String content = messageField.getText().trim();
            if (!content.isEmpty()) {
                messageDAO.sendMessage(Main.currentUser.getId(), chatPartner.getId(), content);
                messageField.clear();
                refreshChat();
            }
        });
        inputBox.getChildren().addAll(messageField, sendBtn);

        refreshChat();

        root.getChildren().addAll(headerBox, scrollPane, inputBox);

        Scene scene = new Scene(root, 500, 600);
        stage.setTitle("Social Media App - Chat");
        stage.setScene(scene);
        stage.show();
    }

    private void refreshChat() {
        messageBox.getChildren().clear();
        List<Message> history = messageDAO.getChatHistory(Main.currentUser.getId(), chatPartner.getId());

        for (Message m : history) {
            boolean isMine = (m.getSenderId() == Main.currentUser.getId());

            HBox row = new HBox();
            row.setAlignment(isMine ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

            VBox bubble = new VBox(3);
            bubble.setPadding(new Insets(8));
            bubble.setStyle("-fx-background-color: " + (isMine ? "#dcf8c6" : "#ffffff") +
                    "; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: lightgray;");

            Label msgLbl = new Label(m.getContent());
            msgLbl.setWrapText(true);
            msgLbl.setMaxWidth(300);

            Label timeLbl = new Label(m.getSentAt().toString());
            timeLbl.setStyle("-fx-font-size: 9px; -fx-text-fill: gray;");

            bubble.getChildren().addAll(msgLbl, timeLbl);
            row.getChildren().add(bubble);

            messageBox.getChildren().add(row);
        }
    }
}
