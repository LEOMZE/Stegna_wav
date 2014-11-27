package RWSteg;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;

public class SRead {
    private String filePath;
    private int numByte;
    private int numberOfBit;

    public SRead(String filePath , int numberOfBit, int numByte){
        this.filePath = filePath;
        this.numByte = numByte;
        this.numberOfBit = numberOfBit;
    }

    public String desteg(){
        String secret = new String();
        try {
            int bytes;
            byte[] data = new byte[128];
            boolean endFlag = false;
            FileInputStream fileInputStream = new FileInputStream(filePath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            bufferedInputStream.mark(1);
            long msgLength = getLengthMsg(bufferedInputStream);
            bufferedInputStream.reset();
            bufferedInputStream.skip(59);
            System.out.println("\n\nStart desteg!!!");
            String str = new String();
            int tick = 0;
            while((bytes = bufferedInputStream.read(data)) > -1){
                for(int i=0; i<bytes; i++) {
                    if ((i % numByte == 0) && endFlag == false) {
                        for(int j = 0; j <= numberOfBit; j++){
                            str += getBit(data[i], j);
                            if(str.length() == msgLength){
                                System.out.println(str);
                                secret = new String(new BigInteger(str, 2).toByteArray(), "Cp866");
                                return secret.substring(8, secret.length());
                            }
                        }
                    }
                }
            }
            System.out.println(tick);
            System.out.println(secret);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return secret.substring(8, secret.length());
    }

    private static int getBit(int data, int position) {
        return (data >> position) & 1;
    }

    private long getLengthMsg(BufferedInputStream bis){
        long length = 0;
        int msgOfLength = 64 / (numberOfBit+1);
        int bytes;
        int cursorMsg = 0;
        byte[] data = new byte[128];
        String str = new String();
        try {
            bis.skip(59);
            while((bytes = bis.read(data)) > -1){
                for(int i=0; i < bytes; i++) {
                    if (i % numByte == 0) {
                        for(int j = 0; j <= numberOfBit; j++){
                            str += getBit(data[i], j);
                        }
                        cursorMsg++;
                        if(cursorMsg > msgOfLength){
                            String l = new String(new BigInteger(str.substring(0,64), 2).toByteArray(), "Cp866");
                            length = Long.parseLong(l, 16);
                            return length + 64;
                        }
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return length + 64;
    }

}

