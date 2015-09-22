uniform mat4 uMVPMatrix; //×Ü±ä»»¾ØÕó
uniform mat4 modelMatrix; 

uniform float lightAngle; 
uniform float exponent;

uniform vec3 lightDirction;
uniform vec4 ambientColor;
uniform vec4 diffuseColor;
uniform vec4 specularColor;
uniform vec4 lightPosition;
uniform float shiningness;
uniform vec3 cameraPosition;

attribute vec3 aPosition; 
attribute vec3 aNormal;

//attribute vec4 aColor;    
varying vec4 vColor;      
varying vec4 vAmbient;
varying vec4 vDiffuse;
varying vec4 vSpecular;

void diffuseLight(in vec3 normal,in vec4 lightPosition,in vec3 lightDirction,in vec3 vertPosition, inout float diffuse){
   vec3 vp = normalize(lightPosition.xyz-vertPosition);
   if(dot((-vp),normalize(lightDirction))<cos(radians(lightAngle))){
	diffuse = 0.0;
   }else{
	diffuse = max(dot(normal,-vp),0.0);
   }
}

void specularLight(in vec3 normal, in vec4 lightPosition,in vec3 lightDirction, in vec3 vertPosition ,inout float specular,in float shiningness){
   vec3 v1 = normalize(cameraPosition - vertPosition);
   vec3 v2 = normalize(lightPosition.xyz - vertPosition);
   if(dot((-v2),normalize(lightDirction))<cos(radians(lightAngle))){
	specular = 0.0;
   }else{
	vec3 halfVector = normalize(v1 + v2);
	specular = max(pow(dot(normal,halfVector),shiningness),0.0);
   }
}

void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); 
   vec3 vPosition = (modelMatrix * vec4(aPosition,1)).xyz;
   vec3 tempNormal = aPosition + aNormal;
   tempNormal = (modelMatrix * vec4(tempNormal,1)).xyz;
   vec3 newNormal = normalize(tempNormal - vPosition);
   vAmbient = ambientColor;
   vec3 vp = normalize(lightPosition.xyz-vPosition);
   float power = pow(max(dot((-vp),normalize(lightDirction)),0.0),exponent);

   float range;
   diffuseLight(newNormal,lightPosition,lightDirction,vPosition,range);
   vDiffuse = vec4(power * range * diffuseColor.xyz,1);
   
   float range2;
   specularLight(newNormal,lightPosition,lightDirction,vPosition,range2,shiningness);
   vSpecular =  vec4(power * range2 * specularColor.xyz,1);
   vColor = vec4(0,0,1,1); 
}                      