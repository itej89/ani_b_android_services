package Framework.Validation;

public class ValidationRuleMessage {
    public ValidationRuleMessage(VALIDATION_ERROR_CODES lobjReturn, String strAdditionalInfo)
    {
        ErrorCode = lobjReturn;
        AdditionalInfo = strAdditionalInfo;
    }

    public ValidationRuleMessage(VALIDATION_ERROR_CODES lobjReturn)
    {
        ErrorCode = lobjReturn;
        DisplayMessage = ErrorCode.toString();
    }

    public ValidationRuleMessage(VALIDATION_ERROR_CODES lobjReturn, String MethodInfo, String strAdditionalInfo)
    {
        ErrorCode = lobjReturn;
        AdditionalInfo = MethodInfo;
        DisplayMessage = ErrorCode.toString();
    }

    public String AdditionalInfo = "";
    public VALIDATION_ERROR_CODES ErrorCode = VALIDATION_ERROR_CODES.FAIL;
    public String DisplayMessage = "";
}
