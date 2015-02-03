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
    public long length = 0;
    public String str = new String();

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
            //mark start
            bufferedInputStream.mark(1);
            //get msg length
            long msgLength = getLengthMsg(bufferedInputStream);
            //return to start stream
            bufferedInputStream.reset();
            bufferedInputStream.skip(59);

            System.out.println("\nStart desteg!!!");

            while((bytes = bufferedInputStream.read(data)) > -1){
                //Get each bytes (total 128)
                for(int i=0; i<bytes; i++) {
                    //if changed byte
                    if ((i % numByte == 0) && endFlag == false) {
                        //get all needed bit
                        for(int j = 0; j <= numberOfBit; j++){
                            //get bit to string bin
                            str += getBit(data[i], j);
                            //if string bin length == msg length
                            if(str.length() == msgLength){
                                //bin string to string
                                secret = new String(new BigInteger(str, 2).toByteArray(), "Cp866");
                                return secret.substring(8, secret.length());
                            }
                        }
                    }
                }
            }
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

        int msgOfLength = 64 / (numberOfBit + 1);
        int bytes;
        int cursorMsg = 0;
        byte[] data = new byte[128];
        String str = new String();
        try {
            bis.skip(59);
            while((bytes = bis.read(data)) > -1){
                //Get each bytes (total 128)
                for(int i=0; i < bytes; i++) {
                    //if changed byte
                    if (i % numByte == 0) {
                        //get all needed bit
                        for(int j = 0; j <= numberOfBit; j++){
                            str += getBit(data[i], j);
                        }
                        cursorMsg++;
                        //if cursor == part of length
                        if(cursorMsg > msgOfLength){
                            //bin -> String -> long
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

