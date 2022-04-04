package IO;

import java.io.IOException;
import java.io.OutputStream;

import static java.lang.Integer.parseInt;

public class MyCompressorOutputStream extends OutputStream {
    OutputStream out;

    public MyCompressorOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
    int i = 0;
        for(i=0;i<12;i++) {
            out.write(parseInt(Byte.toString(b[i])));
            out.flush();
        }
        String temp = "";
        while(i<b.length){
            if(temp.length()<8)
                temp=temp + b[i];
            if(temp.length() == 8 || i == b.length-1) {
                while (i == b.length - 1 && temp.length() < 8) {
                    temp = temp + 0;
                }
                int foo = parseInt(temp, 2);
                out.write(foo);
                out.flush();
                temp = "";
            }
            i++;
        }

    }
}
