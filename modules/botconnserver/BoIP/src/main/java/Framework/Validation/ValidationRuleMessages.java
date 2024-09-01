package Framework.Validation;

import java.util.ArrayList;

public class ValidationRuleMessages {
    public ArrayList<ValidationRuleMessage> Messages =  new ArrayList<ValidationRuleMessage>();
    public void Add(ValidationRuleMessage item)
    {
        Messages.add(item);
    }
}
