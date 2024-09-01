//
//  MotionStartType.swift
//  FourierMachines.Ani.Client.MotionGraphics
//
//  Created by Tej Kiran on 19/06/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package FrameworkInterface.DataTypes.Constants;

public enum MotionStartType
{
    //Defines how the current animation going to be started
     WAIT_AND_MOVE, //Wait till previous motion is finished
         INSTANT_MOVE   //stops previous on going motion and start the motion for present animation immediately
}
