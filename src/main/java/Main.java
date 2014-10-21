import RWSteg.SRead;
import RWSteg.SWrite;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws UnsupportedEncodingException {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        SWrite sWrite = new SWrite("E:/taxi3.wav", str);
        sWrite.steg();
        SRead sRead = new SRead("E:/taxi3_stego.wav");
//        sRead.desteg();
        System.out.println("\n Скрытое сообщение: \"" + sRead.desteg() + "\"\n");
    }


}
