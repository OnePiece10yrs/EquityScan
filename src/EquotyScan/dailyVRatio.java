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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author schern
 * @param scan date string 
 */
public class dailyVRatio {
    public dailyVRatio(String datestr){
        sDate = datestr;
    }

    /**
     *
     * @return resultData objects that contains daily price %, volume ratio, short ratio
     */
    public void scanDVR(){
         String[] rToken;
        String coSymbol="", dtc="", pchange="";
        int vol, avgVol;
        float dailyRatio, tmpdtc;
        BufferedReader mBufferedReader;
        String infoMsg="";
               
        
        String symbol;
        
        resultList rlist = new resultList();
        Map<String, resultData> mymap = rlist.getList();
        Set set = mymap.entrySet();
        Iterator itr = set.iterator();
        
         //for logs
        MsgCollector mc = new MsgCollector();
        
        while(itr.hasNext()){
            int i;
            vol=0; 
            avgVol=0;
            mBufferedReader=null;
            
            Map.Entry me = (Map.Entry)itr.next();
            resultData rd = (resultData)me.getValue();
            //System.out.println("key = " + me.getKey() + " ratio = " + rd.getRatio());
            coSymbol = rd.getTicker();
            buildRequest req = new buildRequest();
            String request = req.buildDVR(coSymbol, sDate);
        
           try {
             InputStream input = new URL(request).openStream();
                mBufferedReader=new BufferedReader(new InputStreamReader(input, "UTF-8"));
                String rLine;
                //while ((rLine=mBufferedReader.readLine()) != null) {
                if((rLine=mBufferedReader.readLine()) != null) {
                    rToken = rLine.split(",");
                    for(i=0; i<rToken.length; i++){
                        if(i==0){   //price change in %
                            pchange = stripQuoteChar(rToken[i]);
                            //System.out.println(coSymbol + " price change = " +pchange);
                        }else if(i==1){ //vol
                            vol = Integer.parseInt(rToken[i]);
                        }else if(i==2){ //daily avg vol
                            avgVol = Integer.parseInt(rToken[i]);
                        }else if(i==3){ //short ratio or day-to-cover
                            dtc = stripQuoteChar(rToken[i]);
                        }
                    }
                    //dailyRatio = (Float.valueOf(vol/avgVol)).floatValue();
                    dailyRatio = (float)vol/(float)avgVol;
                    //keep only those with positive price change
                    if(Double.parseDouble(pchange) >= 0.00){
                    //if((int)pchange >= 0){
                        //System.out.println("add to output " + coSymbol);
                        rd = new resultData(coSymbol, pchange, String.format("%.2f",dailyRatio), rd.getRatio() , dtc, "", null);
                        mymap.put(coSymbol, rd);
                       
                        
                    }else{
                        infoMsg = "remove " + coSymbol + " price change= " + pchange + "\n";
                        System.out.println("remove " + coSymbol + " price change= " + pchange);
                        itr.remove();
                        mc.add(infoMsg);
                    }
                    
                    //if(ratio > 2)
                      //  System.out.println(coSymbol + " ratio=" + ratio +": " +rLine);
                     
                }
                 mBufferedReader.close();
                
           }catch(java.io.IOException  e){
               System.out.println("IOExeption msg:" + e.getMessage());
           }catch(Exception e){
               String expMsg = e.getMessage();
               //if(!expMsg.toLowerCase().contains(noRecordMsg))
                    System.out.println(coSymbol + " run into other Exeption msg:" + expMsg);
                    itr.remove();
           }finally{
               //System.out.println("end request");      
           }
        }
        
        /* for now
        
        
        RWCSVFile readFile = new RWCSVFile();
        ArrayList<String> CoList = readFile.readCVS();
        Iterator<String> itr = CoList.iterator();
        while(itr.hasNext()){
            int i;
            vol=0; 
            avgVol=0;
            pchange=0;
            dtc=0;
            BufferedReader mBufferedReader=null;

            coSymbol = itr.next();
            
            buildRequest req = new buildRequest();
            String request = req.buildDVR(coSymbol, sDate);
        
           try {
             InputStream input = new URL(request).openStream();
                mBufferedReader=new BufferedReader(new InputStreamReader(input, "UTF-8"));
                String rLine;
                //while ((rLine=mBufferedReader.readLine()) != null) {
                if((rLine=mBufferedReader.readLine()) != null) {
                    rToken = rLine.split(",");
                    for(i=0; i<rToken.length; i++){
                        if(i==0){   //price change in %
                            pchange = Float.valueOf(stripQuoteChar(rToken[i]));
                            //System.out.println(coSymbol + " price change = " +pchange);
                        }else if(i==1){ //vol
                            vol = Integer.parseInt(rToken[i]);
                        }else if(i==2){ //daily avg vol
                            avgVol = Integer.parseInt(rToken[i]);
                        }else if(i==3){ //short ratio or day-to-cover
                            dtc = Float.valueOf(stripQuoteChar(rToken[i]));
                        }
                    }
                    ratio = vol/avgVol;
                    if(pchange > 3 && ratio > 1){
                        System.out.println("add to output " + coSymbol);
                        resultData rd = new resultData(coSymbol, pchange, ratio, 0, dtc);
                        scanOutput.add(rd);
                    }
                    
                    //if(ratio > 2)
                      //  System.out.println(coSymbol + " ratio=" + ratio +": " +rLine);
                     
                }
                 mBufferedReader.close();
                
           }catch(java.io.IOException  e){
               System.out.println("IOExeption msg:" + e.getMessage());
           }catch(Exception e){
               String expMsg = e.getMessage();
               //if(!expMsg.toLowerCase().contains(noRecordMsg))
                    System.out.println(coSymbol + " run into other Exeption msg:" + expMsg);
           }finally{
               //System.out.println("end request");      
           }
        }//end of while
        
*/ //for now
        
    }
    
    private String stripQuoteChar(String str){
        String newStr = str.replaceAll("^\"|\"$", "");
        newStr = newStr.replaceAll("%", "");
        return newStr;
    }
    
    private final String sDate;
    
    private resultData rObj;
}
