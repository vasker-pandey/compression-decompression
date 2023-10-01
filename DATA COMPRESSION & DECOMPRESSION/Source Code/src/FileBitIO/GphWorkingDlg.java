

package FileBitIO;
import java.io.*;

import javax.swing.*;

import CGZipCompressor.CGZipDecoder;
import CGZipCompressor.CGZipEncoder;

import java.awt.*;
import java.awt.event.*;
import wingph.*;



public class GphWorkingDlg extends javax.swing.JDialog implements GphGuiConstants{
    
    
    	private String gSummary = "";
	private String iFilename,oFilename;
	private boolean bCompress = false;
	private int algoSelected;
    
   
    public GphWorkingDlg(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    GphWorkingDlg(JFrame parent){
		super(parent,true);
		jFrame1 = parent;
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		

		
		
	}
    
    public String getSummary(){
		if(gSummary.length() > 0){
		String line = "----------------------------------------------";
		return line + "\n" + gSummary + line;
		}else return "";
		
		}
    
    void doWork(String inputFilename,String outputFilename,int mode,int algorithm){
		String buf;
		File infile = new File(inputFilename);
		System.out.println(inputFilename);
		
		if(!infile.exists()){
			gSummary += "File Does not Exits!\n";
			return;
			}
		bCompress = (mode == COMPRESS);
                
		
	
		final int falgo = algorithm;
		iFilename = inputFilename;
		oFilename = outputFilename;
		gSummary = "";

		
		final Runnable closeRunner = new Runnable(){
			public void run(){
				setVisible(false);
				dispose();
			}

			};

		Runnable workingThread = new Runnable(){
			public void run(){
				try{
					boolean success = false;
					switch(falgo){
						

					case COMP_GZIP : 
						if(bCompress){
							CGZipEncoder gze = new	CGZipEncoder(iFilename,oFilename);
							success  = gze.encodeFile();
							gSummary += gze.getSummary();
							
						}else{
							CGZipDecoder gzde = new	CGZipDecoder(iFilename,oFilename);
							success = gzde.decodeFile();
							gSummary += gzde.getSummary();
						}
						break;

					}
					
					}catch(Exception e){
					gSummary += e.getMessage();
					}
					
					try{
						SwingUtilities.invokeAndWait(closeRunner );
					}catch(Exception e){
						gSummary += "\n" + e.getMessage();
						}
				
				}
			};
			
			
		Thread work = new Thread(workingThread);
		work.start();
		
		setVisible(true);
		
		
		}
    
    
    private void initComponents() {
        jFrame1 = new javax.swing.JFrame();
        jLabel1 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jLabel1.setText("Nothing");

        jProgressBar1.setValue(10);
        jProgressBar1.setIndeterminate(true);

        jButton1.setText("Cancel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(jButton1)))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        pack();
    }
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GphWorkingDlg(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }
    
    
    private javax.swing.JButton jButton1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JProgressBar jProgressBar1;
    
    
}
