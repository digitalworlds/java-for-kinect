
package j4kdemo.imageavatarapp;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import edu.ufl.digitalworlds.gui.DWApp;
import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.SkeletonStreamSimulator;
import edu.ufl.digitalworlds.opengl.OpenGLImageComposition;

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
public class ImageAvatarApp extends DWApp
{
	
	JButton browse_room;
	JButton browse_avatar1;
	JButton browse_avatar2;
	JButton swap_avatars;
	
	Kinect myKinect;
	SkeletonStreamSimulator stream;
	ViewerPanel3D main_panel;
	public void GUIsetup(JPanel p_root) {
		
		if(System.getProperty("os.arch").toLowerCase().indexOf("64")<0)
		{
			if(DWApp.showConfirmDialog("Performance Warning", "<html><center><br>WARNING: You are running a 32bit version of Java.<br>This may reduce significantly the performance of this application.<br>It is strongly adviced to exit this program and install a 64bit version of Java.<br><br>Do you want to exit now?</center>"))
				System.exit(0);
		}
		
		setLoadingProgress("Intitializing Kinect...",20);
		myKinect=new Kinect();
		if(!myKinect.start(J4KSDK.DEPTH|J4KSDK.SKELETON))
		{
			DWApp.showInformationDialog("Information", "<html><center><br>There was no Kinect sensor detected in your system.</center>");
		}
				
		
		
		JPanel controls=new JPanel(new GridLayout(0,4));
		browse_room=new JButton("Open a different room");
		browse_room.addActionListener(this);
		controls.add(browse_room);
	
		browse_avatar1=new JButton("Open a different avatar 1");
		browse_avatar1.addActionListener(this);
		controls.add(browse_avatar1);
		
		browse_avatar2=new JButton("Open a different avatar 2");
		browse_avatar2.addActionListener(this);
		controls.add(browse_avatar2);
		
		swap_avatars=new JButton("Swap avatars");
		swap_avatars.addActionListener(this);
		controls.add(swap_avatars);
		
		setLoadingProgress("Intitializing OpenGL...",60);
		main_panel=new ViewerPanel3D();
		
		setLoadingProgress("Loading data...",80);
		main_panel.setAvatar1(new CardboardAvatar("http://www.digitalworlds.ufl.edu/angelos/lab/ufdw/j4k/avatars/demo1"));
		main_panel.setAvatar2(new CardboardAvatar("http://www.digitalworlds.ufl.edu/angelos/lab/ufdw/j4k/avatars/demo2"));
		OpenGLImageComposition room=new OpenGLImageComposition();
		room.loadParallel("http://www.digitalworlds.ufl.edu/angelos/lab/ufdw/j4k/rooms/demo");
		main_panel.setRoom(room);
		
		myKinect.setViewer(main_panel);
		
		p_root.add(main_panel, BorderLayout.CENTER);
		p_root.add(controls, BorderLayout.SOUTH);
		
	}
	
	public void GUIclosing()
	{
		myKinect.stop();
	}
	
	
	public static void main(String args[]) {
		
    	createMainFrame("Image Avatar App");
    	app=new ImageAvatarApp();
    	setFrameSize(730,570,null);
    }
	
	@Override
	public void GUIactionPerformed(ActionEvent e)
	{
		if(e.getSource()==browse_room)
		{
			main_panel.room.showOpenDialog();
		}
		else if(e.getSource()==swap_avatars)
		{
			main_panel.swapAvatars();
		}
		else
		{
			JFileChooser chooser = new JFileChooser();
            chooser.setFileHidingEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setMultiSelectionEnabled(false);
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            if(getMostRecentPath().length()>0)
				chooser.setCurrentDirectory(new File(getMostRecentPath()));
            chooser.setDialogTitle("Open Cardboard Avatar");
            chooser.setApproveButtonText("Open"); 
            if (chooser.showOpenDialog(this)== JFileChooser.APPROVE_OPTION) 
            {
            	setMostRecentPath(chooser.getCurrentDirectory().getAbsolutePath());
            	if(e.getSource()==browse_avatar1)
            		main_panel.setAvatar1(new CardboardAvatar(chooser.getSelectedFile().getAbsolutePath()));
            	else if(e.getSource()==browse_avatar2)
            		main_panel.setAvatar2(new CardboardAvatar(chooser.getSelectedFile().getAbsolutePath()));
            }
		}
	}
	
}
