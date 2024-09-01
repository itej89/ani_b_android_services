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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import fouriermachines.anidiag.R;


/**
 * TODO: document your custom view class.
 */
public class AnimatedScanning extends FrameLayout {


    ImageView ImgBlueCircleWBorder;
    ImageView ImgBlueCircleSmall;

    public AnimatedScanning(Context context) {
        super(context);
        init(null, 0);
    }

    public AnimatedScanning(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AnimatedScanning(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        inflate(getContext(), R.layout.sample_animated_scanning, this);
        ImgBlueCircleWBorder = (ImageView)findViewById(R.id.imgBlueCircleWithBorder);
        ImgBlueCircleSmall = (ImageView)findViewById(R.id.imgBlueCicleSmall);

        AnimationSet set_BlueCircleBorder = new AnimationSet(false);

        ScaleAnimation scale_BlueCirclewBorder =  new ScaleAnimation(1f, 3f, 1f, 3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale_BlueCirclewBorder.setRepeatCount(Animation.INFINITE);
        scale_BlueCirclewBorder.setRepeatMode(Animation.RESTART);
        scale_BlueCirclewBorder.setDuration(1500);
        scale_BlueCirclewBorder.setInterpolator(new DecelerateInterpolator());

        AlphaAnimation alpha_BlueCirclewBorder = new AlphaAnimation(1f, 0.1f);
        alpha_BlueCirclewBorder.setRepeatCount(Animation.INFINITE);
        alpha_BlueCirclewBorder.setRepeatMode(Animation.RESTART);
        alpha_BlueCirclewBorder.setDuration(1500);
        alpha_BlueCirclewBorder.setInterpolator(new DecelerateInterpolator());

        set_BlueCircleBorder.addAnimation(scale_BlueCirclewBorder);
        set_BlueCircleBorder.addAnimation(alpha_BlueCirclewBorder);

        ImgBlueCircleWBorder.startAnimation(set_BlueCircleBorder);

        AlphaAnimation alpha_BlueCircleSmall = new AlphaAnimation(1f, 0.5f);
        alpha_BlueCircleSmall.setRepeatCount(Animation.INFINITE);
        alpha_BlueCircleSmall.setRepeatMode(Animation.RESTART);
        alpha_BlueCircleSmall.setDuration(1500);
        alpha_BlueCircleSmall.setInterpolator(new DecelerateInterpolator());
        ImgBlueCircleSmall.startAnimation(alpha_BlueCircleSmall);
    }



}
