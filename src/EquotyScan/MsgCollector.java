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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

/**
 *
 * @author schern
 */
public final class MsgCollector {
    
    MsgCollector(){
        
    
    }

    static void createFrame(){
        mframe=new JFrame("Pattern Logs");
        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize=new Dimension((int)(screenSize.width/2),(int)(screenSize.height/2));
        int x=(int)(frameSize.width/2);
        int y=(int)(frameSize.height/2);
        mframe.setBounds(x,y,frameSize.width,frameSize.height);

        textArea=new JTextArea();
        textArea.setEditable(false);
        mframe.getContentPane().setLayout(new BorderLayout());
	mframe.getContentPane().add(new JScrollPane(textArea),BorderLayout.CENTER);
	
	mframe.setVisible(true);
    }
    
    static JTextArea gettxtArea(){
        return textArea;
    }
    
   
    static void add(String msg){
        //use swing worker to realtime display
        
        //System.out.println("===========msg = " + msg);
        //System.out.println("in add msg, befor swing worker");
        final String tmp = msg;
        SwingWorker<List<String>, String> worker = new SwingWorker<List<String>, String>()   
        {
            
            @Override
            public List<String> doInBackground() throws Exception{
               
                List<String> listm = new ArrayList<String>(5);
                listm.add(tmp);
                publish(tmp);
                return listm;
            }
         
        

        @Override
        protected void process(List<String> chunks) {
            for (String info : chunks) {
               // System.out.println("in Process, info=" + info);
                textArea.append(info + "\n");
            }
        }
 
            
        };
        worker.execute();
       // System.out.print("in add msg, exiting swing worker. msg=");

        //textArea.append(msg);
    }
static private JFrame mframe;
static private JTextArea textArea;
}
