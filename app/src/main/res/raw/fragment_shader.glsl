precision mediump float;

uniform vec4 u_Color;

varying vec3 v_Color;

void main()
{

    vec3 scaledNormal = normalize( vec3(0, 0, 1) );
    vec3 u_VectorToLight = normalize( vec3(2, 3, 5) );

    float ambientStrength = 0.1;
    vec3 amb = ambientStrength * vec3(0, 1, 0);

    gl_FragColor = vec4(amb, 1.0);
}