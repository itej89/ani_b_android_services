package fm.ani.anib;

public class fmapp extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationLifecycleHandler handler = new ApplicationLifecycleHandler();
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);

    }

}
