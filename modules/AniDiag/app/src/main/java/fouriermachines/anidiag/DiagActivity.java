package fouriermachines.anidiag;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.Delegates.UI.DiagConvey;
import Framework.DataTypes.GlobalContext;
import Framework.SystemEventHandlers.UIMAINModuleHandler;
import FrameworkInterface.PublicTypes.Constants.Actuator;
import FrameworkInterface.PublicTypes.EEPROMDetails;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import fouriermachines.anidiag.Screens.EEPROMAddressData;
import fouriermachines.anidiag.Screens.EEPROMReferanceAngleData;
import fouriermachines.anidiag.Screens.ISL94203;
import fouriermachines.anidiag.Screens.ServoEEPROMData;
import fouriermachines.anidiag.Screens.ServoPad;
import fouriermachines.anidiag.datatypes.Delegates.EEPROMDataRecieved;
import fouriermachines.anidiag.datatypes.Delegates.GenericFragmentAPI;
import fouriermachines.anidiag.datatypes.Delegates.ISLDataRecieved;
import fouriermachines.anidiag.datatypes.Delegates.ReferanceAnglesSaveRetrieve;

import static fouriermachines.anidiag.DiagActivity.Screens.BMS_PARAM;
import static fouriermachines.anidiag.DiagActivity.Screens.EEPROMADDRESSData;
import static fouriermachines.anidiag.DiagActivity.Screens.EEPROMData;
import static fouriermachines.anidiag.DiagActivity.Screens.EEPROMREFERANCEData;
import static fouriermachines.anidiag.DiagActivity.Screens.ServoPAD;

public class DiagActivity extends AppCompatActivity implements ReferanceAnglesSaveRetrieve,  DiagConvey, fouriermachines.ani.client.main.datatypes.Delegates.InterActivtyCom {


    Map<Actuator, String> Referance_Angles;
    //ReferanceAnglesSaveRetrieve
    public void SaveReferanceAngles(Map<Actuator, String> Data)
    {
        Referance_Angles = Data;
    }
    public Map<Actuator, String> RetiriveReferanceAngles()
    {
        return  Referance_Angles;
    }
    //End of ReferanceAnglesSaveRetrieve



    //DiagConvey
    public void ServoEEPROMBytes(Actuator actuator, EEPROMDetails details, ArrayList<Integer> Data)
    {
        try {
            EEPROMDataRecieved readconvey = (EEPROMDataRecieved) mSectionsPagerAdapter.getCurrentFragment();
            if (readconvey != null) {
                readconvey.ServoEEPROMDataRecieved(actuator, details, Data);
            }
        }
        catch (Exception e)
        {
            Log.e("DIAGACTIVITY", "Error in : RecievedEEPROMBytes", e);
        }
    }

    public void RecievedEEPROMBytes(EEPROMDetails details, ArrayList<Integer> Data)
    {
        try {
            EEPROMDataRecieved readconvey = (EEPROMDataRecieved) mSectionsPagerAdapter.getCurrentFragment();
            if (readconvey != null) {
                readconvey.EEPROMDataRecieved(details, Data);
            }
        }
        catch (Exception e)
        {
            Log.e("DIAGACTIVITY", "Error in : RecievedEEPROMBytes", e);
        }
    }

    public  void  EEPROMWriteStatus(Boolean Status)
    {

        try {
            EEPROMDataRecieved readconvey = (EEPROMDataRecieved) mSectionsPagerAdapter.getCurrentFragment();
            if (readconvey != null) {
                readconvey.EEPROMWriteStatus(Status);
            }
        }
        catch (Exception e)
        {
            Log.e("DIAGACTIVITY", "Error in : RecievedEEPROMBytes", e);
        }
    }

    public  void  ISLWriteStatus(Boolean Status)
    {
        try {
            ISLDataRecieved readconvey = (ISLDataRecieved) mSectionsPagerAdapter.getCurrentFragment();
            if (readconvey != null) {
                readconvey.ISLWriteStatus(Status);
            }
        }
        catch (Exception e)
        {
            Log.e("DIAGACTIVITY", "Error in : ISLDataRecieved", e);
        }
    }

    public void RecievedISLBytes(EEPROMDetails details, ArrayList<Integer> Data)
    {
        try {
            ISLDataRecieved readconvey = (ISLDataRecieved) mSectionsPagerAdapter.getCurrentFragment();
            if (readconvey != null) {
                readconvey.ISLReadRecieved(details, Data);
            }
        }
        catch (Exception e)
        {
            Log.e("DIAGACTIVITY", "Error in : RecievedISLBytes", e);
        }
    }
    //End of DiagConvey


    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diag);
        GlobalContext.context = this;

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager() , this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        UIMAINModuleHandler.Instance.notifyOnDiagUIUpdate(this);

        NavigationDelegate.interActivtyCom = this;

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        UIMAINModuleHandler.Instance.GetUIMainConveyListener().CloseServiceConnections();
    }





    public  enum  Screens {BMS_PARAM, EEPROMADDRESSData, EEPROMData, ServoPAD, EEPROMREFERANCEData;
        public static Screens fromInteger(int x) {
            switch(x) {
                case 0:
                    return BMS_PARAM;
                case 1:
                    return EEPROMADDRESSData;
                case 2:
                    return EEPROMData;
                case 3:
                    return ServoPAD;
                case 4:
                    return EEPROMREFERANCEData;

            }
            return null;
        }
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        Map<Screens, Fragment> FragmentMap;

        Activity parentActivity;
        public SectionsPagerAdapter(FragmentManager fm, Activity parent) {
            super(fm);
            parentActivity = parent;
            FragmentMap = new HashMap<Screens, Fragment>()
            {{
                put(BMS_PARAM, new ISL94203(parentActivity));
                put(EEPROMADDRESSData, new EEPROMAddressData());
                put(EEPROMData, new ServoEEPROMData());
                put(ServoPAD, new ServoPad());
                put(EEPROMREFERANCEData, new EEPROMReferanceAngleData());
            }};
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return FragmentMap.get(Screens.fromInteger(position));
        }

        private Fragment mCurrentFragment;

        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }
        //...
        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((Fragment) object);
                if(mCurrentFragment instanceof GenericFragmentAPI)
                {
                    ((GenericFragmentAPI)mCurrentFragment).WillComeToForeGround();
                }
            }
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public int getCount() {

            return Screens.values().length;
        }
    }

    //NavigationDelegate
    public  void  Finish()
    {
        this.finish();
    }
    //End of NavigationDelegate
}
