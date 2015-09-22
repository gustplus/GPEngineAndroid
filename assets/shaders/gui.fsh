precision mediump float;
varying vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
uniform sampler2D sTexture;//������������
uniform float alpha;
uniform int functionID;
uniform vec4 destinationColor;
uniform float oneOfchangeTime;

void main()                         
{  
   vec4 tempColor = texture2D(sTexture, vTextureCoord);
   if(functionID == 0){
	gl_FragColor =  tempColor;
   }
   if(functionID == 1){
	gl_FragColor =  vec4(tempColor.xyz,tempColor.w*alpha);
   }
   if(functionID == 2){
	if(tempColor.a == 0.0){
	     gl_FragColor =  vec4(tempColor.xyz, 0.0);
	}else{
	     vec4 changeColor = destinationColor - tempColor;
	     changeColor = changeColor * oneOfchangeTime;
	     gl_FragColor =  tempColor + changeColor;
	}
   }
}              