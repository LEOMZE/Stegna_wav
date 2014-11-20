package RWSteg;

import java.io.*;
import java.util.ArrayList;

public class SWrite {

    private String filePath;
    private String message;
    private int numberOfBit;
    private int numByte;


    public SWrite(String filePath, String message, int numberOfBit , int numByte){
        this.filePath = filePath;
        this.message = message;
        this.numberOfBit = numberOfBit;
        this.numByte = numByte;
    }

    public void steg(){
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
                        if (data[i] == 0 | data[i] == 1) {//TODO maybe del !7
                            c_data[i] = (byte) unsigned;
                        } else {
                            if(cursorMsg < binMsg.length()) {//TODO right there FOR with bit
                                if(Character.getNumericValue(binMsg.charAt(cursorMsg)) == getBit(unsigned, 0/*numberOfBit*/)) {
                                    c_data[i] = unsigned;
                                } else{
                                    c_data[i] = unsigned ^ (1 << 0);
                                }
                                cursorMsg++;
                            } else {
                                c_data[i] = unsigned;
                            }
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
    }

    private static String msgToBit(String msg){

        try {
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
                // binary.append(' ');
            }
            return binary.toString() + "11000110";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }

    private static int getBit(int data, int position) {
//        System.out.println(Integer.toBinaryString(data) + " get last bit " + ((data >> position) & 1));
        return (data >> position) & 1;
    }

    public ArrayList<Integer> getDots(String filePath){
        int bytes, unsigned;
        ArrayList<Integer> dots = new ArrayList<Integer>();
        try {

            FileInputStream s = new FileInputStream(filePath);
            BufferedInputStream b = new BufferedInputStream(s);
            byte[] data = new byte[128];
            b.skip(59);
            while ((bytes = b.read(data)) > 0) {

                for (int i =0; i < bytes; i += 4) {
                    int left = (data[i] & 0xff) | (data[i + 1] << 8);
                    int right = (data[i + 2] & 0xff) | (data[i + 3] << 8);
                    dots.add(left);
                }
            }
//            b.read(data);
            b.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return dots;
    }

}


