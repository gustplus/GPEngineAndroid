precision mediump float;
varying  vec4 vColor; //接收从顶点着色器过来的参数
//varying vec4 vDiffuse;
varying vec4 vSpecular;
varying vec4 vAmbient;
varying vec2 texCoord;
uniform sampler2D reflactMap;

void main()                         
{
   vec4 color = texture2D(reflactMap, texCoord);
   vec4 tempColor = vec4(color.xyz, vAmbient.w);
   gl_FragColor = tempColor + vSpecular;
}