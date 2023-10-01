package FileBitIO;

import java.io.*;

public class CFileBitWriter {

    private String fileName;
    private File outputFile;
    private FileOutputStream fout;
    private BufferedOutputStream outf;
    private String currentByte;

    public CFileBitWriter() throws Exception {
        fileName = "";
    }

    public CFileBitWriter(String txt) throws Exception {
        fileName = txt;
        loadFile(fileName);
    }

    public boolean loadFile(String txt) throws Exception {
        fileName = txt;

        try {
            outputFile = new File(fileName);
            fout = new FileOutputStream(outputFile);
            outf = new BufferedOutputStream(fout);
            currentByte = "";
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    public void putBit(int bit) throws Exception {
        try {
            bit = bit % 2;
            currentByte += Integer.toString(bit);

            if (currentByte.length() >= 8) {
                int byteval = Integer.parseInt(currentByte.substring(0, 8), 2);
                outf.write(byteval);
                currentByte = ""; // Reset
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void putBits(String bits) throws Exception {
        try {
            for (int i = 0; i < bits.length(); i++) {
                int bit = Integer.parseInt(bits.substring(i, i + 1));
                putBit(bit);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void putString(String txt) throws Exception {
        try {
            for (int i = 0; i < txt.length(); i++) {
                String binstring = Integer.toString(txt.charAt(i), 2);
                binstring = leftPad8(binstring);
                putBits(binstring);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private String leftPad8(String txt) {
        while (txt.length() < 8) {
            txt = "0" + txt;
        }
        return txt;
    }

    private String rightPad8(String txt) {
        while (txt.length() < 8) {
            txt += "0";
        }
        return txt;
    }

    public void closeFile() throws Exception {
        try {
           
            while (currentByte.length() > 0) {
                putBit(1);
            }
            outf.close();
        } catch (Exception e) {
            throw e;
        }
    }
}
