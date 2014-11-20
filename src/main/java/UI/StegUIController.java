package UI;


import RWSteg.SWrite;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StegUIController implements Initializable {

    @FXML
    private LineChart<Integer, Integer> wavChart;

    @FXML
    private GridPane gridPane;

    @FXML
    private Slider slider;

    @FXML
    private Label bib;

    @FXML
    private Label filePath;

    @FXML
    private LineChart<Number, Number> lineChart;





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slider.setMax(7);
        slider.setMin(0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        bib.setText("Bit in Byte (" + (int)slider.getValue() + ")");
        lineChart.getStyleClass().add("thick-chart");
        lineChart.setCreateSymbols(false);

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                bib.setText("Bit in Byte (" + (int) slider.getValue() + ")");
            }
        });

    }



    @FXML
    private void btnFile(ActionEvent event){
        int a = -2 >>> 1;
       new Integer(2).toString();
        System.out.println("Click!");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV", "*.wav"));
        File file = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
        String str = file.getAbsolutePath();
        filePath.setText(str);
        if(str.length() != 0){
            ObservableList<XYChart.Series<Number, Number>> lineChartData = FXCollections.observableArrayList();
            ArrayList<Integer> d;

            LineChart.Series<Number, Number> series1 = new LineChart.Series<Number, Number>();
            series1.setName("Series 1");


            d =  new SWrite(null, null, 0, 0).getDots(str);
            int j = 0;
            for(double i=0.0; i < d.size(); i++){
                series1.getData().add(new XYChart.Data<Number, Number>(i, d.get(j).intValue()*0.3));
                j++;
            }


            lineChartData.add(series1);

            lineChart.setData(lineChartData);
            lineChart.createSymbolsProperty();
        }

    }




}
