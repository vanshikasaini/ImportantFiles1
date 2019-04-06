package app.itsherstorebuyer.fcmClasses;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
//import android.support.v4.app.NotificationCompat;
//mport android.support.v4.content.LocalBroadcastManager;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import app.itsherstorebuyer.R;
import app.itsherstorebuyer.activities.GlobalActivity;
import app.itsherstorebuyer.activities.NotificationActivity;
import app.itsherstorebuyer.modelClasses.ModelNotificationsDetails;
import app.itsherstorebuyer.utils.SessionManagement;
import app.itsherstorebuyer.utils.IHW_DB;
import app.itsherstorebuyer.utils.Utils;


/**
 * Created by vanshika on 2/5/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

private SessionManagement sessionManagement;
    private IHW_DB db;
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
          Utils.printValue(TAG, "Msg data payload From: " + remoteMessage.getFrom());
        db=new IHW_DB(getApplicationContext());
        Utils.printValue(TAG, "Message data payload: " + remoteMessage.getData());
        /*Utils.printValue("test", "--- " + remoteMessage.toString());
        Utils.printValue("getNotification->", remoteMessage.getNotification().getBody());
        Utils.printValue(TAG, "Notification Message TITLE: " + remoteMessage.getNotification().getTitle());
        sendCustomNotification("titla "+remoteMessage.getNotification().getBody().toString(),"message","");*/
        if (remoteMessage.getNotification() != null) {
            Utils.printValue("getNotification->", remoteMessage.getNotification().getBody());
        }
        sessionManagement = new SessionManagement(getApplicationContext());
      /*Map<String,String> hash_Map=remoteMessage.getData();
        ArrayMap<String,String> hashMap=(ArrayMap<String, String>) remoteMessage.getData();*/

       /* Set set = hashMap.entrySet();
        Iterator iterator = set.iterator();

      //Iterate over HashMap
        while(iterator.hasNext()) {
            Map.Entry mEntry = (Map.Entry)iterator.next();
            String key = mEntry.getKey();
            String value = mEntry.getValue();
        }*/


      /*for (int i = 0; i < hashMap.size(); i++) {
            String key = hashMap.keyAt(i);
            String value = hashMap.valueAt(i);
            Utils.printValue("Key","==="+key);
            Utils.printValue("value","==="+value);
        }*/

       /* Utils.printValue(TAG, "Msg data payload From: " + remoteMessage.getFrom());
        Utils.printValue(TAG, "Msg data payload getBody: " + remoteMessage.getNotification().getBody());
        Utils.printValue(TAG, "Msg Title: " + remoteMessage.getNotification().getTitle());
        Utils.printValue(TAG, "Message data payload: " + remoteMessage.getData());
        Utils.printValue(TAG, "Message data payload: " + remoteMessage.toString());
        Utils.printValue(TAG, "Message data payload: " + remoteMessage.getData().toString());
        Utils.printValue("test","--- "+remoteMessage.toString());*/

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Utils.printValue(TAG, "Message data payload: " + remoteMessage.getData());
            try {
                JSONObject jsonObject = new JSONObject(remoteMessage.getData());
                Utils.printValue("Json Format", "====" + jsonObject.toString());
/*
* {NotificationID=bf5c3c78-dafb-4280-a10f-6340c343942a,
* time=12/20/2018 5:02:56 AM,
* type=Buyer,
 * image=https://itsherstore.com/waterthumb.aspx?name=eodscreen.jpg&foldername=Notification/6/,
* message=Test Title for u_Test description}
*
*
*
* */

                checkAppSettingSwitch(jsonObject);
                Utils.printValue(TAG, "If" + remoteMessage.getData());
                Utils.printValue(TAG, "else" + remoteMessage.getData());


            } catch (Exception e) {
                Utils.printValue(TAG, "Exception: " + e.getMessage());
            }

        }

    }

    private void checkAppSettingSwitch(JSONObject jsonObject) {
        //{"proname":"Cotton Semi-stitched Dress Material ",
        // "amt":"1389.00",
        // "head":"Cotton Semi-stitched Dress Material",
        // "text":"available in Addwik App  at Rs 1389.00",
        // "time":"5\/2\/2017 4:50:37 PM",
        // "image":"http:\/\/addwik.cstechns.com\/storefiles\/productfiles\/BMP-S1402287\/SEF_WS-95\/SEF_WS-95.jpg",
        // "proid":"SEF_WS-95",
        // "requestid":""}
      //  {NotificationID=8874445a-5488-4d73-8028-81a868b7e8b9,
        // time=6/25/2018 1:33:55 PM, type=Buyer, image=,
        // message=testing25 heading_tesing conrtennnn}
        String type = jsonObject.optString("type");

        String headStr = "", subHeadStr = "", imageUrl = "",NotificationID="",dateStr="";
        imageUrl = jsonObject.optString("image");
        NotificationID=jsonObject.optString("NotificationID");
        String timeStr[]=jsonObject.optString("time").trim().split("\\s+");
        dateStr=timeStr[0];
        Utils.printValue("imageUrl", "=imageUrl==" + imageUrl);
        //if (type.equalsIgnoreCase("Image")) {
        if (!imageUrl.trim().isEmpty()) {
            String[] msgArray=jsonObject.optString("message").split("_");
            if(msgArray.length==2){
            headStr = msgArray[0];
            subHeadStr = msgArray[1];}
                else{
                headStr = msgArray[0];
                subHeadStr ="";
            }
            imageUrl = jsonObject.optString("image");

        } else {

            String[] msgArray=jsonObject.optString("message").split("_");
            headStr = msgArray[0];
            subHeadStr = msgArray[1];

        }


        Utils.printValue("Heading", "===" + headStr);
        Utils.printValue("Text", "===" + subHeadStr);
        Utils.printValue("dateStr", "=dateStr==" + dateStr);


        if (db.getContactsCount() == 50)
        {
            db.deleteFirstRow();
            int count=sessionManagement.getNotificationCount();
            if(count==0){}
            else{--count;}
            sessionManagement.saveNotifcationCount(count);




            sessionManagement.saveNotifcationCount(++count);
        }
        else {
            Utils.printValue(TAG, "Addvalue: value"+sessionManagement.getNotificationCount());
            int tempCount=sessionManagement.getNotificationCount();
            ++tempCount;
            sessionManagement.saveNotifcationCount(tempCount);


          /*  try {


                db.addContact(new ModelNotificationsDetails(jsonObject.optString("NotificationID"),textStr.trim(),headingStr.trim(),jsonObject.optString("image"),jsonObject.optString("time")));
            } catch (Exception e) {
                Utils.printValue("Exception","===="+e.toString());
            }*/
            Utils.printValue(TAG, "Count: value" + db.getContactsCount());

        }

        ModelNotificationsDetails modelObject=new ModelNotificationsDetails(NotificationID,subHeadStr,headStr,imageUrl,dateStr);
        db.addContact(modelObject);
        //if App is in foregound

        if (!isAppIsInBackground(this)) {
            sendBroadcastNotificationScreen();
            sendBroadcastNotificationCount();
        }

        sendCustomNotification(headStr.trim(), subHeadStr.trim(), imageUrl);

    }

    /*
        ======================================
    */
    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
   /*========Show Notifications===========*/

    public void sendBroadcastOpenNotificationScreen() {
        Intent intent = new Intent("notification_Frag");
        Utils.printValue("Notification Screen", "====Open=====");

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendCustomNotification(String title, String message, String imageUrl) {

        //Using RemoteViews to bind custom layouts into Notification
        RemoteViews remoteViews = null;
        Utils.printValue("Notificationss","==Enterted=========");

        //Open NotificationView Class on Notification Click
        //the intent that is started when the notification is clicked (works)

        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("from", "FcmMessage");

     /*FragmentManager fm = this.getFragmentManager();
        NotificationsFrag notificationDialogFragment = NotificationsFrag.newInstance();
        //notificationDialogFragment.setTargetFragment(HomeActivity.class, 1001);
        notificationDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        notificationDialogFragment.show(fm, "Notifications_Frag");*/


        // mBundle.putString("count", count);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (imageUrl.trim().isEmpty()) {
            remoteViews = new RemoteViews(getPackageName(), R.layout.noti_promo_layout);

        } else {
            remoteViews = new RemoteViews(getPackageName(), R.layout.noti_image);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

       // remoteViews.setImageViewResource(R.id.img_notiicon,R.drawable.noti_white_icon);
        if (!imageUrl.trim().isEmpty()) {
            imageUrl = imageUrl.replaceAll(" ", "%20");
            Bitmap bitmap = getBitmapfromUrl(imageUrl);
            // remoteViews.setViewVisibility(R.id.img_noti,ViewGroup.VISIBLE);
            remoteViews.setImageViewBitmap(R.id.img_noti, bitmap);
            remoteViews.setViewVisibility(R.id.img_notiicon, ViewGroup.VISIBLE);
           // remoteViews.setTextViewText(R.id.tv_heading, title);
            remoteViews.setTextViewText(R.id.tv_heading, getString(R.string.emoji_text_view,title));

            remoteViews.setTextViewText(R.id.tv_content, message);
            Utils.printValue("----title--", "==IMMMM=" + title);
            Utils.printValue("----message--", "==IMMMM=" + message);
            notificationBuilder
                    .setSmallIcon(R.mipmap.icon)
                    // .setContent(remoteViews)
                    .setCustomBigContentView(remoteViews)
                    .setContentTitle("IHW message")//This show when normal view is displaying
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    // Set PendingIntent into Notification
                    .setContentIntent(pendingIntent);




        } else {
            remoteViews.setTextViewText(R.id.tv_heading, title);
            remoteViews.setTextViewText(R.id.tv_content, message);
            Utils.printValue("----title--", "==title=" + title);
            Utils.printValue("----message--", "==message=" + message);
            notificationBuilder
                    .setSmallIcon(R.mipmap.icon)
                    //  .setContent(remoteViews)
                    .setCustomBigContentView(remoteViews)
                    .setContentTitle("IHW message")//This show when normal view is displaying
                    // Dismiss Notification
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    // Set PendingIntent into Notification
                    .setContentIntent(pendingIntent);
           // remoteViews.setViewVisibility(R.id.img_notiicon, ViewGroup.VISIBLE);



        }



       /* * Code for Block
        *
*/
      /*  Intent switchIntent = new Intent(this, switchButtonListener.class);
        switchIntent.putExtra("notificationId",1);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 0,switchIntent, 0);


        remoteViews.setOnClickPendingIntent(R.id.ll_blockNotification,pendingSwitchIntent);*/




      /*  For out of Stock*/

        SimpleDateFormat formatt = new SimpleDateFormat("hh:mm");
        System.out.println(formatt.format(new Date()));
        Utils.printValue("----Splash--", "===" + formatt.format(new Date()));

        remoteViews.setTextViewText(R.id.tv_time, formatt.format(new Date()));
        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //notificationmanager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
      //  notificationmanager.notify(1, notificationBuilder.build());


        /*===============================================*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Utils.printValue("notificationss","===Above===lollipop version=========");
           // remoteViews.setImageViewResource(R.id.img_notiicon,R.drawable.noti_white_icon);
            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.icon);
            remoteViews.setImageViewBitmap(R.id.img_notiicon,icon);
            //  CharSequence name = getString(R.string.channel_name);
            CharSequence name = "Channel Name";
            String channelIdStr="1";
            //  String description = getString(R.string.channel_description);
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelIdStr, name, importance);
            //channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
          notificationBuilder
                    // .setSmallIcon(R.mipmap.app_logo)
                  //  .setSmallIcon(R.drawable.app_noti_w)
                    .setSmallIcon(R.drawable.noti_white_icon)
                    .setColor(Color.parseColor("#d71921"))
                    .setContentTitle("IHW message")

                    .setChannelId(channelIdStr);
            notificationmanager.createNotificationChannel(channel);
            notificationmanager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.icon);
            remoteViews.setImageViewBitmap(R.id.img_notiicon,icon);
            Utils.printValue("notificationss","======lollipop version=========");
       notificationBuilder

                   // .setSmallIcon(R.drawable.notification_white_icon)
                    .setSmallIcon(R.drawable.noti_white_icon)
                      .setCustomBigContentView(remoteViews)
                    .setContentTitle("IHW message");
                    /*.setContentText(txt)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(image)
                            .setSummaryText(txt))     *//*Notification with Image*//*
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX);
*/
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        }else {
            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.icon);
            remoteViews.setImageViewBitmap(R.id.img_notiicon,icon);

            Utils.printValue("notificationss","======Below lollipop version========");
            notificationBuilder.setSmallIcon(R.mipmap.icon)
                    //.setSmallIcon(R.drawable.app_noti_w)
                    //.setColor(Color.parseColor("#d71921"))


                    .setCustomBigContentView(remoteViews)
                    .setContentTitle("IHW message")//This show when normal view is displaying



                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);



            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }


      //  notificationmanager.notify(1, notificationBuilder.build());

        /*=================================================*/
    }






    /* *To get a Bitmap image from the URL received
     * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

  /*  =======================================================*/

    public void sendBroadcastNotificationCount()
    {
        Intent intent = new Intent("updateNotificationCount");
        Utils.printValue("NotificationCount", "=========");

      /* int totalCount=SharedPrefs.getInt(SharedPrefs.PREF_KEY.U_NOTIFICATION_COUNT);
        totalCount+=totalCount;
        SharedPrefs.putInt(SharedPrefs.PREF_KEY.U_NOTIFICATION_COUNT,totalCount);*/

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    public void sendBroadcastNotificationScreen()
    {
        Intent intent = new Intent("updateNotificationList");
        Utils.printValue("Notification Screen", "=========");

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


  /*  =======================================================*/
    //this method will return true if the app is in background.

    private boolean isAppIsInBackground(Context context) {

        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            Utils.printValue(context.getApplicationInfo().getClass().getName() + "", "=============zxxxxxxxx================");

                            isInBackground = false;
                        }
                    }
                }
            }
        } else {


            if (GlobalActivity.isActivityVisible()) {
                isInBackground = false;
            }


            Utils.printValue("Below KitKat", "===================xzzzz==========");


        }

        return isInBackground;
    }
}
