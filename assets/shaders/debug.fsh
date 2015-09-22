precision mediump float;
varying  vec3 vColor; //接收从顶点着色器过来的参数

void main()                         
{                       
   gl_FragColor = vec4(vColor, 1);//给此片元颜色值
}