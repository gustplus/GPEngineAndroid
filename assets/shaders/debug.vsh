uniform mat4 uMVPMatrix; //�ܱ任����
attribute vec3 aPosition;  //����λ��
attribute vec3 aColor;    //������ɫ
varying vec3 vColor;  //���ڴ��ݸ�ƬԪ��ɫ���ı���

void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //�����ܱ任�������˴λ��ƴ˶���λ��
   vColor = aColor;//�����յ���ɫ���ݸ�ƬԪ��ɫ�� 
}                      