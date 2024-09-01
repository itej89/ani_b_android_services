package FrameworkInterface.PublicTypes;

/**
 * Created by tej on 24/06/18.
 */

public class EEPROMDetails
{
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

}
