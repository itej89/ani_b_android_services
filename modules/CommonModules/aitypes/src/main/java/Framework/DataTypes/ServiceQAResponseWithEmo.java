package Framework.DataTypes;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceQAResponseWithEmo implements Parcelable {
    public String Message = "";
    public Float Joy = 0.0f;
    public Float Surprise = 0.0f;
    public Float Anger = 0.0f;
    public Float Sadness = 0.0f;
    public Float Fear = 0.0f;
    public Float Disgust = 0.0f;
    public  String Synth = "";
    public  String intent ="";
    public  Float confidence = 0.0f;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Message);
        dest.writeValue(this.Joy);
        dest.writeValue(this.Surprise);
        dest.writeValue(this.Anger);
        dest.writeValue(this.Sadness);
        dest.writeValue(this.Fear);
        dest.writeValue(this.Disgust);
        dest.writeString(this.Synth);
        dest.writeString(this.intent);
        dest.writeValue(this.confidence);
    }

    public ServiceQAResponseWithEmo() {
    }

    protected ServiceQAResponseWithEmo(Parcel in) {
        this.Message = in.readString();
        this.Joy = (Float) in.readValue(Float.class.getClassLoader());
        this.Surprise = (Float) in.readValue(Float.class.getClassLoader());
        this.Anger = (Float) in.readValue(Float.class.getClassLoader());
        this.Sadness = (Float) in.readValue(Float.class.getClassLoader());
        this.Fear = (Float) in.readValue(Float.class.getClassLoader());
        this.Disgust = (Float) in.readValue(Float.class.getClassLoader());
        this.Synth = in.readString();
        this.intent = in.readString();
        this.confidence = (Float) in.readValue(Float.class.getClassLoader());
    }

    public static final Creator<ServiceQAResponseWithEmo> CREATOR = new Creator<ServiceQAResponseWithEmo>() {
        @Override
        public ServiceQAResponseWithEmo createFromParcel(Parcel source) {
            return new ServiceQAResponseWithEmo(source);
        }

        @Override
        public ServiceQAResponseWithEmo[] newArray(int size) {
            return new ServiceQAResponseWithEmo[size];
        }
    };
}
