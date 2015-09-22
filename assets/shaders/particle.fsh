precision mediump float;
varying  vec4 vColor;
varying float pTime;
uniform sampler2D Texture0;


void main()                         
{  
   if(pTime < 0.0){
	discard;
   }else{
	vec4 tColor = texture2D(Texture0, gl_PointCoord);
	gl_FragColor = tColor * vColor;
   }
}