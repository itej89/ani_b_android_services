package Framework.DataTypes;


import Framework.DataTypes.Constants.UIElementAnimationOptions;

public enum AnimationObject {
    Image_EyeBrowRight(20),
    Image_EyeBrowLeft(15),
    Image_EyeBallRight(17),
    Image_EyeBallLeft(12),
    Image_EyeRight(16),
    Image_EyeLeft(11),
    Image_EyePupilRight(18),
    Image_EyePupilLeft(13),
    Image_EyeLidRight(19),
    Image_EyeLidLeft(14),
    Motor_Turn(21),
    Motor_Lift(22),
    Motor_Lean(23),
    Motor_Tilt(24),
    NA(-1);

    public static AnimationObject fromInt(int x) {
        switch(x) {
            case 20:
                return Image_EyeBrowRight;
            case 15:
                return Image_EyeBrowLeft;
            case 17:
                return Image_EyeBallRight;
            case 12:
                return Image_EyeBallLeft;
            case 16:
                return Image_EyeRight;
            case 11:
                return Image_EyeLeft;
            case 18:
                return Image_EyePupilRight;
            case 13:
                return Image_EyePupilLeft;
            case 19:
                return Image_EyeLidRight;
            case 14:
                return Image_EyeLidLeft;
            case 21:
                return Motor_Turn;
            case 22:
                return Motor_Lift;
            case 23:
                return Motor_Lean;
            case 24:
                return Motor_Tilt;
            case -1:
                return NA;
        }
        return null;
    }

    private final int value;

    private AnimationObject(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}