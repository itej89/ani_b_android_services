package fm.ani.anib.customcontrols.shapes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by tej on 28/02/18.
 */

public class Eyelid extends View {

    private  int MeasuredHeight;
    private  int MeasuredWidth;

    public Eyelid(Context context)
    {
        super(context);
        init();
    }

    public Eyelid(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public Eyelid(Context context, AttributeSet attrs, int defStyle)
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
//        paint.setShadowLayer(5, 3, 3, Color.BLACK);

        // Important for certain APIs
//        setLayerType(LAYER_TYPE_SOFTWARE, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Paint paint = new Paint();

        paint.setColor(Color.TRANSPARENT);
        canvas.drawPaint(paint);

        paint.setStrokeWidth(1);
        paint.setColor(Color.parseColor("#808080"));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);




        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.addArc(new RectF(0.0f,0.0f,275f,275f), 168, 204);

        path.close();

        canvas.drawPath(path, paint);
    }
}
