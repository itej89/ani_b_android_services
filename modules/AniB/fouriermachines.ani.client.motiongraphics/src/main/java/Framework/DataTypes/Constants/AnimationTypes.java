//
//  EnumTypes.swift
//  FourierMachines.Ani.Client.MotionGraphics
//
//  Created by Tej Kiran on 02/04/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Constants;


import java.util.HashMap;
import java.util.Map;

public  class  AnimationTypes {

   public enum Type {

        Tranformation(0),
        CircularPath(1),
        Identity(2),
        TransformOverlay(3),
        NA(4);

        static Integer count = Type.NA.getValue() + 1;

        private final int value;
        private Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }


    }

   public static Map<String, Type> AnimationTypeStringToOptions= new HashMap<String, Type>(){{
        put("Tranformation", Type.Tranformation);
        put("CircularPath", Type.CircularPath);
        put("Identity", Type.Identity);
        put("TransformOverlay",Type.TransformOverlay);
        put("NA", Type.NA);

    }};

}