package CGZipCompressor;

import java.io.*;
import java.util.zip.GZIPOutputStream;

public class CGZipEncoder {
    private String fileName;
    private String outputFilename;
    private long fileLen;
    private long outputFilelen;
    private String gSummary;

    public CGZipEncoder() {
        loadFile("", "");
    }

    public CGZipEncoder(String txt) {
        loadFile(txt);
    }

    public CGZipEncoder(String txt, String txt2) {
        loadFile(txt, txt2);
    }

    public void loadFile(String txt) {
        fileName = txt;
        outputFilename = txt + ".gz";
        gSummary = "";
    }

    public void loadFile(String txt, String txt2) {
        fileName = txt;
        outputFilename = txt2;
        gSummary = "";
    }

    public boolean encodeFile() throws Exception {
        if (fileName.length() == 0) return false;

        try (FileInputStream in = new FileInputStream(fileName);
             BufferedInputStream bufferedIn = new BufferedInputStream(in);
             FileOutputStream out = new FileOutputStream(outputFilename);
             GZIPOutputStream gzipOut = new GZIPOutputStream(out)) {

            fileLen = in.available();
            if (fileLen == 0) throw new Exception("Source File Empty!");
            gSummary += "Original Size : " + fileLen + "\n";

            byte[] buf = new byte[1024];
            int len;
            while ((len = bufferedIn.read(buf)) > 0) {
                gzipOut.write(buf, 0, len);
            }

            gzipOut.finish();

            outputFilelen = new File(outputFilename).length();
            float cratio = (float) (outputFilelen * 100) / fileLen;
            gSummary += ("Compressed File Size : " + outputFilelen + "\n");
            gSummary += ("Compression Ratio : " + cratio + "%" + "\n");
        } catch (Exception e) {
            throw e;
        }

        return true;
    }

    public String getSummary() {
        return gSummary;
    }
}
