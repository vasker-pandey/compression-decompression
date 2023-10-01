package CGZipCompressor;

import java.io.*;

import java.util.zip.GZIPInputStream;

public class CGZipDecoder {
    private String fileName;
    private String outputFilename;
    private long fileLen;
    private long outputFilelen;
    private String gSummary;

    public CGZipDecoder() {
        loadFile("", "");
    }

    public CGZipDecoder(String txt) {
        loadFile(txt);
    }

    public CGZipDecoder(String txt, String txt2) {
        loadFile(txt, txt2);
    }

    public void loadFile(String txt) {
        fileName = txt;
        outputFilename = stripExtension(txt, ".gz");
        gSummary = "";
    }

    public void loadFile(String txt, String txt2) {
        fileName = txt;
        outputFilename = txt2;
        gSummary = "";
    }

    String stripExtension(String ff, String ext) {
        ff = ff.toLowerCase();
        if (ff.endsWith(ext.toLowerCase())) {
            return ff.substring(0, ff.length() - ext.length());
        }
        return ff + ".dat";
    }

    public boolean decodeFile() throws Exception {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(fileName));
             FileOutputStream out = new FileOutputStream(outputFilename)) {

            fileLen = new File(fileName).length();

            byte[] buf = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            outputFilelen = new File(outputFilename).length();
            gSummary += ("Compressed File Size : " + fileLen + "\n");
            gSummary += ("Original   File Size : " + outputFilelen + "\n");
        } catch (Exception e) {
            throw e;
        }

        return true;
    }

    public String getSummary() {
        return gSummary;
    }
}
