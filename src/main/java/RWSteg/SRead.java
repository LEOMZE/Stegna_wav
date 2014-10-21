package RWSteg;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

public class SRead {
    private String filePath;

    public SRead(String filePath){
        this.filePath = filePath;
    }

    public String desteg(){
        StringBuilder secretMsg = new StringBuilder();
        try {
            int bytes;
            int cursorMsg = 0;
            byte[] data = new byte[128];
            boolean endFlag = false;
            FileInputStream fileInputStream = new FileInputStream(filePath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            bufferedInputStream.skip(59);
            System.out.println("\n\nStart desteg!!!");
            while((bytes = bufferedInputStream.read(data)) > -1){
                for(int i=0; i<bytes; i++) {
                    if (i % 4 == 0 & data[i] != 1 & data[i] != 0 & endFlag == false) {
                        if (cursorMsg == 8) {
                            String str = new String();
                            for (Integer b : arrayList) {
                                b = b & 0xFF;
                                str += getBit(b, 0);
                            }
//                            System.out.println(str);
                            if(!str.equals("11000110") && endFlag == false){
                               String secret = new String(new BigInteger(str, 2).toByteArray(), "Cp866");//тут был utf-8
//                                System.out.println("secret letter: " + secret + " | " + str);
                                secretMsg.append(secret);
                                cursorMsg = 0;
                                arrayList.clear();
                            } else {
                                endFlag = true;
                            }
                        }
                        arrayList.add((int)(data[i]));
                        cursorMsg++;
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return secretMsg.toString();
    }

    private static int getBit(int data, int position) {
//        System.out.println(Integer.toBinaryString(data) + " get last bit " + ((data >> position) & 1));
        return (data >> position) & 1;
    }

}


