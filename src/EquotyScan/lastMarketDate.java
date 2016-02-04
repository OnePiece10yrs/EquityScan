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


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author schern
 */
public class lastMarketDate {
    
    public String getLastWorkDate(){
        Date today = new Date();
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        
        if(dayOfWeek == Calendar.MONDAY){
            cal.add(Calendar.DATE, - 3);
        }else if(dayOfWeek == Calendar.MONDAY){
            cal.add(Calendar.DATE, - 2);
        }else{
            cal.add(Calendar.DATE, - 1);
        }
        
        Date pDate = cal.getTime();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        
        return df.format(pDate);
        
    }
    
    //convert date format in "EEE, d MMM yyyy HH:mm:ss Z" to "yyyyMMdd" 
    public String convertDate(String longDt){
        
       // Date date=null;
        String sdate="";
        
        try{
        //convet long str to date format
        //System.out.println("converdt: longdt = " + longDt);
        SimpleDateFormat ldf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
        //System.out.println("after ldf = " + ldf.toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
         //System.out.println("after sdf = " + sdf.toString());
        sdate = sdf.format(ldf.parse(longDt));
         //System.out.println("after sdate = " + sdate);
        
        }catch(Exception e){
            System.out.println("convert date error: " + e.getMessage().toString());
        }
        //System.out.println("convert to new date format = " + sdate);
        return sdate;
    }
    
    //compare if the news date is publish within three days including today, today and last market date
    public boolean latestDays(String newsDt){
        
        //get news date in string date short format 'yyyyMMdd'
        String shDt = convertDate(newsDt);
        
        //today's news
        String dt = new SimpleDateFormat("yyyyMMdd").format(new Date());
        if(dt.equals(shDt)) return true;
        
        //yesterday's news
        dt = getLastWorkDate();
        if(dt.equals(shDt)) return true;
        //two days ago
        
        return false;
    }
}
