precision mediump float;
varying vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
uniform sampler2D Texture0;//������������
void main()                         
{                      
   gl_FragColor =  texture2D(Texture0, vTextureCoord);
}              