package RWSteg;

import java.io.*;

public class SWrite implements Runnable{

    private String filePath;
    private String message;
    private int numberOfBit;
    private int numByte;

    public SWrite(String filePath, String message, int numberOfBit , int numByte){
        this.filePath = filePath;
        this.message = message;
        this.numberOfBit = numberOfBit;
        this.numByte = numByte;
    }//конструктор класса

    private void steg(){
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            OutputStream outputStream = new FileOutputStream( new File(filePath.substring(0, filePath.indexOf(".")) + "_stego.wav"));
            byte[] b = new byte[59];
            bufferedInputStream.read(b, 0, 59);
            outputStream.write(b);
            byte[] data = new byte[128];
            int bytes;
            String binMsg = msgToBit(message);
            System.out.println("Start steg! \nMessage \"" + message + "\" to binary: \n" + binMsg);
            int cursorMsg = 0;
            int unsigned;
            while ((bytes = bufferedInputStream.read(data)) > 0) {
                int[] c_data = new int[128];
                for(int i=0; i < bytes; i++) {
                    unsigned = data[i] & 0xFF;
                    if(i % numByte == 0){
                            if(cursorMsg < binMsg.length()) {
                                for(int j = 0; j <= numberOfBit; j++){
                                    unsigned = (byte) (unsigned & ~(1 << j));
                                }
                                for(int j = 0; j <= numberOfBit; j ++){
                                    if(cursorMsg < binMsg.length()){
                                        unsigned ^= Character.getNumericValue(binMsg.charAt(cursorMsg)) << j;
                                    }
                                    cursorMsg++;
                                }
                                c_data[i] = unsigned;

                            } else {
//                                System.out.println("finish write!");
                                c_data[i] = unsigned;
                            }
                    }else{
                        c_data[i] = data[i];
                    }

                }
                for(int i = 0; i < c_data.length; i++){
                    outputStream.write(c_data[i]);
                }
            }
            System.out.println("\nfinish steg!");
            bufferedInputStream.read(data);
            bufferedInputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//основной алгоритм

    private static String msgToBit(String msg){

        try {
            String msgLength = Integer.toHexString(msg.getBytes("Cp866").length * 8);
            msg = repeat("0", 8 - msgLength.length()) + msgLength + msg;
            System.out.println(msg);
            byte[] bytes = msg.getBytes("Cp866");
            StringBuilder binary = new StringBuilder();
            for (byte b : bytes)
            {
                int val = b;
                for (int i = 0; i < 8; i++)
                {
                    binary.append((val & 128) == 0 ? 0 : 1);
                    val <<= 1;
                }
            }

            return binary.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }

    private static String repeat(String string, int times) {
        StringBuilder out = new StringBuilder();
        while (times-- > 0) {
            out.append(string);
        }
        return out.toString();
    }

    @Override
    public void run() {
        steg();
    }
}


