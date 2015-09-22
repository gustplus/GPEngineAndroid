uniform mat4 uMVPMatrix; //×Ü±ä»»¾ØÕó

//uniform float shiningness;
attribute vec3 aPosition; 
attribute vec3 aNormal;
    
varying vec3 vNormal;
varying vec3 vPosition;
varying vec2 vTexCoords;

void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); 
   vNormal = aNormal;
   vPosition = aPosition;
}                      