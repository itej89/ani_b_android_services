package fm.ani.anib.customcontrols;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import fm.ani.anib.R;

/**
 * Created by tej on 23/02/18.
 */


public class AspectFitRelativeLayout extends RelativeLayout
{
    private float mAspectRatioWidth;
    private float mAspectRatioHeight;

    private float MeasuredWidth;
    private float MEasuredHeight;

    public AspectFitRelativeLayout(Context context)
    {
        super(context);
    }

    public AspectFitRelativeLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AspectFitRelativeLayout, 0, 0);
        try {
            mAspectRatioWidth = ta.getDimension(R.styleable.AspectFitRelativeLayout_AspectRation_Width, 100);
            mAspectRatioHeight = ta.getDimension(R.styleable.AspectFitRelativeLayout_AspectRation_Height, 100);
        } finally {
            ta.recycle();
        }
        init(context, attrs);
    }

    public AspectFitRelativeLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {

    }



    // **overrides**

    @Override protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        float originalWidth = MeasureSpec.getSize(widthMeasureSpec);

        float originalHeight = MeasureSpec.getSize(heightMeasureSpec);

        float calculatedHeight = originalWidth * mAspectRatioHeight / mAspectRatioWidth;

        float finalWidth, finalHeight;

        if (calculatedHeight > originalHeight)
        {
            finalWidth = originalHeight * mAspectRatioWidth / mAspectRatioHeight;
            finalHeight = originalHeight;
        }
        else
        {
            finalWidth = originalWidth;
            finalHeight = calculatedHeight;
        }

        MeasuredWidth = finalWidth;
        MEasuredHeight = finalHeight;
        super.onMeasure(
                MeasureSpec.makeMeasureSpec((int)finalWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int)finalHeight, MeasureSpec.EXACTLY));

        ScaletheChildLayout();
    }


    public void ScaletheChildLayout()
    {
       float ScaleFactor =  MeasuredWidth/mAspectRatioWidth;
       View view =  this.getChildAt(0);

        view.setScaleX(ScaleFactor);
        view.setScaleY(ScaleFactor);

    }

}