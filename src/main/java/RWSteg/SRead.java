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
        StringBuilder secretMsg = new StringBuilder();
        try {
            int bytes;
            byte[] data = new byte[128];
            boolean endFlag = false;
            FileInputStream fileInputStream = new FileInputStream(filePath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            bufferedInputStream.mark(128);
            long msgLength = getLengthMsg(bufferedInputStream);
            bufferedInputStream.reset();
            bufferedInputStream.skip(59);
            System.out.println("\n\nStart desteg!!!");
            String str = new String();
            String secret = new String();
            while((bytes = bufferedInputStream.read(data)) > -1){
                for(int i=0; i<bytes; i++) {
                    if ((i % numByte == 0) & data[i] != 1 & data[i] != 0 & endFlag == false) {
//                        if (cursorMsg == 8) {
//                            String str = new String();
//                            for (Integer b : arrayList) {
//                                b = b & 0xFF;
//                                for(int j = 0; j < numberOfBit;j++){
//                                    str += getBit(b, j);
//                                    arrayOfByte.add((byte) getBit(b, j));
//                                }
//
//                            }
//                            System.out.println(str);
//                            if(endFlag == false){
//                               String secret = new String(new BigInteger(str, 2).toByteArray(), "Cp866");//тут был utf-8
//
//                                secretMsg.append(secret);
//                                cursorMsg = 0;
//                                arrayList.clear();
//                            } else {
//                                endFlag = true;
//                            }
//                        }
//                        arrayList.add((int)(data[i]));
//                        cursorMsg++;
                        for(int j = 0; j < numberOfBit; j++){
                            str += getBit(data[i], j);
                            if(str.length() == msgLength){
                                for(int ii = 0; ii < msgLength; ii = ii + 8){
                                    String s = str.substring(ii , ii + 8);
                                    secret += new String(new BigInteger(s , 2).toByteArray()/*, "Cp866"*/);
                                    endFlag = true;
                                }
                            }
                        }
                    }
                }
            }
            System.out.println(secret);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return secretMsg.toString();
    }

    private static int getBit(int data, int position) {
        return (data >> position) & 1;
    }

    private long getLengthMsg(BufferedInputStream bis){
        long length = 0;
        int msgOfLength = 64 / numberOfBit;
        int bytes;
        int cursorMsg = 0;
        byte[] data = new byte[128];
        String str = new String();
        try {
            bis.skip(59);
            while((bytes = bis.read(data)) > -1){
                for(int i=0; i < bytes; i++) {
                    if ((i % numByte == 0) & data[i] != 1 & data[i] != 0) {
                        for(int j = 0; j < numberOfBit;j++){
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


