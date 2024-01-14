package j4kdemo.xedconvertapp;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.ufl.digitalworlds.gui.DWApp;
import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.utils.ProgressListener;

/*
 * Copyright 2011-2014, Digital Worlds Institute, University of 
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
public class XEDConvertApp extends DWApp implements ProgressListener{

	JButton button;
	public JLabel fps;
	KinectRecorder kinect;
	
	@Override
	public void GUIsetup(JPanel root) {
		
		if(System.getProperty("os.arch").toLowerCase().indexOf("64")<0)
		{
			if(DWApp.showConfirmDialog("Performance Warning", "<html><center><br>WARNING: You are running a 32bit version of Java.<br>This may reduce significantly the performance of this application.<br>It is strongly adviced to exit this program and install a 64bit version of Java.<br><br>Do you want to exit now?</center>"))
				System.exit(0);
		}
		
		setLoadingProgress("Intitializing Kinect...",20);
		kinect=new KinectRecorder(this);
		if(!kinect.start(J4KSDK.DEPTH|J4KSDK.COLOR))
		{
			DWApp.showErrorDialog("ERROR", "<html><center><br>ERROR: The Kinect device could not be initialized.<br><br>1. Check if the Microsoft's Kinect SDK was succesfully installed on this computer.<br> 2. Check if the Kinect is plugged into a power outlet.<br>3. Check if the Kinect is connected to a USB port of this computer.</center>");	
		}
		else
		{
			kinect.setNearMode(false);
		}
		
		setLoadingProgress("Intitializing Window...",80);
		button=new JButton("Start");
		button.addActionListener(this);
		fps=new JLabel("0");
		
		JPanel panel=new JPanel(new GridLayout(1,0));
		panel.add(button);
		panel.add(fps);
		root.add(panel);
		
	}

	public void GUIclosing()
	{
		kinect.stop();
	}
	
	
	public static void main(String args[]) {
		
    	createMainFrame("J4K");
    	app=new XEDConvertApp();
    	setFrameSize(200,100,null);
    }
	
	@Override
	public void GUIactionPerformed(ActionEvent e)
	{
		if(e.getSource()==button)
		{
			if(button.getText().compareTo("Start")==0)
			{
				JFileChooser chooser = new JFileChooser();
		        chooser.setFileHidingEnabled(false);
		        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        chooser.setMultiSelectionEnabled(false);
		        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		        if(getMostRecentPath().length()>0)
					chooser.setCurrentDirectory(new File(getMostRecentPath()));
		        chooser.setDialogTitle("Save Kinect Data (zip)");
		        chooser.setApproveButtonText("Save"); 
		        
		        if (chooser.showSaveDialog(this)== JFileChooser.APPROVE_OPTION) 
		        {
		        	setMostRecentPath(chooser.getCurrentDirectory().getAbsolutePath());
		        	String filename=chooser.getSelectedFile().getAbsolutePath();
					kinect.startRecording(filename);
					button.setText("Stop");
		        }
			}
			else
			{
				kinect.stopRecording();
				button.setEnabled(false);
				button.setText("Start");
			}
		}
	}

	int max_progress=1;
	
	@Override
	public void setMaxProgress(int value) {max_progress=value;}

	@Override
	public void setProgress(int value) {fps.setText(""+(int)(value*100f/max_progress)+"%");if(value==max_progress)button.setEnabled(true);}
}
