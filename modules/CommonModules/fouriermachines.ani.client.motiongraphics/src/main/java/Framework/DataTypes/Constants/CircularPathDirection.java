//
//  CircularPathDirection.swift
//  FourierMachines.Ani.Client.MotionGraphics
//
//  Created by Tej Kiran on 02/04/18.
//  Copyright © 2018 FourierMachines. All rights reserved.
//

package Framework.DataTypes.Constants;

import java.util.HashMap;
import java.util.Map;

public  class  CircularPathDirection {
    public enum Direction {
        clockwise(0),
        anitiClockwise(1);

        static Integer count = Direction.anitiClockwise.getValue() + 1;

        private final int value;

        private Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }



    public static Map<String ,Direction> CircularPathDirectionStringToOptions =new HashMap<String ,Direction>()
    {{ put("clockwise",Direction.clockwise);
      put("anitiClockwise",Direction.anitiClockwise);}};

}