package com.socialmedia;

import com.socialmedia.models.User;
import com.socialmedia.views.AuthView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    // Define global current user session
    public static User currentUser = null;

    @Override
    public void start(Stage primaryStage) {
        AuthView authView = new AuthView(primaryStage);
        authView.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
