package UI;



import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class StegUIController implements Initializable {

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
