package FrameworkInterface.PublicTypes.Delegates;

import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;

public interface KineticsParameterUpdatesConvey
{
    public void  ParameterUpdated(KineticsRequest request);
    public void  ParametersSetSuccessfully();
}
