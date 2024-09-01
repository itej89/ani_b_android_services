package com.permissioneverywhere;

import android.annotation.DrawableRes;
import android.content.Context;

/**
 * Created by Farruxx on 30.04.2016.
 */
public class PermissionEverywhere {
    public static PermissionRequest getPermission(Context context,
                                                  String[] permissions,
                                                  int requestCode,
                                                  String notificationTitle,
                                                  String notificationText,
                                                  @DrawableRes int notificationIcon){
        return new PermissionRequest(context, permissions, requestCode,
                notificationTitle, notificationText, notificationIcon);
    }
}
