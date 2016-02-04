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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author schern
 */
public class MsgConsole extends WindowAdapter implements WindowListener, Runnable{
    private JFrame frame;
    private JTextArea textArea;
    private Thread reader;
    private Thread reader2;
    private boolean quit;

    private final PipedInputStream pin=new PipedInputStream();
    private final PipedInputStream pin2=new PipedInputStream();
    
    Thread errorThrower; // just for testing (Throws an Exception at this Console
    
    MsgConsole()
    {
        System.out.println("In msgconsole!");
        // create all components and add them
        frame=new JFrame("POption Logs");
	Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
	Dimension frameSize=new Dimension((int)(screenSize.width/2),(int)(screenSize.height/2));
	int x=(int)(frameSize.width/2);
	int y=(int)(frameSize.height/2);
	frame.setBounds(x,y,frameSize.width,frameSize.height);

	textArea=new JTextArea();
	textArea.setEditable(false);
        //not need now
	//JButton button=new JButton("clear");

        frame.getContentPane().setLayout(new BorderLayout());
	frame.getContentPane().add(new JScrollPane(textArea),BorderLayout.CENTER);
	// syc not need now
        //frame.getContentPane().add(button,BorderLayout.SOUTH);
	frame.setVisible(true);

        //syc not need now
	//frame.addWindowListener(this);
	
        //button.addActionListener(this);

        //re-direct system.out
	try 
	{
            PipedOutputStream pout=new PipedOutputStream(this.pin);
            System.setOut(new PrintStream(pout,true));
	}
	catch (java.io.IOException io)
	{
            textArea.append("Couldn't redirect STDOUT to this console\n"+io.getMessage());
	}
	catch (SecurityException se)
	{
            textArea.append("Couldn't redirect STDOUT to this console\n"+se.getMessage());
        }

        //re-direct syste.err
	try
	{
            PipedOutputStream pout2=new PipedOutputStream(this.pin2);
            System.setErr(new PrintStream(pout2,true));
	}
	catch (java.io.IOException io)
	{
			textArea.append("Couldn't redirect STDERR to this console\n"+io.getMessage());
	}
	catch (SecurityException se)
	{
            textArea.append("Couldn't redirect STDERR to this console\n"+se.getMessage());
	}

	quit=false; // signals the Threads that they should exit

	// Starting two separate threads to read from the PipedInputStreams
	//
	reader=new Thread(this);
	reader.setDaemon(true);
	reader.start();
		//
	reader2=new Thread(this);
	reader2.setDaemon(true);
	reader2.start();

	// testing part
	// you may omit this part for your application
	//
	
	System.out.println("Do NOT close this Log window!");
        System.out.println("Some log messages are very informational...\n");
        
                // testing part
	// you may omit this part for your application
	/*
	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	String[] fontNames=ge.getAvailableFontFamilyNames();
	
                for(int n=0;n<fontNames.length;n++)  System.out.println(fontNames[n]);
		// Testing part: simple an error thrown anywhere in this JVM will be printed on the Console
		// We do it with a seperate Thread becasue we don't wan't to break a Thread used by the Console.

            System.out.println("\nLets throw an error on this console");
            errorThrower=new Thread(this);
            errorThrower.setDaemon(true);
            errorThrower.start();
        */
	}
   
    public synchronized void windowClosed(WindowEvent evt)
    {
        quit=true;
        this.notifyAll(); // stop all threads
        try { reader.join(1000);pin.close();   } catch (Exception e){}
        try { reader2.join(1000);pin2.close(); } catch (Exception e){}
        System.exit(0);
    }
    
    public synchronized void windowClosing(WindowEvent evt)
    {
        frame.setVisible(false); // default behaviour of JFrame
        frame.dispose();
    }
   /* syc 
    public synchronized void actionPerformed(ActionEvent evt)
    {
        //clear all text
        textArea.setText("");
    }
    */
    public synchronized void run()
    {
        try
        {
            while (Thread.currentThread()==reader)
            {
                try { this.wait(100);}catch(InterruptedException ie) {
                    System.out.println("reader interruped");
                 }
                if (pin.available()!=0)
                {
                    String input=this.readLine(pin);
                    textArea.append(input);
                }
                if (quit) return;
            }

            while (Thread.currentThread()==reader2)
            {
                try { this.wait(100);}catch(InterruptedException ie) {}
                if (pin2.available()!=0)
                {
                    String input=this.readLine(pin2);
                    textArea.append(input);
                }
                if (quit) return;
            }
        } catch (Exception e)
        {
            textArea.append("\nConsole reports an Internal error.");
            textArea.append("The error is: "+e);
        }

	// just for testing (Throw a Nullpointer after 1 second)
        /*
        if (Thread.currentThread()==errorThrower)
        {
            try { this.wait(1000); }catch(InterruptedException ie){}
            throw new NullPointerException("Application test: throwing an NullPointerException It should arrive at the console");
        }
                */

    }
    
    public synchronized String readLine(PipedInputStream in) throws IOException
    {
        String input="";
        do
        {
            int available=in.available();
            if (available==0) break;
            byte b[]=new byte[available];
            in.read(b);
            input=input+new String(b,0,b.length);
        }while( !input.endsWith("\n") &&  !input.endsWith("\r\n") && !quit);
        return input;
    }
    

    
    
}
