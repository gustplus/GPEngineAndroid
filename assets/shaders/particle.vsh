uniform mat4 uMVPMatrix; //�ܱ任����
attribute vec3 aPosition;  //����λ��
attribute vec3 velocity;
attribute float time;//ÿ�����㷢��ʱ��
uniform vec4 beginColor;
uniform vec4 endColor;
uniform vec3 effectForce;
uniform float pastTime;
uniform float liveTime;
uniform float pointSize;

varying vec4 vColor;
varying float pTime;

void main()     
{  
	float trueTime = pastTime + time;
	if(trueTime < 0.0){
	    trueTime = 0.0;
	    return;
	}
	while(trueTime > liveTime){
	    trueTime -= liveTime;
	}
	pTime = trueTime;
	vec3 finalPosition = aPosition + velocity * trueTime + 0.5 * effectForce * trueTime * trueTime;
	gl_Position = uMVPMatrix * vec4(finalPosition,1);
	vColor = beginColor + (endColor - beginColor) *  (trueTime / liveTime);
	gl_PointSize = pointSize * (1.0 - (trueTime / liveTime));
}