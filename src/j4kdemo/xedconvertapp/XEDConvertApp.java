package j4kdemo.xedconvertapp;

import java.awt.event.ActionEvent;


import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import edu.ufl.digitalworlds.gui.DWApp;
import edu.ufl.digitalworlds.j4k.J4KSDK;

/*
 * Copyright 2011, Digital Worlds Institute, University of 
 * Florida, Angelos Barmpoutis.
 * All rights reserved.
 *
 * When this program is used for academic or research purposes, 
 * please cite the following article that introduced this Java library: 
 * 
 * A. Barmpoutis. "Tensor Body: Real-time Reconstruction of the Human Body 
 * and Avatar Synthesis from RGB-D', IEEE Transactions on Cybernetics, 
 * October 2013, Vol. 43(5), Pages: 1347-1356. 
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *     * Redistributions of source code must retain this copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce this
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
@SuppressWarnings("serial")
public class XEDConvertApp extends DWApp
{
	
	public Kinect myKinect;
	JButton save;
	String most_recent_path="";
	
	public void GUIsetup(JPanel p_root) {
		
		
		setLoadingProgress("Intitializing Kinect...",20);
		
		myKinect=new Kinect();
		if(myKinect.start(true,J4KSDK.NUI_IMAGE_RESOLUTION_320x240,J4KSDK.NUI_IMAGE_RESOLUTION_640x480)==0)
		{
			DWApp.showErrorDialog("ERROR", "<html><center><br>ERROR: The Kinect device could not be initialized.<br><br>1. Check if the Microsoft's Kinect SDK was succesfully installed on this computer.<br> 2. Check if the Kinect is plugged into a power outlet.<br>3. Check if the Kinect is connected to a USB port of this computer.</center>");
			System.exit(0); 
		}
		myKinect.setNearMode(true);
		
			save=new JButton("Save");
			save.addActionListener(this);
			p_root.add(save);
	}
	
	public void GUIclosing()
	{
		myKinect.closeDepthFile();
		myKinect.stop();
	}
	
	
	public void GUIactionPerformed(ActionEvent e)
	{
		if(e.getSource()==save && save.getText().compareTo("Save")==0)
		{
			JFileChooser chooser = new JFileChooser();
	        chooser.setFileHidingEnabled(false);
	        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        chooser.setMultiSelectionEnabled(false);
	        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
	        if(most_recent_path.length()>0)
				chooser.setCurrentDirectory(new File(most_recent_path));
	        chooser.setDialogTitle("Save depth frames");
	        chooser.setApproveButtonText("Save"); 
	        
	        if (chooser.showSaveDialog(this)== JFileChooser.APPROVE_OPTION) 
	        {
	        	most_recent_path=chooser.getCurrentDirectory().getAbsolutePath();
	        	String filename=chooser.getSelectedFile().getAbsolutePath();
	        	myKinect.saveDepthFrames(filename);
	        	save.setText("Stop");
	        }
		}
		else if(e.getSource()==save && save.getText().compareTo("Stop")==0)
		{
			myKinect.stop();
			myKinect.closeDepthFile();
			if(myKinect.start(true,J4KSDK.NUI_IMAGE_RESOLUTION_320x240,J4KSDK.NUI_IMAGE_RESOLUTION_640x480)==0)
			{
				DWApp.showErrorDialog("ERROR", "<html><center><br>ERROR: The Kinect device could not be initialized.<br><br>1. Check if the Microsoft's Kinect SDK was succesfully installed on this computer.<br> 2. Check if the Kinect is plugged into a power outlet.<br>3. Check if the Kinect is connected to a USB port of this computer.</center>");
				System.exit(0); 
			}
			myKinect.setNearMode(true);
			
			save.setText("Save");
		}
	}
	
	public static void main(String args[]) {
		
    	createMainFrame("XED Convert App");
    	app=new XEDConvertApp();
    	setFrameSize(730,570,null);
    }

}