package de.alfiron.treegenerator.objects;

import java.util.Arrays;
import java.util.Vector;

import de.alfiron.treegenerator.TreeGeneratorRenderer;
import de.alfiron.treegenerator.util.Point;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glUniform4f;



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

    @Override
    public float[][] calcNormals() {

        float normals[][] = new float[3][3];

        int index = 3;
        while( hasNextReferencePoint( index ) ){
            float[] a = getVector( new Point(0f,0f,0f), new Point(0,0,0) );
            float[] b = getVector( new Point(0f,0f,0f), new Point(0,0,0) );

            dotProduct( a, b );

        }

        return normals;
    }

    public boolean hasNextReferencePoint( int index ){
        return index < this.vertices.length;
    }

    public float[] getVector(Point pPoi, Point pRef){
        float[] v = new float[3];
            v[0] = pRef.getX() - pPoi.getX();
            v[1] = pRef.getY() - pPoi.getY();
            v[2] = pRef.getZ() - pPoi.getZ();
        return v;
    }

    public float[] dotProduct( float[] a, float[] b ){
        float[] normal = new float[3];

        normal[0] = a[1]*b[2] - b[1]*a[2];
        normal[1] = a[2]*b[0] - b[2]*a[0];
        normal[2] = a[0]*b[1] - b[0]*a[1];

        return normal;
    }


    @Override
    public void draw() {
        super.draw();

        glUniform4f(TreeGeneratorRenderer.getuColorLocation(), 1f, 0f, 0f, 0);
        topCircle.draw();
    }
}
