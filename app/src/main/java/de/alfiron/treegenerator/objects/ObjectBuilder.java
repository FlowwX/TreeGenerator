package de.alfiron.treegenerator.objects;


import android.opengl.Matrix;

import static android.opengl.Matrix.scaleM;

public class ObjectBuilder {

    private static final int POSITION_COMPONENT_COUNT = 3;

    public static float[] generateCircleVertexData(int size){

        int amountOfPoints = size + 2;
        float[] vertexData = new float[amountOfPoints * POSITION_COMPONENT_COUNT];

        float radius  = 1f;
        float centerX = 0f;
        float centerY = 0f;
        float centerZ = 0f;

        //add center point
        vertexData[0] = centerX;
        vertexData[1] = centerY;
        vertexData[2] = centerZ;
        int index = 3;

        for(int i=0; i<=size; i++){
            double angleInRadians = ( (float) i / (float) size) * Math.PI * 2f;
            vertexData[index++] = centerX + (float) (radius * Math.cos(angleInRadians));
            vertexData[index++] = centerY;
            vertexData[index++] = centerZ + (float) (radius * Math.sin(angleInRadians));;
        }

        return vertexData;
    }


}
