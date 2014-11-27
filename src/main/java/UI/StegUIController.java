package UI;


import RWSteg.SRead;
import RWSteg.SWrite;
import Util.AudioWaveformCreator;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IAddStreamEvent;
import com.xuggle.xuggler.IStreamCoder;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
    Label allowableVal;

    @FXML
    Label msgLthLabel;

    @FXML
    TextArea msgAreaS;
    @FXML
    TextArea msgAreaD;

    @FXML
    ProgressBar progressInd;

    @FXML
    Button convert;

    @FXML
    Button playM;

    @FXML
    Button stopM;

    private Media hit;
    private MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bitSlider.setMax(7);
        bitSlider.setMin(0);
        bitSlider.setShowTickLabels(true);
        bitSlider.setShowTickMarks(true);

        msgAreaS.setWrapText(true);
        msgAreaD.setWrapText(true);

        byteSlider.setMax(10);
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
                if(filePath.getText().length() != 0){
                    long length = (long )(((new File(filePath.getText()).length() - 59) / (int) ( byteSlider.getValue() + 1)) * (bitSlider.getValue() + 1) / 8);
                    allowableVal.setText("Allowable value: " + length / 1024 + "Kb / "  + length + " byte");///TODO incorrect
                }
            }
        });

        byteSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                choiceLabel.setText("Every " + (int) byteSlider.getValue() + " byte: ");
                if(filePath.getText().length() != 0){
                    long length = (long )(((new File(filePath.getText()).length() - 59) / (int) byteSlider.getValue() ) * (bitSlider.getValue() + 1) / 8);
                    allowableVal.setText("Allowable value: " + length /1024 + " Kb / "  + length + " byte");///TODO incorrect
                }
            }
        });

        msgAreaS.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                long length = msgAreaS.getText().getBytes().length;
                msgLthLabel.setText("Byte in text: " + length / 1024 + " Kb / " + length + " byte");
            }
        });



    }


    @FXML
    private void btnFile(ActionEvent event){
        System.out.println(byteSlider.getValue());
        
        System.out.println("Click!");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV", "*.wav", "MP3", "*.mp3"));
        File file = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
        String str = file.getAbsolutePath();
        filePath.setText(str);
        filePath.setTooltip(new Tooltip(str));
        int length = 0;
        if(filePath.getText().length() != 0){
            length = (int )(((new File(filePath.getText()).length()-59) / (int)( byteSlider.getValue() + 1)) * (bitSlider.getValue() + 1) / 8);
            allowableVal.setText("Allowable value: " + length/1024 + " Kb/ "  + length + " byte");///TODO incorrect
        }

        System.out.println(length);
        if(filePath.getText().matches(".*\\.(wav)")){
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
    private void btnConvert(ActionEvent event){
        convertToMP3(new File(filePath.getText()), new File(filePath.getText().substring(0,filePath.getText().indexOf(".")) + ".wav"));
    }

    @FXML
    private void btnSteg(ActionEvent event){

        System.out.println("Click!");
        progressInd.getStyleClass().clear();
        progressInd.getStyleClass().add("progress-indicator_show");

        SWrite sWrite = new SWrite(filePath.getText(), msgAreaS.getText(), (int) bitSlider.getValue() ,(int) byteSlider.getValue());
        final Thread thread = new Thread(sWrite);

        Thread thread1 =  new Thread(new Runnable() {
            @Override
            public void run() {
                File file1 = new File(filePath.getText());
                thread.start();
                File file2 = new File(filePath.getText().substring(0,filePath.getText().indexOf(".")) + "_stego.wav");
                do {
                    final float i = (float)(file2.length() / (file1.length()/100.0)) * 0.01f ;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressInd.setProgress(i);
                }while (file1.length() != file2.length());

                progressInd.getStyleClass().clear();
                progressInd.getStyleClass().add("progress-indicator_hide");
            }
        });


        thread1.start();
    }

    @FXML
    private void btnDesteg(ActionEvent event){
        System.out.println("Click!");
        progressInd.getStyleClass().clear();
        progressInd.getStyleClass().add("progress-indicator_hide");
        ObservableList<XYChart.Series<Number, Double>> lineChartData = FXCollections.observableArrayList();
        ArrayList<Double> d;

        LineChart.Series<Number, Double> series2 = new LineChart.Series<Number, Double>();
        series2.setName("Series 2");


        d =  new AudioWaveformCreator().createWaveForm(filePath.getText());
        int j = 0;
        for(double i = 0.0; i < d.size(); i++){
            series2.getData().add(new XYChart.Data<Number, Double>(i, d.get(j)));
            j++;
        }


        lineChartData.add(series2);

        lineChart.getData().add(0, series2);
        lineChart.createSymbolsProperty();

        new Thread(new Runnable() {
            @Override
            public void run() {
                msgAreaD.setText(new SRead(filePath.getText(), (int) bitSlider.getValue(), (int) byteSlider.getValue()).desteg());
            }
        }).start();

    }

    @FXML
    private void playMBtn(ActionEvent event){
        hit = new Media(new File(filePath.getText()).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }

    @FXML
    private void stopMBtn(ActionEvent event){
        mediaPlayer.stop();
    }

    public void convertToMP3(File input, File output/*, int kbps*/) {
        IMediaReader mediaReader = ToolFactory.makeReader(input.getPath());
        IMediaWriter mediaWriter = ToolFactory.makeWriter(output.getPath(), mediaReader);
        mediaReader.addListener(mediaWriter);

        mediaWriter.addListener(new MediaListenerAdapter() {
            @Override
            public void onAddStream(IAddStreamEvent event) {
                IStreamCoder streamCoder = event.getSource().getContainer().getStream(event.getStreamIndex()).getStreamCoder();
                streamCoder.setFlag(IStreamCoder.Flags.FLAG_QSCALE, false);
//                streamCoder.setBitRate(kbps);
//                streamCoder.setBitRateTolerance(0);
            }
        });

        while (mediaReader.readPacket() == null);
    }




}
