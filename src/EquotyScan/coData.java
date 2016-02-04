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

/**
 *
 * @author schern
 */
public class coData {
    
    public coData(coData rD){
        this.Ticker = rD.Ticker;
        this.Sector = rD.Sector;
    }
    
    coData(String sym, String sector){
        this.Ticker = sym;
        this.Sector = sector;
    }
    
    public coData Symbol(String sym){
        this.Ticker = sym;
        return this;
    }
    
     public coData maTrend(String sector){
        this.Sector = sector;
        return this;
    }
     
    
   
    public String getTicker(){
        return Ticker;
    }
    
     public String getSector(){
        return Sector;
    }
     
    
    
    private String Ticker = "";
    private String Sector = "";
    

    
}
