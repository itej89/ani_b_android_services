package fm.ani.anib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.Delegates.UI.AniUIConvey;
import Framework.DataTypes.Delegates.UI.AniUIRead;
import Framework.DataTypes.GlobalContext;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import androidx.appcompat.app.AppCompatActivity;
import fm.ani.anib.customcontrols.shapes.EyebrowLeft;
import fm.ani.anib.customcontrols.shapes.EyebrowRight;
import fm.ani.anib.customcontrols.shapes.Eyelid;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Ani extends AppCompatActivity implements AniUIRead {

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent e)
    {
        if(e.getAxisValue(MotionEvent.AXIS_Y) == -1)
        {
            //aniUIConvey.JoyTriggerDetected();
            aniUIConvey.JoySnapDetected();
        }
        return  true;
    }

    @Override
    public  boolean dispatchKeyEvent(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.KEYCODE_BUTTON_1 && e.getAction() == KeyEvent.ACTION_UP)
        {
            aniUIConvey.JoySnapDetected();
        }
        return  true;
    }


    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        super.setRequestedOrientation(requestedOrientation);

        int rotationAnimation = WindowManager.LayoutParams.ROTATION_ANIMATION_CROSSFADE;
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        winParams.rotationAnimation = rotationAnimation;
        win.setAttributes(winParams);

    }

    //AniUIReadAndConvey
    public Map<Integer, View> GetAllUIViews(){
        return UIViews;
    }
   public ArrayList<View> GetAllUIElements()  {
       ArrayList<View> elements =  new ArrayList<View>(){{
           add(ImgLeftEyeBrow);
           add(ImgRightEyeBrow);
           add(ImgLeftEyeClose);
           add(ImgRightEyeClose);
           add(ImgLeftEye);
           add(ImgRightEye);
           add(ImgLeftEyeBall);
           add(ImgRightEyeBall);
           add(ImgLeftPupil);
           add(ImgRightPupil);
       }};

        return elements;
    }

    public void ShowStudioSession()
    {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(dialog != null && dialog.isShowing())
                    dialog.cancel();

                ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class

                Paint paint = rectShapeDrawable.getPaint();

                paint.setColor(Color.MAGENTA);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5); // you can change the value of 5
                ViewHead.setBackgroundDrawable(rectShapeDrawable);

            }
        });
    }

    public void CloseStudioSession()
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(dialog != null && dialog.isShowing())
                    dialog.cancel();

                ViewHead.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            }
        });
    }

    public void ShowStudioSessionConnecting()
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ShowDialog("Visible to Studio");
            }
        });
    }

    public void ShowStudioSessionDisconnecting()
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ShowDialog("Disconnecting from Studio");
            }
        });
    }

    public void ShowAnimationDataSaved(final Boolean Status)
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Status == true) {
                    ShowDialog("Expression saved!");
                } else {
                    ShowDialog("Expression not saved!");
                }


                final Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(dialog != null && dialog.isShowing())
                        dialog.cancel();
                    }
                }, 2000);
            }
        });
    }

    public void CloseAnyProgress()
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(dialog != null && dialog.isShowing())
                    dialog.cancel();
            }
        });

    }

    public void ShutdownRequest()
    {
        this.runOnUiThread (new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        ViewHeadLayer.setAlpha(0);
                        ViewHeadLayer.setVisibility(View.VISIBLE);
                        ObjectAnimator colorAnimation = ObjectAnimator.ofFloat(ViewHeadLayer, "alpha", 1.0f);
                        colorAnimation.setDuration(1000); // milliseconds
                        colorAnimation.setInterpolator(new AccelerateInterpolator());

                        colorAnimation.addListener(new AnimatorListenerAdapter()
                        {
                            @Override
                            public void onAnimationEnd(Animator animation)
                            {
                                try {
                                    PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
                                    powerManager.shutdown(false, null, false);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        colorAnimation.start();

                    }
                });
            }
        });

    }

    public void ShowBlankScreen()
    {
        this.runOnUiThread (new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        ViewHeadLayer.setAlpha(0);
                        ViewHeadLayer.setVisibility(View.VISIBLE);
                        ObjectAnimator colorAnimation = ObjectAnimator.ofFloat(ViewHeadLayer, "alpha", 1.0f);
                        colorAnimation.setDuration(1000); // milliseconds
                        colorAnimation.setInterpolator(new AccelerateInterpolator());
                        colorAnimation.start();

                    }
                });
            }
        });
    }

    //End of AniUIReadAndConvey

    public  void  ShowDialog(final String msg)
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(dialog != null && dialog.isShowing())
                    dialog.cancel();


                dialog = ProgressDialog.show(Ani.this, null, msg);
            }
        });
    }


    //All Animation Images on the UI
    EyebrowLeft ImgLeftEyeBrow;
    EyebrowRight ImgRightEyeBrow;
    Eyelid ImgLeftEyeClose;
    Eyelid ImgRightEyeClose;
    RelativeLayout ImgLeftEye;
    RelativeLayout ImgRightEye;
    RelativeLayout ImgLeftEyeBall;
    RelativeLayout ImgRightEyeBall;
    RelativeLayout ImgLeftPupil;
    RelativeLayout ImgRightPupil;
    RelativeLayout ViewLeftEye;
    RelativeLayout ViewRightEye;
    View ViewHeadLayer;

    RelativeLayout ViewHead;

    ProgressDialog dialog;

    AniUIConvey aniUIConvey;

    boolean IsFirstTimeUILoaded = false;

    Map<Integer, View> UIViews = new HashMap<>();

    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IsFirstTimeUILoaded = false;
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        GlobalContext.context = getApplicationContext();
        activity = this;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_ani);

        ImgLeftEyeBrow = findViewById(R.id.ImgLeftEyebrow);
        ImgRightEyeBrow = findViewById(R.id.ImgRightEyebrow);
        ImgRightEyeClose = findViewById(R.id.ImgRighteyelid);
        ImgLeftEyeClose = findViewById(R.id.Imglefteyelid);
        ImgRightEye = findViewById(R.id.Imgrighteye);
        ImgLeftEye = findViewById(R.id.Imglefteye);
        ImgRightEyeBall = findViewById(R.id.Imgrighteyeball);
        ImgLeftEyeBall = findViewById(R.id.Imglefteyeball);
        ImgRightPupil = findViewById(R.id.Imgrightpupil);
        ImgLeftPupil = findViewById(R.id.Imgleftpupil);
        ViewRightEye = findViewById(R.id.ViewRightEye);
        ViewLeftEye = findViewById(R.id.ViewLeftEye);
        ViewHead = findViewById(R.id.ViewHead);
        ViewHeadLayer = findViewById(R.id.ViewHeadLayer);


        UIMAINModuleHandler.Instance.setAniUIHandler(this);

       // ScreenOrientationLocker.lockOrientation(UIInterfaceOrientationMask.landscape, andRotateTo: UIInterfaceOrientation.landscapeRight  )

//        ImgLeftEye.setVisibility(View.INVISIBLE);
//        ImgRightEye.setVisibility(View.INVISIBLE);
//        ImgLeftPupil.setVisibility(View.INVISIBLE);
//        ImgRightPupil.setVisibility(View.INVISIBLE);
//        ImgLeftEyeBall.setVisibility(View.INVISIBLE);
//        ImgRightEyeBall.setVisibility(View.INVISIBLE);
//        ImgLeftEyeBrow.setVisibility(View.INVISIBLE);
//        ImgRightEyeBrow.setVisibility(View.INVISIBLE);
//        ImgLeftEyeClose.setVisibility(View.INVISIBLE);
//        ImgRightEyeClose.setVisibility(View.INVISIBLE);
//
//        ViewRightEye.setBackgroundColor(Color.BLACK);
//        ViewLeftEye.setBackgroundColor(Color.BLACK);


        aniUIConvey = UIMAINModuleHandler.Instance.GetAniUIListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                final Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int imgEyeWidth = ImgLeftEyeClose.getWidth();
                        int imgEyeHeight = ImgLeftEyeClose.getHeight();
                        if(imgEyeWidth > 0 && imgEyeHeight > 0 && !IsFirstTimeUILoaded) {
                            IsFirstTimeUILoaded = true;
                            aniUIConvey.AniStarted(new ArrayList<View>() {{
                                UIViews.put(Integer.parseInt((String)ImgLeftEye.getTag()), ImgLeftEye);
                                UIViews.put(Integer.parseInt((String)ImgRightEye.getTag()), ImgRightEye);
                                UIViews.put(Integer.parseInt((String)ImgLeftPupil.getTag()), ImgLeftPupil);
                                UIViews.put(Integer.parseInt((String)ImgRightPupil.getTag()), ImgRightPupil);
                                UIViews.put(Integer.parseInt((String)ImgLeftEyeBall.getTag()), ImgLeftEyeBall);
                                UIViews.put(Integer.parseInt((String)ImgRightEyeBall.getTag()), ImgRightEyeBall);
                                UIViews.put(Integer.parseInt((String)ImgLeftEyeBrow.getTag()), ImgLeftEyeBrow);
                                UIViews.put(Integer.parseInt((String)ImgRightEyeBrow.getTag()), ImgRightEyeBrow);
                                UIViews.put(Integer.parseInt((String)ImgLeftEyeClose.getTag()), ImgLeftEyeClose);
                                UIViews.put(Integer.parseInt((String)ImgRightEyeClose.getTag()), ImgRightEyeClose);

                                add(ImgLeftEye);
                                add(ImgRightEye);
                                add(ImgLeftPupil);
                                add(ImgRightPupil);
                                add(ImgLeftEyeBall);
                                add(ImgRightEyeBall);
                                add(ImgLeftEyeBrow);
                                add(ImgRightEyeBrow);
                                add(ImgLeftEyeClose);
                                add(ImgRightEyeClose);
                            }});

                            ObjectAnimator colorAnimation = ObjectAnimator.ofFloat(ViewHeadLayer, "alpha", 0.0f);
                            colorAnimation.setDuration(1500); // milliseconds
                            colorAnimation.setInterpolator(new AccelerateInterpolator());

                            colorAnimation.addListener(new AnimatorListenerAdapter()
                            {
                                @Override
                                public void onAnimationEnd(Animator animation)
                                {
                                    ViewHeadLayer.setVisibility(View.INVISIBLE);

                                }
                            });
                            colorAnimation.start();
                        }
                    }
                });

            }
        };
        ViewHeadLayer.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

    }



    public void EyesInitSleepy(){

//        ImgRightEyeBrow.animate().translationX(0).translationY(-170).scaleX(0.7f).scaleY(1.3f).rotationBy(UnitConvertor.RadiasToDegree(0.24f)).setDuration(2500).setInterpolator(new DecelerateInterpolator());
//        ImgLeftEyeBrow.animate().translationX(0).translationY(-170).scaleX(0.7f).scaleY(1.3f).rotationBy(UnitConvertor.RadiasToDegree(-0.24f)).setDuration(2500).setInterpolator(new DecelerateInterpolator());
//
//        ImgLeftEye.animate().translationX(22).translationY(0).scaleX(1.1f).scaleY(0.9f).rotationBy(UnitConvertor.RadiasToDegree(5.84f)-360).setDuration(2500).setInterpolator(new DecelerateInterpolator());
//        ImgRightEye.animate().translationX(-22).translationY(0).scaleX(1.1f).scaleY(0.9f).rotationBy(UnitConvertor.RadiasToDegree(-5.84f)+360).setDuration(2500).setInterpolator(new DecelerateInterpolator());
//
//        ImgLeftEyeClose.animate().translationX(22).translationY(0).scaleX(1.1f).scaleY(0.9f).rotationBy(UnitConvertor.RadiasToDegree(5.84f)-360).setDuration(2500).alpha(1.0f).setInterpolator(new DecelerateInterpolator());
//        ImgRightEyeClose.animate().translationX(-22).translationY(0).scaleX(1.1f).scaleY(0.9f).rotationBy(UnitConvertor.RadiasToDegree(-5.84f)+360).setDuration(2500).alpha(1.0f).setInterpolator(new DecelerateInterpolator());
//
//        ImgLeftEyeBall.animate().translationX(80).translationY(-8).setDuration(2000).setInterpolator(new DecelerateInterpolator());
//        ImgRightEyeBall.animate().translationX(-80).translationY(-8).setDuration(2000).setInterpolator(new DecelerateInterpolator());
//
//        ImgLeftPupil.animate().translationX(80).translationY(-36).setDuration(2000).setInterpolator(new DecelerateInterpolator());
//        ImgRightPupil.animate().translationX(-80).translationY(-36).setDuration(2000).setInterpolator(new DecelerateInterpolator());

    }

    public void  MachineLoadApplet()
    {
        this.runOnUiThread (new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {                       // ViewMainHeadLayer.setVisibility(View.VISIBLE);

                        ObjectAnimator colorAnimation = ObjectAnimator.ofFloat(ViewHeadLayer, "alpha", 1.0f);
                        colorAnimation.setDuration(200); // milliseconds
                        colorAnimation.setInterpolator(new AccelerateInterpolator());

                        colorAnimation.addListener(new AnimatorListenerAdapter()
                        {
                            @Override
                            public void onAnimationEnd(Animator animation)
                            {
                                Intent Interaction = new Intent();
                                Interaction.setClass(activity, activity_applet.class);
                                startActivity(Interaction);

                            }
                        });
                        colorAnimation.start();


                    }
                });
            }
        });


    }



}
