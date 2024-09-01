package fouriermachines.anidiag.Screens;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import Framework.DataTypes.GlobalContext;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.EEPROMDetails;
import androidx.fragment.app.Fragment;
import fouriermachines.anidiag.R;
import fouriermachines.anidiag.datatypes.Delegates.EEPROMDataRecieved;
import fouriermachines.anidiag.datatypes.Delegates.GenericFragmentAPI;
import fouriermachines.anidiag.datatypes.ServoEEPROMCalibData;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class ServoEEPROMData extends Fragment implements GenericFragmentAPI, EEPROMDataRecieved {


    public  void WillComeToForeGround()
    {

    }

    ProgressDialog dialog;

    Actuator actuatorType;
    Integer Index = 0;
    ArrayList<Integer> EEPRData;
    ArrayList<ServoEEPROMCalibData> ServoCalibrationData;
    Integer ServoCalibCount = 180;
    Integer ServoCalibDegreeCharacterCount = 3;
    TextView txtServoName;

    public  void  ShowDialog(final String msg)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(dialog != null && dialog.isShowing())
                    dialog.cancel();


                dialog = ProgressDialog.show(getActivity(), null, msg);
            }
        });
    }

    //EEPROMDataRecieved
    public  void  ServoEEPROMDataRecieved(Actuator actuator, EEPROMDetails Details, ArrayList<Integer> Data)
    {
        if(actuatorType == actuator && Data != null && Data.size() > 0)
        {
            EEPRData.addAll(Data);
            ReadCalibFromServoEEPROMData();
        }

    }

    public  void  EEPROMDataRecieved(EEPROMDetails Details, ArrayList<Integer> Data)
    {

    }
    public  void  EEPROMWriteStatus(Boolean Status)
    {

    }
    //End of EEPROMDataRecieved


    ListView DataTable;
    FrameLayout ContextContentView;
    ServoEEPRomDataListViewAdaptor listAdaptor;

    View.OnClickListener onCalibButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Index = 0;
            EEPRData = new ArrayList<>();
            actuatorType = (Actuator) v.getTag();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ContextContentView.removeAllViews();
                    UIMAINModuleHandler.Instance.GetDiagRequestListenerConveyListener().RemoveServoCalibrationData(actuatorType);
                }
            });
            txtServoName.setText(actuatorType.toString());
            ReadCalibFromServoEEPROMData();
            ShowDialog("Reading EEPROM Data");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mlayout = inflater.inflate(R.layout.fragment_servo_eepromdata, container, false);

        Button btnTURNEPR = mlayout.findViewById(R.id.btnTURNEPR);
        btnTURNEPR.setTag(Actuator.TURN);
        Button btnLIFTEPR = mlayout.findViewById(R.id.btnLIFTEPR);
        btnLIFTEPR.setTag(Actuator.LIFT);
        Button btnLEANEPR = mlayout.findViewById(R.id.btnLEANEPR);
        btnLEANEPR.setTag(Actuator.LEAN);
        Button btnTILTEPR = mlayout.findViewById(R.id.btnTILTEPR);
        btnTILTEPR.setTag(Actuator.TILT);
        Button btnLLOCKEPR = mlayout.findViewById(R.id.btnLLOCKEPR);
        btnLLOCKEPR.setTag(Actuator.LOCK_LEFT);
        Button btnULOCKEPR = mlayout.findViewById(R.id.btnULOCKEPR);
        btnULOCKEPR.setTag(Actuator.LOCK_RIGHT);
        txtServoName = mlayout.findViewById(R.id.txtServoName);

        btnTURNEPR.setOnClickListener(onCalibButtonClick);
        btnLIFTEPR.setOnClickListener(onCalibButtonClick);
        btnLEANEPR.setOnClickListener(onCalibButtonClick);
        btnTILTEPR.setOnClickListener(onCalibButtonClick);
        btnLLOCKEPR.setOnClickListener(onCalibButtonClick);
        btnULOCKEPR.setOnClickListener(onCalibButtonClick);



        ContextContentView= mlayout.findViewById(R.id.ContextContentView);

        return mlayout;
    }

    void ReadCalibFromServoEEPROMData()
    {
        if(Index < ServoCalibCount) {
           final EEPROMDetails details = new EEPROMDetails(Index++*ServoCalibDegreeCharacterCount, 3);

           UIMAINModuleHandler.Instance.GetDiagRequestListenerConveyListener().ReadServoEEPROMData(actuatorType, details);

       }
       else
        {

            ServoCalibrationData = new ArrayList<ServoEEPROMCalibData>();




            for(int i=0; i<EEPRData.size(); i=i+3)
            {
               final ServoEEPROMCalibData servoEEPROMCalibData = new ServoEEPROMCalibData();
                servoEEPROMCalibData.Label = String.valueOf(i/3);
                servoEEPROMCalibData.Data = String.valueOf(EEPRData.get(i))+String.valueOf(EEPRData.get(i+1))+String.valueOf(EEPRData.get(i+2));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UIMAINModuleHandler.Instance.GetDiagRequestListenerConveyListener().SaveServoCalibrationData(actuatorType, Integer.parseInt(servoEEPROMCalibData.Label),Integer.parseInt(servoEEPROMCalibData.Data));
                    }
                });
                ServoCalibrationData.add(servoEEPROMCalibData);
            }

            if(dialog != null && dialog.isShowing())
                dialog.cancel();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SetServoCalibrationListAsViewContext();
                }
            });

        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }



    public void SetServoCalibrationListAsViewContext()
    {
        DataTable = new ListView(GlobalContext.context);

        DataTable.setDivider(null);
        DataTable.setDividerHeight(0);
        FrameLayout.LayoutParams layout_params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        layout_params.setMargins(10, 10, 10, 10);
        ContextContentView.addView(DataTable, layout_params);

        listAdaptor = new ServoEEPRomDataListViewAdaptor(GlobalContext.context, ServoCalibrationData);
        DataTable.setAdapter(listAdaptor);
        listAdaptor.notifyDataSetChanged();
    }


    public  class  ViewHolder
    {
        TextView txtLabel;
        TextView txtData;

        Integer position;
    }


    public class ServoEEPRomDataListViewAdaptor extends ArrayAdapter<ServoEEPROMCalibData> {

        @Override
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        public ServoEEPRomDataListViewAdaptor(Context context, ArrayList<ServoEEPROMCalibData> dataList) {
            super(context, 0, dataList);
        }
        @Override
        public View getView(int position, View rowView, ViewGroup parent) {


            ServoEEPROMCalibData listItem  = getItem(position);



            // Check if an existing view is being reused, otherwise inflate the view
            if (rowView == null) {
                final ViewHolder rowHolder = new ViewHolder();
                rowView = LayoutInflater.from(getContext()).inflate(R.layout.servo_eeprom_datarow, parent, false);
                rowHolder.position = position;
                rowHolder.txtData = rowView.findViewById(R.id.txtData);
                rowHolder.txtData.setText(listItem.Data);

                rowHolder.txtLabel = rowView.findViewById(R.id.txtLable);
                rowHolder.txtLabel.setText(listItem.Label);



                rowView.setTag(rowHolder);

            }
            return rowView;
        }
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
