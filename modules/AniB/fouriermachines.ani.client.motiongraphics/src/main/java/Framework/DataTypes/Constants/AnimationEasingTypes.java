//
//  AnimationEasingTypes.swift
//  FourierMachines.Ani.Client.MotionGraphics
//
//  Created by Tej Kiran on 02/04/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//


package Framework.DataTypes.Constants;

import java.util.HashMap;
import java.util.Map;

public  class  AnimationEasingTypes {

   public enum Types {
        linear(0),
        easeIN(1),
        easeOut(2),
        easeInOut(3);

        private final int value;

        private Types(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static Map<String, Types> AnimationEasingTypesStringToOptions= new HashMap<String, Types>(){{
        put("linear", Types.linear);
        put("easeIN", Types.easeIN);
        put("easeOut",  Types.easeOut);
        put("easeInOut", Types.easeInOut);

    }};


  public static  Map<String, Types> AnimationTypeStringToOptions= new HashMap<String, Types>(){{
        put("linear", Types.linear);
        put("easeIN", Types.easeIN);
        put("easeOut",  Types.easeOut);
        put("easeInOut", Types.easeInOut);

    }};

    static  String kCAMediaTimingFunctionLinear = "kCAMediaTimingFunctionLinear";
    static  String kCAMediaTimingFunctionEaseIn = "kCAMediaTimingFunctionEaseIn";
    static String kCAMediaTimingFunctionEaseOut = "kCAMediaTimingFunctionEaseOut";
    static String kCAMediaTimingFunctionEaseInEaseOut = "kCAMediaTimingFunctionEaseInEaseOut";

  public  static  Map<Types, String> AnimationEasingTypesToCAMediaTimingFunction= new HashMap<Types, String>(){{
        put(Types.linear,kCAMediaTimingFunctionLinear);
        put(Types.easeIN,kCAMediaTimingFunctionEaseIn);
        put(Types.easeOut,kCAMediaTimingFunctionEaseOut);
        put(Types.easeInOut,kCAMediaTimingFunctionEaseInEaseOut);

    }};

}