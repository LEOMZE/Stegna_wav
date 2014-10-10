package Desteg;

import java.io.*;

public class SRead {
    private String filePath;
    private String message;

    public SRead(String filePath){
        this.filePath = filePath;

    }

    public String desteg(){
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            bufferedInputStream.skip(59);
            byte[] data = new byte[128];
            int bytes;
            int cursorMsg = 0;
            StringBuilder msg = new StringBuilder();
            while((bytes = bufferedInputStream.read(data)) > 0){
                byte[] c_data = new byte[128];
                for(int i=0; i<bytes; i++) {

                    if (i % 4 == 0) {
                        if (data[i] != 0) {
//                           message +=  data[i];
//                            System.out.println(Integer.toBinaryString(data[i]) + "->" + getBit(data[i]));
                            msg.append(getBit(data[i]));
                            System.out.println(msg.toString());
//                            cursorMsg++;
                        }
                    } else {
                        c_data[i] = data[i];
                    }
                }
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    public int getBit(byte ID)
    {
        return (ID >> 0) & 1;
    }

}
