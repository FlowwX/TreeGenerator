package de.alfiron.treegenerator.objects;

import java.util.Arrays;

import de.alfiron.treegenerator.util.Point;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static de.alfiron.treegenerator.TreeGeneratorRenderer.POSITION_COMPONENT_COUNT;

public class ConeFrustum extends Object3D{

    private Point centerBottom;

    private Circle topCircle;
    private Circle bottomCircle;

    public ConeFrustum( Point centerBottom, float rTop, float rBottom, float height ) {
        this.centerBottom = centerBottom;
        Point topCenter = new Point( centerBottom.getX(), centerBottom.getY() + height, centerBottom.getY() );
        topCircle = new Circle( topCenter, rTop );
        bottomCircle = new Circle( centerBottom, rBottom );
        drawMode = GL_TRIANGLE_STRIP;
        init();
    }

    @Override
    public float[] buildVertices() {

        float[] topVertices     = topCircle.getVertices();
        float[] bottomVertices  = bottomCircle.getVertices();

        //remove first vertice because its the center
        topVertices = Arrays.copyOfRange(topVertices,3,topVertices.length);
        bottomVertices = Arrays.copyOfRange(bottomVertices,3,bottomVertices.length);

        float[] vertices = new float[topVertices.length+bottomVertices.length];

        int index=0;
        for( int i=0; i < topVertices.length; i+=3 ){

            vertices[index]     = topVertices[i];           //x
            vertices[index+1]   = topVertices[i+1];         //y
            vertices[index+2]   = topVertices[i+2];         //z

            index+=3;

            vertices[index]     = bottomVertices[i];           //x
            vertices[index+1]   = bottomVertices[i+1];         //y
            vertices[index+2]   = bottomVertices[i+2];         //z

            index+=3;

        }

        return vertices;
    }
}
