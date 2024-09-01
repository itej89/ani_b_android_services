#!/bin/bash

# CHANGE THESE FOR YOUR APP
app_package="fm.ani.kinetics"
dir_app_name="fm.ani.kinetics"
MAIN_ACTIVITY="MainActivity"

ADB="/home/tej/Documents/A.n.i_B/Android/Sdk/platform-tools/adb" # how you execute adb
ADB_SH="$ADB shell " # this script assumes using `adb root`. for `adb su` see `Caveats`
cd /home/tej/Documents/fmSpin/Ani/ClientApp/Android_Ani//Kinetics
path_sysapp="/system/priv-app" # assuming the app is priviledged
apk_host="./app/build/outputs/apk/debug/app-debug.apk"
apk_name="$dir_app_name.apk"
apk_target_dir="$path_sysapp/$dir_app_name"
apk_target_sys="$apk_target_dir/$apk_name"

#libSerialPath="/system/lib"
#libSerial="./libserial/src/main/jniLibs/armeabi-v7a/libserial.so"
echo  Delete previous APK
rm -f $apk_host


echo  Compile the APK: you can adapt this for production build, flavors, etc.
./gradlew assembleDebug || exit -1 # exit on failure


# Install APK: using adb su
# $ADB_SH "mount -o remount,rw /system"
# $ADB_SH "chmod 777 /system/lib/"
# $ADB_SH "mkdir -p /sdcard/tmp" 2> /dev/null
# $ADB_SH "mkdir -p $apk_target_dir" 2> /dev/null
# $ADB push $apk_host /sdcard/tmp/$apk_name 2> /dev/null
# $ADB_SH "mv /sdcard/tmp/$apk_name $apk_target_sys"
# $ADB_SH "rmdir /sdcard/tmp" 2> /dev/null


echo  Install APK: using adb root
$ADB root 2> /dev/null
$ADB remount # mount system
$ADB_SH "mkdir $apk_target_dir"
$ADB push $apk_host $apk_target_sys
#$ADB push $libSerial $libSerialPath

echo  Give permissions
$ADB_SH "chmod 755 $apk_target_dir"
$ADB_SH "chmod 644 $apk_target_sys"

echo Unmount system
$ADB_SH "mount -o remount,ro /"

#echo  Stop the app
$ADB shell "am force-stop $app_package"

#echo  Re execute the app
#$ADB shell "am start -n \"$app_package/.$MAIN_ACTIVITY\" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"

echo  execute service to give initial permission
$ADB shell "am start-foreground-service fm.ani.kinetics/fm.ani.kinetics.KineticsService"


#$ADB shell am set-debug-app $app_package
