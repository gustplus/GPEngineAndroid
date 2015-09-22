precision mediump float;
uniform highp mat4 projViewMatrix;
uniform mat4 modelMatrix;

uniform float lightAngle; 

uniform vec3 lightDirction;

uniform vec4 ambientColor;
uniform vec4 diffuseColor;
uniform vec4 specularColor;
uniform vec4 lightPosition;
uniform sampler2D Texture0;
uniform vec3 cameraPosition;

varying vec4 vColor;  
varying vec3 vPosition;
varying vec3 vNormal;

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
   vec3 tempNormal = vPosition+vNormal;
   vec3 finalPosition = (modelMatrix * vec4(vPosition,1)).xyz;
   tempNormal = (modelMatrix * vec4(tempNormal,1)).xyz;
   vec3 finalNormal = normalize(tempNormal-finalPosition);

   float range;
   diffuseLight(finalNormal,lightPosition,lightDirction,finalPosition,range);
   vec4 vDiffuse = range * diffuseColor;
   
   float range2;
   specularLight(finalNormal,lightPosition,lightDirction,finalPosition,range2,90.0);
   vec4 vSpecular =  range2 * specularColor;

   vec4 proPosition = projViewMatrix*vec4(finalPosition,1);
   proPosition = proPosition/proPosition.w;
   float s = proPosition.s+0.5;
   float t = proPosition.t+0.5;
   if(s>=0.0&&s<=1.0&&t>=0.0&&t<=1.0){
	vec4 textureColor = texture2D(Texture0,vec2(s,1.0-t));
	vDiffuse=vDiffuse*textureColor;
	vSpecular=vSpecular*textureColor;
   }
   gl_FragColor = ambientColor*vColor + vDiffuse*vColor + vSpecular*vColor;
}