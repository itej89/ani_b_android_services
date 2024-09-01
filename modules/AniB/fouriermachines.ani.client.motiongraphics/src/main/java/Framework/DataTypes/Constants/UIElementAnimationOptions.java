//
//  UIElementAnimationOptions.swift
//  FourierMachines.Ani.Client.MotionGraphics
//
//  Created by Tej Kiran on 02/04/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Constants;

import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.UIViewAnimationOptions;

public  class  UIElementAnimationOptions {
    public  enum Options {
        autoreverse(0),
        beginFromCurrentState(1),
        curveEaseIn(2),
        curveEaseInOut(3),
        curveEaseOut(4),
        curveLinear(5),
        showHideTransitionViews(6),
        transitionCrossDissolve(7),
        transitionCurlDown(8),
        transitionCurlUp(9),
        transitionFlipFromBottom(10),
        transitionFlipFromLeft(11),
        transitionFlipFromRight(12),
        transitionFlipFromTop(13),
        allowUserInteraction(14);

        public static Options fromInt(int x) {
            switch(x) {
                case 0:
                    return autoreverse;
                case 1:
                    return beginFromCurrentState;
                case 2:
                    return curveEaseIn;
                case 3:
                    return curveEaseInOut;
                case 4:
                    return curveEaseOut;
                case 5:
                    return curveLinear;
                case 6:
                    return showHideTransitionViews;
                case 7:
                    return transitionCrossDissolve;
                case 8:
                    return transitionCurlDown;
                case 9:
                    return transitionCurlUp;
                case 10:
                    return transitionFlipFromBottom;
                case 11:
                    return transitionFlipFromLeft;
                case 12:
                    return transitionFlipFromRight;
                case 13:
                    return transitionFlipFromTop;
                case 14:
                    return allowUserInteraction;
            }
            return null;
        }

        private final int value;

        private Options(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public  static Integer count = Options.allowUserInteraction.getValue() + 1;


    }


    public static Map<String, UIViewAnimationOptions.Options> UITransitionCurveStringToOptions = new HashMap<String, UIViewAnimationOptions.Options>(){
        {
            put("autoreverse",UIViewAnimationOptions.Options.autoreverse);
            put("beginFromCurrentState",UIViewAnimationOptions.Options.beginFromCurrentState);
            put("curveEaseIn",UIViewAnimationOptions.Options.curveEaseIn);
            put("curveEaseInOut",UIViewAnimationOptions.Options.curveEaseInOut);
            put("curveLinear",UIViewAnimationOptions.Options.curveLinear);
            put("showHideTransitionViews",UIViewAnimationOptions.Options.showHideTransitionViews);
            put("transitionCrossDissolve",UIViewAnimationOptions.Options.transitionCrossDissolve);
            put("transitionCurlDown",UIViewAnimationOptions.Options.transitionCurlDown);
            put("transitionCurlUp",UIViewAnimationOptions.Options.transitionCurlUp);
            put("transitionFlipFromBottom",UIViewAnimationOptions.Options.transitionFlipFromBottom);
            put("transitionFlipFromLeft",UIViewAnimationOptions.Options.transitionFlipFromLeft);
            put("transitionFlipFromRight",UIViewAnimationOptions.Options.transitionFlipFromRight);
            put("transitionFlipFromTop",UIViewAnimationOptions.Options.transitionFlipFromTop);
            put("allowUserInteraction",UIViewAnimationOptions.Options.allowUserInteraction);
            put("curveEaseOut",UIViewAnimationOptions.Options.curveEaseOut);
        }};

    public static  Map<Integer, UIViewAnimationOptions.Options> UITransitionCurveOptions = new HashMap<Integer, UIViewAnimationOptions.Options>(){
        {
            put(0,UIViewAnimationOptions.Options.autoreverse);
            put(1,UIViewAnimationOptions.Options.beginFromCurrentState);
            put(2,UIViewAnimationOptions.Options.curveEaseIn);
            put(3,UIViewAnimationOptions.Options.curveEaseInOut);
            put(5,UIViewAnimationOptions.Options.curveLinear);
            put(6,UIViewAnimationOptions.Options.showHideTransitionViews);
            put(7,UIViewAnimationOptions.Options.transitionCrossDissolve);
            put(8,UIViewAnimationOptions.Options.transitionCurlDown);
            put(9,UIViewAnimationOptions.Options.transitionCurlUp);
            put(10,UIViewAnimationOptions.Options.transitionFlipFromBottom);
            put(11,UIViewAnimationOptions.Options.transitionFlipFromLeft);
            put(12,UIViewAnimationOptions.Options.transitionFlipFromRight);
            put(13,UIViewAnimationOptions.Options.transitionFlipFromTop);
            put(14,UIViewAnimationOptions.Options.allowUserInteraction);
            put(4,UIViewAnimationOptions.Options.curveEaseOut);
        }};

}