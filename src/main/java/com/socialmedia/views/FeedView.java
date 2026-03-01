package com.socialmedia.views;

import com.socialmedia.Main;
import com.socialmedia.dao.CommentDAO;
import com.socialmedia.dao.LikeDAO;
import com.socialmedia.dao.PostDAO;
import com.socialmedia.models.Comment;
import com.socialmedia.models.Post;
import com.socialmedia.structures.PostList;
import com.socialmedia.utils.Constants;
import com.socialmedia.utils.DateUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class FeedView {

    private Stage stage;
    private PostDAO postDAO;
    private VBox feedBox;
    private int feedOffset = 0;

    public FeedView(Stage stage) {
        this.stage = stage;
        this.postDAO = new PostDAO();
    }

    public void show() {
        if (Main.currentUser == null)
            return;

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        HBox headerBox = new HBox(15);
        Label titleLabel = new Label("News Feed");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Button profileBtn = new Button("My Profile");
        profileBtn.setOnAction(e -> new ProfileView(stage).show());
        Button friendsBtn = new Button("Friends");
        friendsBtn.setOnAction(e -> new FriendsView(stage).show());
        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(e -> new SearchView(stage).show());
        Button notifBtn = new Button("Notifs");
        notifBtn.setOnAction(e -> new NotificationView(stage).show());
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            Main.currentUser = null;
            new AuthView(stage).show();
        });
        headerBox.getChildren().addAll(titleLabel, profileBtn, friendsBtn, searchBtn, notifBtn, logoutBtn);

        // Create Post Area
        VBox createPostBox = new VBox(10);
        createPostBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10;");
        TextArea postArea = new TextArea();
        postArea.setPromptText("What's on your mind, " + Main.currentUser.getName() + "?");
        postArea.setPrefRowCount(3);

        TextField imageField = new TextField();
        imageField.setPromptText("Image URL (Optional)");
        Button attachImageBtn = new Button("Attach Image");
        attachImageBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpeg", "*.jpg"));
            File f = fc.showOpenDialog(stage);
            if (f != null) {
                imageField.setText(f.toURI().toString());
            }
        });

        Button postBtn = new Button("Post Update");
        Label postMessageLabel = new Label();
        postBtn.setOnAction(e -> {
            postMessageLabel.setText("");
            String content = postArea.getText().trim();
            String img = imageField.getText().trim();
            if (!content.isEmpty() || !img.isEmpty()) {
                boolean success = postDAO.createPost(Main.currentUser.getId(), content, img.isEmpty() ? null : img);
                if (success) {
                    postArea.clear();
                    imageField.clear();
                    refreshFeed(true);
                } else {
                    postMessageLabel.setText("Failed to create post.");
                    postMessageLabel.setStyle("-fx-text-fill: red;");
                }
            }
        });

        HBox actions = new HBox(10, attachImageBtn, imageField, postBtn);
        createPostBox.getChildren().addAll(postArea, actions, postMessageLabel);

        // Feed display container
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        feedBox = new VBox(10);
        scrollPane.setContent(feedBox);

        Button loadMoreBtn = new Button("Load More Posts");
        loadMoreBtn.setOnAction(e -> {
            feedOffset += Constants.FEED_LIMIT;
            refreshFeed(false);
        });

        refreshFeed(true);

        root.getChildren().addAll(headerBox, createPostBox, scrollPane, loadMoreBtn);

        Scene scene = new Scene(root, Constants.FEED_WINDOW_WIDTH, Constants.FEED_WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        stage.setTitle("Social Media App - Feed");
        stage.setScene(scene);
        stage.show();
    }

    private void refreshFeed(boolean clear) {
        if (clear) {
            feedBox.getChildren().clear();
            feedOffset = 0;
        }
        PostList posts = postDAO.getFeedPosts(Main.currentUser.getId(), Constants.FEED_LIMIT, feedOffset);

        LikeDAO likeDAO = new LikeDAO();
        CommentDAO commentDAO = new CommentDAO();

        for (int i = 0; i < posts.size(); i++) {
            Post p = posts.get(i);
            VBox postUI = new VBox(5);
            postUI.setStyle("-fx-border-color: #ddd; -fx-padding: 10; -fx-background-color: #f9f9f9;");

            Label author = new Label(p.getAuthorName() != null ? p.getAuthorName() : "Unknown User");
            author.setStyle("-fx-font-weight: bold;");
            Label time = new Label(DateUtils.formatForDisplay(p.getCreatedAt()));
            time.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

            Label content = new Label(p.getContent() != null ? p.getContent() : "");
            content.setWrapText(true);

            postUI.getChildren().addAll(author, time, content);

            if (p.getImageUrl() != null && !p.getImageUrl().isEmpty()) {
                Label imgLabel = new Label("[Attached Image: " + p.getImageUrl() + "]");
                imgLabel.setStyle("-fx-text-fill: blue; -fx-font-size: 11px;");
                postUI.getChildren().add(imgLabel);
            }

            // Interaction section
            HBox interactionBox = new HBox(10);
            interactionBox.setStyle("-fx-padding: 10 0 0 0;");
            int likeCount = likeDAO.getLikeCount(p.getId());
            boolean hasLiked = likeDAO.hasLiked(p.getId(), Main.currentUser.getId());

            Button likeBtn = new Button(hasLiked ? "Unlike (" + likeCount + ")" : "Like (" + likeCount + ")");
            likeBtn.setOnAction(e -> {
                likeDAO.toggleLike(p.getId(), Main.currentUser.getId());
                refreshFeed(true);
            });

            TextField commentField = new TextField();
            commentField.setPromptText("Write a comment...");
            Button commentBtn = new Button("Comment");
            commentBtn.setOnAction(e -> {
                if (!commentField.getText().trim().isEmpty()) {
                    commentDAO.addComment(p.getId(), Main.currentUser.getId(), commentField.getText().trim());
                    refreshFeed(true);
                }
            });

            interactionBox.getChildren().addAll(likeBtn, commentField, commentBtn);
            postUI.getChildren().add(interactionBox);

            // Comments list
            List<Comment> comments = commentDAO.getCommentsForPost(p.getId());
            VBox commentsBox = new VBox(5);
            commentsBox.setStyle(
                    "-fx-padding: 5 0 0 10; -fx-border-color: transparent transparent transparent #ddd; -fx-border-width: 0 0 0 2;");
            for (Comment c : comments) {
                Label cLbl = new Label(c.getAuthorName() + ": " + c.getContent());
                cLbl.setStyle("-fx-font-size: 11px;");
                commentsBox.getChildren().add(cLbl);
            }
            if (!comments.isEmpty()) {
                postUI.getChildren().add(commentsBox);
            }

            feedBox.getChildren().add(postUI);
        }
    }
}
