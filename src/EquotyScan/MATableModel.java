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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author schern
 */
public class MATableModel extends AbstractTableModel{
    public MATableModel() {
        for (int i = 0; i < columnNames.length; i++) {
            columnNameMapping.put(columnNames[i], i);
        }
    }
    
    @Override
    public int getRowCount() {
        return maTable.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        List<Object> tValue = maTable.get(row);
        return tValue.get(col);
        
    }
    
    @Override
    public String getColumnName(int col){
        return columnNames[col];
    }
    
    public void add(List<Object> rData){
       maTable.add(rData);
       fireTableRowsInserted(maTable.size()-1, maTable.size()-1);
   }
    
   public void setDisplayValue(List<maData> rData){
        int i=0;
        for(maData ni : rData){
            maTable.add(resultToList(ni));
            fireTableRowsInserted(i,i);
            i++;
        }   
    }
   
   // extract only needed info from object
   
    private List<Object> resultToList(maData item){
        List<Object> list = new ArrayList<Object>();
        list.add(item.getTicker());
        list.add(item.getMATrend());
        return list;
        
    }
    
    static {
        final String[] tmp = {
            "Ticker",
            "Trend",
        };
        
        columnNames = tmp;
    }
    private static final String[] columnNames;
    private final Map<String, Integer> columnNameMapping = new ConcurrentHashMap<String, Integer>();
    private final List<List<Object>> maTable = new ArrayList<List<Object>>();
}
