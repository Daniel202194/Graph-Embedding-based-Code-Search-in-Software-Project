package IO;

import java.io.IOException;
import java.io.InputStream;

public class MyDecompressorInputStream extends InputStream {
    InputStream in;

    public MyDecompressorInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int i=0;
        while(i<12){
            b[i++] = ((Integer)in.read()).byteValue();
        }
        while(i<b.length){
            Integer temp = in.read();
            String s =  String.format("%8s", Integer.toBinaryString(temp)).replace(" ", "0");
            for(int j=0; j<8 & i<b.length; j++) {
//                int t = s.charAt(j) & 255;
                b[i++] = Byte.parseByte(s.charAt(j)+"",2);
            }
        }
        return 0;
    }


}
