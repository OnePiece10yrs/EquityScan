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

import java.net.URL;

/**
 *
 * @author schern
 */
public class resultData {
    
    public resultData(resultData rD){
        this.Ticker = rD.Ticker;
        this.PCratio = rD.PCratio;
        this.dailyVR = rD.dailyVR;
        this.PCratio = rD.PCratio;
        this.DTC = rD.DTC;
        this.event = rD.event;
        this.newsUrl = rD.newsUrl;
    }
    
    resultData(String sym, String pc, String dvr, String pcr, String dtc, String keywd, URL url){
        this.Ticker = sym;
        this.pChange = pc;
        this.dailyVR = dvr;
        this.PCratio = pcr;
        this.DTC = dtc;
        this.event = keywd;
        this.newsUrl = url;
    }
    
    public resultData Symbol(String sym){
        this.Ticker = sym;
        return this;
    }
    
     public resultData PCratio(String pcr){
        this.PCratio = pcr;
        return this;
    }
     
     public resultData priceChange(String pc){
        this.pChange = pc;
        return this;
    }
     
    public resultData dailyVolR(String dvr){
        this.dailyVR = dvr;
        return this;
    }
    
   public resultData dayToCover(String dtc){
        this.DTC = dtc;
        return this;
    } 

   public resultData setEvent(String event) {
        this.event = event;
        return this;
    }
   
   public resultData setUrl(URL url){
       this.newsUrl = url;
       return this;
   }
   
    public String getTicker(){
        return Ticker;
    }
    
     public String getRatio(){
        return PCratio;
    }
     
     public String getPChange(){
        return pChange;
    }
     
    public String getDailyVR(){
        return dailyVR;
    }
    
    public String getDTC(){
        return DTC;
    }
    
    public String getEvent() {
        return event;
    }
    
    public URL getURL(){
        return newsUrl;
    }
    
    private String Ticker = "";
    private String pChange = "";
    private String dailyVR = "";
    private String PCratio = "";
    private String DTC = "";
    private String event = "";
    private URL newsUrl = null;

    
}
