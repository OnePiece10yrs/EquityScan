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
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author schern
 */
public class statusMsg {
    statusMsg(){
        smf.setSize(300,200);
        smf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
    public void setMsg(String msg){
        JLabel lb = new JLabel(msg, SwingConstants.CENTER);
        //lb.setPreferredSize(new Dimension(300, 100));
        //smf.getContentPane().setLayout(new FlowLayout());
        smf.getContentPane().add(lb);
        
        smf.setLocationRelativeTo(null);
        smf.pack();
        smf.setVisible(true);
    }
    
    public void dispost(){
        smf.dispose();
    }
    private JFrame smf = new JFrame("Status");
}
