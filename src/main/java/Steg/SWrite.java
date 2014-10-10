package Steg;

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
            BufferedOutputStream newBufferO = new BufferedOutputStream(
                                              new FileOutputStream(
                                              new File(filePath.substring(0, filePath.indexOf(".")) + "_stego.wav")));
            byte[] b = new byte[59];
            bufferedInputStream.read(b, 0, 59);
            newBufferO.write(b);
            byte[] data = new byte[128];
            int bytes;
            String binMsg = msgToBit(message);
            System.out.println(binMsg);
            int cursorMsg = 0;
            while ((bytes = bufferedInputStream.read(data)) > 0) {
                // do something
                byte[] c_data = new byte[128];

                for(int i=0; i<bytes; i++) {

                    if(i%4 == 0) {
                        if (data[i] == 0) {
                            c_data[i] = data[i];
                        } else {
                            if(cursorMsg < binMsg.length()) {
                                c_data[i] = (byte) (data[i] & Character.getNumericValue(binMsg.toCharArray()[cursorMsg]));
                                cursorMsg++;
                            } else {
                                c_data[i] = data[i];
                            }
                        }
                    }else{
                        c_data[i] = data[i];
                    }

                }

                newBufferO.write(c_data);
            }
            System.out.println("finish!");
            bufferedInputStream.read(data);
            bufferedInputStream.close();
            newBufferO.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String msgToBit(String msg){
        byte[] infoBin;
        String binString = new String();
        try {
            infoBin = msg.getBytes("UTF-8");
            for (byte b : infoBin) {
                binString += Integer.toBinaryString(b);
            }
            return binString;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }


}

//byte[] buffer = {1,2,3,4,5};
//InputStream is = new ByteArrayInputStream(buffer);
//
//byte[] chunk = new byte[2];
//while(is.available() > 0) {
//        int count = is.read(chunk);
//        if (count == chunk.length) {
//        System.out.println(Arrays.toString(chunk));
//        } else {
//        byte[] rest = new byte[count];
//        System.arraycopy(chunk, 0, rest, 0, count);
//        System.out.println(Arrays.toString(rest));
//        }
//        }

