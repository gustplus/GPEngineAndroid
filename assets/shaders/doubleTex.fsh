precision mediump float;
varying vec2 vTextureCoord; //接收从顶点着色器过来的参数
varying float CurrY;
uniform float height;
uniform float across;
uniform sampler2D Texture0;
uniform sampler2D Texture1;

void main()                         
{
   vec4 fColor = texture2D(Texture0, vTextureCoord);
   vec4 sColor = texture2D(Texture1, vTextureCoord);
   vec4 finalColor;
   if(CurrY<(height)){
	  finalColor = fColor;
   }else if(CurrY>(height+across)){
	finalColor = sColor;
   }else{
	float range = (CurrY-height)/across;
	finalColor =  sColor*range+fColor*(1.0-range) ; 
   }
   gl_FragColor = finalColor;
}              