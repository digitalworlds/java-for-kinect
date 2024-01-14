package j4kdemo.xedconvertapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import edu.ufl.digitalworlds.utils.ParallelThread;

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

public class FileCompressor extends ParallelThread{
	
	KinectFileWriter out;
	int num_of_depth_frames;
	
	public FileCompressor(KinectFileWriter file)
	{
		out=file;
		num_of_depth_frames=out.num_of_depth_frames;
	}
	
	@Override
	public void run() {
		
		try {
			File f=new File(out.filename+".temp");
			FileInputStream in=new FileInputStream(f);
		
			out.openZipFile();
			byte depth_array[]=new byte[16+12+2*out.depth_width*out.depth_height];
			//byte video_array[]=new byte[4*out.video_width*out.video_height];
			
			setMaxProgress(num_of_depth_frames);
			
			for(int i=0;i<num_of_depth_frames;i++)
			{
				int bytes_read=in.read(depth_array,0,depth_array.length);
				while(bytes_read<depth_array.length)
					bytes_read+=in.read(depth_array,bytes_read,depth_array.length-bytes_read);
				out.writeDepthFrameZip(depth_array);

				/*bytes_read=in.read(video_array,0,video_array.length);
				while(bytes_read<video_array.length)
					bytes_read+=in.read(video_array,bytes_read,video_array.length-bytes_read);
				out.writeVideoFramePNG(video_array);*/
				
				setProgress(i+1);
			}
			in.close();
			out.closeZipFile();
			f.delete();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
