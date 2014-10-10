import Desteg.SRead;

import java.io.UnsupportedEncodingException;

public class Main {



    public static void main(String[] args) throws UnsupportedEncodingException {
//        String str = "hello";
//        SWrite sWrite = new SWrite("E:/taxi3.wav", str);
//        sWrite.steg();
        SRead sRead = new SRead("E:/taxi3_stego.wav");
        sRead.desteg();
    }
}
