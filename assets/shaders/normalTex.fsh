precision mediump float;
uniform mat4 modelMatrix;

uniform vec4 ambientColor;
uniform vec4 diffuseColor;
uniform vec4 specularColor;
uniform vec4 lightPosition;
uniform sampler2D Texture0;
uniform sampler2D Texture1;
uniform vec3 cameraPosition;

varying vec4 vColor;  
varying vec3 vPosition;
varying vec3 vNormal;
varying vec3 vTangent;
varying vec2 vTexCoor;

void diffuseLight(in vec3 normal,in vec4 lightPosition,in vec3 lightDirction,in vec3 vertPosition, out float diffuse){
   vec3 vp = normalize(lightPosition.xyz-vertPosition); 
	diffuse = max(dot(normal,-vp),0.0);
}

void specularLight(in vec3 normal, in vec4 lightPosition,in vec3 lightDirction, in vec3 vertPosition ,out float specular,in float shiningness){
   vec3 v1 = normalize(cameraPosition - vertPosition);
   vec3 v2 = normalize(lightPosition.xyz - vertPosition);
   vec3 halfVector = normalize(v1 + v2);
   specular = max(pow(dot(normal,halfVector),shiningness),0.0);
}

void main()                         
{
   //求得世界坐标系中的法向量
   vec3 tempNormal = vPosition+vNormal;
   vec3 finalPosition = (modelMatrix * vec4(vPosition,1)).xyz;
   tempNormal = (modelMatrix * vec4(tempNormal,1)).xyz;
   vec3 finalNormal = normalize(tempNormal-finalPosition);
  
   //求得世界坐标系中的切向量
   vec3 tempTangent = vPosition+vTangent;
   tempTangent = (modelMatrix * vec4(tempTangent,1)).xyz;
   vec3 finalTangent = normalize(tempTangent - finalPosition);

   vec3 otherTangent = normalize(cross(finalNormal,finalTangent));

   //法线贴图表示的实际法线；
   vec3 fNormal = texture2D(Texture1,vTexCoor).rgb;
   fNormal = (fNormal.rgb-0.5)*2;
   fNormal = normalize(fNormal);
   //由局部坐标转换为世界坐标的变换矩阵
   mat3 transformMatrix = mat3(finalTangent,otherTangent,finalNormal);
   // 最后片元的法向量
   fNormal = normalize(transformMatrix*fNormal);

   //计算贴图颜色
   vec4 texColor = texture2D(Texture0,vTexCoor);

   float diffuse;
   diffuseLight(finalNormal,lightPosition,lightDirction,finalPosition,diffuse);
   vec4 vDiffuse = diffuse * diffuseColor;
   
   float specular;
   specularLight(finalNormal,lightPosition,lightDirction,finalPosition,specular,90.0);
   vec4 vSpecular = specular * specularColor;

   gl_FragColor = ambientColor*texColor + vDiffuse*texColor + vSpecular*texColor;
}