package c.demo.ecommerce_shopping.activities_classes;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import c.demo.ecommerce_shopping.utilities.SharedPrefs;
import io.fabric.sdk.android.Fabric;
import java.util.HashMap;

import c.demo.ecommerce_shopping.utilities.Utils;

public class GlobalActivity extends Application {
    private static GlobalActivity appInstance;
    public static final String TAG = GlobalActivity.class.getSimpleName();
    private RequestQueue mRequestQueue;
    public static   FirebaseAnalytics fa=null;
    private AppEventsLogger logger;
    private static boolean activityVisible;
    private static Context appContext;
    /*
     * 1 minute=60000 ms
     * 1 hr=60 minutes=60* 60000=3600000 ms
     *
     *
     * */

    public static Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context mAppContext) {
        this.appContext = mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
       // Fabric.with(this, new Crashlytics());
      //  MultiDex.install(this);
        appInstance=this;
        setAppContext(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        /*
         *How to tell AAPT that we are using <vector> for Pre-lollipop versions.
         * */
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        AppEventsLogger.activateApp(this);
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        logger = AppEventsLogger.newLogger(getApplicationContext());

        /*
         * Firebase Analytics
         * */
// Create an instance of FirebaseAnalytics
        if (fa == null)
        {fa = FirebaseAnalytics.getInstance(getApplicationContext());}
        /*======Remote Config==============*/
      /*  final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        // set in-app defaults
        java.util.Map<String, Object> remoteConfigDefaults = new HashMap();

        remoteConfigDefaults.put(ForceUpdateChecker.KEY_BETA_VERSION_FLAG, true);
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "0.0.5");
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_BETA_VERSION, "0.0.5");

        firebaseRemoteConfig.fetch(60) // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Utils.printValue(TAG, "remote config is fetched.");
                            firebaseRemoteConfig.activateFetched();
                        }
                        else{
                            Utils.printValue(TAG, "remote config is Error.");
                        }
                    }
                });*/
        /*=========================================*/
    }
    public AppEventsLogger getAppEventsLogger() {
        if (logger == null) {
            logger = AppEventsLogger.newLogger(getApplicationContext());
        }
        return  logger;
    }
    public static Bundle  FirebaseAnalytic_GoogleAnalytic(){

// Create a Bundle containing information about
// the analytics event
        Bundle eventDetails = new Bundle();
        if(SharedPrefs.getString(SharedPrefs.PREF_KEY.IS_LOGIN).trim().equalsIgnoreCase("1"))
        {
            eventDetails.putString("buyer_id", SharedPrefs.getString(SharedPrefs.PREF_KEY.BUYER_ID).trim());
        }
        else{
            eventDetails.putString("buyer_id", "User is not logined");
        }
        return eventDetails;

    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null)
        {
            // mRequestQueue = Volley.newRequestQueue(getApplicationContext());

            Cache cache = new DiskBasedCache(appInstance.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }

        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {

        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }



    public static synchronized GlobalActivity getInstance() {
        return appInstance;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }



}
