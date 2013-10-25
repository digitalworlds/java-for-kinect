package j4kdemo.kinectviewerapp;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
public class KinectViewerApp extends DWApp implements ChangeListener
{
	
	Kinect myKinect;
	ViewerPanel3D main_panel;
	JSlider elevation_angle;
	JCheckBox near_mode;
	JCheckBox seated_skeleton;
	JCheckBox track_skeleton;
	JButton turn_off;
	JComboBox depth_resolution;
	JComboBox video_resolution;
	JCheckBox show_video;
	JCheckBox mask_players;
	JLabel accelerometer;
	
	public void GUIsetup(JPanel p_root) {
		
		
		setLoadingProgress("Intitializing Kinect...",20);
		myKinect=new Kinect();
		if(myKinect.start(true,Kinect.NUI_IMAGE_RESOLUTION_320x240,Kinect.NUI_IMAGE_RESOLUTION_640x480)==0)
		{
			DWApp.showErrorDialog("ERROR", "<html><center><br>ERROR: The Kinect device could not be initialized.<br><br>1. Check if the Microsoft's Kinect SDK was succesfully installed on this computer.<br> 2. Check if the Kinect is plugged into a power outlet.<br>3. Check if the Kinect is connected to a USB port of this computer.</center>");
			//System.exit(0); 
		}
		
		myKinect.computeUV(true);
		
		near_mode=new JCheckBox("Near mode");
		near_mode.addActionListener(this);
		
		seated_skeleton=new JCheckBox("Seated skeleton");
		seated_skeleton.addActionListener(this);
		
		elevation_angle=new JSlider();
		elevation_angle.setMinimum(-27);
		elevation_angle.setMaximum(27);
		elevation_angle.setValue((int)myKinect.getElevationAngle());
		elevation_angle.setToolTipText("Elevation Angle ("+elevation_angle.getValue()+" degrees)");
		elevation_angle.addChangeListener(this);
		
		turn_off=new JButton("Turn off");
		turn_off.addActionListener(this);
		
		depth_resolution=new JComboBox();
		depth_resolution.addItem("80x60");
		depth_resolution.addItem("320x240");
		depth_resolution.addItem("640x480");
		depth_resolution.setSelectedIndex(1);
		depth_resolution.addActionListener(this);
		
		video_resolution=new JComboBox();
		video_resolution.addItem("640x480");
		video_resolution.addItem("1280x960");
		video_resolution.setSelectedIndex(0);
		video_resolution.addActionListener(this);
		
		track_skeleton=new JCheckBox("Track Skeletons");
		track_skeleton.setSelected(true);
		track_skeleton.addActionListener(this);
		
		show_video=new JCheckBox("Show video");
		show_video.setSelected(true);
		show_video.addActionListener(this);
		
		mask_players=new JCheckBox("Mask Players");
		mask_players.setSelected(false);
		mask_players.addActionListener(this);
		
		JPanel controls=new JPanel(new GridLayout(0,6));
		controls.add(new JLabel("Depth Stream:"));
		controls.add(depth_resolution);
		controls.add(near_mode);
		
		controls.add(new JLabel("Video Stream:"));
		controls.add(video_resolution);
		controls.add(show_video);
		
		controls.add(new JLabel("Skeleton Stream:"));
		controls.add(track_skeleton);
		controls.add(seated_skeleton);
		
		controls.add(mask_players);
		accelerometer=new JLabel("0,0,0");
		controls.add(accelerometer);
		controls.add(elevation_angle);
		//controls.add(turn_off);
		
		
		setLoadingProgress("Intitializing OpenGL...",60);
		main_panel=new ViewerPanel3D();
		myKinect.setViewer(main_panel);
		myKinect.setLabel(accelerometer);
		
		p_root.add(main_panel, BorderLayout.CENTER);
		p_root.add(controls, BorderLayout.SOUTH);
		
	}
	
	public void GUIclosing()
	{
		myKinect.stop();
	}
	
	private void resetKinect()
	{
		if(turn_off.getText().compareTo("Turn on")==0) return;
		
		myKinect.stop();
		int depth_res=Kinect.NUI_IMAGE_RESOLUTION_INVALID;
		if(depth_resolution.getSelectedIndex()==0) depth_res=Kinect.NUI_IMAGE_RESOLUTION_80x60;
		else if(depth_resolution.getSelectedIndex()==1) depth_res=Kinect.NUI_IMAGE_RESOLUTION_320x240;
		else if(depth_resolution.getSelectedIndex()==2) depth_res=Kinect.NUI_IMAGE_RESOLUTION_640x480;
		
		int video_res=Kinect.NUI_IMAGE_RESOLUTION_INVALID;
		if(video_resolution.getSelectedIndex()==0) video_res=Kinect.NUI_IMAGE_RESOLUTION_640x480;
		else if(video_resolution.getSelectedIndex()==1) video_res=Kinect.NUI_IMAGE_RESOLUTION_1280x960;
		
		
		myKinect.start(track_skeleton.isSelected(),depth_res,video_res);
		myKinect.computeUV(true);
		if(seated_skeleton.isSelected())myKinect.startSkeletonTracking(true);
		if(near_mode.isSelected()) myKinect.setNearMode(true);
	}
	
	public static void main(String args[]) {
		
    	createMainFrame("Kinect Viewer App");
    	app=new KinectViewerApp();
    	setFrameSize(730,570,null);
    }
	
	@Override
	public void GUIactionPerformed(ActionEvent e)
	{
		if(e.getSource()==near_mode)
		{
			if(near_mode.isSelected()) myKinect.setNearMode(true);
			else myKinect.setNearMode(false);
		}
		else if(e.getSource()==seated_skeleton)
		{
			if(seated_skeleton.isSelected()) myKinect.startSkeletonTracking(true);
			else myKinect.startSkeletonTracking(false);
		}
		else if(e.getSource()==track_skeleton)
		{
			if(track_skeleton.isSelected())
			{
				if(seated_skeleton.isSelected()) myKinect.startSkeletonTracking(true);
				else myKinect.startSkeletonTracking(false);
			}
			else myKinect.stopSkeletonTracking();
		}
		else if(e.getSource()==turn_off)
		{
			if(turn_off.getText().compareTo("Turn off")==0)
			{
				myKinect.stop();
				turn_off.setText("Turn on");
			}
			else
			{
				turn_off.setText("Turn off");
				resetKinect();
			}
		}
		else if(e.getSource()==depth_resolution)
		{
			resetKinect();
		}
		else if(e.getSource()==video_resolution)
		{
			resetKinect();
		}
		else if(e.getSource()==show_video)
		{
			main_panel.setShowVideo(show_video.isSelected());
		}
		else if(e.getSource()==mask_players)
		{
			myKinect.maskPlayers(mask_players.isSelected());
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource()==elevation_angle)
		{
			if(!elevation_angle.getValueIsAdjusting())
			{
				myKinect.setElevationAngle(elevation_angle.getValue());
				elevation_angle.setToolTipText("Elevation Angle ("+elevation_angle.getValue()+" degrees)");
			}
		}
	}

}
