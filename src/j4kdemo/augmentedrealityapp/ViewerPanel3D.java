package j4kdemo.augmentedrealityapp;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


import javax.media.opengl.GL2;

import edu.ufl.digitalworlds.math.Geom;
import edu.ufl.digitalworlds.opengl.OpenGLPanel;
import edu.ufl.digitalworlds.opengl.OpenGLTexture;
import edu.ufl.digitalworlds.j4k.DepthMap;
import edu.ufl.digitalworlds.j4k.Skeleton;
import edu.ufl.digitalworlds.j4k.VideoFrame;

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
public class ViewerPanel3D extends OpenGLPanel
{
	private float view_rotx = 0.0f, view_roty = 0.0f, view_rotz = 0.0f;
	private int prevMouseX, prevMouseY;
	
	DepthMap current_map=null;
	
	VideoFrame videoTexture;
	
	Skeleton skeletons[];
	
	OpenGLTexture xray;
	OpenGLTexture box;
	
	int mode=5;
	float mem_sk[];
	
	public void setup()
	{
		mem_sk=new float[25*3];
		xray=new OpenGLTexture("data/xray.png");
		box=new OpenGLTexture("data/box.png");
		
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
		    
			
			skeletons=new Skeleton[6];
			
			videoTexture=new VideoFrame();
			
		    background(0, 0, 0);	
	}	
	
	
	public void draw() {
		
		GL2 gl=getGL2();
		
		
		pushMatrix();
	    
		translate(0,0,-2);
	    rotate(view_rotx, 1.0, 0.0, 0.0);
	    rotate(view_roty, 0.0, 1.0, 0.0);
	    rotate(view_rotz, 0.0, 0.0, 1.0);
	    translate(0,0,2);        
	    
	    DepthMap map=current_map;
	    
	    if(map!=null) 
	    {
	    	if(mode==0 || mode==1 || mode==2 || mode==3 || mode==4 || mode==5)
	    	{
	    		gl.glDisable(GL2.GL_LIGHTING);
	    		gl.glEnable(GL2.GL_TEXTURE_2D);
	    		gl.glColor3f(1f,1f,1f);
	    		videoTexture.use(gl);
	    		map.drawTexture(gl);
	    		gl.glDisable(GL2.GL_TEXTURE_2D);
	    	}
	
	    	if(mode==1)
	    	{
	    		gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
	    		gl.glEnable(GL2.GL_TEXTURE_2D);
	    		xray.use(gl);
	    		pushMatrix();
	    			translate(0,0.05,-3);
	    			image(0.8,2.1);
	    		popMatrix();
	    		gl.glDisable(GL2.GL_TEXTURE_2D);
	    		gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
	
	    		gl.glColorMask(false,false,false,false);
	    		color(1,0,0);
	    		pushMatrix();
    				translate(-0.55,0,-0.4);
    				rect(1,1);
    			popMatrix();
    			pushMatrix();
					translate(+0.55,0,-0.4);
					rect(1,1);
				popMatrix();
    			pushMatrix();
					translate(0,0.63,-0.4);
					rect(1,1);
				popMatrix();
    			pushMatrix();
					translate(0,-0.63,-0.4);
					rect(1,1);
				popMatrix();
	    		gl.glColorMask(true,true,true,true);
	    	}
	    	
	    	if(mode==1 || mode==2 || mode==3)
	    	{
	    		gl.glEnable(GL2.GL_LIGHTING);
	    		gl.glDisable(GL2.GL_TEXTURE_2D);
	    		gl.glColor3f(0.9f,0.9f,0.9f);
	    		map.maskPlayers();
	    		map.drawNormals(gl);
	    	}
	    }    
	    
	    if(mode==3 || mode==4 || mode==5)
	    {
	    if(mode==3||mode==4||mode==5)gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
	    
	    gl.glDisable(GL2.GL_LIGHTING);
	    gl.glLineWidth(2);
	    gl.glColor3f(1f,0f,0f);
	    boolean found=false;
	    for(int i=0;i<6 && !found;i++)
	    	if(skeletons[i]!=null) 
	    	{
	    		if(skeletons[i].isTracked())
	    		{
	    		float sk[]=skeletons[i].getJointPositions();
	    		for(int j=0;j<sk.length;j++) mem_sk[j]=mem_sk[j]*0.8f+sk[j]*0.2f;
	    		found=true;
	    		}	
	    		if((mode==3 || mode==4) && skeletons[i].getTimesDrawn()<=10 && skeletons[i].isTracked())
	    		{
	    			skeletons[i].draw(gl);
	    			skeletons[i].increaseTimesDrawn();
	    		}
	    	}
	    }
	    
	    if(mode==5)
	    {
	    Skeleton sk=new Skeleton();
	    sk.setJointPositions(mem_sk);
	    double transf[]=Geom.identity4();
	    double inv_transf[]=Geom.identity4();
	    double s=sk.getBetweenHandsTransform(transf, inv_transf);
	    
	    //System.out.println(s);
	    
	    pushMatrix();
	    	gl.glLoadIdentity();
			gl.glMultMatrixd(transf,0);
			gl.glScaled(s,s,s);
			/*gl.glBegin(GL2.GL_LINES);
			gl.glColor3f(0,0,0);gl.glVertex3d(-0.5f,0,0);gl.glColor3f(1,0,0);gl.glVertex3d(0.5f,0,0);
			gl.glColor3f(0,0,0);gl.glVertex3d(0,-0.5f,0);gl.glColor3f(0,1,0);gl.glVertex3d(0,0.5f,0);
			gl.glColor3f(0,0,0);gl.glVertex3d(0,0,-0.5f);gl.glColor3f(0,0,1);gl.glVertex3d(0,0,0.5f);
			gl.glEnd();*/
			
			rotateY(180);
			
			pushMatrix();
			translate(0,0,+0.4);
			//rotateY(180);
			color(1,1,1);
			box.use(gl);
			image(0.8,0.8);
		popMatrix();
		
		pushMatrix();
		translate(+0.4,0,0);
		rotateY(90);
		color(1,1,1);
		box.use(gl);
		image(0.8,0.8);
		popMatrix();
		
		pushMatrix();
		translate(-0.4,0,0);
		rotateY(-90);
		color(1,1,1);
		box.use(gl);
		image(0.8,0.8);
		popMatrix();
		
		pushMatrix();
		translate(0,-0.4,0);
		rotateX(90);
		color(1,1,1);
		box.use(gl);
		image(0.8,0.8);
		popMatrix();
		
		pushMatrix();
		translate(0,0.4,0);
		rotateX(-90);
		color(1,1,1);
		box.use(gl);
		image(0.8,0.8);
		popMatrix();
			
		popMatrix();
	    }
	    popMatrix();
	}
	
	
	public void mouseDragged(int x, int y, MouseEvent e) {

	    //Dimension size = e.getComponent().getSize();

	    
	    if(isMouseButtonPressed(3)||isMouseButtonPressed(1))
	    {
	    	//float thetaY = 360.0f * ( (float)(x-prevMouseX)/(float)size.width);
	    	//float thetaX = 360.0f * ( (float)(prevMouseY-y)/(float)size.height);
	    	//view_rotx -= thetaX;
	    	//view_roty += thetaY;		
	    }
	    
	    prevMouseX = x;
	    prevMouseY = y;

	}

	public void mousePressed(int x, int y, MouseEvent e) {
		prevMouseX = x;
	    prevMouseY = y;
	}
	
	public void keyPressed(char keyChar, KeyEvent e)
	{
		if(e.getKeyCode()==33)//page up
		{
			mode+=1;
			if(mode>5)mode=0;
			
		}
		else if(e.getKeyCode()==34)//page down
		{
			mode-=1;
			if(mode<0) mode=5;
		}
	
	}

}
