package fm.ani.anib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import Framework.DataTypes.Delegates.UI.AppletUIRead;
import FrameworkImplementation.DataTypes.Enums.LINK_ANCHORS;
import Framework.DataTypes.GlobalContext;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import androidx.appcompat.app.AppCompatActivity;

public class activity_applet extends AppCompatActivity implements AppletUIRead
{
    Activity activity;

    View ViewHeadLayer;

    WebView WebLinkView = null;

    private LineChart PulseChart;

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent e)
    {
        if(e.getAxisValue(MotionEvent.AXIS_Y) == -1)
        {
            UIMAINModuleHandler.Instance.GetAppletUIListener().UserRaisedTriggerEvent();
        }
        return  true;
    }

    @Override
    public  boolean dispatchKeyEvent(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.KEYCODE_BUTTON_1 && e.getAction() == KeyEvent.ACTION_UP)
        {
            UIMAINModuleHandler.Instance.GetAppletUIListener().CloseAppletRequested();
        }
        return  true;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        GlobalContext.context = getApplicationContext();
        activity = this;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        switch (UIMAINModuleHandler.Instance.GetAppletUIListener().GetAppletIntent())
        {
            case ACCESSORY_CONNECTED: {
                setContentView(R.layout.layout_accessory);
                ViewHeadLayer = findViewById(R.id.accbkgViewMainHeadLayer);

                break;
            }
            case ZERODOC:{
                setContentView(R.layout.layout_nurse);
                ViewHeadLayer = findViewById(R.id.nursebkgViewMainHeadLayer);
                PulseChart = findViewById(R.id.pulseChart);

                PulseChart.setViewPortOffsets(0, 0, 0, 0);
                PulseChart.setBackgroundColor(Color.rgb(255, 255, 255));

                // no description text
                PulseChart.getDescription().setEnabled(false);

                // enable touch gestures
                PulseChart.setTouchEnabled(false);

                // enable scaling and dragging
                PulseChart.setDragEnabled(false);
                PulseChart.setScaleEnabled(false);

                // if disabled, scaling can be done on x- and y-axis separately
                PulseChart.setPinchZoom(false);

                PulseChart.setDrawGridBackground(false);
                PulseChart.setMaxHighlightDistance(300);

                XAxis x = PulseChart.getXAxis();
                x.setEnabled(false);

                YAxis y = PulseChart.getAxisLeft();
                y.setEnabled(false);

                PulseChart.getAxisRight().setEnabled(false);


                PulseChart.getLegend().setEnabled(false);

                PulseChart.animateXY(2000, 2000);

                // don't forget to refresh the drawing
                PulseChart.invalidate();




                break;
            }
            default: {
                setContentView(R.layout.activity_applet);

                WebLinkView = findViewById(R.id.URLViewer);

                WebLinkView.setWebViewClient(new WebViewClient() {

                    public void onPageFinished(WebView view, String url) {
                        UIMAINModuleHandler.Instance.GetAppletUIListener().AppletLoadedURL(url);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                ObjectAnimator colorAnimation = ObjectAnimator.ofFloat(WebLinkView, "alpha", 1.0f);
                                colorAnimation.setDuration(1500);
                                colorAnimation.setInterpolator(new AccelerateInterpolator());
                                colorAnimation.start();
                            }
                        });
                    }
                });

                WebLinkView.setAlpha(0);

                WebSettings webSettings = WebLinkView.getSettings();
                webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                webSettings.setAppCacheEnabled(true);
                webSettings.setDomStorageEnabled(true);
                webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                webSettings.setUseWideViewPort(true);
                webSettings.setSaveFormData(true);

                webSettings.setJavaScriptEnabled(true);
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

                ViewHeadLayer = findViewById(R.id.ViewHeadLayer);
            }
        }

        ObjectAnimator colorAnimation = ObjectAnimator.ofFloat(ViewHeadLayer, "alpha", 0.0f);
        colorAnimation.setDuration(500);
        colorAnimation.setInterpolator(new AccelerateInterpolator());
        colorAnimation.start();


        UIMAINModuleHandler.Instance.AppletUIHandler = this;
        UIMAINModuleHandler.Instance.GetAppletUIListener().AppletLoaded();
    }




    @Override
    public void CloseApplet(){

        if(WebLinkView != null) {
            ObjectAnimator colorAnimation = ObjectAnimator.ofFloat(WebLinkView, "alpha", 0.0f);
            colorAnimation.setDuration(1000); // milliseconds
            colorAnimation.setInterpolator(new AccelerateInterpolator());

            colorAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    if (WebLinkView != null)
                        WebLinkView.loadUrl("about:blank");

                    UIMAINModuleHandler.Instance.AppletUIHandler = null;

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            final Handler handler = new Handler();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {                       // ViewMainHeadLayer.setVisibility(View.VISIBLE);

                                    ObjectAnimator colorAnimation = ObjectAnimator.ofFloat(ViewHeadLayer, "alpha", 1.0f);
                                    colorAnimation.setDuration(500); // milliseconds
                                    colorAnimation.setInterpolator(new AccelerateInterpolator());

                                    colorAnimation.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {


                                        }
                                    });
                                    colorAnimation.start();
                                }
                            });
                        }
                    });


                }
            });
            colorAnimation.start();

            this.runOnUiThread (new Runnable() {
                @Override
                public void run() {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {                       // ViewMainHeadLayer.setVisibility(View.VISIBLE);

                            Intent Interaction = new Intent();
                            Interaction.setClass(activity, Ani.class);
                            startActivity(Interaction);

                        }
                    },1600);
                }
            });
        }
        else
        {
            UIMAINModuleHandler.Instance.AppletUIHandler = null;

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    final Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {                       // ViewMainHeadLayer.setVisibility(View.VISIBLE);

                            ObjectAnimator colorAnimation = ObjectAnimator.ofFloat(ViewHeadLayer, "alpha", 1.0f);
                            colorAnimation.setDuration(500); // milliseconds
                            colorAnimation.setInterpolator(new AccelerateInterpolator());

                            colorAnimation.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {

                                    Intent Interaction = new Intent();
                                    Interaction.setClass(activity, Ani.class);
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

    public void ShutdownRequest() {
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

    @Override
    public void ShowBlankScreen(){
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



     AnimatorSet _imgrecbtnanimationset;
     AnimatorSet _txtrecanimationset;
     boolean IsNurseRecording = false;
    @Override
    public void StartNurseRecording() {
        IsNurseRecording = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final   ImageView imgBtnRec = (ImageView)findViewById(R.id.imgRecButton);
                final TextView txtRec = (TextView)findViewById(R.id.txtRec);


                ObjectAnimator fadeimgrecbtnOut = ObjectAnimator.ofFloat(imgBtnRec, "alpha",  1f, .3f);
                fadeimgrecbtnOut.setDuration(500);
                ObjectAnimator fadeimgrecbtnIn = ObjectAnimator.ofFloat(imgBtnRec, "alpha", .3f, 1f);
                fadeimgrecbtnIn.setDuration(500);

                _imgrecbtnanimationset = new AnimatorSet();
                _imgrecbtnanimationset.play(fadeimgrecbtnIn).after(fadeimgrecbtnOut);

                _imgrecbtnanimationset.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if(IsNurseRecording) {
                            _imgrecbtnanimationset.start();

                        }else {
                            _imgrecbtnanimationset.removeAllListeners();
                            _imgrecbtnanimationset.cancel();
                            imgBtnRec.setAlpha(1.0f);
                        }
                    }
                });
                _imgrecbtnanimationset.start();


                ObjectAnimator txtrecfadeOut = ObjectAnimator.ofFloat(txtRec, "alpha",  1f, .3f);
                txtrecfadeOut.setDuration(500);
                ObjectAnimator txtrecfadeIn = ObjectAnimator.ofFloat(txtRec, "alpha", .3f, 1f);
                txtrecfadeIn.setDuration(500);

                _txtrecanimationset = new AnimatorSet();
                _txtrecanimationset.play(txtrecfadeIn).after(txtrecfadeOut);

                _txtrecanimationset.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if(IsNurseRecording)
                            _txtrecanimationset.start();
                        else {
                            _txtrecanimationset.removeAllListeners();
                            _txtrecanimationset.cancel();
                            txtRec.setAlpha(1.0f);
                        }
                    }
                });



                _txtrecanimationset.start();

            }
        });




    }

    @Override
    public  void  StopNurseRecording() {
        IsNurseRecording = false;
    }

    @Override
    public void ShowURL(String Link){
        if(WebLinkView != null && !Link.isEmpty())
        {
            WebLinkView.loadUrl(Link);
        }
    }

    int EntryIndex = 0;
    int MaxEntries = 40;
    public void SetPulse(int pulse)
    {


        LineDataSet set1;

        if (PulseChart.getData() != null &&
                PulseChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) PulseChart.getData().getDataSetByIndex(0);
            if(EntryIndex < MaxEntries)
            {
                set1.addEntry(new Entry(EntryIndex++, pulse));
            }
            else
            {
                set1.removeEntry(0);
                for(int i=0; i<set1.getEntryCount(); i++)
                    set1.getEntryForIndex(i).setX(i);
                set1.addEntry(new Entry(MaxEntries-1, pulse));
            }
            set1.notifyDataSetChanged();
            PulseChart.getData().notifyDataChanged();
            PulseChart.notifyDataSetChanged();
            PulseChart.invalidate();
        } else {
            ArrayList<Entry> Entries = new ArrayList<>();
            Entries.add(new Entry(0, pulse));
            // create a dataset and give it a type
            set1 = new LineDataSet(Entries, "DataSet 1");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            set1.setDrawFilled(false);
            set1.setDrawCircles(false);
            set1.setLineWidth(3.5f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.RED);
            set1.setColor(Color.RED);
            set1.setDrawHorizontalHighlightIndicator(false);

            // create a data object with the data sets
            LineData data = new LineData(set1);
            data.setDrawValues(false);

            // set data
            PulseChart.setData(data);
        }
    }

    @Override
    public void UpdateNurseData(int BPM, int Pulse, int SPO2, double Temp){

        class NurseDataUpdateTask implements Runnable {
            int BPM; int Pulse; int SPO2; double Temp;

            NurseDataUpdateTask(int _BPM, int _Pulse, int _SPO2, double _Temp)
            {
                BPM = _BPM; Pulse = _Pulse; SPO2 = _SPO2; Temp = _Temp;
            }

            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView txtBPM = (TextView) findViewById(R.id.txtBPM);
                        if (txtBPM != null)
                            if(BPM != -1)txtBPM.setText(String.valueOf(BPM));

                        TextView txtSPO2 = (TextView) findViewById(R.id.txtSPO2);
                        if (txtSPO2 != null)
                            if(SPO2 != -1)txtSPO2.setText(String.valueOf(SPO2));

                        TextView txtTemp = (TextView) findViewById(R.id.txtTemperature);
                        if (txtTemp != null)
                            if(Temp != -1)txtTemp.setText(String.valueOf(Temp));

                        SetPulse(Pulse);
                    }
                });

            }
        }

        Thread t = new Thread(new NurseDataUpdateTask(BPM, Pulse, SPO2, Temp));
        t.start();
    }



    public void AccessoryConnected(ArrayList<LINK_ANCHORS> ANCHOR)
    {

        final ArrayList<LINK_ANCHORS> ANCHORS = new ArrayList<>();
        for (int i = 0; i < ANCHOR.size(); i++) {
            ANCHORS.add(ANCHOR.get(i));
        }

        this.runOnUiThread (new Runnable() {
            @Override
            public void run() {

                final Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {


                        ImageView imgAcc = findViewById(R.id.imgaccessoryimg);
                        ImageView imgLogo = findViewById(R.id.imgaccessorylogo);
                        if (imgAcc != null && imgLogo != null) {
                            switch (ANCHORS.get(0)) {
                                case JOY:
                                    imgAcc.setImageDrawable(getResources().getDrawable(R.drawable.accmyli));
                                    imgLogo.setImageDrawable(getResources().getDrawable(R.drawable.myli_logo));
                                    break;
                                case HEART_MONITOR:
                                    break;
                                case HEART_BEAT:
                                    imgAcc.setImageDrawable(getResources().getDrawable(R.drawable.acccali));
                                    imgLogo.setImageDrawable(getResources().getDrawable(R.drawable.cali_logo));
                                    break;
                            }

                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    UIMAINModuleHandler.Instance.GetAppletUIListener().CloseAppletRequested();
                                }
                            }, 3 * 1000);

                        }
                    }
                });

            }
        });

    }

    public void AccessoryDisconnected(LINK_ANCHORS ANCHOR) {
                switch (ANCHOR) {
                    case JOY:
                        break;
                    case HEART_MONITOR:
                        break;
                    case HEART_BEAT:
                        break;
                }
            }


}