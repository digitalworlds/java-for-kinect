package j4kdemo.rawviewerapp;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.ufl.digitalworlds.gui.DWApp;
import edu.ufl.digitalworlds.j4k.DepthMap;

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
public class RAWViewerApp extends DWApp implements ChangeListener
{

	ViewerPanel3D main_panel;
	JSlider time_domain;
	JButton play_button;
	JButton browse_button;
	JLabel frame_now_label;
	JLabel file_now;
	JSlider z_threshold;
	JCheckBox show_diff;
	String most_recent_path="";
	String filename="";
	long frames=0;
	int frame_now;
	int w;
	int h;
	
	public void GUIsetup(JPanel p_root) {
		
		
		setLoadingProgress("Initalizing OpenGL Panel...",60);
		
		JPanel controls=new JPanel(new GridLayout(0,5));
		browse_button=new JButton("Open file");
		browse_button.addActionListener(this);
		file_now=new JLabel("No file selected");
		frame_now_label=new JLabel("Frame: 0/0");
		z_threshold=new JSlider();
		z_threshold.setMaximum(400);
		z_threshold.setMinimum(40);
		z_threshold.setValue(400);
		z_threshold.setToolTipText("Threshold Z axis");
		z_threshold.addChangeListener(this);
		show_diff=new JCheckBox("Show Delta Z");
		show_diff.addActionListener(this);
		controls.add(browse_button);
		controls.add(file_now);
		controls.add(frame_now_label);
		controls.add(z_threshold);
		controls.add(show_diff);
		
		
		JPanel player=new JPanel(new BorderLayout());
		time_domain=new JSlider();
		time_domain.setMinimum(1);
		time_domain.setValue(1);
		time_domain.setEnabled(false);
		time_domain.addChangeListener(this);
		play_button=new JButton("Play");
		play_button.setEnabled(false);
		play_button.addActionListener(this);
		player.add(time_domain,BorderLayout.CENTER);
		player.add(play_button,BorderLayout.WEST);
		
			main_panel=new ViewerPanel3D();
			setLoadingProgress("Initializing ...",80);
			p_root.add(main_panel);
			
			p_root.add(controls,BorderLayout.NORTH);
			p_root.add(player,BorderLayout.SOUTH);
	}
	
	
	public static void main(String args[]) {
		
    	createMainFrame("RAW Viewer App");
    	app=new RAWViewerApp();
    	setFrameSize(730,570,null);
    }


	public void GUIactionPerformed(ActionEvent e)
	{
		if(e.getSource()==browse_button)
		{
			JFileChooser chooser = new JFileChooser();
	        chooser.setFileHidingEnabled(false);
	        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        chooser.setMultiSelectionEnabled(false);
	        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
	        if(most_recent_path.length()>0)
				chooser.setCurrentDirectory(new File(most_recent_path));
	        chooser.setDialogTitle("Open a raw depth file");
	        chooser.setApproveButtonText("Open"); 
	        
	        if (chooser.showOpenDialog(this)== JFileChooser.APPROVE_OPTION) 
	        {
	        	most_recent_path=chooser.getCurrentDirectory().getAbsolutePath();
	        	filename=chooser.getSelectedFile().getAbsolutePath();
	        	file_now.setText(filename);
	        	file_now.setToolTipText(filename);
	        	frames=DepthMap.framesInRawDepthFile(new File(filename));
				w=DepthMap.frameWidthInRawDepthFile(new File(filename));
				h=DepthMap.frameHeightInRawDepthFile(new File(filename));
	        	time_domain.setValue(1);
	        	frame_now=1;
        		play_button.setText("Play");
	        	if(frames>0)
	        	{
	        		frame_now_label.setText("Frame: 1/"+frames);
	        		time_domain.setMaximum((int)frames);
	        		play_button.setEnabled(true);
	        		time_domain.setEnabled(true);
	        	}
	        	else
	        	{
	        		frame_now_label.setText("Frame: 0/0");
	        		time_domain.setMaximum(1);
	        		play_button.setEnabled(false);
	        		time_domain.setEnabled(false);
	        	}
	        	
	        }
		}
		else if(e.getSource()==play_button)
		{
			if(play_button.getText().compareTo("Play")==0)
			{
				play_button.setText("Pause");
				if(frame_now==frames)frame_now=1;
				main_panel.play(this);
			}
			else
			{
				play_button.setText("Play");
				main_panel.stop();
			}
		}
	}
	
	public void stateChanged(ChangeEvent e) {
		if(e.getSource()==time_domain)
		{
			frame_now_label.setText("Frame: "+time_domain.getValue()+"/"+frames);
			frame_now=time_domain.getValue();
			main_panel.update(this);
		}
		else if(e.getSource()==z_threshold)
		{
			main_panel.update(this);
		}
	}

}