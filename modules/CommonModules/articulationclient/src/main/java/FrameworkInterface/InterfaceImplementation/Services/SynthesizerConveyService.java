package FrameworkInterface.InterfaceImplementation.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import FrameworkInterface.DataTypes.Delegates.IPlayerConveyAIDL;
import FrameworkInterface.DataTypes.Delegates.ISynthesizerConveyAIDL;
import FrameworkInterface.InterfaceImplementation.ArticulationManager;

public class SynthesizerConveyService extends Service {

    public SynthesizerConveyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        ArticulationManager.Instance.ArticulationServiceConnected();


        return new ISynthesizerConveyAIDL.Stub() {
            public void FinishedSynthesis() {
                ArticulationManager.Instance.FinishedSynthesis();
            }
        };
    }
}
