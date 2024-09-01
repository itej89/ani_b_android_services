package fouriermachines.anidiag.Screens;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import Framework.DataTypes.ActuatorCalibration;
import Framework.DataTypes.GlobalContext;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import FrameworkInterface.PublicTypes.Config.MachineConfig;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.Constants.KineticsEEPROM;
import FrameworkInterface.PublicTypes.EEPROMDetails;
import androidx.fragment.app.Fragment;
import fouriermachines.anidiag.R;
import fouriermachines.anidiag.datatypes.ActuatorAddressData;
import fouriermachines.anidiag.datatypes.Delegates.EEPROMDataRecieved;
import fouriermachines.anidiag.datatypes.Delegates.GenericFragmentAPI;
import fouriermachines.anidiag.datatypes.Delegates.ReferanceAnglesSaveRetrieve;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link EEPROMReferanceAngleData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EEPROMReferanceAngleData extends Fragment implements GenericFragmentAPI, EEPROMDataRecieved {

    public  void WillComeToForeGround()
    {
//        if(getActivity() instanceof ReferanceAnglesSaveRetrieve) {
//            Map<Actuator, String> Ref_Angles = ((ReferanceAnglesSaveRetrieve) getActivity()).RetiriveReferanceAngles();
//
//            if (Ref_Angles != null) {
//                for (Map.Entry<Actuator, String> entry : Ref_Angles.entrySet()) {
//                    for (int i = 0; i < listAdaptor.getCount(); i++) {
//
//                        KineticsEEPROM.EEPROMParameter parameter = MachineConfig.Instance.MachineActuatorList.get(entry.getKey()).RefAngleAddressLocationInEERPOM;
//
//                        if (listAdaptor.getItem(i).EEPromAddress == new KineticsEEPROM().MEMORY_MAP.get(parameter).Address) {
//                            listAdaptor.getItem(i).Value = Byte.parseByte(entry.getValue());
//
//                            break;
//                        }
//                    }
//                }
//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
//                        listAdaptor.notifyDataSetChanged();
//
//                        Toast.makeText(getActivity(), "Referance Angles set Success!", Toast.LENGTH_SHORT);
//                    }
//                });
//            }
//        }
    }

    //EEPROMDataRecieved
    public  void  ServoEEPROMDataRecieved(Actuator actuator, EEPROMDetails Details, ArrayList<Integer> Data)
    {

    }
    public  void  EEPROMDataRecieved(EEPROMDetails Details, ArrayList<Integer> Data)
    {
        if(Data != null && Data.size() == 1) {
            for (int i = 0; i < listAdaptor.getCount(); i++) {

                if (listAdaptor.getItem(i).EEPromAddress == Details.Address) {
                    listAdaptor.getItem(i).Value =  (byte)(Data.get(0) & 0xFF);

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            listAdaptor.notifyDataSetChanged();

                            Toast.makeText(getActivity(), "EEPROM Read Success!", Toast.LENGTH_SHORT);
                        }
                    });

                    break;
                }
            }
        }
    }
    public  void  EEPROMWriteStatus(Boolean Status)
    { getActivity().runOnUiThread(new Runnable() {
        public void run() {

            Toast.makeText(getActivity(), "EEPROM Write Success!", Toast.LENGTH_SHORT);
        }
    });

    }
    //End of EEPROMDataRecieved

    ListView DataTable;
    FrameLayout ContextContentView;
    ArrayList<ActuatorAddressData> actuatorAddressList;
    EEPRomReferanceAngleAddressDataListViewAdaptor listAdaptor;

    public EEPROMReferanceAngleData() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EEPROMReferanceAngleData newInstance() {
        EEPROMReferanceAngleData fragment = new EEPROMReferanceAngleData();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mlayout = inflater.inflate(R.layout.fragment_eepromaddressdata, container, false);
        ContextContentView  = (FrameLayout)mlayout.findViewById(R.id.ContextContentView);

        actuatorAddressList = new ArrayList<ActuatorAddressData>();

        for (Map.Entry<Actuator, ActuatorCalibration> keyval: MachineConfig.Instance.MachineActuatorList.entrySet()) {
            ActuatorAddressData actuatorAddressData  =new ActuatorAddressData();
            actuatorAddressData.Type = keyval.getKey();
            actuatorAddressData.ActuatorName = keyval.getKey().toString();
            actuatorAddressData.EEPromAddress = new KineticsEEPROM().MEMORY_MAP.get(keyval.getValue().RefAngleAddressLocationInEERPOM).Address;
            actuatorAddressData.EEPromByteCount = new KineticsEEPROM().MEMORY_MAP.get(keyval.getValue().RefAngleAddressLocationInEERPOM).NoOfBytes;
            actuatorAddressData.DataInfo = "Referance Position";
            actuatorAddressData.Parameter = keyval.getValue().RefAngleAddressLocationInEERPOM;
            actuatorAddressList.add(actuatorAddressData);
        }

        for (Map.Entry<Actuator, ActuatorCalibration> keyval: MachineConfig.Instance.MachineActuatorList.entrySet()) {
            ActuatorAddressData actuatorAddressData  =new ActuatorAddressData();
            actuatorAddressData.Type = keyval.getKey();
            actuatorAddressData.ActuatorName = keyval.getKey().toString();
            actuatorAddressData.EEPromAddress = new KineticsEEPROM().MEMORY_MAP.get(keyval.getValue().ShutdownAngleAddressLocationInEERPOM).Address;
            actuatorAddressData.EEPromByteCount = new KineticsEEPROM().MEMORY_MAP.get(keyval.getValue().ShutdownAngleAddressLocationInEERPOM).NoOfBytes;
            actuatorAddressData.DataInfo = "OFF Delta Position";
            actuatorAddressData.Parameter = keyval.getValue().ShutdownAngleAddressLocationInEERPOM;
            actuatorAddressList.add(actuatorAddressData);
        }

        return mlayout;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        SetScanListAsViewContext();
    }

    public void SetScanListAsViewContext()
    {
        DataTable = new ListView(GlobalContext.context);

        DataTable.setDivider(null);
        DataTable.setDividerHeight(0);
        FrameLayout.LayoutParams layout_params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        layout_params.setMargins(10, 10, 10, 10);
        ContextContentView.addView(DataTable, layout_params);

        listAdaptor = new EEPRomReferanceAngleAddressDataListViewAdaptor(GlobalContext.context, actuatorAddressList);
        DataTable.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
        DataTable.setAdapter(listAdaptor);
        listAdaptor.notifyDataSetChanged();
    }


    public  class  ViewHolder
    {
        TextView ServoName;
        TextView EEPRomLocation;
        TextView ByteCount;
        TextView Value;

        Button btnRead;
        Button btnWrite;

        int position;
    }


    public class EEPRomReferanceAngleAddressDataListViewAdaptor extends ArrayAdapter<ActuatorAddressData> {

        @Override
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        public EEPRomReferanceAngleAddressDataListViewAdaptor(Context context, ArrayList<ActuatorAddressData> dataList) {
            super(context, 0, dataList);
        }
        @Override
        public View getView(int position, View rowView, ViewGroup parent) {


            ActuatorAddressData listItem  = getItem(position);



            // Check if an existing view is being reused, otherwise inflate the view
            if (rowView == null) {
                final ViewHolder rowHolder = new ViewHolder();
                rowView = LayoutInflater.from(getContext()).inflate(R.layout.eepromdatarow, parent, false);
                rowHolder.position = position;
                rowHolder.ServoName = rowView.findViewById(R.id.ServoName);
                rowHolder.ServoName.setText(listItem.ActuatorName + " : " + listItem.DataInfo);

                rowHolder.EEPRomLocation = rowView.findViewById(R.id.EEPROMLocation);
                rowHolder.EEPRomLocation.setText(listItem.EEPromAddress.toString());

                rowHolder.ByteCount = rowView.findViewById(R.id.ByteCount);
                rowHolder.ByteCount.setText(listItem.EEPromByteCount.toString());

                rowHolder.Value = rowView.findViewById(R.id.Value);
                rowHolder.Value.setTag(listItem.Parameter);
                rowHolder.Value.setText("97");

                rowHolder.btnRead = rowView.findViewById(R.id.btnRead);
                rowHolder.btnWrite = rowView.findViewById(R.id.btnWrite);
                rowHolder.btnRead.setTag(rowHolder);
                rowHolder.btnWrite.setTag(rowHolder);
                rowHolder.btnRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ViewHolder rowholder = (ViewHolder)v.getTag();
                        UIMAINModuleHandler.Instance.GetDiagRequestListenerConveyListener().ReadEEPROMData(new KineticsEEPROM().MEMORY_MAP.get(rowholder.Value.getTag()));
                    }
                });
                rowHolder.btnWrite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ViewHolder rowholder = (ViewHolder)v.getTag();
                        final String data = rowHolder.Value.getText().toString();
                        if(data != null && data != "")
                        {
                            final Integer IntValue = Integer.parseInt(data);
                            UIMAINModuleHandler.Instance.GetDiagRequestListenerConveyListener().WriteEEPROMData(new KineticsEEPROM().MEMORY_MAP.get(rowholder.Value.getTag()),
                                    new ArrayList<Integer>(){{
                                        add(IntValue);
                                    }}
                            );

                        }
                    }
                });

                rowView.setTag(rowHolder);

            }
            else {

                ViewHolder holder = (ViewHolder)rowView.getTag();
                if (holder.position == position && listItem.Value != null)
                    holder.Value.setText(listItem.Value.toString());
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
