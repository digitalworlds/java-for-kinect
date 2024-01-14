package j4kdemo.imageavatarapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.ufl.digitalworlds.files.FileUtils;
import edu.ufl.digitalworlds.j4k.ImageAvatar;
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

public class CardboardAvatar extends ImageAvatar{

	public CardboardAvatar(String foldername)
	{
		super();
		loadFromXML(foldername,null);
	}
	
	public CardboardAvatar(String foldername, Object inJar)
	{
		super();
		loadFromXML(foldername,inJar);
	}
	
	private void loadFromXML(String foldername, Object inJar)
	{
		InputStream is;
		try {			
			is = FileUtils.open(foldername+"/avatar.xml",inJar);
			if(is==null) return;
			Document doc=null;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(is);	
			if(doc==null)return;	
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("AVATAR");

			boolean done=false;
			String s;
			for (int temp = 0; temp < nList.getLength() && !done; temp++) 
			{
				  Node nNode = nList.item(temp);
				  if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				  {
				      Element eElement = (Element) nNode;
				      
				      
				      double v[]=new double[4];v[3]=1;
				      
				      v[0]=FileUtils.parseFloat(FileUtils.getXMLTagValue("CENTER_SHOULDER_X", eElement));
				      v[1]=FileUtils.parseFloat(FileUtils.getXMLTagValue("CENTER_SHOULDER_Y", eElement));
				      v[2]=FileUtils.parseFloat(FileUtils.getXMLTagValue("CENTER_SHOULDER_Z", eElement));
				      setTorsoPoint(SHOULDER_CENTER,v);
				      
				      v[0]=FileUtils.parseFloat(FileUtils.getXMLTagValue("LEFT_SHOULDER_X", eElement));
				      v[1]=FileUtils.parseFloat(FileUtils.getXMLTagValue("LEFT_SHOULDER_Y", eElement));
				      v[2]=FileUtils.parseFloat(FileUtils.getXMLTagValue("LEFT_SHOULDER_Z", eElement));
				      setTorsoPoint(SHOULDER_LEFT,v);
				      
				      v[0]=FileUtils.parseFloat(FileUtils.getXMLTagValue("RIGHT_SHOULDER_X", eElement));
				      v[1]=FileUtils.parseFloat(FileUtils.getXMLTagValue("RIGHT_SHOULDER_Y", eElement));
				      v[2]=FileUtils.parseFloat(FileUtils.getXMLTagValue("RIGHT_SHOULDER_Z", eElement));
				      setTorsoPoint(SHOULDER_RIGHT,v);
				      
				      v[0]=FileUtils.parseFloat(FileUtils.getXMLTagValue("CENTER_HIP_X", eElement));
				      v[1]=FileUtils.parseFloat(FileUtils.getXMLTagValue("CENTER_HIP_Y", eElement));
				      v[2]=FileUtils.parseFloat(FileUtils.getXMLTagValue("CENTER_HIP_Z", eElement));
				      setTorsoPoint(HIP_CENTER,v);
				      
				      v[0]=FileUtils.parseFloat(FileUtils.getXMLTagValue("LEFT_HIP_X", eElement));
				      v[1]=FileUtils.parseFloat(FileUtils.getXMLTagValue("LEFT_HIP_Y", eElement));
				      v[2]=FileUtils.parseFloat(FileUtils.getXMLTagValue("LEFT_HIP_Z", eElement));
				      setTorsoPoint(HIP_LEFT,v);
				      
				      v[0]=FileUtils.parseFloat(FileUtils.getXMLTagValue("RIGHT_HIP_X", eElement));
				      v[1]=FileUtils.parseFloat(FileUtils.getXMLTagValue("RIGHT_HIP_Y", eElement));
				      v[2]=FileUtils.parseFloat(FileUtils.getXMLTagValue("RIGHT_HIP_Z", eElement));
				      setTorsoPoint(HIP_RIGHT,v);
				      
				      setSegmentLength(HEAD,FileUtils.parseFloat(FileUtils.getXMLTagValue("HEAD_LENGTH", eElement)));
				      setSegmentLength(TORSO,FileUtils.parseFloat(FileUtils.getXMLTagValue("TORSO_LENGTH", eElement)));
				      setSegmentLength(ARM_LEFT,FileUtils.parseFloat(FileUtils.getXMLTagValue("LEFT_ARM_LENGTH", eElement)));
				      setSegmentLength(FOREARM_LEFT,FileUtils.parseFloat(FileUtils.getXMLTagValue("LEFT_FOREARM_LENGTH", eElement)));
				      setSegmentLength(ARM_RIGHT,FileUtils.parseFloat(FileUtils.getXMLTagValue("RIGHT_ARM_LENGTH", eElement)));
				      setSegmentLength(FOREARM_RIGHT,FileUtils.parseFloat(FileUtils.getXMLTagValue("RIGHT_FOREARM_LENGTH", eElement)));
				      setSegmentLength(THIGH_LEFT,FileUtils.parseFloat(FileUtils.getXMLTagValue("LEFT_THIGH_LENGTH", eElement)));
				      setSegmentLength(LEG_LEFT,FileUtils.parseFloat(FileUtils.getXMLTagValue("LEFT_LEG_LENGTH", eElement)));
				      setSegmentLength(THIGH_RIGHT,FileUtils.parseFloat(FileUtils.getXMLTagValue("RIGHT_THIGH_LENGTH", eElement)));
				      setSegmentLength(LEG_RIGHT,FileUtils.parseFloat(FileUtils.getXMLTagValue("RIGHT_LEG_LENGTH", eElement)));
				
				    //setImageAspectRatio(HEAD,FileUtils.parseFloat(FileUtils.getXMLTagValue("HEAD_TEXTURE_ASPECT_RATIO", eElement)));					      
				      setImageAspectRatio(TORSO,FileUtils.parseFloat(FileUtils.getXMLTagValue("TORSO_TEXTURE_ASPECT_RATIO", eElement)));
				      setImageAspectRatio(ARM_LEFT,FileUtils.parseFloat(FileUtils.getXMLTagValue("LEFT_ARM_TEXTURE_ASPECT_RATIO", eElement)));
				      setImageAspectRatio(FOREARM_LEFT,FileUtils.parseFloat(FileUtils.getXMLTagValue("LEFT_FOREARM_TEXTURE_ASPECT_RATIO", eElement)));
				      setImageAspectRatio(ARM_RIGHT,FileUtils.parseFloat(FileUtils.getXMLTagValue("RIGHT_ARM_TEXTURE_ASPECT_RATIO", eElement)));
				      setImageAspectRatio(FOREARM_RIGHT,FileUtils.parseFloat(FileUtils.getXMLTagValue("RIGHT_FOREARM_TEXTURE_ASPECT_RATIO", eElement)));
				      setImageAspectRatio(THIGH_LEFT,FileUtils.parseFloat(FileUtils.getXMLTagValue("LEFT_THIGH_TEXTURE_ASPECT_RATIO", eElement)));
				      setImageAspectRatio(LEG_LEFT,FileUtils.parseFloat(FileUtils.getXMLTagValue("LEFT_LEG_TEXTURE_ASPECT_RATIO", eElement)));
				      setImageAspectRatio(THIGH_RIGHT,FileUtils.parseFloat(FileUtils.getXMLTagValue("RIGHT_THIGH_TEXTURE_ASPECT_RATIO", eElement)));
				      setImageAspectRatio(LEG_RIGHT,FileUtils.parseFloat(FileUtils.getXMLTagValue("RIGHT_LEG_TEXTURE_ASPECT_RATIO", eElement)));
				      
				      
				      //s=foldername+"/"+FileUtils.getXMLTagValue("HEAD_TEXTURE", eElement);
				      //textures[HEAD].loadImage(ResourceRetriever.getResourceAsStream(s),true);
				      
				      s=foldername+"/"+FileUtils.getXMLTagValue("TORSO_TEXTURE", eElement);
				      setImage(TORSO,FileUtils.open(s,inJar));
				      
				      s=foldername+"/"+FileUtils.getXMLTagValue("LEFT_ARM_TEXTURE", eElement);
				      setImage(ARM_LEFT,FileUtils.open(s,inJar));
				      
				      s=foldername+"/"+FileUtils.getXMLTagValue("LEFT_FOREARM_TEXTURE", eElement);
				      setImage(FOREARM_LEFT,FileUtils.open(s,inJar));
				      
				      s=foldername+"/"+FileUtils.getXMLTagValue("RIGHT_ARM_TEXTURE", eElement);
				      setImage(ARM_RIGHT,FileUtils.open(s,inJar));
				      
				      s=foldername+"/"+FileUtils.getXMLTagValue("RIGHT_FOREARM_TEXTURE", eElement);
				      setImage(FOREARM_RIGHT,FileUtils.open(s,inJar));				      
				      
				      s=foldername+"/"+FileUtils.getXMLTagValue("LEFT_THIGH_TEXTURE", eElement);
				      setImage(THIGH_LEFT,FileUtils.open(s,inJar));
				      
				      s=foldername+"/"+FileUtils.getXMLTagValue("LEFT_LEG_TEXTURE", eElement);
				      setImage(LEG_LEFT,FileUtils.open(s,inJar));				  
				      
				      s=foldername+"/"+FileUtils.getXMLTagValue("RIGHT_THIGH_TEXTURE", eElement);
				      setImage(THIGH_RIGHT,FileUtils.open(s,inJar));
				      
				      s=foldername+"/"+FileUtils.getXMLTagValue("RIGHT_LEG_TEXTURE", eElement);
				      setImage(LEG_RIGHT,FileUtils.open(s,inJar));
				      
				
				      
				      done=true;
				  }
			}
			
			is.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

	}
	
	
}
