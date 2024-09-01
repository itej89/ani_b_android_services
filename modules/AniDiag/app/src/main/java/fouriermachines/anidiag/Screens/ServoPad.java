package fouriermachines.anidiag.Screens;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import Framework.SystemEventHandlers.UIMAINModuleHandler;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import androidx.fragment.app.Fragment;
import fouriermachines.anidiag.R;
import fouriermachines.anidiag.customcontrols.CircularSlider;
import fouriermachines.anidiag.datatypes.Delegates.GenericFragmentAPI;
import fouriermachines.anidiag.datatypes.Delegates.ReferanceAnglesSaveRetrieve;

public class ServoPad extends Fragment implements GenericFragmentAPI{


    public  void WillComeToForeGround()
    {

    }

    Map<Actuator, String> ActuatorAngles = new HashMap<>();


    CompoundButton.OnCheckedChangeListener OnConnectCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                UIMAINModuleHandler.Instance.GetDiagRequestListenerConveyListener().AttachActuator((Actuator) buttonView.getTag());
            }
            else
            {
                UIMAINModuleHandler.Instance.GetDiagRequestListenerConveyListener().DettachActuator((Actuator) buttonView.getTag());
            }
        }
    };

    View.OnTouchListener sldrTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                CircularSliderMoved sldrmoved = (CircularSliderMoved)v.getTag();

                if(sldrmoved.IsMoved) {
                    sldrmoved.IsMoved = false;

                    if(sldrmoved.Degree >=0 && sldrmoved.Degree <= 179) {
                        UIMAINModuleHandler.Instance.GetDiagRequestListenerConveyListener().MoveActuator(sldrmoved.actuatorType, sldrmoved.Degree);
                    }
                    }
                }
            return false;
        }
    };

    public  class  CircularSliderMoved implements CircularSlider.OnSliderMovedListener
    {
        private Actuator actuatorType;
        private TextView txtDegreeView;
        public Integer Degree;
        public Boolean IsMoved = false;

        public CircularSliderMoved(Actuator actuator, TextView txtView)
        {
            actuatorType = actuator;
            txtDegreeView = txtView;
        }

        @Override
        public  void onSliderMoved(double pos)
        {
            if(pos == 0)
            {
                Degree = 0;
            }
            else
            if(pos > 0)
            {
                double radians = pos * 2 * Math.PI;
                Degree = (int)((180/Math.PI) * radians);
            }
            else
            {
                pos = -1 * pos;
                double radians = pos * 2 * Math.PI;
                Degree = 360 - (int)((180/Math.PI) * radians);
            }

            Degree = 180 - Degree;
            if(Degree < 0)
            {
                Degree = 360 + Degree;
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(Degree >=0 && Degree <= 179) {
                        if(ActuatorAngles.containsKey(actuatorType))
                        {
                            ActuatorAngles.remove(actuatorType);
                        }
                        ActuatorAngles.put(actuatorType, Degree.toString());

                        txtDegreeView.setText(String.valueOf(Degree));
                    }
                }
            });

            IsMoved = true;
         }
    }

    public ServoPad() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mlayout = inflater.inflate(R.layout.fragment_servo_pad, container, false);
        Switch tglConnectTurn = mlayout.findViewById(R.id.tglCNCTTURN);
        tglConnectTurn.setTag(Actuator.TURN);
        Switch tglConnectLift = mlayout.findViewById(R.id.tglCNCTLIFT);
        tglConnectLift.setTag(Actuator.LIFT);
        Switch tglConnectLean = mlayout.findViewById(R.id.tglCNCTLEAN);
        tglConnectLean.setTag(Actuator.LEAN);
        Switch tglConnectTilt = mlayout.findViewById(R.id.tglCNCTTILT);
        tglConnectTilt.setTag(Actuator.TILT);

        tglConnectTurn.setOnCheckedChangeListener(OnConnectCheckedChangeListener);
        tglConnectLift.setOnCheckedChangeListener(OnConnectCheckedChangeListener);
        tglConnectLean.setOnCheckedChangeListener(OnConnectCheckedChangeListener);
        tglConnectTilt.setOnCheckedChangeListener(OnConnectCheckedChangeListener);

        CircularSlider sldrTurn = mlayout.findViewById(R.id.sldrTURN);
        CircularSlider sldrLIFT = mlayout.findViewById(R.id.sldrLIFT);
        CircularSlider sldrLEAN = mlayout.findViewById(R.id.sldrLEAN);
        CircularSlider sldrTILT = mlayout.findViewById(R.id.sldrTILT);

        TextView txtDegreeTURN = mlayout.findViewById(R.id.txtDegTURN);
        TextView txtDegreeLIFT = mlayout.findViewById(R.id.txtDegLIFT);
        TextView txtDegreeLEAN = mlayout.findViewById(R.id.txtDegLEAN);
        TextView txtDegreeTILT = mlayout.findViewById(R.id.txtDegTILT);

        CircularSliderMoved crsldrMovedTurn = new CircularSliderMoved(Actuator.TURN, txtDegreeTURN);
        CircularSliderMoved crsldrMovedLift = new CircularSliderMoved(Actuator.LIFT, txtDegreeLIFT);
        CircularSliderMoved crsldrMovedLean = new CircularSliderMoved(Actuator.LEAN, txtDegreeLEAN);
        CircularSliderMoved crsldrMovedTilt = new CircularSliderMoved(Actuator.TILT,txtDegreeTILT);

        sldrTurn.setTag(crsldrMovedTurn);
        sldrLIFT.setTag(crsldrMovedLift);
        sldrLEAN.setTag(crsldrMovedLean);
        sldrTILT.setTag(crsldrMovedTilt);

        sldrTurn.setOnSliderMovedListener(crsldrMovedTurn);
        sldrLIFT.setOnSliderMovedListener(crsldrMovedLift);
        sldrLEAN.setOnSliderMovedListener(crsldrMovedLean);
        sldrTILT.setOnSliderMovedListener(crsldrMovedTilt);

        sldrTurn.setOnTouchListener(sldrTouchListener);
        sldrLIFT.setOnTouchListener(sldrTouchListener);
        sldrLEAN.setOnTouchListener(sldrTouchListener);
        sldrTILT.setOnTouchListener(sldrTouchListener);

        Button btnRefAngles = mlayout.findViewById(R.id.btnSetRefState);
        btnRefAngles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof ReferanceAnglesSaveRetrieve)
                {
                    ((ReferanceAnglesSaveRetrieve)getActivity()).SaveReferanceAngles(ActuatorAngles);
                }
            }
        });

        return mlayout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
