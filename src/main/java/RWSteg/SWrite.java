package RWSteg;

import java.io.*;

public class SWrite {

    private String filePath;
    private String message;

    public SWrite(String filePath, String message){
        this.filePath = filePath;
        this.message = message;
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
//            System.out.println("\nChange last bit of byte: ");
            while ((bytes = bufferedInputStream.read(data)) > 0) {
                int[] c_data = new int[128];
                for(int i=0; i < bytes; i++) {
                    unsigned = data[i] & 0xFF;

                    if((i % 4 == 0 | i % 3 == 0)) {
                        if (data[i] == 0 | data[i] == 1) {
                            c_data[i] = (byte) unsigned;
                        } else {
                            if(cursorMsg < binMsg.length()) {
                                if(Character.getNumericValue(binMsg.charAt(cursorMsg)) == 1) {
                                    if (getBit(unsigned, 0) == 1) {
                                        c_data[i] = unsigned;
//                                        System.out.print((cursorMsg + 1) + ") "
//                                                + Character.getNumericValue(binMsg.charAt(cursorMsg))
//                                                + " (" + unsigned + ", "
//                                                + Integer.toBinaryString(unsigned) + ") "
//                                                + " -> " + c_data[i] + " "
//                                                + Integer.toBinaryString(c_data[i]) + " 1 == 1\n");
                                    } else {
                                        c_data[i] = unsigned ^ (1 << 0);
//                                        System.out.print((cursorMsg + 1) + ") "
//                                                + Character.getNumericValue(binMsg.charAt(cursorMsg))
//                                                + " ^ (" + unsigned + ", "
//                                                + Integer.toBinaryString(unsigned) + ") "
//                                                + " -> " + c_data[i] + " "
//                                                + Integer.toBinaryString(c_data[i]) + " 1 ^ 0\n");
                                    }
                                } else{
                                    if(getBit(unsigned, 0) == 1){
                                        c_data[i] = unsigned ^ (1 << 0);
//                                        System.out.print((cursorMsg + 1) + ") "
//                                                + Character.getNumericValue(binMsg.charAt(cursorMsg))
//                                                + " ^ (" + unsigned + ", "
//                                                + Integer.toBinaryString(unsigned) + ") "
//                                                + " -> " + c_data[i] + " "
//                                                + Integer.toBinaryString(c_data[i]) + " 0 ^ 1\n");
                                    }else {
                                        c_data[i] = unsigned;
//                                        System.out.print((cursorMsg + 1) + ") "
//                                                + Character.getNumericValue(binMsg.charAt(cursorMsg))
//                                                + " (" + unsigned + ", "
//                                                + Integer.toBinaryString(unsigned) + ") "
//                                                + " -> " + c_data[i] + " "
//                                                + Integer.toBinaryString(c_data[i]) + " 0 == 0\n");
                                    }

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

}


