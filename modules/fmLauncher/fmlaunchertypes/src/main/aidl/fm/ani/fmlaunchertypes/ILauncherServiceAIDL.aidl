// ILauncherServiceAIDL.aidl
package fm.ani.fmlaunchertypes;

// Declare any non-default types here with import statements
interface ILauncherServiceAIDL {
    void LaunchApp(String connectionID, String packageName);

   void SetPotrait();
}