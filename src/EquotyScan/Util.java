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

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

/**
 *
 * @author schern
 */
public class Util {
    
    public static void setDefaultLookNFeel(){
         try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("InternalFrame.activeTitleBackground", new ColorUIResource(Color.WHITE ));
        }
        catch (Exception exp) {
            System.out.println("Error in setDefaultLookNFeel. msg: "+ exp.getMessage());
        }
         
         //todo: set color n font later
    }
    
    public static Image getThumbnailIncon(){
        Image img=null;
        java.net.URL url = ClassLoader.getSystemResource("EquotyScan/images/icon.png");
        if(url == null)
            System.err.println("no image path");
        else{
            Toolkit kit = Toolkit.getDefaultToolkit();
            img = kit.createImage(url);
        }
        return img;
    }
    //info dialog message 
    public static void infoDialog(String title, String infoMessage){
        JOptionPane Jop = new JOptionPane();
        Jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        Jop.setMessage(infoMessage);
        JDialog dialog = Jop.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        Image img = Util.getThumbnailIncon();
                    if(img !=null){
                        dialog.setIconImage(img);
                    }
                    
        dialog.setVisible(true);
    }
    
    private String[] sectorList(){
        return sectors;
    }
    
    static String[] sectors = new String[]{
        "Select one",
        "Basic Materials", 
        "Consumer Goods", 
        "Energy",
        "Financial",
        "Healthcare",
        "Industrial Goods",
        "Services",
        "Technology",
        "Utilities",
        "Others",
        "All"
    };
    
}
