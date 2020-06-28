#version 120

attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec4 a_colour;
uniform mat4 u_projTrans;

varying vec2 v_texCoords;
varying vec4 v_colour;
void main()
{
    v_colour = a_colour;
    v_texCoords = a_texCoord0;
    gl_Position =  u_projTrans * a_position;

}