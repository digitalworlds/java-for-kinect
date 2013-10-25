package j4kdemo.wrlconvertapp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
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
public class WRLConvertApp extends DWApp
{

	ViewerPanel3D main_panel;
	JButton open;
	String most_recent_path="";
	
	public void GUIsetup(JPanel p_root) {
		
		
		setLoadingProgress("Initalizing OpenGL Panel...",60);
		
			main_panel=new ViewerPanel3D();
			setLoadingProgress("Initializing ...",80);
			p_root.add(main_panel);
			open=new JButton("Convert depth file to VRML (wrl)");
			open.addActionListener(this);
			p_root.add(open,BorderLayout.SOUTH);
	}
	
	public void GUIactionPerformed(ActionEvent e)
	{
		if(e.getSource()==open)
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
	        	main_panel.setFilename(chooser.getSelectedFile().getAbsolutePath());
	     
	        }
		}
	}
	
	public static void main(String args[]) {
		
    	createMainFrame("WRL Convert App");
    	app=new WRLConvertApp();
    	setFrameSize(730,570,null);
    }

}