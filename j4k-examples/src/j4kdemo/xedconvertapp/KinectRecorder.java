package j4kdemo.xedconvertapp;

import edu.ufl.digitalworlds.j4k.J4KSDK;

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

public class KinectRecorder extends J4KSDK{

	KinectFileWriter out;
	VideoData last_video_frame;
	boolean stop_recording=false;
	int frame_counter=0;
	XEDConvertApp app;
	
	public KinectRecorder(XEDConvertApp app)
	{
		super();
		this.app=app;
	}
	
	public void startRecording(String filename)
	{
		stop_recording=false;
		out=new KinectFileWriter(filename);
		out.openTempFile();
	}
	
	public void stopRecording()
	{
		stop_recording=true;
		while(isProcessingNewFrame(false,false)){try {Thread.sleep(10);} catch (InterruptedException e) {}}
		
		out.closeTempFile();
		FileCompressor fc=new FileCompressor(out);
		fc.addProgressListener(app);
		fc.start("Kinect Compressor");
		out=null;
		stop_recording=false;
	}
	
	private boolean is_processing=false;
	public synchronized boolean isProcessingNewFrame(boolean write, boolean value)
	{
		if(write)is_processing=value;
		return is_processing;
	}
	
	@Override
	public void onDepthFrameEvent(short[] depth_frame, byte[] player_index, float[] XYZ, float[] UV) {
		long time=System.currentTimeMillis();
		if(out!=null)
		{
			if(!stop_recording)
			{			
				frame_counter+=1;
				app.fps.setText(""+frame_counter);
				VideoData video=last_video_frame;
				if(video!=null)
				{
					isProcessingNewFrame(true,true);
					out.depth_width=getDepthWidth();
					out.depth_height=getDepthHeight();
					out.video_width=getColorWidth();
					out.video_height=getColorHeight();
					out.device_type=getDeviceType();
					out.writeDepthFrameTemp(depth_frame,getAccelerometerReading(),time,video.timestamp);
					out.writeVideoFrameTemp(video.data);
					isProcessingNewFrame(true,false);
				}
			}
		}
		
	}

	@Override
	public void onSkeletonFrameEvent(boolean[] flags, float[] positions, float[] orientations, byte[] joint_status) {}
	
	@Override
	public void onColorFrameEvent(byte[] data) {
			last_video_frame=new VideoData(data);
		}
}
