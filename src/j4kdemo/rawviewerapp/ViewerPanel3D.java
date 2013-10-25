package j4kdemo.rawviewerapp;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.media.opengl.GL2;

import edu.ufl.digitalworlds.opengl.OpenGLPanel;
import edu.ufl.digitalworlds.j4k.DepthMap;
import edu.ufl.digitalworlds.j4k.VideoFrame;

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
public class ViewerPanel3D extends OpenGLPanel
{
	private float view_rotx = 0.0f, view_roty = 0.0f, view_rotz = 0.0f;
	private int prevMouseX, prevMouseY;
	
	RAWViewerApp app=null;
	DepthMap map=null;
	DepthMap map_previous=null;
	boolean is_playing=false;
	VideoFrame video_frame;
	
	public void setup()
	{
		
		//OPENGL SPECIFIC INITIALIZATION (OPTIONAL)
		    GL2 gl=getGL2();
		    gl.glEnable(GL2.GL_CULL_FACE);
		    float light_model_ambient[] = {0.3f, 0.3f, 0.3f, 1.0f};
		    float light0_diffuse[] = {0.9f, 0.9f, 0.9f, 0.9f};   
		    float light0_direction[] = {0.0f, -0.4f, 1.0f, 0.0f};
			gl.glEnable(GL2.GL_NORMALIZE);
		    gl.glShadeModel(GL2.GL_SMOOTH);
		    
		    gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_FALSE);
		    gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_FALSE);    
		    gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, light_model_ambient,0);
		    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light0_diffuse,0);
		    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light0_direction,0);
		    gl.glEnable(GL2.GL_LIGHT0);
			
		    gl.glEnable(GL2.GL_COLOR_MATERIAL);
			gl.glEnable(GL2.GL_LIGHTING);
			gl.glColor3f(0.9f,0.9f,0.9f);
		    
			gl.glBlendFunc (GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
			
		    background(0, 0, 0);	
		    
		    video_frame=new VideoFrame();
	}	
	
	
	public void draw() {
		
		GL2 gl=getGL2();
		
		
		pushMatrix();
	    
		translate(0,0,-2);
	    rotate(view_rotx, 1.0, 0.0, 0.0);
	    rotate(view_roty, 0.0, 1.0, 0.0);
	    rotate(view_rotz, 0.0, 0.0, 1.0);
	    translate(0,0,2);        
	    
	    if(app!=null)
		{
	    	if(is_playing)
	    	{
	    		map_previous=map;
	    		map=new DepthMap(app.w,app.h,DepthMap.fromRawDepthFile(new File(app.filename), app.frame_now-1, app.w, app.h, false));
	    		map.maskZ(app.z_threshold.getValue()/100f);
	    		//map.maskRect(130,40,100,100);//head
	    		//map.maskRect(50,140,130,100);//arms
	    		map.setUVuniform();
	    		if(app.frame_now<app.frames)
	    		{
	    			app.frame_now+=1;
	    			app.time_domain.setValue(app.frame_now);
	    		}
	    		else
	    		{
	    			app.play_button.setText("Play");
	    			stop();
	    		}
	    	}
		}
	    
	    if(map!=null)
	    {
	    	if(app.show_diff.isSelected() && map!=null && map_previous!=null)
	    	{

		    	//map.drawNormals(gl);
		    
		    	//gl.glClear(GL2.GL_DEPTH);
			    //gl.glEnable(GL2.GL_BLEND);
		    	
		    	computeColorMap();
		    	gl.glDisable(GL2.GL_LIGHTING);
				gl.glEnable(GL2.GL_TEXTURE_2D);
				gl.glColor4f(1f,1f,1f,0.75f);
				video_frame.use(gl);
				map.drawTextureNormals(gl);
				gl.glDisable(GL2.GL_TEXTURE_2D);
				gl.glEnable(GL2.GL_LIGHTING);
				gl.glDisable(GL2.GL_BLEND);
		    }
	    	else
	    	{
	    		gl.glColor3f(0.9f,0.9f,0.9f);
	    		map.drawNormals(gl);
	    	}
	    }
		
	    popMatrix();
	}
	
	private void computeColorMap()
	{
		if(!app.show_diff.isSelected())return;
		if(map==null || map_previous==null) return;
		byte data[]=new byte[app.w*app.h*4];
		int i=0;
		for(int y=0;y<app.h;y++)
		{
			for(int x=0;x<app.w;x++)
			{
				if(map.realZ[i]>DepthMap.FLT_EPSILON && map_previous.realZ[i]>DepthMap.FLT_EPSILON )
				{
					boolean compute=true;
					for(int yy=-2;yy<=2 && compute;yy++)
					for(int xx=-2;xx<=2 && compute;xx++)
					{
						if(xx!=0 && yy!=0 && x+xx>=0 && x+xx<app.w && y+yy>=0 && y+yy<app.h)
						{
							if(map.realZ[i+xx+yy*app.w]<DepthMap.FLT_EPSILON)compute=false;
							if(map_previous.realZ[i+xx+yy*app.w]<DepthMap.FLT_EPSILON)compute=false;
							if(Math.abs(map.realZ[i+xx+yy*app.w]-map.realZ[i])>0.05) compute=false;
						}
					}
					
					
					
					if(compute)
					{
						if(map.realZ[i]<map_previous.realZ[i])
						{
							data[i*4+0]=0;
							data[i*4+1]=0;
							data[i*4+2]=(byte)Math.min(255,10000*(map_previous.realZ[i]-map.realZ[i]));
						}
						else 
						{
							data[i*4+0]=(byte)Math.min(255,10000*(map.realZ[i]-map_previous.realZ[i]));
							data[i*4+1]=0;
							data[i*4+2]=0;
						}
						//data[i*4+0]=(byte)255;
						//data[i*4+1]=(byte)255;
						//data[i*4+2]=(byte)255;
					}
				}
				
				i++;
			}
		}
		video_frame.update(app.w, app.h, data);
	}
	
	public void play(RAWViewerApp app)
	{
		this.app=app;
		is_playing=true;
	}
	
	public void stop()
	{
		is_playing=false;
	}
	
	public void update(RAWViewerApp app)
	{
		if(!is_playing && app.filename.length()>0)
		{
			this.app=app;
			
			if(app.frame_now>1)
			{
				map_previous=new DepthMap(app.w,app.h,DepthMap.fromRawDepthFile(new File(app.filename), app.frame_now-2, app.w, app.h, false));
	    		map_previous.maskZ(app.z_threshold.getValue()/100f);
	    		map_previous.setUVuniform();
			}
			map=new DepthMap(app.w,app.h,DepthMap.fromRawDepthFile(new File(app.filename), app.frame_now-1, app.w, app.h, false));
    		map.maskZ(app.z_threshold.getValue()/100f);
    		map.setUVuniform();
		}
	}
	
	public void mouseDragged(int x, int y, MouseEvent e) {

	    Dimension size = e.getComponent().getSize();

	    
	    if(isMouseButtonPressed(3)||isMouseButtonPressed(1))
	    {
	    	float thetaY = 360.0f * ( (float)(x-prevMouseX)/(float)size.width);
	    	float thetaX = 360.0f * ( (float)(prevMouseY-y)/(float)size.height);
	    	view_rotx -= thetaX;
	    	view_roty += thetaY;		
	    }
	    
	    prevMouseX = x;
	    prevMouseY = y;

	}

	public void mousePressed(int x, int y, MouseEvent e) {
		prevMouseX = x;
	    prevMouseY = y;
	}
	

}