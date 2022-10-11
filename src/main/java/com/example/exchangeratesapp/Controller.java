package com.example.exchangeratesapp;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller {

    @FXML
    private Button getData, resetOneTable, resetAllTable;

    @FXML
    private TextField amountTextField, dateTextField;

    @FXML
    private ComboBox<String> fromValue, toValue;

    @FXML
    private TableView<HistoryInfo> tableInfo;

    @FXML
    private TableColumn<HistoryInfo, Integer> idColumn;

    @FXML TableColumn<HistoryInfo, Double> rateColumn, resultColumn;

    @FXML
    private TableColumn<HistoryInfo, String> dateColumn;

    @FXML
    private Text resultInfo, rateInfo, errorText;

    @FXML
    void initialize() {
        String outputInfo = getUrlContent("https://api.exchangerate.host/symbols");
        JSONObject jo = new JSONObject(outputInfo).getJSONObject("symbols");
        JSONArray ja = jo.toJSONArray(jo.names());

        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < ja.length(); i ++){
            arrayList.add(ja.getJSONObject(i).getString("code") + " - " + ja.getJSONObject(i).getString("description"));
        }
        Collections.sort(arrayList);

        fromValue.getItems().addAll(arrayList);
        toValue.getItems().addAll(arrayList);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        rateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));

        AtomicInteger id = new AtomicInteger();
        getData.setOnAction(event -> {
            try {
                if (amountTextField.getText().isEmpty()) {
                    amountTextField.setText("1");
                }

                if (dateTextField.getText().isEmpty()){
                    dateTextField.setText(LocalDate.now().toString());
                }

                @SuppressWarnings("unused")
                LocalDate dateErrorCheck = LocalDate.parse(dateTextField.getText());

                String from = fromValue.getValue().substring(0, 3);
                String to = toValue.getValue().substring(0, 3);
                int amount = Integer.valueOf(amountTextField.getText());
                String date = dateTextField.getText();
                String outputConvert = getUrlContent("https://api.exchangerate.host/convert?from=" + from + "&to=" + to + "&amount=" + amount + "&places=2&date=" + date);

                rateInfo.setText("Rate: " + String.valueOf(new JSONObject(outputConvert).getJSONObject("info").getDouble("rate")));
                resultInfo.setText("Result: " + String.valueOf(new JSONObject(outputConvert).getDouble("result")));

                HistoryInfo historyInfo = new HistoryInfo(id.addAndGet(1), Double.parseDouble(rateInfo.getText().substring(6)), date, Double.parseDouble(resultInfo.getText().substring(8)));
                ObservableList<HistoryInfo> historyInfos = tableInfo.getItems();
                historyInfos.add(historyInfo);
                tableInfo.setItems(historyInfos);

                errorText.setOpacity(0);
            } catch (NullPointerException nlp) {
                errorText.setText("From or To is wrong");
                errorText.setOpacity(1);
            } catch (Exception e) {
                errorText.setText("Amount or Date is wrong");
                errorText.setOpacity(1);
            }
        });

//        ImageView imageView = new ImageView("https://www.freeiconspng.com/thumbs/restart-icon/black-panel-restart-system-icon--6.png");
        ImageView imageViewOne = new ImageView("https://cdn.pixabay.com/photo/2012/04/12/12/27/arrow-29801_1280.png");
        imageViewOne.setFitHeight(20);
        imageViewOne.setFitWidth(20);
        resetOneTable.setGraphic(imageViewOne);
        resetOneTable.setOnAction(event -> {
            try {
                tableInfo.getItems().remove(id.get() - 1);

                id.set(id.get() - 1);
            } catch (Exception e) {}
        });

        ImageView imageViewAll = new ImageView("https://www.freeiconspng.com/thumbs/restart-icon/black-panel-restart-system-icon--6.png");
        imageViewAll.setFitHeight(20);
        imageViewAll.setFitWidth(20);
        resetAllTable.setGraphic(imageViewAll);
        resetAllTable.setOnAction(event -> {
            try {
                tableInfo.getItems().clear();

                id.set(0);
            } catch (Exception e) {}
        });
    }

    private static String getUrlContent(String urlAdress){
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(urlAdress);
            URLConnection urlConn = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;

            while((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e){
            System.out.println("getUrlContent exception");
        }

        return content.toString();
    }
}