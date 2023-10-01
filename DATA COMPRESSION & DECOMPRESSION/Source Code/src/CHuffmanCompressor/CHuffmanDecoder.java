package CHuffmanCompressor;

import java.io.FileOutputStream;

import java.io.IOException;

import FileBitIO.CFileBitReader;

public class CHuffmanDecoder implements huffmanSignature {

    private String fileName;
    private String outputFilename;
    private String[] hCodes = new String[MAXCHARS];
    private int distinctChars = 0;
    private long fileLen = 0;
    private long outputFileLen;
    private FileOutputStream outf;
    private StringBuilder gSummary;

    public CHuffmanDecoder() {
        loadFile("", "");
    }

    public CHuffmanDecoder(String txt) {
        loadFile(txt);
    }

    public CHuffmanDecoder(String txt, String txt2) {
        loadFile(txt, txt2);
    }

    public void loadFile(String txt) {
        fileName = txt;
        outputFilename = stripExtension(txt, strExtension);
        gSummary = new StringBuilder();
    }

    public void loadFile(String txt, String txt2) {
        fileName = txt;
        outputFilename = txt2;
        gSummary = new StringBuilder();
    }

    String stripExtension(String ff, String ext) {
        ff = ff.toLowerCase();
        if (ff.endsWith(ext.toLowerCase())) {
            return ff.substring(0, ff.length() - ext.length());
        }
        return "_" + ff;
    }

    public boolean decodeFile() throws IOException, Exception {
        if (fileName.length() == 0)
            return false;

        for (int i = 0; i < MAXCHARS; i++)
            hCodes[i] = "";

        long i;
        CFileBitReader fin = new CFileBitReader(fileName);
        fileLen = fin.available();

        String buf;
        buf = fin.getBytes(hSignature.length());

        if (!buf.equals(hSignature))
            return false;

        outputFileLen = Long.parseLong(fin.getBits(32), 2);
        distinctChars = Integer.parseInt(fin.getBits(8), 2) + 1;
        gSummary.append("Compressed File Size: ").append(fileLen).append("\n");
        gSummary.append("Original File Size: ").append(outputFileLen).append("\n");
        gSummary.append("Distinct Chars: ").append(distinctChars).append("\n");

        for (i = 0; i < distinctChars; i++) {
            int ch = Integer.parseInt(fin.getBits(8), 2);
            int len = Integer.parseInt(leftPadder(fin.getBits(5), 8), 2);
            hCodes[ch] = fin.getBits(len);
        }

        try {
            outf = new FileOutputStream(outputFilename);
            i = 0;
            int k;
            int ch;

            while (i < outputFileLen) {
                buf = "";
                for (k = 0; k < 32; k++) {
                    buf += fin.getBit();
                    ch = findCodeword(buf);
                    if (ch > -1) {
                        outf.write((char) ch);
                        buf = "";
                        i++;
                        break;
                    }
                }
                if (k >= 32)
                    throw new Exception("Corrupted File!");
            }

            outf.close();
            return true;

        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    int findCodeword(String cw) {
        int ret = -1;
        for (int i = 0; i < MAXCHARS; i++) {
            if (!hCodes[i].isEmpty() && cw.equals(hCodes[i])) {
                ret = i;
                break;
            }
        }
        return ret;
    }

    String leftPadder(String txt, int n) {
        while (txt.length() < n)
            txt = "0" + txt;
        return txt;
    }

    String rightPadder(String txt, int n) {
        while (txt.length() < n)
            txt += "0";
        return txt;
    }

    public String getSummary() {
        return gSummary.toString();
    }
}
