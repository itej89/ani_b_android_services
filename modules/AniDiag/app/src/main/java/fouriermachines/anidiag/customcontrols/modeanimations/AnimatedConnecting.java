package fouriermachines.ani.client.main.customcontrols.modeanimations;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import fouriermachines.anidiag.R;


/**
 * TODO: document your custom view class.
 */
public class AnimatedConnecting extends FrameLayout {


    ImageView ImgPurpleGear;


    public AnimatedConnecting(Context context) {
        super(context);
        init(null, 0);
    }

    public AnimatedConnecting(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AnimatedConnecting(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle)
    {
        inflate(getContext(), R.layout.sample_animated_connecting, this);
        ImgPurpleGear = (ImageView)findViewById(R.id.ImgPurpleGear);

        RotateAnimation rotate_purplegear = new RotateAnimation(0.0f, 360.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate_purplegear.setRepeatCount(Animation.INFINITE);
        rotate_purplegear.setRepeatMode(Animation.RESTART);
        rotate_purplegear.setInterpolator(new LinearInterpolator());
        rotate_purplegear.setDuration(3000);
        ImgPurpleGear.setDrawingCacheEnabled(true);
        ImgPurpleGear.startAnimation(rotate_purplegear);
    }


}
