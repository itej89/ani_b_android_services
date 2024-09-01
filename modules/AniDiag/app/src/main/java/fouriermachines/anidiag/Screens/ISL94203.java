package fouriermachines.anidiag.Screens;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.android.material.internal.DescendantOffsetUtils;

import java.util.ArrayList;

import Framework.DataTypes.GlobalContext;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import FrameworkInterface.PublicTypes.EEPROMDetails;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes.Constants.ISL94203ConfigurationParameters;
import client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes.ISL94203ConfigurationObjects;
import client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes.ISL94203Parameter;
import fouriermachines.anidiag.R;
import fouriermachines.anidiag.customcontrols.ISL94203PARAM_ROW;
import fouriermachines.anidiag.datatypes.Delegates.ISLDataRecieved;

public class ISL94203 extends Fragment implements ISLDataRecieved {

    Activity parentActivity;
    //ISLDataRecieved
    public  void ISLReadRecieved(EEPROMDetails Details, ArrayList<Integer> Data)
    {
        ISL94203ConfigurationObjects.Instance.PARAMETERS.get(CurrentParameter).reg.clear();
        ISL94203ConfigurationObjects.Instance.PARAMETERS.get(CurrentParameter).BitValues.clear();
        for(int i=0; i<Data.size(); i++)
        {
            ISL94203ConfigurationObjects.Instance.PARAMETERS.get(CurrentParameter).reg.add(Data.get(i));
            for(int j=0; j<8; j++)
            {
                ISL94203ConfigurationObjects.Instance.PARAMETERS.get(CurrentParameter).BitValues.add(((i*8) + j),  Data.get(i) >> j  & (0x01));
            }
        }

        ISL94203ConfigurationObjects.Instance.PARAMETERS.get(CurrentParameter).ConvertToPhysical();

        Integer indexPath = ISL94203ConfigurationObjects.Instance.AVAILABLE_PARAM.indexOf(CurrentParameter);
        for (final ISL94203PARAM_ROW row: rows
             ) {
            if(row.PARAMTER_TYPE == CurrentParameter)
            {
                parentActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        row.UpdateData(ISL94203ConfigurationObjects.Instance.PARAMETERS.get(CurrentParameter));
                    }
                });

          }
        }


    }
    public  void  ISLWriteStatus(Boolean Status)
    {

    }
    //End of ISLDataRecieved



    public ISL94203(Activity parent) {
        parentActivity = parent;
        // Required empty public constructor
    }

    ISL94203ConfigurationParameters CurrentParameter;

    ListView DataTable;
    FrameLayout ContextContentView;
    ArrayList<ISL94203ConfigurationParameters> ISL94203PARAM_ROWS = new ArrayList<>();
    ArrayList<ISL94203PARAM_ROW> rows = new ArrayList<>();
    ISL94203PARAMListViewAdaptor listAdaptor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mlayout =  inflater.inflate(R.layout.fragment_isl94203, container, false);


        ISL94203PARAM_ROWS.addAll(ISL94203ConfigurationObjects.Instance.AVAILABLE_PARAM);
        ContextContentView  = (FrameLayout)mlayout.findViewById(R.id.ContextContentView);

        return  mlayout;
    }

    boolean IsFirstTimeApploaded = true;
    public void onResume()
    {
        super.onResume();
        if(IsFirstTimeApploaded) {
            SetScanListAsViewContext();
            IsFirstTimeApploaded = false;
        }
        }

    public void SetScanListAsViewContext()
    {
        DataTable = new ListView(GlobalContext.context);
        DataTable.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        DataTable.setDivider(null);
        DataTable.setDividerHeight(0);
        FrameLayout.LayoutParams layout_params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        layout_params.setMargins(10, 10, 10, 10);
        ContextContentView.addView(DataTable, layout_params);

        listAdaptor = new ISL94203PARAMListViewAdaptor(GlobalContext.context, ISL94203PARAM_ROWS);
        DataTable.setItemsCanFocus(true);
        DataTable.setAdapter(listAdaptor);
        DataTable.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        listAdaptor.notifyDataSetChanged();
    }



    void WriteISLEEPROMLowByte(final ISL94203Parameter Parameter)
    {
        CurrentParameter = Parameter.PARAM_TYPE;
        UIMAINModuleHandler.Instance.GetDiagRequestListenerConveyListener().WriteISL94203EEPROMData(Parameter.Address.get(0), new ArrayList<Integer>(){{ add(112); }});

//        UIMAINModuleHandler.Instance.GetDiagRequestListenerConveyListener().WriteISL94203EEPROMData(Parameter.Address.get(0), new ArrayList<Integer>(){{ add(Parameter.reg.get(0)); }});
    }

    void WriteISLRAMLowByte(final ISL94203Parameter Parameter)
    {
        CurrentParameter = Parameter.PARAM_TYPE;
        UIMAINModuleHandler.Instance.GetDiagRequestListenerConveyListener().WriteISL94203RAMMData(Parameter.Address.get(0), new ArrayList<Integer>(){{ add(Parameter.reg.get(0)); }});
    }

    void WriteISLEEPROMHighByte(final ISL94203Parameter Parameter)
    {
        CurrentParameter = Parameter.PARAM_TYPE;
        UIMAINModuleHandler.Instance.GetDiagRequestListenerConveyListener().WriteISL94203EEPROMData(Parameter.Address.get(1), new ArrayList<Integer>(){{ add(Parameter.reg.get(1)); }});
    }

    void WriteISLRAMHighByte(final ISL94203Parameter Parameter)
    {
        CurrentParameter = Parameter.PARAM_TYPE;
        UIMAINModuleHandler.Instance.GetDiagRequestListenerConveyListener().WriteISL94203RAMMData(Parameter.Address.get(1), new ArrayList<Integer>(){{ add(Parameter.reg.get(1)); }});
    }



    void ReadISLEEPROMByte(ISL94203Parameter Parameter)
    {
        CurrentParameter = Parameter.PARAM_TYPE;
        EEPROMDetails ReadDetails = new EEPROMDetails(Parameter.Address.get(0), Parameter.Number_Of_Bytes);
        UIMAINModuleHandler.Instance.GetDiagRequestListenerConveyListener().ReadISL94203EEPROMData(ReadDetails);
    }

    void ReadISLRAMByte(ISL94203Parameter Parameter)
    {
        CurrentParameter = Parameter.PARAM_TYPE;
        EEPROMDetails ReadDetails = new EEPROMDetails(Parameter.Address.get(0), Parameter.Number_Of_Bytes);
        UIMAINModuleHandler.Instance.GetDiagRequestListenerConveyListener().ReadISL94203RAMData(ReadDetails);
    }


    public class ISL94203PARAMListViewAdaptor extends ArrayAdapter<ISL94203ConfigurationParameters> {

        @Override
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        public ISL94203PARAMListViewAdaptor(Context context, ArrayList<ISL94203ConfigurationParameters> dataList) {
            super(context, 0, dataList);
        }

        ArrayList<View> RowViews = new ArrayList<>();

        @Override
        public View getView(final int position, View rowView, ViewGroup parent) {

            ISL94203Parameter listItem  = ISL94203ConfigurationObjects.Instance.PARAMETERS.get(getItem(position));

            if(rows.size() > position && RowViews.size() > position)
            {
                rowView = RowViews.get(position);
            }
            else
            // Check if an existing view is being reused, otherwise inflate the view
            if (rowView == null) {
                ISL94203PARAM_ROW row = new ISL94203PARAM_ROW();
                rowView = LayoutInflater.from(getContext()).inflate(R.layout.isl94203datarow, parent, false);
                row.ExtractUI((ConstraintLayout) rowView);
                row.FillData(listItem);
                row.ReadParameter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataTable.setSelection(position);
                        ISL94203ConfigurationParameters ConfigParam = ISL94203ConfigurationParameters.ConvertFromInt((Integer)v.getTag());

                        if((Integer)v.getTag() < ISL94203ConfigurationParameters.STATUS1.getValue())
                        {
                           ReadISLEEPROMByte(ISL94203ConfigurationObjects.Instance.PARAMETERS.get(ConfigParam));
                        }
                        else
                        {
                            ReadISLRAMByte(ISL94203ConfigurationObjects.Instance.PARAMETERS.get(ConfigParam));
                        }
                    }
                });
                row.WriteHighByte.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ISL94203ConfigurationParameters ConfigParam = ISL94203ConfigurationParameters.ConvertFromInt((Integer)v.getTag());

                        if((Integer)v.getTag() < ISL94203ConfigurationParameters.STATUS1.getValue())
                        {
                            WriteISLEEPROMHighByte(ISL94203ConfigurationObjects.Instance.PARAMETERS.get(ConfigParam));
                            //WriteISLRAMHighByte(ISL94203ConfigurationObjects.Instance.PARAMETERS.get(ConfigParam));
                        }
                        else
                        {
                            WriteISLRAMHighByte(ISL94203ConfigurationObjects.Instance.PARAMETERS.get(ConfigParam));
                        }
                    }
                });
                row.WriteLowByte.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ISL94203ConfigurationParameters ConfigParam = ISL94203ConfigurationParameters.ConvertFromInt((Integer)v.getTag());

                        if((Integer)v.getTag() < ISL94203ConfigurationParameters.STATUS1.getValue())
                        {
                            WriteISLEEPROMLowByte(ISL94203ConfigurationObjects.Instance.PARAMETERS.get(ConfigParam));
                            //WriteISLRAMLowByte(ISL94203ConfigurationObjects.Instance.PARAMETERS.get(ConfigParam));
                        }
                        else
                        {
                            WriteISLEEPROMLowByte(ISL94203ConfigurationObjects.Instance.PARAMETERS.get(ConfigParam));
                        }
                    }
                });
                rows.add(row);
                RowViews.add(rowView);
                rowView.setTag(row);
            }
//            else
//            {
//            //    ISL94203PARAM_ROW row = (ISL94203PARAM_ROW)rowView.getTag();
////                row.FillData(listItem);
//            }


            return rowView;

        }
    }
}










