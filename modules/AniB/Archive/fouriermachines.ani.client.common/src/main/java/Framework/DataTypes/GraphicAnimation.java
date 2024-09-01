package Framework.DataTypes;

import android.animation.TypeEvaluator;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class GraphicAnimation extends Animation {

    private Matrix matrix;

    public GraphicAnimation(Matrix matrix) {
        this.matrix = matrix;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        t.getMatrix().set(matrix);
    }
}

//
//
//
//implements TypeEvaluator<Matrix> {
//public Matrix evaluate(float fraction,
//        Matrix startValue,
//        Matrix endValue) {
//        float[] startEntries = new float[9];
//        float[] endEntries = new float[9];
//        float[] currentEntries = new float[9];
//
//        startValue.getValues(startEntries);
//        endValue.getValues(endEntries);
//
//        for (int i = 0; i < 9; i++)
//        currentEntries[i] = (1 - fraction) * startEntries[i]
//        + fraction * endEntries[i];
//
//        Matrix matrix = new Matrix();
//        matrix.setValues(currentEntries);
//        return matrix;
//        }
//        }
