package fm.ani.fmlauncher.DataTypes;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.abpconsult.inputinjector.InjectionManager;
import com.abpconsult.inputinjector.Input;

import java.util.Calendar;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import Framework.DataTypes.GlobalContext;
import fm.ani.fmlauncher.MainActivity;
import fm.ani.fmlauncher.R;

/**
 * TODO: document your custom view class.
 */
public class fmOverlay extends View {

    public  String AppName = "";

    public fmOverlay(Context context, String appName) {
        super(context);
        AppName = appName;
        init(null, 0);
    }

    public fmOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public fmOverlay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
    }

    private void invalidateTextPaintAndMeasurements() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }



    private String GetCurrentAppName(Context context) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        long start = calendar.getTimeInMillis();
        long end = System.currentTimeMillis();
        Map<String, UsageStats> stats = usageStatsManager.queryAndAggregateUsageStats(start, end);

        if (stats != null && stats.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : stats.values()) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }

            if (mySortedMap != null && !mySortedMap.isEmpty()) {

                String appName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                return appName;
            }
        }
        return "";
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {

            switch (AppName)
            {
                case "com.kiloo.subwaysurf":
                    if(event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_1) {
                        InjectionManager im = new InjectionManager(getContext());
                        im.injectTouchEventDown(400, 600);
                        im.injectTouchEventRelease(440, 660);
                        Log.e("Executed app", "Tapped: screen");
                    }
                break;

                case "net.osaris.turboflydemo":
                    if(event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_1) {
                        Input im = new Input(getContext());
                        im.keyEvent(KeyEvent.KEYCODE_ENTER);
                        Log.e("Executed app", "Tapped: screen");
                    }
                    break;
            }

        return super.onKeyDown(keycode, event);
    }

    public enum JOY_GESTURES {LEFT_START, LEFT, RIGHT_START, RIGHT, UP_START, UP, DOWN_START ,DOWN, CENTRE};

    JOY_GESTURES JOY_GESTURE = JOY_GESTURES.CENTRE;

    Pair<Integer, Integer> Coordinates = new Pair<>(0,0);
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        switch (AppName)
        {
            case "com.kiloo.subwaysurf":
                break;

            case "net.osaris.turboflydemo":   InjectionManager im = new InjectionManager(getContext());
                if(event.getAxisValue(MotionEvent.AXIS_Y) < -0.4 && JOY_GESTURE == JOY_GESTURES.CENTRE )
                {
                    im.injectTouchEventDown(350, 1000);
                    JOY_GESTURE = JOY_GESTURES.UP_START;
                    Log.e("FM_ACTION", JOY_GESTURE.toString());
                }

                if(event.getAxisValue(MotionEvent.AXIS_Y) < -0.4 && JOY_GESTURE == JOY_GESTURES.UP_START) {

                    double slide = (Math.abs(event.getAxisValue(MotionEvent.AXIS_Y)) - 0.4)/0.4;
                    if(slide > 1) slide =1;

                    if(slide >= 1) {
                        im.injectTouchMoveEvent(350, 800,100);
                        im.injectTouchEventRelease(350, 800);
                        JOY_GESTURE = JOY_GESTURES.UP;
                        Log.e("FM_ACTION", JOY_GESTURE.toString());
                    }
                    else
                    {
                        Coordinates = new Pair<>(800, 800-(int)(300*slide));
                        im.injectTouchMoveEvent(800, 800-(int)(300*slide), 100);
                    }
                }



                if(event.getAxisValue(MotionEvent.AXIS_X) < -0.4  && JOY_GESTURE == JOY_GESTURES.CENTRE )
                {
                    JOY_GESTURE = JOY_GESTURES.LEFT_START;
                    im.injectTouchEventDown(400, 800);
                    Log.e("FM_ACTION", JOY_GESTURE.toString());
                }

                if(event.getAxisValue(MotionEvent.AXIS_X) < -0.4 && JOY_GESTURE == JOY_GESTURES.LEFT_START) {

                    double slide = (Math.abs(event.getAxisValue(MotionEvent.AXIS_X)) - 0.4)/0.4;
                    if(slide > 1) slide =1;

                    if(slide >= 1) {
                        im.injectTouchMoveEvent(200, 800,100);
                        im.injectTouchEventRelease(200, 800);
                        JOY_GESTURE = JOY_GESTURES.LEFT;
                        Log.e("FM_ACTION", JOY_GESTURE.toString());
                    }
                    else
                    {
                        Coordinates = new Pair<>(400-(int)(200*slide), 800);
                        im.injectTouchMoveEvent(400-(int)(200*slide), 800, 100);
                    }
                }



                if(event.getAxisValue(MotionEvent.AXIS_Y) > 0.4 && JOY_GESTURE == JOY_GESTURES.CENTRE  )
                {
                    JOY_GESTURE = JOY_GESTURES.DOWN_START;
                    im.injectTouchEventDown(800, 800);
                    Log.e("FM_ACTION", JOY_GESTURE.toString());
                }

                if(event.getAxisValue(MotionEvent.AXIS_Y) > 0.4  && JOY_GESTURE == JOY_GESTURES.DOWN_START) {

                    double slide = (Math.abs(event.getAxisValue(MotionEvent.AXIS_Y)) - 0.4)/0.4;
                    if(slide > 1) slide =1;

                    if(slide >= 1) {
                        im.injectTouchMoveEvent(800, 1000,100);
                        im.injectTouchEventRelease(800, 1000);
                        JOY_GESTURE = JOY_GESTURES.DOWN;
                        Log.e("FM_ACTION", JOY_GESTURE.toString());
                    }
                    else
                    {
                        Coordinates = new Pair<>(800, 800+(int)(300*slide));
                        im.injectTouchMoveEvent(800, 800+(int)(300*slide), 100);
                    }
                }



                if(event.getAxisValue(MotionEvent.AXIS_X) > 0.4 && JOY_GESTURE == JOY_GESTURES.CENTRE )
                {
                    JOY_GESTURE = JOY_GESTURES.RIGHT_START;
                    im.injectTouchEventDown(400, 800);
                    Log.e("FM_ACTION", JOY_GESTURE.toString());
                }

                if(event.getAxisValue(MotionEvent.AXIS_X) > 0.4 && JOY_GESTURE == JOY_GESTURES.RIGHT_START) {

                    double slide = (Math.abs(event.getAxisValue(MotionEvent.AXIS_X)) - 0.4)/0.4;
                    if(slide > 1) slide =1;

                    if(slide >= 1) {
                        im.injectTouchMoveEvent(600, 800,100);
                        im.injectTouchEventRelease(600, 800);
                        JOY_GESTURE = JOY_GESTURES.RIGHT;
                        Log.e("FM_ACTION", JOY_GESTURE.toString());
                    }
                    else
                    {
                        Coordinates = new Pair<>(400+(int)(200*slide), 800);
                        im.injectTouchMoveEvent(400+(int)(200*slide), 800, 100);
                    }
                }


                if(event.getAxisValue(MotionEvent.AXIS_X) < 0.2 && event.getAxisValue(MotionEvent.AXIS_X) > -0.2
                        && event.getAxisValue(MotionEvent.AXIS_Y) < 0.2 && event.getAxisValue(MotionEvent.AXIS_Y) > -0.2
                        &&JOY_GESTURE != JOY_GESTURES.CENTRE) {
                    JOY_GESTURE = JOY_GESTURES.CENTRE;
                    im.injectTouchEventRelease(Coordinates.first, Coordinates.second);
                    Log.e("FM_ACTION", JOY_GESTURE.toString());
                }
                break;
        }

        return super.onGenericMotionEvent(event);
    }

}
