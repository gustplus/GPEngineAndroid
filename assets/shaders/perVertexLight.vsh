uniform mat4 uMVPMatrix; //×Ü±ä»»¾ØÕó

//uniform float shiningness;
attribute vec3 aPosition; 
attribute vec3 aNormal;

//attribute vec4 aColor;    
varying vec4 vColor;  
varying vec3 vNormal;
varying vec3 vPosition;

void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); 
   vNormal = aNormal;
   vPosition = aPosition;
   vColor = vec4(0,0,1,1); 
}                      