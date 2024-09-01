//
//  ACTUATOR.swift
//  FourierMachines.Ani.Client.Kinetics
//
//  Created by Tej Kiran on 04/03/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package FrameworkInterface.PublicTypes.Constants;

//Integers are assigned based on the AnimationObject Types for Motors
public enum Actuator
{
    TURN(21),
    LIFT(22),
    LEAN(23),
    TILT(24),
    LOCK_RIGHT(25),
    LOCK_LEFT(26),
    UNKNOWN(-2);
        private final int value;
        private Actuator(int value) {
        this.value = value;
        }

        public int getValue() {
        return value;
        }
}

