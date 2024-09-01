//
//  AnimationActionCreator.swift
//  Ani_AnimationStudio
//
//  Created by Uday on 03/06/17.
//  Copyright © 2017 Ani. All rights reserved.
//


package Framework;


import android.animation.ObjectAnimator;
import android.animation.TimeAnimator;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

import Framework.DataTypes.CGAffineTransform;
import Framework.DataTypes.Delegates.AnimationActionCreatorConvey;
import Framework.DataTypes.GraphicAnimation;
import Framework.DataTypes.MatricesMathHelper;
import Framework.DataTypes.AnimationAction;
import Framework.DataTypes.AnimationObject;
import Framework.DataTypes.Constants.AnimationTypes;
import Framework.DataTypes.ImageAnimationState;
import Framework.DataTypes.TransformMatrixToValueConvertor;
import Framework.DataTypes.TransformValues;
import Framework.DataTypes.UnitConvertor;
import FrameworkInterface.MachinePositionContext;

public class AnimationActionCreator {
    
   public AnimationAction DefaultState = new AnimationAction();


    AnimationActionCreator()
    {
        DefaultState.Position.State.StateSet.put(AnimationObject.Image_EyeBrowRight,  new ImageAnimationState( AnimationObject.Image_EyeBrowRight, new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true), new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true),new Pair<>(0.0,false) , new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false)));
        
        DefaultState.Position.State.StateSet.put(AnimationObject.Image_EyeBrowLeft, new ImageAnimationState(AnimationObject.Image_EyeBrowRight, new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true), new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true),new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false)));
        
        DefaultState.Position.State.StateSet.put(AnimationObject.Image_EyeRight, new ImageAnimationState( AnimationObject.Image_EyeBrowRight, new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true), new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true),new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false)));
        
        DefaultState.Position.State.StateSet.put(AnimationObject.Image_EyeLeft, new ImageAnimationState(AnimationObject.Image_EyeBrowRight, new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true), new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true),new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false)));
        
        DefaultState.Position.State.StateSet.put(AnimationObject.Image_EyeBallRight, new ImageAnimationState( AnimationObject.Image_EyeBrowRight, new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true), new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true),new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false)));
        
        DefaultState.Position.State.StateSet.put(AnimationObject.Image_EyeBallLeft, new ImageAnimationState( AnimationObject.Image_EyeBrowRight, new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true), new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true),new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false)));
        
        DefaultState.Position.State.StateSet.put(AnimationObject.Image_EyePupilRight, new ImageAnimationState(AnimationObject.Image_EyeBrowRight, new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true), new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true),new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false)));
        
        DefaultState.Position.State.StateSet.put(AnimationObject.Image_EyePupilLeft, new ImageAnimationState( AnimationObject.Image_EyeBrowRight, new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true), new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true),new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false)));
        
        DefaultState.Position.State.StateSet.put(AnimationObject.Image_EyeLidLeft, new ImageAnimationState(AnimationObject.Image_EyeBrowRight, new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true), new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true),new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false)));
        
        DefaultState.Position.State.StateSet.put(AnimationObject.Image_EyeLidRight, new ImageAnimationState( AnimationObject.Image_EyeBrowRight, new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true), new Pair<>(1.0,true), new Pair<>(0.0,true), new Pair<>(0.0,true),new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false), new Pair<>(0.0,false)));
        
    }
    
    
   public void SetDefault(final View image, final AnimationActionCreatorConvey convey, final Integer Timing, final  Integer Delay) {

       final AnimationObject Tag = AnimationObject.fromInt(Integer.parseInt((String)image.getTag()));


       CGAffineTransform transform = new CGAffineTransform();

       transform.a = (((ImageAnimationState)DefaultState.Position.State.StateSet.get(Tag)).Matrix.a.first.floatValue());

       transform.b = (((ImageAnimationState)DefaultState.Position.State.StateSet.get(Tag)).Matrix.b.first.floatValue());

       transform.c = ((ImageAnimationState)DefaultState.Position.State.StateSet.get(Tag)).Matrix.c.first.floatValue();

       transform.d = ((ImageAnimationState)DefaultState.Position.State.StateSet.get(Tag)).Matrix.d.first.floatValue();

       transform.tx = ((ImageAnimationState)DefaultState.Position.State.StateSet.get(Tag)).Matrix.tx.first.floatValue();

       transform.ty = ((ImageAnimationState)DefaultState.Position.State.StateSet.get(Tag)).Matrix.ty.first.floatValue();



       final TransformValues transform2D = TransformMatrixToValueConvertor.GetValuesFromMatrix(transform);

       final float alphaParam = (((ImageAnimationState)DefaultState.Position.State.StateSet.get(Tag)).opacity.first.floatValue());

       new Handler(Looper.getMainLooper()).post(new Runnable() {
           @Override
           public void run() {

               image.setPivotX((((ImageAnimationState)DefaultState.Position.State.StateSet.get(Tag)).AnchorX.first.floatValue()));

               image.setPivotY((((ImageAnimationState)DefaultState.Position.State.StateSet.get(Tag)).AnchorY.first.floatValue()));

               //Multiply by -1 for ios rotaion direction
               image.animate().rotation(UnitConvertor.RadiasToDegree(transform2D.RotationInRadians) * -1).scaleX(transform2D.ScaleX).scaleY(transform2D.ScaleY).translationX(transform2D.Tx).translationY(transform2D.Ty).setDuration(Timing).setStartDelay(Delay).setInterpolator(new DecelerateInterpolator()).alpha(alphaParam).withEndAction(new Runnable() {
                   @Override
                   public void run() {
                       if(convey != null)
                       {
                           convey.SetDefaultCompleted(Tag);
                       }
                   }
               });

           }
       });

//        image.center.x = CGFloat((DefaultState.Position.State.StateSet[AnimationObject(rawValue: image.tag)!] as! ImageAnimationState).centreX.keys.first!)
//
//        image.center.y = CGFloat((DefaultState.Position.State.StateSet[AnimationObject(rawValue: image.tag)!] as! ImageAnimationState).centreY.keys.first!)

    }
    
 public  void updateDefaultState(View view)
    {
        CGAffineTransform _2Dtransorm = new MatricesMathHelper().GetAffineFromAndroidMatrix(view.getMatrix());

         ((ImageAnimationState)DefaultState.Position.State.StateSet.get(AnimationObject.fromInt(Integer.parseInt((String)view.getTag())))).AnimationKind = AnimationTypes.Type.Tranformation;

        ((ImageAnimationState)DefaultState.Position.State.StateSet.get(AnimationObject.fromInt(Integer.parseInt((String)view.getTag())))).Matrix.a = new Pair<>(_2Dtransorm.a.doubleValue(), true);

        ((ImageAnimationState)DefaultState.Position.State.StateSet.get(AnimationObject.fromInt(Integer.parseInt((String)view.getTag())))).Matrix.b = new Pair<>(_2Dtransorm.b.doubleValue(), true);


        ((ImageAnimationState)DefaultState.Position.State.StateSet.get(AnimationObject.fromInt(Integer.parseInt((String)view.getTag())))).Matrix.c = new Pair<>(_2Dtransorm.c.doubleValue(), true);


        ((ImageAnimationState)DefaultState.Position.State.StateSet.get(AnimationObject.fromInt(Integer.parseInt((String)view.getTag())))).Matrix.d = new Pair<>(_2Dtransorm.d.doubleValue(), true);

        ((ImageAnimationState)DefaultState.Position.State.StateSet.get(AnimationObject.fromInt(Integer.parseInt((String)view.getTag())))).Matrix.tx = new Pair<>(_2Dtransorm.tx.doubleValue(), true);


        ((ImageAnimationState)DefaultState.Position.State.StateSet.get(AnimationObject.fromInt(Integer.parseInt((String)view.getTag())))).Matrix.ty = new Pair<>(_2Dtransorm.ty.doubleValue(), true);



        ((ImageAnimationState)DefaultState.Position.State.StateSet.get(AnimationObject.fromInt(Integer.parseInt((String)view.getTag())))).opacity = new Pair<>(Float.valueOf(view.getAlpha()).doubleValue(), true);
        
        
//        (DefaultState.Position.State.StateSet[AnimationObject(rawValue: view.tag)!] as! ImageAnimationState).centreX = [Double(view.center.x):true]
//
//
//        (DefaultState.Position.State.StateSet[AnimationObject(rawValue: view.tag)!] as! ImageAnimationState).centreY = [Double(view.center.y):true]
//

        ((ImageAnimationState)DefaultState.Position.State.StateSet.get(AnimationObject.fromInt(Integer.parseInt((String)view.getTag())))).AnchorX = new Pair<>(Float.valueOf(view.getPivotX()).doubleValue(), true);


        ((ImageAnimationState)DefaultState.Position.State.StateSet.get(AnimationObject.fromInt(Integer.parseInt((String)view.getTag())))).AnchorY = new Pair<>(Float.valueOf(view.getPivotY()).doubleValue(), true);
        
        MachinePositionContext.Instance.CurrentParameters.State.StateSet.put(AnimationObject.fromInt(Integer.parseInt((String)view.getTag())), ((ImageAnimationState)DefaultState.Position.State.StateSet.get(AnimationObject.fromInt(Integer.parseInt((String)view.getTag())))));
        
    }

  public  static AnimationActionCreator instance = new AnimationActionCreator();
    
}


