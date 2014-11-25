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
    private Slider bitSlider;

    @FXML
    private Label bib;

    @FXML
    private Label filePath;

    @FXML
    private LineChart<Number, Double> lineChart;

    @FXML
    Slider byteSlider;

    @FXML
    Label choiceLabel;

    @FXML
    Label msgLthLabel;

    @FXML
    TextArea msgAreaS;

    @FXML
    ProgressIndicator progressInd;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bitSlider.setMax(7);
        bitSlider.setMin(0);
        bitSlider.setShowTickLabels(true);
        bitSlider.setShowTickMarks(true);

        msgAreaS.setWrapText(true);

        byteSlider.setMax(5);
        byteSlider.setMin(1);
        byteSlider.setShowTickLabels(true);
        byteSlider.setShowTickMarks(true);
        byteSlider.setTooltip(new Tooltip("Select the byte stride"));

        progressInd.getStyleClass().add("progress-indicator_hide");

        bib.setText("Bit in Byte (" + (int) bitSlider.getValue() + ")");
        lineChart.getStyleClass().add("thick-chart");
        lineChart.setCreateSymbols(false);

        bitSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                bib.setText("Bit in Byte (" + (int) bitSlider.getValue() + ")");
            }
        });

        byteSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                choiceLabel.setText("Every " + (int) byteSlider.getValue() + " byte: ");
            }
        });

        msgAreaS.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                msgLthLabel.setText("Byte in text: " + msgAreaS.getText().getBytes().length);
            }
        });

    }



    @FXML
    private void btnFile(ActionEvent event){
        System.out.println(byteSlider.getValue());

        System.out.println("Click!");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV", "*.wav"));
        File file = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
        String str = file.getAbsolutePath();
        filePath.setText(str);
        filePath.setTooltip(new Tooltip(str));
        long fileLength;
        if(byteSlider.getValue() != 0){
            fileLength = (file.length() - 59) / (int)byteSlider.getValue();
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
        progressInd.getStyleClass().clear();
        progressInd.getStyleClass().add("progress-indicator_show");
   //TODO     new SWrite(filePath.getText(), msgArea.getText(), (int) bitSlider.getValue() , choiceBox.getValue().intValue());

    }

    @FXML
    private void btnDesteg(ActionEvent event){
        System.out.println("Click!");
        progressInd.getStyleClass().clear();
        progressInd.getStyleClass().add("progress-indicator_hide");
   //TODO     new SRead(filePath.getText(), choiceBox.getValue().intValue());
    }




}
