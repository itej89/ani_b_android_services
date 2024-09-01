package Framework.DataTypes;


import android.util.Pair;

import org.json.JSONObject;

import Framework.DataTypes.Constants.AnimationTypes;
import Framework.DataTypes.Constants.CircularPathDirection;

import static Framework.DataTypes.Constants.CircularPathDirection.CircularPathDirectionStringToOptions;

public class ImageAnimationState extends AnimationState
{

   public AnimationTypes.Type AnimationKind = AnimationTypes.Type.NA;

    public AnimationObject TargetObject = AnimationObject.NA;

    public class TransformMatrix
    {
        public Pair<Double, Boolean> a = new Pair<>(0.0, false);
        public  Pair<Double, Boolean> b = new Pair<>(0.0, false);
        public  Pair<Double, Boolean> c = new Pair<>(0.0, false);
        public  Pair<Double, Boolean> d = new Pair<>(0.0, false);

        public  Pair<Double, Boolean> tx = new Pair<>(0.0, false);
        public  Pair<Double, Boolean> ty = new Pair<>(0.0, false);
    }

    public class CircularCurve
    {

        public  Pair<Double, Boolean> MidX = new Pair<>(0.0, false);
        public  Pair<Double, Boolean> MidY = new Pair<>(0.0, false);
        public  Pair<Double, Boolean> Radius = new Pair<>(0.0, false);
        public  Pair<Double, Boolean> StartAngle = new Pair<>(0.0, false);

        public  Pair<Double, Boolean> EndAngle = new Pair<>(0.0, false);
        public  Pair<CircularPathDirection.Direction, Boolean> Direction = new Pair<>(CircularPathDirection.Direction.clockwise, false);
    }

    public  TransformMatrix Matrix = new TransformMatrix();
    public  CircularCurve CircularPath = new CircularCurve();

    public  Pair<Double, Boolean> opacity = new Pair<>(0.0, false);
    public  Pair<Double, Boolean> centreX = new Pair<>(0.0, false);
    public  Pair<Double, Boolean> centreY = new Pair<>(0.0, false);
    public  Pair<Double, Boolean> AnchorX = new Pair<>(0.0, false);
    public  Pair<Double, Boolean> AnchorY = new Pair<>(0.0, false);

    @Override
    public void parseJson(JSONObject json)
    {
        try {
            JSONObject dictionary = json.getJSONObject("AnimationState");

            AnimationKind = AnimationTypes.AnimationTypeStringToOptions.get(
                    dictionary.getString("AnimationKind"));


            JSONObject dictionaryProperty = dictionary.getJSONObject("a");

            Matrix.a = new Pair<>(dictionaryProperty.getDouble("key"),
                    dictionaryProperty.getBoolean("value"));

            dictionaryProperty = dictionary.getJSONObject("b");

            Matrix.b = new Pair<>(dictionaryProperty.getDouble("key"),
                    dictionaryProperty.getBoolean("value"));

            dictionaryProperty = dictionary.getJSONObject("c");

            Matrix.c = new Pair<>(dictionaryProperty.getDouble("key"),
                    dictionaryProperty.getBoolean("value"));

            dictionaryProperty = dictionary.getJSONObject("d");

            Matrix.d = new Pair<>(dictionaryProperty.getDouble("key"),
                    dictionaryProperty.getBoolean("value"));

            dictionaryProperty = dictionary.getJSONObject("tx");

            Matrix.tx = new Pair<>(dictionaryProperty.getDouble("key"),
                    dictionaryProperty.getBoolean("value"));

            dictionaryProperty = dictionary.getJSONObject("ty");

            Matrix.ty = new Pair<>(dictionaryProperty.getDouble("key"),
                    dictionaryProperty.getBoolean("value"));


            dictionaryProperty = dictionary.getJSONObject("MidX");

            CircularPath.MidX = new Pair<>(dictionaryProperty.getDouble("key"),
                    dictionaryProperty.getBoolean("value"));

            dictionaryProperty = dictionary.getJSONObject("MidY");

            CircularPath.MidY = new Pair<>(dictionaryProperty.getDouble("key"),
                    dictionaryProperty.getBoolean("value"));

            dictionaryProperty = dictionary.getJSONObject("Radius");

            CircularPath.Radius = new Pair<>(dictionaryProperty.getDouble("key"),
                    dictionaryProperty.getBoolean("value"));

            dictionaryProperty = dictionary.getJSONObject("StartAngle");

            CircularPath.StartAngle = new Pair<>(dictionaryProperty.getDouble("key"),
                    dictionaryProperty.getBoolean("value"));

            dictionaryProperty = dictionary.getJSONObject("EndAngle");

            CircularPath.EndAngle = new Pair<>(dictionaryProperty.getDouble("key"),
                    dictionaryProperty.getBoolean("value"));

            dictionaryProperty = dictionary.getJSONObject("Direction");

            CircularPath.Direction = new Pair<>(CircularPathDirectionStringToOptions.get(dictionaryProperty.getString("key")), dictionaryProperty.getBoolean("value"));


            dictionaryProperty = dictionary.getJSONObject("opacity");

            opacity = new Pair<>(dictionaryProperty.getDouble("key"),
                    dictionaryProperty.getBoolean("value"));

            dictionaryProperty = dictionary.getJSONObject("centreX");

            centreX = new Pair<>(dictionaryProperty.getDouble("key"),
                    dictionaryProperty.getBoolean("value"));

            dictionaryProperty = dictionary.getJSONObject("centreY");

            centreY = new Pair<>(dictionaryProperty.getDouble("key"),
                    dictionaryProperty.getBoolean("value"));

            dictionaryProperty = dictionary.getJSONObject("AnchorX");

            AnchorX = new Pair<>(dictionaryProperty.getDouble("key"),
                    dictionaryProperty.getBoolean("value"));

            dictionaryProperty = dictionary.getJSONObject("AnchorY");

            AnchorY = new Pair<>(dictionaryProperty.getDouble("key"),
                    dictionaryProperty.getBoolean("value"));

        }
        catch (Exception e)
        {

        }
    }


    @Override
    public String Json(){

        String json = "";

        json = json.concat("{ \"AnimationState\" : {");



        json = json.concat(" \"AnimationKind\" : \""+AnimationKind.toString()+"\" , ");



        json = json.concat(" \"a\" : {");
        json = json.concat(" \"key\" : \""+Matrix.a.first.toString()+"\" , "+" \"value\" : \""+Matrix.a.second.toString()+"\"  },");


        json = json.concat(" \"b\" : {");
        json = json.concat(" \"key\" : \""+Matrix.b.first.toString()+"\" , "+" \"value\" : \""+Matrix.b.second.toString()+"\"  },");


        json = json.concat(" \"c\" : {");
        json = json.concat(" \"key\" : \""+Matrix.c.first.toString()+"\" , "+" \"value\" : \""+Matrix.c.second.toString()+"\"  },");


        json = json.concat(" \"d\" : {");
        json = json.concat(" \"key\" : \""+Matrix.d.first.toString()+"\" , "+" \"value\" : \""+Matrix.d.second.toString()+"\"  },");


        json = json.concat(" \"tx\" : {");
        json = json.concat(" \"key\" : \""+Matrix.tx.first.toString()+"\" , "+" \"value\" : \""+Matrix.tx.second.toString()+"\"  },");

        json = json.concat(" \"ty\" : {");
        json = json.concat(" \"key\" : \""+Matrix.ty.first.toString()+"\" , "+" \"value\" : \""+Matrix.ty.second.toString()+"\"  },");





        json = json.concat(" \"MidX\" : {");
        json = json.concat(" \"key\" : \""+CircularPath.MidX.first.toString()+"\" , "+" \"value\" : \""+CircularPath.MidX.second.toString()+"\"  },");



        json = json.concat(" \"MidY\" : {");
        json = json.concat(" \"key\" : \""+CircularPath.MidY.first.toString()+"\" , "+" \"value\" : \""+CircularPath.MidY.second.toString()+"\"  },");



        json = json.concat(" \"StartAngle\" : {");
        json = json.concat(" \"key\" : \""+CircularPath.StartAngle.first.toString()+"\" , "+" \"value\" : \""+CircularPath.StartAngle.second.toString()+"\"  },");



        json = json.concat(" \"EndAngle\" : {");
        json = json.concat(" \"key\" : \""+CircularPath.EndAngle.first.toString()+"\" , "+" \"value\" : \""+CircularPath.EndAngle.second.toString()+"\"  },");


        json = json.concat(" \"Radius\" : {");
        json = json.concat(" \"key\" : \""+CircularPath.Radius.first+"\" , "+" \"value\" : \""+CircularPath.Radius.second+"\"  },");

        String direction = (CircularPath.Direction.first.toString());

        json = json.concat(" \"Direction\" : {");
        json = json.concat(" \"key\" : \""+direction+"\" , "+" \"value\" : \""+CircularPath.Direction.second.toString()+"\"  },");









        json = json.concat(" \"opacity\" : {");
        json = json.concat(" \"key\" : \""+opacity.first.toString()+"\" , "+" \"value\" : \""+opacity.second.toString()+"\"  },");


        json = json.concat(" \"centreX\" : {");
        json = json.concat(" \"key\" : \""+centreX.first.toString()+"\" , "+" \"value\" : \""+centreX.second.toString()+"\"  },");


        json = json.concat(" \"centreY\" : {");
        json = json.concat(" \"key\" : \""+centreY.first.toString()+"\" , "+" \"value\" : \""+centreY.second.toString()+"\"  },");


        json = json.concat(" \"AnchorX\" : {");
        json = json.concat(" \"key\" : \""+AnchorX.first.toString()+"\" , "+" \"value\" : \""+AnchorX.second.toString()+"\"  },");


        json = json.concat(" \"AnchorY\" : {");
        json = json.concat(" \"key\" : \""+AnchorY.first.toString()+"\" , "+" \"value\" : \""+AnchorY.second.toString()+"\"  }");


        json = json.concat(" }");

        json = json.concat("}");

        return json;

    }


    public ImageAnimationState(){}

    public ImageAnimationState(AnimationObject _TargetObject,
                        Pair<Double, Boolean> _a,
                        Pair<Double, Boolean> _b,
                        Pair<Double, Boolean> _c,
                        Pair<Double, Boolean> _d,
                        Pair<Double, Boolean> _tx,
                        Pair<Double, Boolean> _ty,
                        Pair<Double, Boolean> _opacity,
                        Pair<Double, Boolean> _centreX,
                        Pair<Double, Boolean> _centreY,
                        Pair<Double, Boolean> _anchorX,
                        Pair<Double, Boolean> _anchorY
    )
    {
        TargetObject = _TargetObject;
        Matrix.a = _a;
        Matrix.b = _b;
        Matrix.c = _c;
        Matrix.d = _d;
        Matrix.tx = _tx;
        Matrix.ty = _ty;
        opacity = _opacity;
        centreX = _centreX;
        centreY = _centreY;
        AnchorX = _anchorX;
        AnchorY = _anchorY;
    }

}



