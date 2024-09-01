package Framework.DataTypes;

import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by tej on 03/03/18.
 */

public class TransformMatrixToValueConvertor {

    //Transformation Matrix
    //  -      -
    // | a  b  0 |
    // | c  d  0 |
    // | tx ty 1 |
    //  -      -
    public  static TransformValues GetValuesFromMatrix(CGAffineTransform Transform)
    {
        TransformValues tValues = new TransformValues();
        tValues.Tx = Transform.tx;
        tValues.Ty = Transform.ty;
        tValues.ScaleX = (float) (Math.signum(Transform.a)*(Math.sqrt(Math.pow(Transform.a,2)+Math.pow(Transform.b,2))));
        tValues.ScaleY = (float) (Math.signum(Transform.d)*(Math.sqrt(Math.pow(Transform.c,2)+Math.pow(Transform.d,2))));
        tValues.RotationInRadians = (float) Math.atan2(Transform.c, Transform.d);

        return  tValues;
    }

}
