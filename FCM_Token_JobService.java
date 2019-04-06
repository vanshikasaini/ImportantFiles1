package app.itsherstorebuyer.fcmClasses;

import android.app.Dialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.itsherstorebuyer.activities.GlobalActivity;
import app.itsherstorebuyer.utils.ApisClass;
import app.itsherstorebuyer.utils.SessionManagement;
import app.itsherstorebuyer.utils.Utils;

public class FCM_Token_JobService extends JobService  {
    private Dialog progressDialog=null;
    private JobParameters jobCurrent=null;
SessionManagement sessionManagement=null;
    private static final String TAG = "FCM_Token_JobService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Utils.printValue(TAG, "Performing long running task in scheduled job");
        // TODO(developer): add long running task here.
        jobCurrent=jobParameters;
        sessionManagement=new SessionManagement(getApplicationContext());
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        if (sessionManagement.isLgin().trim().equalsIgnoreCase("1")) {
            sendRegistrationToServer(refreshedToken);

        } else {
            jobFinished(jobCurrent,false);
            onStopJob(jobCurrent);

        }
        /*====Running on another method (long running operation)=======*/
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        /*======Do u want to run again if this updation stopped in middle======*/
        /* true means, we're not done, please reschedule */
        return true;
    }
    private void sendRegistrationToServer(String token) {

        HashMap<String, String> params = new HashMap<>();

        params.put("buyerid", token);
        //String url="";
        ////http://mrtapi.pricewar.com.my/buyer.svc/UpdateGcmValue?
        //    // Buyerid=Byr-51A99D77-A78A-4327-B613-851D088EF56E&Gcmid=111111111&devicetype=android
        String url = ApisClass.FCMTOKEN_NEWURL+"Buyerid="+sessionManagement.getBuyerId()+"&Gcmid="+sessionManagement.getFCMToken()+"&devicetype=android";

        Utils.printValue("GCMID update", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // NotificationUtils.dismissProgressDialog();

                        try {
                            Utils.printValue("GCMID update post->", jsonObject.toString());
                            int statusCode = Integer.parseInt(jsonObject.getString("StatusCode"));
                            if (statusCode == 1) {
                                sessionManagement.sendFCMToken("true");
                                Utils.printValue("GCMID update", "Updated successfully");
                                stopSelf();
                                //  Utils.showToast(getApplicationContext(), "Email verification link sent successfully");
                            } else {
                                new SessionManagement(getApplicationContext()).sendFCMToken("");

                            }
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                            Utils.showToast(getApplicationContext(), "FCM ID :"+jsonException.toString());
                            // NotificationUtils.dismissProgressDialog();
                            // Utils.showToast(getApplicationContext(), "Server could not respond");
                            sessionManagement.sendFCMToken("");

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                sessionManagement.sendFCMToken("");

                Utils.showToast(getApplicationContext(), "Server could not respond :FCM ID");
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                (int) ApisClass.SERVICE_TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GlobalActivity.getInstance().addToRequestQueue(jsonObjReq, "OTPActivity");

    }
}
