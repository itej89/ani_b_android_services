// IKineticsParameterUpdatesConvey.aidl
package FrameworkInterface.PublicTypes.Delegates;

import Framework.DataTypes.Parsers.RequestCommandParser.KineticsRequest;
// Declare any non-default types here with import statements

interface IKineticsParameterUpdatesConveyAIDL {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void  ParameterUpdated(in KineticsRequest request);
     void  ParametersSetSuccessfully();
}
