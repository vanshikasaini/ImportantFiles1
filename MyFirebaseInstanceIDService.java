package app.itsherstorebuyer.fcmClasses;

import android.content.Intent;
import android.os.Build;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import app.itsherstorebuyer.utils.SessionManagement;
import app.itsherstorebuyer.utils.Utils;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private FirebaseJobDispatcher mDispatcher;
    private static final String JOB_TAG = "FCM_Token_UpdateService_Login";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Utils.printValue("FCM Token","----"+refreshedToken);
        Utils.printValue("FCM Token","--FCCCCCCCCMMM--"+refreshedToken);

        SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
        if(refreshedToken!=null&&!refreshedToken.isEmpty()) {
            sessionManagement.saveFCMToken(refreshedToken);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {


                Intent intent_gcm = new Intent(this, FCMUpdateIntentService.class);
                startService(intent_gcm);
            }
            else{

                Utils.printValue("FCM Token","-oreo oreo oreo-FCCCCCCCCMMM--"+refreshedToken);
                mDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
                Job job1 = mDispatcher.newJobBuilder().setService(FCM_Token_JobService.class)
                        .setLifetime(Lifetime.FOREVER).setRecurring(false)
                        .setTag(JOB_TAG).setTrigger(Trigger.executionWindow(5, 5))//seconds
                        .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                        .setReplaceCurrent(false).setConstraints(Constraint.ON_ANY_NETWORK)

                        .build();


                mDispatcher.mustSchedule(job1);
            }

        }
    }
}
