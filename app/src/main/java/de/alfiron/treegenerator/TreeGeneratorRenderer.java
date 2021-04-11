package de.alfiron.treegenerator;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.perspectiveM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import de.alfiron.treegenerator.util.LoggerConfig;
import de.alfiron.treegenerator.util.ShaderHelper;
import de.alfiron.treegenerator.util.TextRessourceReader;

class TreeGeneratorRenderer implements GLSurfaceView.Renderer {

    private static final int POSITION_COMPONENT_COUNT = 4;
    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;

    private final Context context;
    private int program;

    private static final String U_COLOR = "u_Color";
    private int uColorLocation;

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    private static final String U_MATRIX = "u_Matrix";
    private int uMatrixLocation;
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];




    TreeGeneratorRenderer( Context context ) {
        this.context = context;

        float[] tableVerticesWithTriangles = {

                // Triangle 1
                -0.5f, -0.5f, 0f, 1f,
                0.5f, 0.5f, 0f, 1f,
                -0.5f, 0.5f, 0f, 1f,

                // Triangle 2
                -0.5f, -0.5f, 0f, 1f,
                0.5f, -0.5f, 0f, 1f,
                0.5f, 0.5f, 0f, 1f
        };

        vertexData = ByteBuffer
                .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(tableVerticesWithTriangles);

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        String vertexShaderSource = TextRessourceReader.readTextFileFromResource( context, R.raw.vertex_shader );
        String fragmentShaderSource = TextRessourceReader.readTextFileFromResource( context, R.raw.fragment_shader );

        int vertexShader = ShaderHelper.compileVertexShader( vertexShaderSource );
        int fragmentShader = ShaderHelper.compileFragmentShader( fragmentShaderSource );

        program = ShaderHelper.linkProgram(vertexShader,fragmentShader);

        if(LoggerConfig.ON){
            ShaderHelper.validateProgram(program);
        }

        glUseProgram(program);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uColorLocation = glGetUniformLocation(program, U_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);

        //connect data with openGL
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);


    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);

        final float aspectRatio = (float)width / (float)height;
        perspectiveM(projectionMatrix, 0, 80, aspectRatio, 1f, 10f);

        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, 0f, 0f, -2f);
       rotateM(modelMatrix, 0, -45f, 1f, 0f,0f);

        final float[] temp = new float[16];
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT);

        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }
}
