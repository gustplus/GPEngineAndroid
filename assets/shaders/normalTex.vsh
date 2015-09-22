uniform mat4 uMVPMatrix; //总变换矩阵

attribute vec3 aPosition; 
attribute vec3 aNormal;
attribute vec3 aTangent; //切向量
attribute vec2 aTexCoor;
    
varying vec4 vColor;  
varying vec3 vNormal;
varying vec3 vPosition;
varying vec3 vTangent;
varying vec2 vTexCoor;

void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); 
   vNormal = aNormal;
   vPosition = aPosition;
   vTangent = aTangent;
   vTexCoor = aTexCoor;
}                      