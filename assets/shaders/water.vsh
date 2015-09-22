uniform mat4 uMVPMatrix; //×Ü±ä»»¾ØÕó
uniform mat4 modelMatrix; 

uniform vec4 lightDirction;
uniform vec4 ambientColor;
uniform vec4 diffuseColor;
uniform vec4 specularColor;
uniform float shiningness;
uniform vec3 cameraPosition;

attribute vec3 aPosition; 
attribute vec3 aNormal;

//attribute vec4 aColor;         
varying vec4 vAmbient;
//varying vec4 vDiffuse;
varying vec4 vSpecular;
varying vec2 texCoord;

void diffuseLight(in vec3 normal,in vec4 lightDirction,in vec3 vertPosition, inout float diffuse){
   diffuse = max(dot(normal,-lightDirction.xyz),0.0);
}

void specularLight(in vec3 normal,in vec4 lightDirction, in vec3 vertPosition ,inout float specular,in float shiningness){
   vec3 v1 = normalize(cameraPosition - vertPosition);
   vec3 v2 = normalize(lightDirction.xyz);
   vec3 halfVector = normalize(v1 - v2);
   specular = max(pow(dot(normal,halfVector),shiningness),0.0);
}

void sphereMap(in vec3 normal, in vec3 vertPosition, inout vec2 texCoords){
   vec3 v = normalize(vertPosition - cameraPosition);
   vec3 vTexCoords = reflect(v, normal);
   float m = vTexCoords.x * vTexCoords.x + vTexCoords.y * vTexCoords.y + (vTexCoords.z+1.0) * (vTexCoords.z+1.0);
   m = sqrt(m);
   texCoords.s = vTexCoords.x / m + 0.5;
   texCoords.t = vTexCoords.y / m + 0.5;
}


void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); 
   vec3 vPosition = (modelMatrix * vec4(aPosition,1)).xyz;
   vec3 tempNormal = aPosition + aNormal;
   tempNormal = (modelMatrix * vec4(tempNormal,1)).xyz;
   vec3 newNormal = normalize(tempNormal - vPosition);
   vAmbient = ambientColor;

   //float range;
   //diffuseLight(newNormal,lightDirction,vPosition,range);
   //vDiffuse = vec4(range * diffuseColor.xyz, 0.4 * diffuseColor.xyz);
   
   float range2;
   specularLight(newNormal,lightDirction,vPosition,range2,shiningness);
   vSpecular =  range2 * specularColor;
   //vSpecular =  specularColor;


   vec2 coord;
   sphereMap(newNormal,vPosition,coord);
   texCoord = coord;
}                      