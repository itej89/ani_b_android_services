package Framework.DataTypes;

import android.graphics.Matrix;

public class MatricesMathHelper {


    public CATransform3D Make3DAffineTransformFrom2DMatrix(CGAffineTransform _2DAffine)
    {
        CATransform3D _3DAffine = new CATransform3D();

        _3DAffine.m11 = _2DAffine.a;
        _3DAffine.m12 = _2DAffine.b;
        _3DAffine.m13 = 0.0f;
        _3DAffine.m14 = 0.0f;


        _3DAffine.m21 = _2DAffine.c;
        _3DAffine.m22 = _2DAffine.d;
        _3DAffine.m23 = 0.0f;
        _3DAffine.m24 = 0.0f;


        _3DAffine.m31 = 0.0f;
        _3DAffine.m32 = 0.0f;
        _3DAffine.m33 = 1.0f;
        _3DAffine.m34 = 0.0f;


        _3DAffine.m41 = _2DAffine.tx;
        _3DAffine.m42 = _2DAffine.ty;
        _3DAffine.m43 = 0.0f;
        _3DAffine.m44 = 1.0f;

        return _3DAffine;
    }


    public CATransform3D Make3DAffineTransformFrom2DMatrix(Float[][] _2DMatrix)
    {
        CATransform3D _3DAffine = new CATransform3D();

        _3DAffine.m11 = _2DMatrix[0][0];
        _3DAffine.m12 = _2DMatrix[0][1];
        _3DAffine.m13 = 0.0f;
        _3DAffine.m14 = _2DMatrix[0][2];


        _3DAffine.m21 = _2DMatrix[1][0];
        _3DAffine.m22 = _2DMatrix[1][1];
        _3DAffine.m23 = 0.0f;
        _3DAffine.m24 = _2DMatrix[1][2];


        _3DAffine.m31 = 0.0f;
        _3DAffine.m32 = 0.0f;
        _3DAffine.m33 = 1.0f;
        _3DAffine.m34 = 0.0f;


        _3DAffine.m41 = _2DMatrix[2][0];
        _3DAffine.m42 = _2DMatrix[2][1];
        _3DAffine.m43 = 0.0f;
        _3DAffine.m44 = _2DMatrix[2][2];

        return _3DAffine;
    }


    public Float[][] Make3DMatrixFrom2DMatrix(Float[][] _2DMatrix)
    {
        Float[][] _3DMatrix = {
                {_2DMatrix[0][0],_2DMatrix[0][1],0.0f,_2DMatrix[0][2]},
                {_2DMatrix[1][0],_2DMatrix[1][1],0.0f,_2DMatrix[1][2]},
                {      0.0f,             0.0f,         1.0f,     0.0f},
                {_2DMatrix[2][0],_2DMatrix[2][1],0.0f,_2DMatrix[2][2]}
                };

        return _3DMatrix;
    }



    // a  b  c
// d  e  f
// g  h  i
    public Float[][] Multiple3X3Matrix(Float[][]leftMatrix, Float[][] RightMatrix)
    {
        Float a = (leftMatrix[0][0] * RightMatrix[0][0])+(leftMatrix[0][1] * RightMatrix[1][0])+(leftMatrix[0][2] * RightMatrix[2][0]);
        Float b = (leftMatrix[0][0] * RightMatrix[0][1])+(leftMatrix[0][1] * RightMatrix[1][1])+(leftMatrix[0][2] * RightMatrix[2][1]);
        Float c = (leftMatrix[0][0] * RightMatrix[0][2])+(leftMatrix[0][1] * RightMatrix[1][2])+(leftMatrix[0][2] * RightMatrix[2][2]);


        Float d = (leftMatrix[1][0] * RightMatrix[0][0])+(leftMatrix[1][1] * RightMatrix[1][0])+(leftMatrix[1][2] * RightMatrix[2][0]);
        Float e = (leftMatrix[1][0] * RightMatrix[0][1])+(leftMatrix[1][1] * RightMatrix[1][1])+(leftMatrix[1][2] * RightMatrix[2][1]);
        Float f = (leftMatrix[1][0] * RightMatrix[0][2])+(leftMatrix[1][1] * RightMatrix[1][2])+(leftMatrix[1][2] * RightMatrix[2][2]);


        Float g = (leftMatrix[2][0] * RightMatrix[0][0])+(leftMatrix[2][1] * RightMatrix[1][0])+(leftMatrix[2][2] * RightMatrix[2][0]);
        Float h = (leftMatrix[2][0] * RightMatrix[0][1])+(leftMatrix[2][1] * RightMatrix[1][1])+(leftMatrix[2][2] * RightMatrix[2][1]);
        Float i = (leftMatrix[2][0] * RightMatrix[0][2])+(leftMatrix[2][1] * RightMatrix[1][2])+(leftMatrix[2][2] * RightMatrix[2][2]);

        return new Float[][]{ {a,b,c},{d,e,f},{g,h,i}};
    }

    public  Matrix GetIdentityMatrix()
    {
        Matrix m = new Matrix();
        m.setValues(new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1});
        return  m;
    }

    public  Matrix GetAndroidMatrixFromAffine(CGAffineTransform _2DTransform)
    {
        Matrix m = new Matrix();

        // SCALE_X SHEAR_X TRANS_X      //a  c  tx
        // SHEAR_Y SCALE_Y TRANS_Y      //b  d  ty
        // PERSP_0 PERSP_1 PERSP_2      //0  0  1
        //https://developer.android.com/reference/kotlin/android/graphics/Matrix
        //Float values: SCALE_X, SHEAR_X, TRANS_X, SHEAR_Y, SCALE_Y, TRANS_Y, PERSP_0, PERSP_1, PERSP_2
        float[] elements = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        m.getValues(elements);
        elements[0] = _2DTransform.a;
        elements[3] = _2DTransform.b;
        elements[1] = _2DTransform.c;
        elements[4] = _2DTransform.d;
        elements[2] = _2DTransform.tx;
        elements[5] = _2DTransform.ty;
        elements[8] = 1;

        m.setValues(elements);

        return  m;
    }

    public  CGAffineTransform GetAffineFromAndroidMatrix(Matrix m)
    {
        CGAffineTransform _2DTransform = new CGAffineTransform();

        // SCALE_X SHEAR_X TRANS_X      //a  c  tx
        // SHEAR_Y SCALE_Y TRANS_Y      //b  d  ty
        // PERSP_0 PERSP_1 PERSP_2      //0  0  1
        //https://developer.android.com/reference/kotlin/android/graphics/Matrix
        //Float values: SCALE_X, SHEAR_X, TRANS_X, SHEAR_Y, SCALE_Y, TRANS_Y, PERSP_0, PERSP_1, PERSP_2
        float[] elements = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        m.getValues(elements);
        _2DTransform.a = elements[0];
        _2DTransform.b = elements[3];
        _2DTransform.c = elements[1];
        _2DTransform.d = elements[4];
        _2DTransform.tx = elements[2];
        _2DTransform.ty = elements[5];

        return  _2DTransform;
    }

}
