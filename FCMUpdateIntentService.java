package app.itsherstorebuyer.fcmClasses;

import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.annotation.Nullable;
import app.itsherstorebuyer.activities.GlobalActivity;
import app.itsherstorebuyer.utils.ApisClass;
import app.itsherstorebuyer.utils.ConnectionDetector;
import app.itsherstorebuyer.utils.SessionManagement;
import app.itsherstorebuyer.utils.Utils;

public class FCMUpdateIntentService extends IntentService {
    private static final String TAG="FCMUpdateIntentService";
    private String fcmToken="";
    private SessionManagement sessionManagement;
    private ConnectionDetector objConnectionDetector;
    private Dialog dialog=null;
    public FCMUpdateIntentService() {
        super(TAG);

    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
       /* fcmToken = SharedPrefs.getString(SharedPrefs.PREF_KEY.U_FCMTOKEN);
        if(!fcmToken.trim().isEmpty()&&!SharedPrefs.getString(SharedPrefs.PREF_KEY.USER_ID).trim().isEmpty()) {
            sendRegistrationToServer();
        }*/
        sessionManagement = new SessionManagement(getApplicationContext());
        sessionManagement.saveFCMToken(refreshedToken);
        Utils.printValue("Token","==Fcm ===="+refreshedToken);
        ApisClass.IS_FCMID_INTENT_SERVICE_RUNNING="1";
        if(sessionManagement.isLgin().trim().equalsIgnoreCase("1")) {

            sendRegistrationToServer(refreshedToken);
        }
        else
            {

            ApisClass.IS_FCMID_INTENT_SERVICE_RUNNING="0";
            stopSelf();
        }
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
                                Utils.printValue("GCMID update", "Updated successfully");
                                sessionManagement.sendFCMToken("true");
                                stopSelf();
                                //  Utils.showToast(getApplicationContext(), "Email verification link sent successfully");
                            } else {
                                sessionManagement.sendFCMToken("");
                            }
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                            sessionManagement.sendFCMToken("");
                            Utils.showToast(getApplicationContext(), "FCM ID :"+jsonException.toString());
                            // NotificationUtils.dismissProgressDialog();
                            // Utils.showToast(getApplicationContext(), "Server could not respond");
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

    private void startGCMIDService()
    {
        Intent intent_gcm = new Intent(this, FCMUpdateIntentService.class);
        startService(intent_gcm);
    }
}
