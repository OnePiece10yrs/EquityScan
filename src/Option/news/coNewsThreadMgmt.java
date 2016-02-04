/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Option.news;

import Option.news.yahooRSSNewsEngine;

/**
 *
 * @author schern
 */
public class coNewsThreadMgmt {
    
    public void add(Thread t, yahooRSSNewsEngine newsO){
        currT = t;
        newsObj = newsO;
    }
    
    public void remove(){
        System.out.println("before shut down news thread");
        currT.interrupt();
        //don't wait for previous thread end, go ahead create new one
        currT = null; //if interrup failed, set it to null to make it be GC
        newsObj = null;
    }
    
    private Thread currT;
    private yahooRSSNewsEngine newsObj;
}
