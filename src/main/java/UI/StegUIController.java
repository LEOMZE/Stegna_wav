package UI;


import Util.AudioWaveformCreator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StegUIController implements Initializable {

    @FXML
    private GridPane gridPane;

    @FXML
    private Slider slider;

    @FXML
    private Label bib;

    @FXML
    private Label filePath;

    @FXML
    private LineChart<Number, Double> lineChart;

    @FXML
    ChoiceBox<Number> choiceBox;

    @FXML
    Label choiceLabel;

    @FXML
    Label msgLthLabel;

    @FXML
    TextArea msgArea;





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slider.setMax(7);
        slider.setMin(0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        msgArea.setWrapText(true);
        choiceBox.setItems(FXCollections.observableArrayList((Number) 1, 2, 3, 4, 5));
        choiceBox.setTooltip(new Tooltip("Select the byte stride"));
        choiceBox.getSelectionModel();
        bib.setText("Bit in Byte (" + (int)slider.getValue() + ")");
        lineChart.getStyleClass().add("thick-chart");
        lineChart.setCreateSymbols(false);

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                bib.setText("Bit in Byte (" + (int) slider.getValue() + ")");
            }
        });

        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                choiceLabel.setText("Every " + (number2.intValue() + 1) + " byte: ");
                System.out.println(choiceBox.getValue());
            }
        });

        msgArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                msgLthLabel.setText("Byte in text: " + msgArea.getText().getBytes().length);
            }
        });

    }



    @FXML
    private void btnFile(ActionEvent event){
        System.out.println(choiceBox.getValue() + " " +  choiceBox.getValue().intValue());

        System.out.println("Click!");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV", "*.wav"));
        File file = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
        String str = file.getAbsolutePath();
        filePath.setText(str);
        long fileLength;
        if(choiceBox.getValue().intValue() != 0){
            fileLength = (file.length() - 59) / choiceBox.getValue().intValue();
        } else {
            fileLength = (file.length() - 59) / 1;
        }

        System.out.println(fileLength);
        if(str.length() != 0){
            ObservableList<XYChart.Series<Number, Double>> lineChartData = FXCollections.observableArrayList();
            ArrayList<Double> d;

            LineChart.Series<Number, Double> series1 = new LineChart.Series<Number, Double>();
            series1.setName("Series 1");


            d =  new AudioWaveformCreator().createWaveForm(filePath.getText());
            int j = 0;
            for(double i=0.0; i < d.size(); i++){
                series1.getData().add(new XYChart.Data<Number, Double>(i, d.get(j)));
                j++;
            }


            lineChartData.add(series1);

            lineChart.setData(lineChartData);
            lineChart.createSymbolsProperty();
        }

    }

    @FXML
    private void btnSteg(ActionEvent event){

        System.out.println("Click!");
   //TODO     new SWrite(filePath.getText(), msgArea.getText(), (int) slider.getValue() , choiceBox.getValue().intValue());

    }

    @FXML
    private void btnDesteg(ActionEvent event){
        System.out.println("Click!");
   //TODO     new SRead(filePath.getText(), choiceBox.getValue().intValue());
    }




}
