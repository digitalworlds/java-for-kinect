package j4kdemo.xedconvertapp;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

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

public class KinectFileWriter {

	ZipOutputStream f;
	public int depth_width=0;
	public int depth_height=0;
	public int video_width=0;
	public int video_height=0;
	public int num_of_depth_frames=0;
	public byte device_type=0;
	public String filename;
	
	FileOutputStream temp_file;
	public KinectFileWriter(String filename)
	{
		this.filename=filename;
	}
	
	public void openTempFile()
	{
		try {
			temp_file=new FileOutputStream(new File(filename+".temp"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void closeTempFile()
	{
		if(temp_file!=null)
		{
			try {
				temp_file.close();
				temp_file=null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void openZipFile()
	{
		try {
			f = new ZipOutputStream(new FileOutputStream(new File(filename)));
			num_of_depth_frames=0;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void closeZipFile()
	{
		if(f==null)return;
		try {
			writeHeader();
			f.close();
			f=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeHeader()
	{
		if(f==null)return;
		try {
			
		ZipEntry outEntry = new ZipEntry("header");
		
		//outEntry.setMethod(ZipEntry.STORED);
		//outEntry.setSize(16);
		//outEntry.setCompressedSize(16);
		//outEntry.setCrc(crc.getValue());
		outEntry.setMethod(ZipEntry.DEFLATED);
		f.putNextEntry(outEntry);
		
		PrintWriter outprint = new PrintWriter(f);
		outprint.println("<?xml version=\"2.0\"?>");
		outprint.println("<HEADER>");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			outprint.println("\t<J4K_VERSION>1.0</J4K_VERSION>");
			if(device_type==J4KSDK.MICROSOFT_KINECT_1)
				outprint.println("\t<DEVICE_TYPE>MICROSOFT_KINECT_1</DEVICE_TYPE>");
			else if(device_type==J4KSDK.MICROSOFT_KINECT_2)
				outprint.println("\t<DEVICE_TYPE>MICROSOFT_KINECT_2</DEVICE_TYPE>");
			else	outprint.println("\t<DEVICE_TYPE>UNKNOWN</DEVICE_TYPE>");
			
			outprint.println("\t<DATE>"+sdf.format(date)+"</DATE>");
			outprint.println("\t<DEPTH_WIDTH>"+depth_width+"</DEPTH_WIDTH>");
			outprint.println("\t<DEPTH_HEIGHT>"+depth_height+"</DEPTH_HEIGHT>");
			outprint.println("\t<VIDEO_WIDTH>"+video_width+"</VIDEO_WIDTH>");
			outprint.println("\t<VIDEO_HEIGHT>"+video_height+"</VIDEO_HEIGHT>");
			outprint.println("\t<DEPTH_FRAMES>"+num_of_depth_frames+"</DEPTH_FRAMES>");
		outprint.println("</HEADER>");
		outprint.flush();
		f.closeEntry();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printInfo()
	{
		System.out.println("Depth frame size: "+depth_width+" x "+depth_height);
		System.out.println("Number of depth frames: "+num_of_depth_frames);
	}
	
	private static String int2string(int i)
	{
		if(i<0) return "000000";
		else if(i<10) return "00000"+i;
		else if(i<100) return "0000"+i;
		else if(i<1000) return "000"+i;
		else if(i<10000) return "00"+i;
		else if(i<100000) return "0"+i;
		else return ""+i;
	}
	
	public void writeDepthFrameZip(byte array[])
	{
		if(f==null) return;
		try
		{
			ZipEntry outEntry = new ZipEntry(int2string(num_of_depth_frames)+".depth");
			outEntry.setMethod(ZipEntry.DEFLATED);
			f.putNextEntry(outEntry);
			num_of_depth_frames+=1;
			f.write(array);
			f.closeEntry();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeDepthFrameZip(short array[],float acc[],long time1, long time2)
	{
		if(f==null) return;
		try
		{
			ZipEntry outEntry = new ZipEntry(int2string(num_of_depth_frames)+".depth");
			outEntry.setMethod(ZipEntry.DEFLATED);
			f.putNextEntry(outEntry);
			writeDepthFrame(f,array,acc,time1,time2);
			f.closeEntry();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeDepthFrameTemp(short array[],float acc[],long time1,long time2)
	{
		if(temp_file==null)return;
		writeDepthFrame(temp_file,array,acc,time1,time2);
	}
	
	byte depth_buffer_acc[];
	byte depth_buffer_short[];
	byte time_buff[];
	
	public void writeDepthFrame(OutputStream os, short array[],float acc[],long time1,long time2)
	{
		try
		{
			if(array.length==320*240)
			{
				depth_width=320;
				depth_height=240;
			}
			else if(array.length==640*480)
			{
				depth_width=640;
				depth_height=480;
			}
			
			num_of_depth_frames+=1;
			
			if(time_buff==null) time_buff=new byte[16];
			LongBuffer lb=ByteBuffer.wrap(time_buff).order(ByteOrder.LITTLE_ENDIAN).asLongBuffer();
			lb.put(time1);
			lb.put(time2);
			os.write(time_buff);
			
			if(depth_buffer_acc==null)depth_buffer_acc=new byte[12];
			ByteBuffer.wrap(depth_buffer_acc).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer().put(acc);
			os.write(depth_buffer_acc);
			
			if(depth_buffer_short==null)depth_buffer_short=new byte[array.length*2];
			ByteBuffer.wrap(depth_buffer_short).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(array);
			os.write(depth_buffer_short);
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}

	public void writeVideoFrameZip(byte array[])
	{
		if(f==null) return;
		try {
			
			ZipEntry outEntry = new ZipEntry(int2string(num_of_depth_frames-1)+".image");
			outEntry.setMethod(ZipEntry.DEFLATED);
			f.putNextEntry(outEntry);
			writeVideoFrame(f,array);
			f.closeEntry();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeVideoFrameTemp(byte array[])
	{
		if(temp_file==null)return;
		writeVideoFrame(temp_file,array);
	}
	
	/*public void writeTimeStamp(long time)
	{
		if(temp_file==null)return;
		if(time_buff==null) time_buff=new byte[8];
		ByteBuffer.wrap(time_buff).order(ByteOrder.LITTLE_ENDIAN).asLongBuffer().put(time);
		try {
			temp_file.write(time_buff);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	public void writeVideoFrame(OutputStream os, byte array[])
	{
		try {
			
			if(array.length==320*240*4)
			{
				video_width=320;
				video_height=240;
			}
			else if(array.length==640*480*4)
			{
				video_width=640;
				video_height=480;
			}			
			os.write(array);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeVideoFramePNG(byte array[])
	{
		if(f==null)return;
		try {
			ZipEntry outEntry = new ZipEntry(int2string(num_of_depth_frames-1)+".png");
			outEntry.setMethod(ZipEntry.DEFLATED);
			f.putNextEntry(outEntry);
			
			if(array.length==320*240*4)
			{
				video_width=320;
				video_height=240;
			}
			else if(array.length==640*480*4)
			{
				video_width=640;
				video_height=480;
			}
			
			BufferedImage img=new BufferedImage(video_width,video_height,BufferedImage.TYPE_INT_RGB);
			int idx=0;
			for(int y=0;y<video_height;y++)
			for(int x=0;x<video_width;x++)
			{
				img.setRGB(x,y,(new Color(array[idx+0]&0xFF,array[idx+1]&0xFF,array[idx+2]&0xFF)).getRGB());
				idx+=4;
			}
			
			ImageIO.write(img,"PNG",f);
			
			f.closeEntry();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
