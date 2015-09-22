precision mediump float;
uniform mat4 modelMatrix; 
uniform mat4 viewMatrix;

uniform float lightAngle; 
uniform float exponent; 
uniform samplerCube cube;

uniform float shiningness;
uniform float refractness;//折射率

uniform vec3 lightDirction;
uniform vec4 ambientColor;
uniform vec4 diffuseColor;
uniform vec4 specularColor;
uniform vec4 lightPosition;
uniform vec3 cameraPosition;

varying vec3 vPosition;
varying vec3 vNormal;


void diffuseLight(in vec3 normal,in vec3 verticeToLight,in vec3 lightDirction, inout float diffuse){
   float temp = dot((-verticeToLight),normalize(lightDirction));
   if(temp>cos(radians(lightAngle))){
   //实现光线从中间向四周衰减的效果
    float power = pow(max(temp,0.0),exponent);
	diffuse = max(dot(normal,-verticeToLight),0.0)* power ;
   }else{
	diffuse = 0.0;
   }
}

void specularLight(in vec3 normal, in vec3 verticeToLight,in vec3 lightDirction, in vec3 vertPosition ,inout float specular,in float shiningness){
 vec3 v1 = normalize(cameraPosition - vertPosition);
   float temp = dot((-verticeToLight),normalize(lightDirction));
   if(temp>cos(radians(lightAngle))){
	vec3 halfVector = normalize(v1 + verticeToLight);
	//实现光线从中间向四周衰减的效果
	float power = max(pow(temp,exponent),0.0);
	specular = max(pow(dot(normal,halfVector),shiningness),0.0)* power ;
   }else{
	specular = 0.0;
   }
}

void main()                         
{
   vec3 tempNormal = vPosition+vNormal;
   vec3 finalPosition = (modelMatrix * vec4(vPosition,1)).xyz;
   tempNormal = (modelMatrix * vec4(tempNormal,1)).xyz;
   vec3 finalNormal = normalize(tempNormal-finalPosition);
   vec3 verticeToLight;
   if(lightPosition.w == 1.0)
   verticeToLight = normalize(lightPosition.xyz-finalPosition);
   if(lightPosition.w == 0.0)
   verticeToLight = -lightPosition.xyz;

   float range;
   diffuseLight(finalNormal,verticeToLight,lightDirction,range);
   vec4 vDiffuse = range * diffuseColor;
   
   float range2;
   specularLight(finalNormal,verticeToLight,lightDirction,finalPosition,range2,shiningness);
   vec4 vSpecular =  range2 * specularColor;

   vec3 v = normalize(cameraPosition - finalPosition);
   vec3 vTexCoords = refract(-v, finalNormal,refractness);
   vec4 refractColor = textureCube(cube, vTexCoords);

   gl_FragColor = ambientColor*refractColor + vDiffuse*refractColor + vSpecular;
}