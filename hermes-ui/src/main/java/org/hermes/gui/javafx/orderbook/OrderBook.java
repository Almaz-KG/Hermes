package org.hermes.gui.javafx.orderbook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OrderBook extends Application{
    private static final String VIEW_NAME = "views/orderbook/orderbook.fxml";
    private static final String DEFAULT_TITLE = "Торговый стакан";

    public void start(Stage primaryStage) throws Exception{
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(VIEW_NAME));

        primaryStage.setTitle(DEFAULT_TITLE);
        primaryStage.setScene(new Scene(root, 300, 420));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(OrderBook.class, args);
    }
}
