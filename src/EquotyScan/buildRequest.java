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

    //build put call request string
public class buildRequest {
    public String buildOCC(String ticker, String date){
        String request = "http://www.theocc.com/webapps/volume-query?reportDate=" + date + "&format=csv&volumeQueryType=O" +
    "&symbolType=O&symbol=" + ticker + "&reportType=D&accountType=ALL&productKind=ALL&porc=BOTH";
        
       return request;
    }
    
    //build daily volume ratio request, currently use yahoo finance
    public String buildDVR(String ticker, String date){
        /* arguments:
            p2 price change in %
            v   volume
            a2 avg daily vol
            s7 short interest ratio
        */
        String request = "http://finance.yahoo.com/d/quotes.csv?s=" + ticker+ "&f=p2va2s7&d2=" +date;        
       return request;
    }
    
    //50 & 200 days moving average
    public String buildMA(String ticker, String date){
        /* arguments:
            p previous close price
            s3 50 days moving average
            s4 200 days moving average
        */
        String request = "http://finance.yahoo.com/d/quotes.csv?s=" + ticker+ "&f=pm3m4&d2=" +date;        
       return request;
    }
    
    //get company profile. At this moment only sector is used
    public String buildProfile(String ticker){
        /* arguments:
            p previous close price
            s3 50 days moving average
            s4 200 days moving average
        */
        String request = "http://finance.yahoo.com/q/pr?s=" + ticker;        
       return request;
    }
}
