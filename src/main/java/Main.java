
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.UnsupportedEncodingException;

public class Main extends Application{

    public static void main(String[] args) throws UnsupportedEncodingException {
//        Scanner scanner = new Scanner(System.in);
//        String mes = scanner.nextLine();
//        String str = "";
//        SWrite sWrite = new SWrite("E:/14 Blue Jean Blues.wav", str);
//        sWrite.steg();
//        SRead sRead = new SRead("E:/14 Blue Jean Blues_stego.wav");
////        sRead.desteg();
//        System.out.println("\n Скрытое сообщение: \"" + sRead.desteg() + "\"\n");



    launch(args);

    }


    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("steg_ui.fxml"));
        primaryStage.setTitle("WAV_STEGA APP v0.1");
        primaryStage.setWidth(550);
        primaryStage.setHeight(550);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

}
