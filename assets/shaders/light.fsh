precision mediump float;
varying  vec4 vColor; //���մӶ�����ɫ�������Ĳ���
varying vec4 vDiffuse;
varying vec4 vSpecular;
varying vec4 vAmbient;

void main()                         
{
   
   gl_FragColor = vAmbient*vColor + vDiffuse*vColor + vSpecular;//����ƬԪ��ɫֵ
}