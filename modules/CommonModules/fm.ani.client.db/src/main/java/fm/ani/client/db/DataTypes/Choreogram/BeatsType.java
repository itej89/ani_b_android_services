package fm.ani.client.db.DataTypes.Choreogram;

import android.os.Parcel;
import android.os.Parcelable;

public class BeatsType implements Parcelable {
    public int Beat_Id;
    public int Act_Id;
    public String Action_Data;
    public float JOY;
    public float SURPRISE;
    public float FEAR;
    public float SADNESS;
    public float ANGER;
    public float DISGUST;
    public int StartSec;
    public int EndSec;

    public BeatsType(int act_Id, int beat_ID, String action_Data, float joy, float surprise, float fear, float sadness, float anger, float disgust, int startSec, int endSec)
    {
        Act_Id = act_Id;
        Beat_Id = beat_ID;
        Action_Data = action_Data;
        JOY = joy;
        SURPRISE = surprise;
        FEAR = fear;
        SADNESS = sadness;
        ANGER = anger;
        DISGUST = disgust;
        StartSec = startSec;
        EndSec = endSec;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Beat_Id);
        dest.writeInt(this.Act_Id);
        dest.writeString(this.Action_Data);
        dest.writeFloat(this.JOY);
        dest.writeFloat(this.SURPRISE);
        dest.writeFloat(this.FEAR);
        dest.writeFloat(this.SADNESS);
        dest.writeFloat(this.ANGER);
        dest.writeFloat(this.DISGUST);
        dest.writeInt(this.StartSec);
        dest.writeInt(this.EndSec);
    }

    protected BeatsType(Parcel in) {
        this.Beat_Id = in.readInt();
        this.Act_Id = in.readInt();
        this.Action_Data = in.readString();
        this.JOY = in.readFloat();
        this.SURPRISE = in.readFloat();
        this.FEAR = in.readFloat();
        this.SADNESS = in.readFloat();
        this.ANGER = in.readFloat();
        this.DISGUST = in.readFloat();
        this.StartSec = in.readInt();
        this.EndSec = in.readInt();
    }

    public static final Creator<BeatsType> CREATOR = new Creator<BeatsType>() {
        @Override
        public BeatsType createFromParcel(Parcel source) {
            return new BeatsType(source);
        }

        @Override
        public BeatsType[] newArray(int size) {
            return new BeatsType[size];
        }
    };
}
