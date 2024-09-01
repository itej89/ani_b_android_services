package Framework.DataTypes.Transports.Helpers;

import android.os.Parcel;
import android.os.Parcelable;

public class RecievedData implements Parcelable {
    public  String IPAddress;
    public Integer Port;
    public byte[] Data;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.IPAddress);
        dest.writeValue(this.Port);
        dest.writeByteArray(this.Data);
    }

    public RecievedData() {
    }

    protected RecievedData(Parcel in) {
        this.IPAddress = in.readString();
        this.Port = (Integer) in.readValue(Integer.class.getClassLoader());
        this.Data = in.createByteArray();
    }

    public static final Creator<RecievedData> CREATOR = new Creator<RecievedData>() {
        @Override
        public RecievedData createFromParcel(Parcel source) {
            return new RecievedData(source);
        }

        @Override
        public RecievedData[] newArray(int size) {
            return new RecievedData[size];
        }
    };
}
