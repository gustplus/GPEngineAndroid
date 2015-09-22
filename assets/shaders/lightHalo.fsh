precision mediump float;
uniform vec4 rootColor;
uniform vec4 endColor;

uniform float jitter;
uniform float near;
uniform float far;

varying  vec3 vPosition;

void main()                         
{  
   float range = (vPosition.y+near)/(near-far)*1.57;
   //vec4 finalColor = ((1.0-range)*rootColor)+(endColor*range);
   vec4 finalColor = vec4(rootColor.rgb,(rootColor.a*(1.0-sin(range))));
   gl_FragColor = finalColor;
}