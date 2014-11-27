import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Main extends Application{

    public static void main(final String[] args) throws UnsupportedEncodingException {
//        Scanner scanner = new Scanner(System.in);
////        String mes = scanner.nextLine();
//        String str = "Спасибо Олесе, за хорошее настроение :)";
//        byte[] bytes = str.getBytes();
//        SWrite sWrite = new SWrite("E:/Gorillaz-Clint_Eastwood.wav", str, 0, 4);
//        sWrite.run();
//        SRead sRead = new SRead("E:/Gorillaz-Clint_Eastwood_stego.wav", 0, 4);
////        sRead.desteg();
//        System.out.println("\n Скрытое сообщение: \"" + sRead.desteg() + "\"\n");


//

                launch(args);
                System.exit(0);
////
//
//
//        byte b = (byte) 0xca;
//        for(int  i = 0; i < 3;i++){
//            b = (byte) (b & ~(1 << i));
//        }
//
//        for(int i = 0; i < 3; i ++){
//            b^= (1 << i);
//        }
//        System.out.println(Integer.toBinaryString(0xca) + " \n" + Integer.toBinaryString(b & 0xff));
    }


    @Override
    public void start(Stage primaryStage) throws Exception{

        try {

            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("scene.fxml"));
            primaryStage.setTitle("WAV_STEGA APP v0.1");
            primaryStage.setWidth(600);
            primaryStage.setHeight(430);
            primaryStage.setResizable(false);
            root.getStylesheets().add("chart_style.css");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
