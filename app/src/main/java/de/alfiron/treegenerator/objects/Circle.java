package de.alfiron.treegenerator.objects;

import de.alfiron.treegenerator.util.Point;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static de.alfiron.treegenerator.TreeGeneratorRenderer.POSITION_COMPONENT_COUNT;

public class Circle extends Object3D{

    public static final int AMOUNT_POINTS = 16;

    private Point center;
    private float radius;

    public Circle( Point center, float radius ) {
        this.center = center;
        this.radius  = radius;
        this.drawMode = GL_TRIANGLE_FAN;

        init();
    }

    @Override
    public float[] buildVertices() {

        float[] vertices = new float[AMOUNT_POINTS * POSITION_COMPONENT_COUNT];

        //add center point
        vertices[0] = center.getX();
        vertices[1] = center.getY();
        vertices[2] = center.getZ();

        int verticesOnCircumference = AMOUNT_POINTS - 2;
        int index = 3;
        for(int i=0; i <= verticesOnCircumference; i++) {
            double angleInRadians = ((float) i / (float) verticesOnCircumference) * Math.PI * 2f;
            vertices[index++] = center.getX() + (float) (radius * Math.cos(angleInRadians));
            vertices[index++] = center.getY();
            vertices[index++] = center.getZ() + (float) (radius * Math.sin(angleInRadians));
        }
        return vertices;
    }
}
