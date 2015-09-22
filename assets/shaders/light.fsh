precision mediump float;
varying  vec4 vColor; //接收从顶点着色器过来的参数
varying vec4 vDiffuse;
varying vec4 vSpecular;
varying vec4 vAmbient;

void main()                         
{
   
   gl_FragColor = vAmbient*vColor + vDiffuse*vColor + vSpecular;//给此片元颜色值
}