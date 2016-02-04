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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author schern
 */
public class RWCSVFile {
    
  public ArrayList<String> readCVS() {
        ArrayList<String> toTrace = new ArrayList<String>();

	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
 
	try {
 
		br = new BufferedReader(new FileReader(csvFileOrg));
		while ((line = br.readLine()) != null) {
 
		        // use comma as separator
                        //String ticker = line.split(cvsSplitBy);
			toTrace.add(line);
 
		}
 
	} catch (FileNotFoundException e) {
            //should not reach here!
            e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
 
	//System.out.println("Done");
        return toTrace;
  }
   
  //public ArrayList<ArrayList<String>> genList() {
  public void genList() {
      System.out.println("in genlist");
        //ArrayList<ArrayList<String>> AllList = new ArrayList<ArrayList<String>>();

	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
 
	try {
 
		br = new BufferedReader(new FileReader(csvFile));
                /*
                line = br.readLine();
                System.out.println("lin = " + line);
                scanList.add(parseLine(line));
                */
                
		while ((line = br.readLine()) != null) {
                        AllList.add(parseLine(line));
 
		}
 
	} catch (FileNotFoundException e) {
            //should not reach here!
            e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
 
	//System.out.println("Done");
        //return AllList;
  }
  
  public ArrayList<String> parseLine(String ln){
      ArrayList<String> perLine = new ArrayList<String> ();
      if(ln == null){
          System.err.println("Readline is empty");
          return perLine;
      }else{
        String[] subStr = ln.split(";");
        for(int i=0; i <subStr.length; i++){
            //System.out.println(subStr[i]);
            perLine.add(subStr[i]);
        }
        return perLine;
      }
  }
  
  public ArrayList<String> subSetList(String sector){
      ArrayList<String> subList = new ArrayList<String>();
      String symbol;
      //genList();
      // ArrayList<ArrayList<String>> testList = readFile.genList();
            Iterator<ArrayList<String>> itr2 = AllList.iterator();

            while(itr2.hasNext()){
                ArrayList<String> perItem = itr2.next();
               // for(String s : itr)
               // System.out.println("itr2 = " + perItem);
                if(perItem.get(1).contains(sector) || sector_selected.equals("All")){
                    //System.out.println(" matched " + perItem.get(0));
                    subList.add(perItem.get(0));
                }
                    /*
                Iterator<String> itr3 = perItem.iterator();
                while(itr3.hasNext()){
                    System.out.println("next = " + itr3.next());
                    
                   // System.out.println("prev = " + itr3.Previous());
                }
                */
            }
      return subList;
  }
  public boolean saveScanPattern(String sdate){
      String csvFile = fileDir + sdate + ".csv";
      
      try{
      // create file to write
      FileWriter writer = new FileWriter(csvFile);
      
      //header line
      writer.append("Ticker");
      writer.append(',');
      writer.append("Price Chg %");
      writer.append(',');
      writer.append("Daily Vol %");
      writer.append(',');
      writer.append("Put Ratio");
      writer.append(',');
      writer.append("Day-to-Cover");
      writer.append(',');
      writer.append("Event");
      writer.append('\n');
      
      resultList rlist = new resultList();
      Map<String, resultData> mymap = rlist.getList();
        Set set = mymap.entrySet();
        Iterator itr = set.iterator();
        while(itr.hasNext()){
            Map.Entry me = (Map.Entry)itr.next();
            resultData rd = (resultData)me.getValue();
            writer.append(rd.getTicker());
            writer.append(',');
            writer.append(rd.getPChange());
            writer.append(',');
            writer.append(rd.getDailyVR());
            writer.append(',');
            writer.append(rd.getRatio());
            writer.append(',');
            writer.append(rd.getDTC());
            writer.append(',');
            writer.append(rd.getEvent());
            writer.append('\n');
            
        }
        writer.flush();
        writer.close();
      }catch(Exception exp){
          System.out.println("write cvs file failed: " + exp.getMessage().toString());
          return false;
      }
      return true;
  }
  
  //check if data file exist
  public boolean DataFileExist(){
    File f = new File(csvFile);
    if(f.exists() && !f.isDirectory()) {
        return true; 
    }else{
        String title = "Configuration Error";
        String msg = "Data file not set. \nPlease copy file to user-home/EuotyScan directory and restart application.\nRefer to Read-me file for more info.";
        Util.infoDialog(title, msg);
        return false;
    }
  }
    
   public boolean writeProfile(){
      String csvFile = csvTmpFile;
      
      try{
      // create file to write
      FileWriter writer = new FileWriter(csvFile);
      
      
      coList clist = new coList();
      Map<String, coData> mymap = clist.getList();
        Set set = mymap.entrySet();
        Iterator itr = set.iterator();
        while(itr.hasNext()){
            Map.Entry me = (Map.Entry)itr.next();
            coData rd = (coData)me.getValue();
            writer.append(rd.getTicker());
            writer.append(',');
            writer.append(rd.getSector());
            writer.append('\n');
            
        }
        writer.flush();
        writer.close();
      }catch(Exception exp){
          System.out.println("write cvs file failed: " + exp.getMessage().toString());
          return false;
      }
      return true;
  }
    static private String fileDir = System.getProperty("user.home") + "\\EquotyScan\\";
    static private String csvFile = fileDir + "5BCos.csv";
    static private String csvFileOrg = fileDir + "5BCos_org.csv";
    static private String csvTmpFile = fileDir + "tmp5BCos.csv";
    static private ArrayList<ArrayList<String>> AllList = new ArrayList<ArrayList<String>>();
    
    static String sector_selected=null;
}
