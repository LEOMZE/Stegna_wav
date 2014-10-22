package UI;


import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;

public class StegUIController {

    @FXML
    private LineChart<Integer, Integer> wavChart;

    @FXML
    private Button fileBtn;

    @FXML
    private Button stegBtn;

    @FXML
    private Label filePathLabel;

    @FXML
    private Label processLabel;

    @FXML
    private ProgressIndicator indicator;

    @FXML
    private Label fileOutPathLabel;

    @FXML
    private TextArea stegTextArea;

    private StegUI stegUI;


    public StegUIController(){

    }






}
