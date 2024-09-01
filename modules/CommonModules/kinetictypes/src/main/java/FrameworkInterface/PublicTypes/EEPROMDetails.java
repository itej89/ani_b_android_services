package FrameworkInterface.PublicTypes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tej on 24/06/18.
 */

public class EEPROMDetails implements Parcelable {
   public Integer Address;
   public Integer NoOfBytes;
    public boolean SignedValue = false; //Sign will be stored in EEPROm in last byte as 0 for Positive and 1 for negative

    public   EEPROMDetails(Integer addrress, Integer noOfBytes)
    {
        Address  = addrress;
        NoOfBytes = noOfBytes;
    }

    public   EEPROMDetails(Integer addrress, Integer noOfBytes, boolean isSigned)
    {
        Address  = addrress;
        NoOfBytes = noOfBytes;
        SignedValue = isSigned;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.Address);
        dest.writeValue(this.NoOfBytes);
        dest.writeByte(this.SignedValue ? (byte) 1 : (byte) 0);
    }

    protected EEPROMDetails(Parcel in) {
        this.Address = (Integer) in.readValue(Integer.class.getClassLoader());
        this.NoOfBytes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.SignedValue = in.readByte() != 0;
    }

    public static final Parcelable.Creator<EEPROMDetails> CREATOR = new Parcelable.Creator<EEPROMDetails>() {
        @Override
        public EEPROMDetails createFromParcel(Parcel source) {
            return new EEPROMDetails(source);
        }

        @Override
        public EEPROMDetails[] newArray(int size) {
            return new EEPROMDetails[size];
        }
    };
}
