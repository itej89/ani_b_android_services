package Framework.DataTypes.Delegates;

import Framework.DataTypes.ServiceQAResponseWithEmo;

interface IAIServerDelegatesAIDL {
    void RecievedAnswerWithEmotion(in ServiceQAResponseWithEmo QAResponse);
}
