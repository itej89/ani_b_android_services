package fm.ani.anib.customcontrols.shapes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by tej on 27/02/18.
 */

public class EyebrowLeft extends View {


    private  int MeasuredHeight;
    private  int MeasuredWidth;

    public EyebrowLeft(Context context)
    {
        super(context);
        init();
    }

    public EyebrowLeft(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public EyebrowLeft(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }
    @Override protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        MeasuredWidth = MeasureSpec.getSize(widthMeasureSpec);;
        MeasuredHeight =  MeasureSpec.getSize(heightMeasureSpec);;
    }

    Paint paint;
    public void init()
    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShadowLayer(5, 3, 3, Color.BLACK);

        // Important for certain APIs
        setLayerType(LAYER_TYPE_SOFTWARE, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       // Paint paint = new Paint();

        paint.setColor(Color.TRANSPARENT);
        canvas.drawPaint(paint);

        paint.setStrokeWidth(1);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);


        Point a = new Point(MeasuredWidth*2/3, MeasuredHeight/6);
        Point b = new Point(MeasuredWidth*3/4, MeasuredHeight/2);
        Point c = new Point(MeasuredWidth/8, MeasuredHeight*3/4);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();

        canvas.drawPath(path, paint);
    }


}
