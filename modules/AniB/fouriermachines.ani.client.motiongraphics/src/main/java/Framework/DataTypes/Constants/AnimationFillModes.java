//
//  AnimationFillModes.swift
//  FourierMachines.Ani.Client.MotionGraphics
//
//  Created by Tej Kiran on 02/04/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Constants;

import android.view.Display;

import java.util.HashMap;
import java.util.Map;

public class AnimationFillModes{
 public enum Modes {
    Removed(0),
     Forward(1),
     Backward(2),
     ForwardAndBackWard(3);
    
    static Integer count =  Modes.ForwardAndBackWard.getValue() + 1 ;

    private final int value;
    private Modes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    
};

    public static Map<String ,Modes> AnimationFillModesStringToOptions= new HashMap<String, Modes>(){{
        put("Removed", Modes.Removed);
        put("Forward", Modes.Forward);
        put("Backward", Modes.Backward);
        put("ForwardAndBackWard",Modes.ForwardAndBackWard);

        }};

    static String kCAFillModeBackwards = "kCAFillModeBackwards";
    static String kCAFillModeForwards = "kCAFillModeForwards";
    static String kCAFillModeBoth = "kCAFillModeBoth";
    static String kCAFillModeRemoved = "kCAFillModeRemoved";

    public static Map<Modes ,String> AnimationFillModesToCGPathFillMode= new HashMap<Modes, String>(){{
        put(Modes.Removed, kCAFillModeRemoved);
        put(Modes.Forward,kCAFillModeForwards);
        put(Modes.Backward,kCAFillModeBackwards);
        put(Modes.ForwardAndBackWard,kCAFillModeBoth);

    }};



}