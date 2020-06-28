#version 120

uniform sampler2D u_texture;
uniform vec4 color;
varying vec4 vColor;
varying vec2 vTexCoord;

void main() {

    vec4 texColor = texture2D(u_texture, vTexCoord);

    gl_FragColor = vec4(texColor.r*color.r , texColor.g*color.g, texColor.b*color.b, texColor.a*color.a);
}