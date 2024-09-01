package Framework.DataTypes.Delegates;


interface IAISTTDelegatesAIDL {
    void RecievedSTTBeginAck(byte status);
    void RecievedSTTProcessingAck(byte status);
    void RecievedSTTFinished(String Response);
}
