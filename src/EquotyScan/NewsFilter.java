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

import Option.news.NewsItem;
import Option.news.yahooRSSNewsEngine;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author schern
 */
public class NewsFilter {

    NewsFilter() {
         //do nothing now
         //keywords = new String[]{"rumor","M&A","take over", "dividend","upgrade", "downgrade"};
    }
    
    
    public void scanNews(){
        String coSymbol;
        String newsDesc;
        String matchedKey;
        String newsDate;
        URL newsUrl;
        
        //loop thru resultList for all companies
        //System.out.println("in Newsfiler, keyword = " + keyword);
        resultList rlist = new resultList();
        Map<String, resultData> mymap = rlist.getList();
        Set set = mymap.entrySet();
        Iterator itr = set.iterator();
        while(itr.hasNext()){
            matchedKey="";
            Map.Entry me = (Map.Entry)itr.next();
            resultData rd = (resultData)me.getValue();
            //System.out.println("key = " + me.getKey() + " ratio = " + rd.getRatio());
            coSymbol = rd.getTicker();
            try{
            yahooRSSNewsEngine yrne = new yahooRSSNewsEngine();
            List<NewsItem> NIList = yrne.urlToBean(coSymbol);
            //int i=0;
            
            //fetch all news items
            for(NewsItem ni : NIList){
               // if(coSymbol.equals("CAM"))
                    //System.out.println(coSymbol + " " + ni.getPubDate() + " :" + ni.getDescription());
                //only news within today and last two market open dates
                
                newsDate = ni.getPubDate();
                
                lastMarketDate lm = new lastMarketDate();
                if(lm.latestDays(newsDate) == false){
                    //assume the news are ordered by date
                    //System.out.println("break now. newsDate = " + newsDate);
                    
                    break;
                }
                
                newsDesc = ni.getDescription();
                newsUrl = ni.getlink();
                
                //loop thru keywords
                for(int k=0; k <keywords.length; k++){
                    
                    if(newsDesc.toLowerCase().contains(keywords[k].toLowerCase()) == true){
                        //matchedKey = matchedKey + " " + keywords[k].substring(0, 3);
                        matchedKey = matchedKey + " " + keywords[k];
                        rd = new resultData(coSymbol, rd.getPChange(), rd.getDailyVR(), rd.getRatio(), rd.getDTC(), matchedKey, ni.getlink());
                        mymap.put(coSymbol, rd);
                    
                        System.out.println(coSymbol + ": " + ni.getPubDate() + " *" + keywords[k] + " * " + newsDesc);
                    }
                }
                
            }
            }catch(XMLStreamException xe){
                //do something later
            }catch(MalformedURLException malExp){
                //do something later
            }
        }
        
    }
    
    static String[] keywords = new String[]{"rumor","M&A","buy", "acquire", "take over", "takeover", 
        "dividend","Upgrade", "downgrade"};
}
