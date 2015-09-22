precision mediump float;

uniform samplerCube cube;

varying vec4 vDiffuse;
varying vec4 vSpecular;
varying vec4 vAmbient;
varying vec3 vTexCoords;

void main()                         
{
   vec4 finalColor = textureCube(cube, vTexCoords);
   gl_FragColor = vAmbient*finalColor + vDiffuse*finalColor + vSpecular;//给此片元颜色值
}