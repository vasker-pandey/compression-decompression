package CHuffmanCompressor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

import FileBitIO.CFileBitWriter;

public class CHuffmanEncoder implements huffmanSignature {

    private String fileName;
    private String outputFilename;
    private HuffmanNode root = null;
    private long[] freq = new long[MAXCHARS];
    private String[] hCodes = new String[MAXCHARS];
    private int distinctChars = 0;
    private long fileLen = 0;
    private long outputFileLen = 0;
    private FileInputStream fin;
    private BufferedInputStream in;
    private StringBuilder gSummary;

    void resetFrequency() {
        for (int i = 0; i < MAXCHARS; i++)
            freq[i] = 0;

        distinctChars = 0;
        fileLen = 0;
        gSummary = new StringBuilder();
    }

    public CHuffmanEncoder() {
        loadFile("", "");
    }

    public CHuffmanEncoder(String txt) {
        loadFile(txt);
    }

    public CHuffmanEncoder(String txt, String txt2) {
        loadFile(txt, txt2);
    }

    public void loadFile(String txt) {
        fileName = txt;
        outputFilename = txt + strExtension;
        resetFrequency();
    }

    public void loadFile(String txt, String txt2) {
        fileName = txt;
        outputFilename = txt2;
        resetFrequency();
    }

    public boolean encodeFile() throws Exception {
        if (fileName.length() == 0)
            return false;

        try {
            fin = new FileInputStream(fileName);
            in = new BufferedInputStream(fin);
        } catch (IOException e) {
            throw e;
        }

        // Frequency Analysis
        try {
            fileLen = in.available();
            if (fileLen == 0)
                throw new IOException("File is Empty!");

            gSummary.append("Original File Size: ").append(fileLen).append("\n");

            long i = 0;

            in.mark((int) fileLen);
            distinctChars = 0;

            while (i < fileLen) {
                int ch = in.read();
                i++;
                if (freq[ch] == 0)
                    distinctChars++;
                freq[ch]++;
            }

            in.reset();
        } catch (IOException e) {
            throw e;
        }

        gSummary.append("Distinct Chars: ").append(distinctChars).append("\n");

        CPriorityQueue cpq = new CPriorityQueue(distinctChars + 1);

        int count = 0;
        for (int i = 0; i < MAXCHARS; i++) {
            if (freq[i] > 0) {
                count++;
                HuffmanNode np = new HuffmanNode(freq[i], (char) i, null, null);
                cpq.enqueue(np);
            }
        }

        HuffmanNode low1, low2;

        while (cpq.totalNodes() > 1) {
            low1 = cpq.dequeue();
            low2 = cpq.dequeue();
            if (low1 == null || low2 == null) {
                throw new IOException("PQueue Error!");
            }
            HuffmanNode intermediate = new HuffmanNode((low1.freq + low2.freq), (char) 0, low1, low2);
            if (intermediate == null) {
                throw new OutOfMemoryError("Not Enough Memory!");
            }
            cpq.enqueue(intermediate);
        }

        root = cpq.dequeue();
        buildHuffmanCodes(root, "");

        for (int i = 0; i < MAXCHARS; i++)
            hCodes[i] = "";

        getHuffmanCodes(root);

        CFileBitWriter hFile = new CFileBitWriter(outputFilename);

        hFile.putString(hSignature);
        String buf;
        buf = leftPadder(Long.toString(fileLen, 2), 32); // fileLen
        hFile.putBits(buf);
        buf = leftPadder(Integer.toString(distinctChars - 1, 2), 8); // No of Encoded Chars
        hFile.putBits(buf);

        for (int i = 0; i < MAXCHARS; i++) {
            if (!hCodes[i].isEmpty()) {
                buf = leftPadder(Integer.toString(i, 2), 8);
                hFile.putBits(buf);
                buf = leftPadder(Integer.toString(hCodes[i].length(), 2), 5);
                hFile.putBits(buf);
                hFile.putBits(hCodes[i]);
            }
        }

        long lcount = 0;
        while (lcount < fileLen) {
            int ch = in.read();
            hFile.putBits(hCodes[ch]);
            lcount++;
        }

        hFile.closeFile();
        outputFileLen = new File(outputFilename).length();
        float cratio = ((float) outputFileLen * 100) / fileLen;
        gSummary.append("Compressed File Size: ").append(outputFileLen).append("\n");
        gSummary.append("Compression Ratio: ").append(cratio).append("%").append("\n");
        return true;
    }

    void buildHuffmanCodes(HuffmanNode parentNode, String parentCode) {
        parentNode.huffCode = parentCode;

        if (parentNode.left != null)
            buildHuffmanCodes(parentNode.left, parentCode + "0");

        if (parentNode.right != null)
            buildHuffmanCodes(parentNode.right, parentCode + "1");
    }

    void getHuffmanCodes(HuffmanNode parentNode) {
        if (parentNode == null)
            return;

        int asciiCode = (int) parentNode.ch;
        if (parentNode.left == null || parentNode.right == null)
            hCodes[asciiCode] = parentNode.huffCode;

        if (parentNode.left != null)
            getHuffmanCodes(parentNode.left);

        if (parentNode.right != null)
            getHuffmanCodes(parentNode.right);
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
