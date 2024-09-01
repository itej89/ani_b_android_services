package fouriermachines.anidiag.customcontrols;

import androidx.constraintlayout.widget.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes.Constants.ISL94203ConfigurationParameters;
import client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes.ISL94203ConfigurationObjects;
import client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes.ISL94203ConversionFormulae;
import client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes.ISL94203Parameter;
import fouriermachines.anidiag.R;

import static client.ani.fouriermachines.fouriermachinesaniclientisl94203.DataTypes.Constants.ISL94203_FLOAT_CONVERSION_TYPES.REG_TO_VOLT;

public class ISL94203PARAM_ROW {

   public ISL94203ConfigurationParameters PARAMTER_TYPE = ISL94203ConfigurationParameters.NA;

    ArrayList<TextView> AddressLabels = new ArrayList<>();

    TextView Param_Name;

    TextView Param_Description;

   public Button ReadParameter;

   public Button WriteHighByte;

   public Button WriteLowByte;

    ArrayList<TextView> NAME_BIT = new ArrayList<>();


    ArrayList<EditText> VAL_BIT = new ArrayList<>();

    TextView HexValue;

    TextView PhysicalValue;


    TextView Unit;

   public void ExtractUI(ConstraintLayout isl94203row)
    {
        AddressLabels.add((TextView) isl94203row.findViewById(R.id.ADRL));
        AddressLabels.add((TextView) isl94203row.findViewById(R.id.ADRH));

        Param_Name = (isl94203row.findViewById(R.id.ParamName));
        Param_Description = (isl94203row.findViewById(R.id.ParameterDescription));

        ReadParameter = isl94203row.findViewById(R.id.R);
        WriteHighByte = isl94203row.findViewById(R.id.WH);
        WriteLowByte = isl94203row.findViewById(R.id.WL);

        HexValue = isl94203row.findViewById(R.id.HexValue);
        PhysicalValue = isl94203row.findViewById(R.id.PhyiscalValue);


        PhysicalValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(PhysicalValue.isFocused() && !PhysicalValue.getText().equals(""))
                {
                     Double value = Double.valueOf(PhysicalValue.getText().toString());

                    ISL94203ConversionFormulae convertor = new ISL94203ConversionFormulae();
                        ArrayList<Byte> regValue =  convertor.FloatToReg(value, (ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).Number_Of_Bytes), (ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).CONVERSION_TYPE));
                        Integer numberOfBytes = (ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).Number_Of_Bytes);

                        for(int i=0; i<=numberOfBytes-1; i++)
                        {
                            if(i == numberOfBytes-1 && ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).CONVERSION_TYPE == REG_TO_VOLT)
                            {
                                ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).reg.set(i, (ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).reg.get(i)) | 0x0F);
                            }
                    else
                            {
                                ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).reg.set(i,  0xFF);
                            }

                            Integer prevregValue = ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).reg.get(i);

                                    ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).reg.set(i, prevregValue & regValue.get(i));


                            for(int j=0; j<=7; j++)
                            {
                                ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).BitValues.set((i*8) + j , ((ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).reg.get(i)) >> j  & (0x01)));
                            }
                        }
                        ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).Value = value;
                        FillData(ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE));

                }
            }
        });




        Unit = isl94203row.findViewById(R.id.unit);

        NAME_BIT.add((TextView) isl94203row.findViewById(R.id.NAME_BIT00));
        NAME_BIT.add((TextView) isl94203row.findViewById(R.id.NAME_BIT01));
        NAME_BIT.add((TextView) isl94203row.findViewById(R.id.NAME_BIT02));
        NAME_BIT.add((TextView) isl94203row.findViewById(R.id.NAME_BIT03));
        NAME_BIT.add((TextView) isl94203row.findViewById(R.id.NAME_BIT04));
        NAME_BIT.add((TextView) isl94203row.findViewById(R.id.NAME_BIT05));
        NAME_BIT.add((TextView) isl94203row.findViewById(R.id.NAME_BIT06));
        NAME_BIT.add((TextView) isl94203row.findViewById(R.id.NAME_BIT07));
        NAME_BIT.add((TextView) isl94203row.findViewById(R.id.NAME_BIT08));
        NAME_BIT.add((TextView) isl94203row.findViewById(R.id.NAME_BIT09));
        NAME_BIT.add((TextView) isl94203row.findViewById(R.id.NAME_BIT10));
        NAME_BIT.add((TextView) isl94203row.findViewById(R.id.NAME_BIT11));
        NAME_BIT.add((TextView) isl94203row.findViewById(R.id.NAME_BIT12));
        NAME_BIT.add((TextView) isl94203row.findViewById(R.id.NAME_BIT13));
        NAME_BIT.add((TextView) isl94203row.findViewById(R.id.NAME_BIT14));
        NAME_BIT.add((TextView) isl94203row.findViewById(R.id.NAME_BIT15));

        VAL_BIT.add((EditText) isl94203row.findViewById(R.id.VAL_BIT00));
        VAL_BIT.add((EditText) isl94203row.findViewById(R.id.VAL_BIT01));
        VAL_BIT.add((EditText) isl94203row.findViewById(R.id.VAL_BIT02));
        VAL_BIT.add((EditText) isl94203row.findViewById(R.id.VAL_BIT03));
        VAL_BIT.add((EditText) isl94203row.findViewById(R.id.VAL_BIT04));
        VAL_BIT.add((EditText) isl94203row.findViewById(R.id.VAL_BIT05));
        VAL_BIT.add((EditText) isl94203row.findViewById(R.id.VAL_BIT06));
        VAL_BIT.add((EditText) isl94203row.findViewById(R.id.VAL_BIT07));
        VAL_BIT.add((EditText) isl94203row.findViewById(R.id.VAL_BIT08));
        VAL_BIT.add((EditText) isl94203row.findViewById(R.id.VAL_BIT09));
        VAL_BIT.add((EditText) isl94203row.findViewById(R.id.VAL_BIT10));
        VAL_BIT.add((EditText) isl94203row.findViewById(R.id.VAL_BIT11));
        VAL_BIT.add((EditText) isl94203row.findViewById(R.id.VAL_BIT12));
        VAL_BIT.add((EditText) isl94203row.findViewById(R.id.VAL_BIT13));
        VAL_BIT.add((EditText) isl94203row.findViewById(R.id.VAL_BIT14));
        VAL_BIT.add((EditText) isl94203row.findViewById(R.id.VAL_BIT15));

        for(int i=0; i<VAL_BIT.size(); i++) {
            VAL_BIT.get(i).addTextChangedListener(new VAL_BIT_TEXT_WATCHER(VAL_BIT.get(i)));
        }
    }

    public  void  UpdateData(ISL94203Parameter ISLVoltageParam)
    {
        HexValue.setText("");
        for (int i = 0; i <= ISLVoltageParam.Number_Of_Bytes - 1; i++) {
            AddressLabels.get(i).setText("0X" + Integer.toHexString(ISLVoltageParam.Address.get(i)));
        }
        for (int i = 0; i <= ISLVoltageParam.BitNames.size() - 1; i++) {
            NAME_BIT.get(i).setText(ISLVoltageParam.BitNames.get(i));
        }

        for(int i = 0; i <= ISLVoltageParam.BitValues.size() - 1; i++)
        {
            VAL_BIT.get(i).setText(String.valueOf(ISLVoltageParam.BitValues.get(i)));
        }

        for(int j = 0; j <= ISLVoltageParam.Number_Of_Bytes - 1; j++)
        {
            HexValue.setText(Integer.toHexString(ISLVoltageParam.reg.get(j))+HexValue.getText());
        }

        PhysicalValue.setText(String.valueOf(ISLVoltageParam.Value));
    }

   public void FillData(ISL94203Parameter ISLVoltageParam)
    {

            PARAMTER_TYPE = ISLVoltageParam.PARAM_TYPE;
            HexValue.setText("");


            Param_Name.setText(ISLVoltageParam.ParameterName.toString());
            Param_Description.setText(ISLVoltageParam.ParameterDescription);

            ReadParameter.setTag(PARAMTER_TYPE.getValue());
            WriteHighByte.setTag(PARAMTER_TYPE.getValue());
            WriteLowByte.setTag(PARAMTER_TYPE.getValue());
            Unit.setText(ISLVoltageParam.ParameterPhysical_Unit);


            for (int i = 0; i <= ISLVoltageParam.Number_Of_Bytes - 1; i++) {
                AddressLabels.get(i).setText("0X" + Integer.toHexString(ISLVoltageParam.Address.get(i)));
            }
            for (int i = 0; i <= ISLVoltageParam.BitNames.size() - 1; i++) {
                NAME_BIT.get(i).setText(ISLVoltageParam.BitNames.get(i));
            }

            for(int i = 0; i <= ISLVoltageParam.BitValues.size() - 1; i++)
            {
                VAL_BIT.get(i).setText(String.valueOf(ISLVoltageParam.BitValues.get(i)));
                VAL_BIT.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.requestFocus();
                    }
                });
            }

            for(int j = 0; j <= ISLVoltageParam.Number_Of_Bytes - 1; j++)
            {
                HexValue.setText(Integer.toHexString(ISLVoltageParam.reg.get(j))+HexValue.getText());
            }

            PhysicalValue.setText(String.valueOf(ISLVoltageParam.Value));



    }


    public  class VAL_BIT_TEXT_WATCHER implements TextWatcher {

        private EditText mEditText;

        public VAL_BIT_TEXT_WATCHER(EditText editText) {
            mEditText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }


        @Override
        public void afterTextChanged(Editable s) {
            if ((s.toString().equals("0") || s.toString().equals("1")) && mEditText.isFocused()) {
                //mEditText.clearFocus();
                Integer bitindex = Integer.valueOf(mEditText.getTag().toString());
                Integer byteIndex = bitindex / 8;

                ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).BitValues.set(bitindex , Integer.valueOf(s.toString()));

                if (s.toString().equals("1")) {
                    ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).reg.set(byteIndex,
                            (ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).reg.get(byteIndex))
                                    | (1 << (bitindex - (byteIndex * 8))));
                } else {
                    ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).reg.set(byteIndex,
                            (ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).reg.get(byteIndex)) & ~(1 << (bitindex - (byteIndex * 8))));
                }

                ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE).ConvertToPhysical();
                FillData(ISL94203ConfigurationObjects.Instance.PARAMETERS.get(PARAMTER_TYPE));
            }
        }
    }

}
