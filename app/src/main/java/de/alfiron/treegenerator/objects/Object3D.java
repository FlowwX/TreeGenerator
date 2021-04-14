package de.alfiron.treegenerator.objects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;
import static de.alfiron.treegenerator.TreeGeneratorRenderer.BYTES_PER_FLOAT;
import static de.alfiron.treegenerator.TreeGeneratorRenderer.POSITION_COMPONENT_COUNT;

public abstract class Object3D {

    private FloatBuffer vertexData;
    private float[] vertices;
    private int aPositionLocation;

    protected int drawMode = GL_TRIANGLES;

    protected void init(){
        vertices = buildVertices();
        vertexData = ByteBuffer
                .allocateDirect(this.vertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer().put(vertices);
    }

    public void draw(){
        bindData();
        glDrawArrays(drawMode, 0, vertices.length/POSITION_COMPONENT_COUNT);
    }

    private void bindData(){
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);
    }

    public float[] getVertices() {
        return vertices;
    }

    abstract public float[] buildVertices();
}
