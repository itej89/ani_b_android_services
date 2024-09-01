package fm.ani.anib.customcontrols.artanimations;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import fm.ani.anib.R;

/**
 * TODO: document your custom view class.
 */
public class AnimatedArtConnecting extends RelativeLayout {


    Context myContext;

    fm.ani.anib.customcontrols.AspectFitRelativeLayout AniArtViewParent;
    FrameLayout AniArtView;
    ImageView ImgAnimArtCurve;
    ImageView ImgAnimArtLean;
    ImageView ImgAnimArtTilt;
    ImageView ImgAnimArtArrow;
    ImageView ImgAnimArtMobile;

    public AnimatedArtConnecting(Context context) {
        super(context);
        init(context,null, 0);
    }

    public AnimatedArtConnecting(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs, 0);
    }

    public AnimatedArtConnecting(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        myContext = context;

        inflate(getContext(), R.layout.sample_animated_art_connecting, this);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public  void  ShowMotionAnimation()
    {


        AniArtViewParent = findViewById(R.id.AniArtViewParent);
        AniArtView = findViewById(R.id.AniArtView);
        ImgAnimArtLean = findViewById(R.id.imgAnimArtLean);
        ImgAnimArtTilt = findViewById(R.id.imgAnimArtTilt);
        ImgAnimArtCurve = findViewById(R.id.ImgAnimArtCurve);
        ImgAnimArtArrow = findViewById(R.id.ImgAnimArtArrow);
        ImgAnimArtMobile = findViewById(R.id.imgAnimArtMobile);




        ImgAnimArtArrow.setTranslationX(-1);
        ImgAnimArtArrow.setTranslationY(-11);
        ImgAnimArtMobile.setTranslationX(0);
        ImgAnimArtMobile.setTranslationY(-25);

        ImgAnimArtLean.animate().rotation(-37.f).x(10).y(-35).setInterpolator(new DecelerateInterpolator()).setDuration(2500).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        ImgAnimArtTilt.animate().rotation(-37.f).x(10).y(-35).setInterpolator(new DecelerateInterpolator()).setDuration(2500);
        ImgAnimArtCurve.animate().x(7).y(-15).setInterpolator(new DecelerateInterpolator()).setDuration(2500);

    }

    public void ShowMobileMountWait()
    {
        ImgAnimArtArrow.setAlpha(1.0f);
        ImgAnimArtMobile.setAlpha(1.0f);


        TranslateAnimation tranlate_AnimArtArrow =  new TranslateAnimation(0, -5, 0, 5);
        tranlate_AnimArtArrow.setRepeatCount(Animation.INFINITE);
        tranlate_AnimArtArrow.setRepeatMode(Animation.REVERSE);
        tranlate_AnimArtArrow.setDuration(400);
        tranlate_AnimArtArrow.setInterpolator(new AccelerateDecelerateInterpolator());
        ImgAnimArtArrow.startAnimation(tranlate_AnimArtArrow);

    }



}
