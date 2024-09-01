package Framework.DOIPLayer;

import Framework.DataTypes.DOIP_OBJECTS.DOIPRequestObject;
import Framework.DataTypes.DOIP_OBJECTS.DOIPResponseObject;
import Framework.Validation.VALIDATION_ERROR_CODES;
import Framework.Validation.ValidationRuleMessage;
import Framework.Validation.ValidationRuleMessages;

public class DoIPGenericHeaderHandler {

    public ValidationRuleMessages ValidationErrors = new ValidationRuleMessages();


    public static DoIPGenericHeaderHandler Instance = new DoIPGenericHeaderHandler();

    public boolean ValidateHeader(DOIPRequestObject objRequest)
    {
        if(objRequest != null)
        {

            if(!DOIPSession.Instance.getValidProtocolVersions().contains((objRequest.GetProtocolVersion())))
            {
                ValidationErrors.Add(new ValidationRuleMessage(VALIDATION_ERROR_CODES.INVALID_DATA,  "Protocol version"));
            }

            byte inverserProtocolVersion = (byte)(objRequest.GetProtocolVersion() ^ (byte)0xFF);
            if((objRequest.GetInverseProtocolVersion()) != inverserProtocolVersion)
            {
                ValidationErrors.Add(new ValidationRuleMessage( VALIDATION_ERROR_CODES.INVALID_DATA, "Inverse Protocol version"));
            }
        }
        else{
            ValidationErrors.Add(new ValidationRuleMessage(VALIDATION_ERROR_CODES.EMPTY,  "Response Object"));
        }

        if(ValidationErrors.Messages.size() > 0)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public boolean ValidateHeader(byte[] protocolbytes)
    {
        if(protocolbytes != null)
        {

            if(!DOIPSession.Instance.getValidProtocolVersions().contains(protocolbytes[0]))
            {
                ValidationErrors.Add(new ValidationRuleMessage(VALIDATION_ERROR_CODES.INVALID_DATA,  "Protocol version"));
            }

            byte inverserProtocolVersion = (byte)(protocolbytes[0] ^ (byte)0xFF);
            if(protocolbytes[1] != inverserProtocolVersion)
            {
                ValidationErrors.Add(new ValidationRuleMessage( VALIDATION_ERROR_CODES.INVALID_DATA, "Inverse Protocol version"));
            }
        }
        else{
            ValidationErrors.Add(new ValidationRuleMessage(VALIDATION_ERROR_CODES.EMPTY,  "Response Object"));
        }

        if(ValidationErrors.Messages.size() > 0)
        {
            return false;
        }
        else
        {
            return true;
        }

    }
}
