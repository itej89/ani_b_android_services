// IBotConnectAccessAIDL.aidl
package FrameworkImplementation.DataTypes.Delegates;

// Declare any non-default types here with import statements


import Framework.DataTypes.Transports.Helpers.RecievedData;

interface ILinkTransportConveyAIDL {
    void LinkDataReceived(String Anchor, in RecievedData Data);
}
