package com.socialmedia.views;

import com.socialmedia.Main;
import com.socialmedia.dao.ProfileDAO;
import com.socialmedia.models.Profile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ProfileView {

    private Stage stage;
    private ProfileDAO profileDAO;

    public ProfileView(Stage stage) {
        this.stage = stage;
        this.profileDAO = new ProfileDAO();
    }

    public void show() {
        if (Main.currentUser == null)
            return;

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(Main.currentUser.getName() + "'s Profile");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Profile Picture
        ImageView imageView = new ImageView();
        imageView.setFitHeight(120);
        imageView.setFitWidth(120);

        Profile existingProfile = profileDAO.getProfile(Main.currentUser.getId());

        TextField picUrlField = new TextField(existingProfile != null ? existingProfile.getPictureUrl() : "");
        picUrlField.setPromptText("Image URL or Local Path");

        TextArea bioArea = new TextArea(existingProfile != null ? existingProfile.getBio() : "");
        bioArea.setPromptText("Write something about yourself...");
        bioArea.setPrefRowCount(4);

        if (existingProfile != null && existingProfile.getPictureUrl() != null
                && !existingProfile.getPictureUrl().isEmpty()) {
            try {
                imageView.setImage(new Image(existingProfile.getPictureUrl(), true));
            } catch (Exception e) {
                System.err.println("Could not load image: " + e.getMessage());
            }
        }

        Button uploadBtn = new Button("Choose Local Picture");
        uploadBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                String uri = selectedFile.toURI().toString();
                picUrlField.setText(uri);
                try {
                    imageView.setImage(new Image(uri, true));
                } catch (Exception ex) {
                    System.err.println("Failed to load chosen image.");
                }
            }
        });

        Button saveBtn = new Button("Save Profile");
        Label messageLabel = new Label();

        saveBtn.setOnAction(e -> {
            boolean success = profileDAO.saveOrUpdateProfile(
                    Main.currentUser.getId(),
                    bioArea.getText(),
                    picUrlField.getText());
            if (success) {
                messageLabel.setText("Profile updated successfully!");
                messageLabel.setStyle("-fx-text-fill: green;");
            } else {
                messageLabel.setText("Failed to update profile.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        Button backBtn = new Button("Back to Feed");
        backBtn.setOnAction(e -> {
            new FeedView(stage).show();
        });

        vbox.getChildren().addAll(titleLabel, imageView, uploadBtn, picUrlField, bioArea, saveBtn, messageLabel,
                backBtn);

        Scene scene = new Scene(vbox, 400, 500);
        stage.setTitle("Social Media App - Profile");
        stage.setScene(scene);
        stage.show();
    }
}
