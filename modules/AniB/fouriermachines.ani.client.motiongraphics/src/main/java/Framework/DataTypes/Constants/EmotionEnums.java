//
//  EmotionEnums.swift
//  FourierMachines.Ani.Client.MotionGraphics
//
//  Created by Tej Kiran on 02/04/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Constants;


public class EmotionEnums {
    
    public enum Emotions
    {
         JOY(0),
         SURPRISE(1),
         FEAR(2),
         SADNESS(3),
         ANGER(4),
         DISGUST(5);
        private final int value;
        private Emotions(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
}
