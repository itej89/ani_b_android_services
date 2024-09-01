package fm.ani.anib.customcontrols.modeanimations;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import fm.ani.anib.R;

/**
 * TODO: document your custom view class.
 */
public class AnimatedMount extends FrameLayout {


    ImageView ImgYellowAnchor;

    public AnimatedMount(Context context) {
        super(context);
        init(null, 0);
    }

    public AnimatedMount(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AnimatedMount(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.sample_animated_mount, this);

        ImgYellowAnchor = findViewById(R.id.ImgYellowAnchor);

        RotateAnimation rotate_purplegear = new RotateAnimation(-13f, 13.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate_purplegear.setRepeatCount(Animation.INFINITE);
        rotate_purplegear.setRepeatMode(Animation.REVERSE);
        rotate_purplegear.setInterpolator(new AccelerateDecelerateInterpolator());
        rotate_purplegear.setDuration(700);
        ImgYellowAnchor.setDrawingCacheEnabled(true);
        ImgYellowAnchor.startAnimation(rotate_purplegear);
    }

}
