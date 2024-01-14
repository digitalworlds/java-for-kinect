package j4kdemo.augmentedrealityapp;

import edu.ufl.digitalworlds.j4k.DepthMap;
import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;


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
public class Kinect extends J4KSDK{

	ViewerPanel3D viewer=null;
	
	public Kinect()
	{
		super();
	}
	
	
	public void setViewer(ViewerPanel3D viewer){this.viewer=viewer;}
	
	
	@Override
	public void onDepthFrameEvent(short[] depth, byte[] player_index, float[] XYZ, float[] UV) {
		if(viewer==null)return;
		DepthMap map=new DepthMap(getDepthWidth(),getDepthHeight(),XYZ);
		map.setPlayerIndex(depth, player_index);
		if(UV!=null) map.setUV(UV);
		map.setMaximumAllowedDeltaZ(5);
		viewer.current_map=map;
	}

	@Override
	public void onSkeletonFrameEvent(boolean[] flags, float[] positions, float[] orientations, byte[] joint_status) {
		if(viewer==null || viewer.skeletons==null)return;
		for(int i=0;i<getMaxNumberOfSkeletons();i++)
		viewer.skeletons[i]=Skeleton.getSkeleton(i, flags, positions,orientations, joint_status,this);
		
	}

	@Override
	public void onColorFrameEvent(byte[] data) {
		if(viewer==null || viewer.videoTexture==null) return;
		viewer.videoTexture.update(getColorWidth(), getColorHeight(), data);
	}


}
