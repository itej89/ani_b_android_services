package fm.ani.anib.datatypes;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by tej on 24/02/18.
 */

public class PxDpConvertors {

    Context context;

    public PxDpConvertors(Context cntxt)
    {
        context = cntxt;
    }

    public static float convertPixelsToDp(float px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }


    private int convertDpToPx(int dp){
        return Math.round(dp*(context.getResources().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));

    }

    private int convertPxToDp(int px){
        return Math.round(px/(Resources.getSystem().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));
    }
}
