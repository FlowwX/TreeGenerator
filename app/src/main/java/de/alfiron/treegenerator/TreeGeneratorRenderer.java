package de.alfiron.treegenerator;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
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

import de.alfiron.treegenerator.objects.Circle;
import de.alfiron.treegenerator.objects.ConeFrustum;
import de.alfiron.treegenerator.objects.ObjectBuilder;
import de.alfiron.treegenerator.util.LoggerConfig;
import de.alfiron.treegenerator.util.Point;
import de.alfiron.treegenerator.util.ShaderHelper;
import de.alfiron.treegenerator.util.TextRessourceReader;

public class TreeGeneratorRenderer implements GLSurfaceView.Renderer {

    public static final int POSITION_COMPONENT_COUNT = 3;
    public static final int BYTES_PER_FLOAT = 4;
    private FloatBuffer vertexData;

    private final Context context;
    private int program;

    private static final String U_COLOR = "u_Color";



    private static int uColorLocation;

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    private static final String U_MATRIX = "u_Matrix";
    private int uMatrixLocation;
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];





    //Objects
    private Circle circleTop;
    private Circle circleBottom;
    private ConeFrustum coneFrustum;

    TreeGeneratorRenderer( Context context ) {
        this.context = context;


        //circleTop = new Circle(new Point(0,0.4f,0),0.3f);
        //circleBottom = new Circle(new Point(0,0,0),1);

        coneFrustum = new ConeFrustum(new Point(0,0,0f),0.3f,1f, 0.4f );
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






    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);

        final float aspectRatio = (float)width / (float)height;
        perspectiveM(projectionMatrix, 0, 45, aspectRatio, 1f, 10f);

        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, 0f, 0f, -5f);
        rotateM(modelMatrix, 0,  40f, 1f, 0f,0f);

        final float[] temp = new float[16];
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT);


        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
        glUniform4f(uColorLocation, 0f, 1f, 0f, 1.0f);

        coneFrustum.draw();
        //circleTop.draw();
        //circleBottom.draw();

    }




    public static int getuColorLocation() {
        return uColorLocation;
    }

}
