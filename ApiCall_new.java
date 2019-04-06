package app.networking;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import app.interfaces_pkg.ApiResponseListener;
import app.material_demo.GlobalApp;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin12 on 8/4/16.
 */
public class ApiCall_new {


    public static ApiCall_new apicall;
   // private int DEFAULT_TIMEOUT_MS = 5000;
    private int DEFAULT_TIMEOUT_MS = 60000;//30 seconds
    private int DEFAULT_MAX_RETRIES = 1;

    public static ApiCall_new getInstance()
    {
        if (apicall == null)
        {
            apicall = new ApiCall_new();
        }
        return apicall;
    }

    public ApiCall_new() {
    }
    //==============POST==================
  //  public void makePostRequest(final Context context, String url, final HashMap<String, String> params, final ApiResponseListener apiResponseListener, String tag, boolean fromUrlBuilder, final int requestCode, final Dialog dialog, final String baseUrlType) {
    public void makePostRequest(final Context context, String url, final HashMap<String, String> params,
                                final ApiResponseListener apiResponseListener,
                                String tag, boolean fromUrlBuilder, final int requestCode, final Dialog dialog) {

       // url = ApiUrls.urlBuilder(url, params,fromUrlBuilder,baseUrlType);
        url = ApiUrls.urlBuilder(url, params,fromUrlBuilder);
       Log.e("url","====="+url);



JsonObjectRequest obj=new JsonObjectRequest(Request.Method.POST, url,null, new Response.Listener<JSONObject>() {
    @Override
    public void onResponse(JSONObject response) {
        if(dialog!=null&&dialog.isShowing())
        {
            dialog.dismiss();
        }

        apiResponseListener.onResponse(response.toString(), requestCode);
    }
}, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialog != null && dialog.isShowing())
        {
            dialog.dismiss();

        }
        //      apiResponseListener.onError(error.toString(), requestCode);
        if (error instanceof NetworkError)
        {
            Toast.makeText(context, "Network error.Please Try again", Toast.LENGTH_SHORT).show();
            return;

        }
        else if (error instanceof ServerError)
        {
            Toast.makeText(context, "Server not found", Toast.LENGTH_SHORT).show();
            return;

        }
        else if (error instanceof NoConnectionError)
        {
            Toast.makeText(context, "Internet connection not found", Toast.LENGTH_SHORT).show();
            return;

        }
        else if (error instanceof TimeoutError)
        {
            Toast.makeText(context, " Network connection is slow. Please try with other network.", Toast.LENGTH_SHORT).show();
            return;

        }
        else
        {
            Toast.makeText(context, "Internet connection not found", Toast.LENGTH_SHORT).show();
            //      apiResponseListener.onError(error.toString(), requestCode);
        }
    }
})


        {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //Implement according to Project

              //  params.put("Content-Type", "application/x-www-form-urlencoded");
//                params.put(GlubbrConstants.API_KEY, GlubbrSharedPrefs.getString(GlubbrSharedPrefs.PREF_KEY.API_KEY));
              //  params.put(ChangeAbhiConstants.API_KEY, "5afd466f9cb37dd6082728fd29cfdd95");
              //  params.put(ChangeAbhiConstants.AUTHORIZATION, "Basic Y2hAbmdlQGJoITpjaEBuJiM=");
                return params;
            }
        };
        obj.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        GlobalApp.getInstance().addToRequestQueue(obj, tag);
    }
}
