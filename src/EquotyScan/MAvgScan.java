/*
 * EqotyScan - Free software to scan potential long/short stock
 * 
 * Copyright (C) 2016 Susan Chern <olivegreen48@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package EquotyScan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author schern
 */
public class MAvgScan extends javax.swing.JFrame {

    /**
     * Creates new form MAvgScan
     */
    public MAvgScan() {
        //initComponents();
        if(RWCSVFile.sector_selected == null){
            System.err.println("MAveScan sector is null. Secious problem...");
            return;
        }
       mvCompare();
       
       if(count > 0)
        this.setVisible(true);
    }

    private void mvCompare(){
        
        String coSymbol="";
        float price, ma50, ma200;
        float prevPrice, prevMa50, prevMa200;
        BufferedReader mBufferedReader;
        String[] rToken;
        count=0;
        Boolean test = true;
        
        String tuDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        lastMarketDate lmd = new lastMarketDate();
        String prevDate = lmd.getLastWorkDate();
        
         //get map List
        maList rl = new maList();
        map = rl.getList();
        int c=0;
        try{
            RWCSVFile readFile = new RWCSVFile();

            ArrayList<String> selectCOs = readFile.subSetList(RWCSVFile.sector_selected);
            Iterator<String> itr2 = selectCOs.iterator();

            
            while(itr2.hasNext()){
                int i;
                price=0;
                ma50=0;
                ma200=0;
                prevPrice=0;
                prevMa50=0;
                prevMa200=0;


                coSymbol = itr2.next();

                //get today's data
                buildRequest req = new buildRequest();
                String request = req.buildMA(coSymbol, tuDate);

                try {
                 InputStream input = new URL(request).openStream();
                    mBufferedReader=new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    String rLine;
                    //while ((rLine=mBufferedReader.readLine()) != null) {
                    if((rLine=mBufferedReader.readLine()) != null) {
                        //System.out.println("rline = " + rLine);
                        rToken = rLine.split(",");
                        for(i=0; i<rToken.length; i++){
                            if(i==0){   //previous close price
                                price = Float.parseFloat(rToken[i]);
                            }else if(i==1){ // 50days moving average
                                ma50 = Float.parseFloat(rToken[i]);
                            }else if(i==2){ //200days moving average
                                ma200 = Float.parseFloat(rToken[i]);
                            }
                        }

                    }
                     mBufferedReader.close();

               }catch(java.io.IOException  e){
                   System.out.println(coSymbol + " IOExeption msg:" + e.getMessage());
               }catch(Exception e){
                   String expMsg = e.getMessage();
                   //if(!expMsg.toLowerCase().contains(noRecordMsg))
                        System.out.println(coSymbol + " MA run into other Exeption msg:" + expMsg);

               }finally{
                   //System.out.println("end request");      
               }

                //get previous day data
                req = new buildRequest();
                request = req.buildMA(coSymbol, prevDate);

                try {
                 InputStream input = new URL(request).openStream();
                    mBufferedReader=new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    String rLine;
                    //while ((rLine=mBufferedReader.readLine()) != null) {
                    if((rLine=mBufferedReader.readLine()) != null) {
                        rToken = rLine.split(",");
                        for(i=0; i<rToken.length; i++){
                            if(i==0){   //previous close price
                                prevPrice = Float.parseFloat(rToken[i]);
                            }else if(i==1){ // 50days moving average
                                prevMa50 = Float.parseFloat(rToken[i]);
                            }else if(i==2){ //200days moving average
                                prevMa200 = Float.parseFloat(rToken[i]);
                            }
                        }


                    }
                     mBufferedReader.close();

               }catch(java.io.IOException  e){
                   System.out.println(coSymbol + " IOExeption msg:" + e.getMessage());
               }catch(Exception e){
                   String expMsg = e.getMessage();
                   //if(!expMsg.toLowerCase().contains(noRecordMsg))
                        System.out.println(coSymbol + " previous MA run into other Exeption msg:" + expMsg);

               }finally{
                   //System.out.println("end request");      
               }

                if((ma50>ma200) && (prevMa50<=prevMa200)){
                    System.out.println(coSymbol + " MA 50 cross UP Ma200");
                     mad = new maData(coSymbol, "UP");
                     map.put(mad.getTicker(), mad);
                     count++;
                }
                if((ma50<ma200) && (prevMa50>=prevMa200)){
                    System.out.println(coSymbol + " MA 50 cross UNDER Ma200");
                    mad = new maData(coSymbol, "DOWN");
                     map.put(mad.getTicker(), mad);
                     count++;
                }
            }

            if(count ==0){
                String infoMessage = "No MA trend change found!";
                String titleBar = "MA Scan Result";
                Util.infoDialog(titleBar, infoMessage);
                JOptionPane Jop = new JOptionPane();
            }else{
                System.out.println("found ma change, count = " + count + "map count =" + map.size());
                //display result table
                //maList malist = new maList();
                //Map<String, maData> maMap = malist.getList();
                Set set = map.entrySet();
                Map.Entry me;
                Iterator maItr = set.iterator();
                while(maItr.hasNext()){
                    System.out.println("found data ");
                    me = (Map.Entry)maItr.next();
                    mad = (maData)me.getValue();
                    maOutput.add(mad);
                }

                maTmodel.setDisplayValue(maOutput);
                initComponents();
            }
        }catch(Exception e){
            //do nothing now
        }
        
    }
    
    
            
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Moving Average Scan");
        setIconImage(Toolkit.getDefaultToolkit().getImage("sm_Icon.png"));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 342, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 170, Short.MAX_VALUE)
        );

        jLabel1.setText("Note:");

        jLabel2.setText("Trend \"UP\" stands for MA50 cross UP MA200");

        jLabel3.setText("Trend \"DOWN\" stands for MA50 cross DOWN MA200");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addGap(32, 32, 32)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
        );

        setTitle(maTitle);
        //setIconImage(getMyIconImage());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new BorderLayout());
        getContentPane().add(jPanel1);

        jTable1 = new JTable();
        jTable1.setModel(maTmodel);
        jTable1.setColumnSelectionAllowed(true);
        jTable1.setBorder(BorderFactory.createEmptyBorder());
        jTable1.setColumnSelectionAllowed(true);

        jTable1.getTableHeader().setFont(jTable1.getFont().deriveFont(jTable1.getFont().getStyle() | java.awt.Font.BOLD, jTable1.getFont().getSize()+1));
        jTable1.getTableHeader().setBackground(headerBG);
        jTable1.getTableHeader().setForeground(Color.GRAY);
        /*
        jTable1.addMouseListener(new gotoNews());

        //set hand cursor
        final Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        final Cursor defaultCursor = Cursor.getDefaultCursor();
        jTable1.addMouseMotionListener(new MouseAdapter()
            {
                public void mouseMoved(MouseEvent e)
                {
                    int cModel = jTable1.columnAtPoint(e.getPoint());
                    int cView = jTable1.convertColumnIndexToView(cModel);
                    if (cView == 5)
                    {
                        jTable1.setCursor(handCursor);
                    }
                    else
                    {
                        jTable1.setCursor(defaultCursor);
                    }
                }
            });
            */

            jTable1.setPreferredScrollableViewportSize(new Dimension(500, 450));
            jScrollPane1 = new JScrollPane();

            jScrollPane1.setViewportView(jTable1);

            jPanel1.add(jScrollPane1, BorderLayout.CENTER);

            this.pack();

            pack();
        }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MAvgScan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MAvgScan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MAvgScan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MAvgScan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MAvgScan maScan = new MAvgScan();
                Image img = Util.getThumbnailIncon();
                if(img !=null){
                        maScan.setIconImage(img);
                }
                   
                if(count > 0){
                    maScan.setVisible(true);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTable jTable1;
    private javax.swing.JScrollPane jScrollPane1;
    MATableModel maTmodel = new MATableModel();
    private Color headerBG = new Color(204,229,255);
    // End of variables declaration//GEN-END:variables

    private String scanDate;
     Map<String, maData> map;
     private java.util.List <maData> maOutput = new ArrayList<maData>();
     maData mad;
     final static String maTitle = "Moving Average Trend Scan";
     private static int count=0;
     
}
