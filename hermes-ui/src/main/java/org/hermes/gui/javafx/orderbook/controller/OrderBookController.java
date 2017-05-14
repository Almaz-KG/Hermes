package org.hermes.gui.javafx.orderbook.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrderBookController implements Initializable{
    @FXML
    private Pane header;
//    @FXML
//    private ComboBox<Share> sharesComboBox;
    @FXML
    private Hyperlink titleHyperlink;
//    @FXML
//    private TableView<Tick> tableView;

    private Mouse mouse = new Mouse();

    public void initialize(URL location, ResourceBundle resources) {
        header.setOnMousePressed(t -> {
            mouse.setX(t.getX());
            mouse.setY(t.getY());
        });
        header.setOnMouseDragged(t -> {
            header.getScene().getWindow().setX( t.getScreenX() - mouse.getX() - 14);
            header.getScene().getWindow().setY( t.getScreenY() - mouse.getY() - 14);
        });

        titleHyperlink.setBorder(Border.EMPTY);
//        sharesComboBox.getItems().addAll(Shares.getShares());
//        sharesComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
//            titleHyperlink.setText(newValue.getCompanyName());
//        });
//
//        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn sell = new TableColumn("Продажа");
        TableColumn price = new TableColumn("Цена");

        TableColumn buy = new TableColumn("Покупка");

        sell.setSortable(false);
        price.setSortable(false);
        buy.setSortable(false);
//        sell.setCellValueFactory( new PropertyValueFactory<Tick, String>("value"));
//        price.setCellValueFactory( new PropertyValueFactory<Tick, String>("price"));
//        buy.setCellValueFactory( new PropertyValueFactory<Tick, String>("value"));
//
//        tableView.getColumns().addAll(sell, price, buy);
    }


    private void updateTableView(Object share) {
//        MarketSnapshot marketSnapshot = Shares.getMarketSnapshot(share);
//        List<Tick> tickList = marketSnapshot.getTickList();
//
//        tableView.setItems(FXCollections.observableArrayList(tickList));
    }

    public void exitButtonPressed(){
        Platform.exit();
    }

    public void showButtonClicked(){
//        updateTableView(sharesComboBox.getValue());
    }

    private static class Mouse {
        private double x = 0;
        private double y = 0;

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
}
