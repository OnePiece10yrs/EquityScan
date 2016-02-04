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

import java.util.TreeMap;

/**
 *
 * @author schern
 */
public class maList {
    public void putList(java.util.List<maData> tempList) {
        
        for (maData rd : tempList) {
            map.put(rd.getTicker(), rd);
        }
    }
    
    //return map list
    public TreeMap<String, maData> getList(){
        return map;
    }
    
    public void put(maData co){
        map.put(co.getTicker(), co);
    }
    
    public maData get(String key){
        return(map.get(key));
    }
    
    static TreeMap<String, maData> map = new TreeMap<String, maData>();
}
