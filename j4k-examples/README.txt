-------------------------------------
------------J4K Library--------------
-------------------------------------

The J4K library is an open source Java library that implements a Java binding (wrapper) for the Microsoft's Kinect SDK, and it works with Kinect 1 and Kinect 2 (new Kinect) sensors. You can use it to create your own Java applications that handle the video, depth, and skeleton streams of the Kinect sensor. You can also use multiple sensors in your programs as long as your system allows. Furthermore, the J4K library contains several Java classes that convert the packed depth frames, skeleton frames, and video frames received by a Kinect sensor into easy-to-use Java objects. 

When this program is used for academic or research purposes, please cite the following article that introduced this Java library:

A. Barmpoutis. "Tensor Body: Real-time Reconstruction of the Human Body and Avatar Synthesis from RGB-D', IEEE Transactions on Cybernetics, October 2013, Vol. 43(5), Pages: 1347-1356. 

Link 1: http://ieeexplore.ieee.org/xpl/articleDetails.jsp?arnumber=6583279 
Link 2: http://www.digitalworlds.ufl.edu/angelos/publications.php

The web-site of this project: http://research.dwi.ufl.edu/ufdw/j4k/

Source code examples: http://research.dwi.ufl.edu/ufdw/j4k/examples.php

How to write a Java-Kinect project in less than 10 lines of Java code: http://research.dwi.ufl.edu/ufdw/j4k/examples.php#how

How to develop Kinect applications in Java / OpenGL / Eclipse: http://www.youtube.com/watch?v=q0K4Y4g-hj0

How to install this project in Eclipse: http://research.dwi.ufl.edu/ufdw/j4k/eclipse.php

Copyright 2011-2014, Digital Worlds Institute, University of Florida, Angelos Barmpoutis. All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain this copyright notice, this list of conditions and the following disclaimer.

Redistributions in binary form must reproduce this copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
When this program is used for academic or research purposes, please cite the following article that introduced this Java library: A. Barmpoutis. "Tensor Body: Real-time Reconstruction of the Human Body and Avatar Synthesis from RGB-D', IEEE Transactions on Cybernetics, October 2013, Vol. 43(5), Pages: 1347-1356.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

---------------------------------------------
---------Opional external libraries----------
---------------------------------------------

Optionally, you can use JogAmp's JOGL Java library to visualize the Kinect data as 3D textured surfaces in openGL. The use of JOGL library is optional, and is not required if you don't use the drawing methods provided in the J4K Java classes. For your convenience a copy of the JOGL library is included in this zip package.

The JogAmp's JOGL Java Library by http://jogamp.org is licensed under a 
Creative Commons Attribution 3.0 License.
You can obtain a copy of this license at: http://creativecommons.org/licenses/by/3.0/us/