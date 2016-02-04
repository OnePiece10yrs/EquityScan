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

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author schern
 */
public class GetProfiles extends javax.swing.JFrame {

    /**
     * Creates new form MAvgScan
     */
    public GetProfiles() {
        //initComponents();
        
       getSector();
       
       if(count > 0)
        this.setVisible(true);
    }

    private void getSector(){
        
        String coSymbol="";
        BufferedReader mBufferedReader;
        String sector="";
        String[] rToken;
        count=0;
        String sectorGrp;
        //coData rd;
        
      
        
        try{
            
            //get maList
            coList cl = new coList();
            map = cl.getList();
            
            RWCSVFile readFile = new RWCSVFile();

            ArrayList<String> CoList = readFile.readCVS();
            Iterator<String> itr = CoList.iterator();

            while(itr.hasNext()){
                int i;

                coSymbol = itr.next();

                //get today's data
                buildRequest req = new buildRequest();
                String request = req.buildProfile(coSymbol);

                try {
                 InputStream input = new URL(request).openStream();
                    mBufferedReader=new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    String rLine;
                    while ((rLine=mBufferedReader.readLine()) != null) {
                    //if((rLine=mBufferedReader.readLine()) != null) {
                     //   if(coSymbol.equals("AA")){
                          //System.out.println(rLine);
                        
                        rToken = rLine.split(",");
                        
                        for(i=0; i<rToken.length; i++){
                            if(rToken[i].toLowerCase().contains(sectorTag)){
                                //System.out.println("found tag, curr = " + rToken[i]);
                                sectorGrp = brkTable(rToken[i]);
                                if(sectorGrp.isEmpty()){
                                    System.out.println(coSymbol + " sector is empty!");
                                }else{
                                    coData rd = new coData(coSymbol, sectorGrp);
                                    System.out.println(coSymbol + " sector not empty!" + sectorGrp);
                                    map.put(rd.getTicker(), rd);
           
                                }
                                break;
                            }
                            
                        }
                     //   }//

                    }
                     mBufferedReader.close();

               }catch(java.io.IOException  e){
                   System.out.println(coSymbol + " Retrieve Profile IOExeption msg:" + e.getMessage());
               }catch(Exception e){
                   String expMsg = e.getMessage().toString();
                   //if(!expMsg.toLowerCase().contains(noRecordMsg))
                        System.out.println(coSymbol + " Retrieve Profile runs into other Exeption msg:" + expMsg);

               }finally{
                   //System.out.println("end request");      
               }

                
            }

            //write to csv file
            System.out.println("begin writing profile");
            Boolean wStatus = new RWCSVFile().writeProfile();
            String msg;
               if(wStatus == true)
                   msg = "Update profile successfully";
               else
                    msg = "Update profile failed";
               
               Util.infoDialog("Update Profile", msg);
            
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

        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Moving Average Scan");
        setIconImage(Toolkit.getDefaultToolkit().getImage("sm_Icon.png"));

        jButton1.setText("Get Sector");
        jButton1.setActionCommand("getSector");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getSectorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jButton1)
                .addContainerGap(293, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jButton1)
                .addContainerGap(350, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void getSectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getSectorActionPerformed
         java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               String msg = "get sector completed";
               getSector();
              
              
               
               Util.infoDialog("Scan sectors ", msg);
            }
        });
    }//GEN-LAST:event_getSectorActionPerformed

    private String brkTable(String tblLine){
        String tbElement;
        String tbRow[];
        
        tbRow = tblLine.split("<tr>");
        for(String rowData : tbRow){
            //System.out.println("tbrow = " + rowData);
            
            //find sector row
            if(rowData.toLowerCase().contains(sectorTag)){
                System.out.println("tbrow = " + rowData);
                rowData = "<" + rowData;
                Pattern p = Pattern.compile("\\>(.*?)\\<");
                Matcher m = p.matcher(rowData);
                while(m.find())
                {
                    //m.group(1) is your string. do what you want
                    tbElement = m.group(1);
                    if(tbElement.length() > 0 && !tbElement.toLowerCase().equals(sectorTag)){
                        System.out.println("tb elecemnt = " + m.group(1));
                        return m.group(1);
                    }
                }
/*
                tbElement = rowData.split("<td");
                for(String value : tbElement){
                    System.out.println("tb elecemnt = " + value);
                    //return value;
                }
*/
            }
        }
        return "";
    }
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
                GetProfiles coProfile = new GetProfiles();
                Image img = Util.getThumbnailIncon();
                if(img !=null){
                        coProfile.setIconImage(img);
                }
                
                //coProfile.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables

    private String scanDate;
     Map<String, coData> map;
     private java.util.List <coData> maOutput = new ArrayList<coData>();
     coData mad;
     final static String maTitle = "Moving Average Trend Scan";
     private static int count=0;
     private static String sectorTag = "sector:";
     //private Map<String, coData> coListMap;
}
