package j4kdemo.multiplekinectapp;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.ufl.digitalworlds.gui.DWApp;

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
public class MultipleKinectApp extends DWApp
{
	Kinect kinect1;
	Kinect kinect2;
	
	JButton button1;
	JButton button2;
	
	public void GUIsetup(JPanel p_root) {
		
		button1=new JButton("Show Kinect #1");
		button1.addActionListener(this);
		button2=new JButton("Show Kinect #2");
		button2.addActionListener(this);
		JPanel p=new JPanel(new GridLayout(0,2));
		p.add(button1);
		p.add(button2);
		p_root.add(p);
		
		setLoadingProgress("Intitializing Kinect...",20);
		kinect1=new Kinect();
		if(kinect1.start(true,Kinect.NUI_IMAGE_RESOLUTION_320x240,Kinect.NUI_IMAGE_RESOLUTION_640x480)==0)
		{
			DWApp.showErrorDialog("ERROR", "<html><center><br>ERROR: The Kinect #1 device could not be initialized.<br><br>1. Check if the Microsoft's Kinect SDK was succesfully installed on this computer.<br> 2. Check if the Kinect is plugged into a power outlet.<br>3. Check if the Kinect is connected to a USB port of this computer.</center>");
			//System.exit(0); 
		}
		kinect1.computeUV(true);
		
		kinect1.showViewerDialog(false);
		
		kinect2=new Kinect();
		if(kinect2.start(true,Kinect.NUI_IMAGE_RESOLUTION_320x240,Kinect.NUI_IMAGE_RESOLUTION_640x480)==0)
		{
			DWApp.showErrorDialog("ERROR", "<html><center><br>ERROR: The Kinect #2 device could not be initialized.<br><br>1. Check if the Microsoft's Kinect SDK was succesfully installed on this computer.<br> 2. Check if the Kinect is plugged into a power outlet.<br>3. Check if the Kinect is connected to a USB port of this computer.</center>");
			//System.exit(0); 
		}
		kinect2.computeUV(true);
		

		kinect2.showViewerDialog(false);
		
	}
	
	public void GUIclosing()
	{
		kinect1.stop();
		kinect2.stop();
	}
	
	
	public static void main(String args[]) {
		
    	createMainFrame("Two Kinects App");
    	app=new MultipleKinectApp();
    	setFrameSize(730,570,null);
    }
	
	@Override
	public void GUIactionPerformed(ActionEvent e)
	{
		if(e.getSource()==button1)
			kinect1.showViewerDialog(false);
		else if(e.getSource()==button2)
			kinect2.showViewerDialog(false);
	}
	

}
