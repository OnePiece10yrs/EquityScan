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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 *  calculate Put ratio by formula : total put / total option volume
 *  this function filter and flag those only with low or high Put ratio
 * @author schern
 * 
 */
public class PCratio {
    PCratio(String sDate){
        scanDate = sDate;
    }
    public Map<String, resultData> scanRatio(){
        String[] rToken;
        String coSymbol="";
        int vol=0,ttl_call=0, ttl_put=0, ttl_vol;
        String infoMsg="";
        int count=0;
        float ratio;
   
        
        
        //get resultList
        resultList rl = new resultList();
        map = rl.getList();
        
        RWCSVFile readFile = new RWCSVFile();
        ArrayList<String> selectCOs = readFile.subSetList(RWCSVFile.sector_selected);
        Iterator<String> itr = selectCOs.iterator();
        //ArrayList<String> CoList = readFile.readCVS();
        //Iterator<String> itr = CoList.iterator();
        
        //for logs
        MsgCollector mc = new MsgCollector();
      
        
        while(itr.hasNext()){
            int i;
            vol=0; 
            ttl_call=0;
            ttl_put=0;
            ttl_vol=0;

            coSymbol = itr.next();
            
            buildRequest req = new buildRequest();
            String request = req.buildOCC(coSymbol, scanDate);
        
           try {
             URL yah = new URL(request);
            URLConnection yc = yah.openConnection();
            //needed?
            //yc.setRequestProperty("Accept-Charset", "UTF-8");

               BufferedReader in = new BufferedReader( new InputStreamReader(yc.getInputStream(), "UTF-8"));
               String inputLine;
                File tmpFile = new File("tmp.xml");
               FileOutputStream fos = new FileOutputStream(tmpFile);
               Writer out = new OutputStreamWriter(fos, "UTF-8");
               //Writer out = new BufferedWriter(new  OutputStreamWriter(new FileOutputStream(tmp), "UTF-8"));
               while((inputLine = in.readLine()) != null){
                   if(inputLine.startsWith("quantity"))
                       continue;
                   else{          
                    rToken = inputLine.split(",");
                    for(i=0; i<rToken.length; i++){
                        if(i==0){
                            vol = Integer.parseInt(rToken[i]);
                        }else if(i==4 && rToken[i].equals("C")){
                            ttl_call += vol;
                        }else if(i==4 && rToken[i].equals("P")){
                            ttl_put += vol;
                        }
                     }
                   
                   }
               }  
               //System.out.println("totol call = " + ttl_call);
               //System.out.println("totol put = " + ttl_put);
               ttl_vol = ttl_put + ttl_call;
               
               //put-call ratio
               //float ratio = (((float)ttl_put) / ((float)ttl_call)) * 100;
               
               //tmp change to put ratio
               ratio = (((float)ttl_put) / ((float)ttl_vol)) * 100;
               
               //select only high and low put/call ratio
               if(ratio < 20 || ratio > 400){
                   if((ratio == 0 && ttl_vol < 1000)|| (ttl_vol < 1000)){
                       String test = coSymbol + " ttl volume too low. vol= " + ttl_call + " Ratio= " + ratio + "\n";
                       
                       infoMsg = coSymbol + " ttl volume too low. vol= " + ttl_call + " Ratio= " + ratio;
                        System.out.println(infoMsg);
                        //for logs
                        mc.add(infoMsg);
                        //System.console().writer().println(infoMsg);
                   }else{
                       //System.out.println(coSymbol + " ttl_vol = " + ttl_vol + " ttl_put=" + ttl_put + " ttl_call=" + ttl_call + " ratio="+ ratio);
                    resultData rd = new resultData(coSymbol, "0","0", String.format("%.2f",ratio), "0", "", null);
                    //scanOutput.add(rd);
                    map.put(rd.getTicker(), rd);
                    count++;
                   }

               }
                 
               //yahooFinance rs = new yahooFinance();                
               //rs.XMLToBean(tmpFile);
           }catch(java.io.IOException  e){
               System.out.println("IOExeption msg:" + e.getMessage());
           }catch(Exception e){
               //System.out.println("error on " + coSymbol);
               String expMsg = e.getMessage();
               if(!expMsg.toLowerCase().contains(noRecordMsg))
                    System.out.println(coSymbol + " run into other Exeption msg:" + expMsg);
           }finally{
               //System.out.println("end request");      
           }
        }
        //System.out.println("count=" + count);
        
        return map;
    }
    
    private String scanDate;
    private static String noRecordMsg = "no record(s) found";
    Map<String, resultData> map;
}
