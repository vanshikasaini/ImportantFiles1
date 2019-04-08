package c.demo.ecommerce_shopping.network_classes;

import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import c.demo.ecommerce_shopping.utilities.Utils;

/**
 * Created by admin12 on 8/4/16.
 */
public class ApiUrls {

    public final static String PROFILE_FROM_HOME = "home";
    public final static String PROFILE_FROM_CHECKOUT = "new_Address";
    public final static String PROFILE_EDIT_ADDRESS = "edit_Address";
    public final static String APP_SCEME = "http";
   public final static String BUYER_UPLOAD_URL = "http://mobileapiV13.dealsdray.com/fos_attendance.aspx";
  // public final static String BUYER_UPLOAD_URL = "http://dealsdraymapiV12.cstechns.com/fos_attendance.aspx";

    public static final String URL_GET_TRACK_ORDERS = "https://www.pricewar.com.my/apptrackorderstatus/";



  public final static String BASE_URL = "coreb2cmrtapiv9.retaxis.com/buyer.svc";//Demo
  //public final static String BASE_URL = "mrtapi.itsherstore.com/buyer.svc";//Demo
  public final static String STATIC_PAGE_URL = "http://www.itsherstore.com/";//Demo
 // public final static String STATIC_PAGE_URL = "http://pricewar.retaxis.net/";//Demo
 // public final static String BASE_URL = "pricewarmrtapi.retaxis.net/buyer.svc";//Demo
   public final static String BASE_URL2 = "mobileapiV13.dealsdray.com/";//Live

 //   public final static String BASE_URL = "dealsdraymapiV12.cstechns.com/fos.svc";//Live
   // public final static String BASE_URL2 = "dealsdraymapiV12.cstechns.com";//Live
    public static enum API_ADDRESS {
GET_ORDER_DETAIL("GetOrderDetails"),FORGORT_PASSWORD("ForgotPassword"),
        ADD_QUESTION("AddQuestion"),ASK_SELLER_QUES("Send_message"),GET_REVIEW_LIST("GetSellerReviews"),
        GET_MESSAGES_LIST("GetMessageList"),GET_CHAT_LIST("GetConversation"),URL_CHAT_REPLY("SendMessagetoSeller"),
CHECK_PINCODE("CheckPincodeAtCheckout"),PINCODE_CHECK_PRODETAIL_SCREEN("PostCodeCheck"),ADD_ANSWER("AddAnswer"),
        CREATE_ORDER("CreateOrder"),DELETE_SHIPPING_ADD("DeleteShippingAddress"),RESEND_ORDER_OTP("ResendCodCode"),VERIFY_OTP("VerifyCod"),GET_ZIPCODE("GetPincodeDetail"),
        LOGIN("fos_login"),CART_ITEM_LIST("DisplayCart"),SUBMIT_REVIEW("Rating"),GET_COUNTRIES("fetchCountryList"),
     INSERT_SHIPPING_ADDRESS("InsertShipingAddress"),COUPAN_API("GetCouponAmount"),UPDATE_PROFILE("Updateprofile"),
     DELETE_CART_ITEM("DeleteItemfromCart"),GET_SHIPPING_ADDRESS("GetShippingAddress"),GET_SHIPPING_CHARGES("GetShipping"),
     ADD_TO_BAG("AddToCart"),FB_LOGIN("facebooklogin"),CHANGE_PASSWORD("ChangePassword"),GET_FILTER("FilterData"),
     WISHLIST("DisplayWishlist"),DELETE_WISHLIST("DeleteItemfromWishlist"),SHAREWISHLIST("sharewishlist"),UPDATE_CART_ITEM("UpdateInCart"),
     RESEND_OTP("ForgetPassword_otp"),OTP_SEND("fos_OTP_login"), RESETPASSWORD("ChangePassword"),STORECREDITS("StoreCreditAmount"),
        USER_REGISTRATION("BuyerRegistration"),CHANGEPASSWORD("ChangePassword"),HOMEPRODUCTS("Homepageproduct"),
       USER_LOGIN("BuyerLogin"),MARKVISITS_ALREADY_EXISTS("markvisits_comment"),GETCATEGORIES("GetRootcategory"),
     GET_EOD_DETAILS("fos_eod_detail"),UPDATE_GCM_ID("UpdateGcmValue"),ORDERLIST("GetOrderList"),GET_PRODUCT_LIST("GetProductListfulltext"),
         U_UPDATE_LATLONG("fos_activities"),INNER_CATEGORY("InnerCategory"),GETPRODUCT_BYCAT("GetProductListby_cat"),GET_LAT_LONG("Getlat_lang")
        ,U_BANNER("GetFOS_Banners"),U_ATTENDENCE("Getattendancedetail"),GET_PRODUCT_INFO("GetProductInfo"),GET_VISIT_DETAIL("View_visits_by_date"),
        U_SMS_EMAIL("Sendinvitemailmsg"),GET_ALL_PRODUCTS("GetAllProduct"),SEARCH_LIST("GetSearchProductList"),GET_CURRENCY_LIST("GetCurrencyAmount")
        ,GET_CATEGORIES("GetCategories"),GET_HOME_DATA("Homepageproduct"),ADD_TOWISHLIST("AddToWishlist"),SUBSCRIBE_API("newsletterragistration");
        public final String path;
//http://dealsdraymapiV12.cstechns.com/fos.svc/View_visits_by_date?buyerid=team-2996E660-E714-4A26-B981-173DE9B5156C&date=06/07/2018&pageno=1
        API_ADDRESS(String path) {
            this.path = path;
        }
    }
    public static String urlBuilder(String createPath, Map<String, String> params,boolean flag)
    {
        //http://thegreenfarmsmobileapis.cstechns.com/buyer.svc/BuyerRegistration?name={name}&email={email}&mobile={mobile}
        // &password={password}&city={city}&community={community}
        // &address1={address1}&address2={address2}&state={state}&pincode={pincode}
        Uri.Builder builder = new Uri.Builder();

        builder.scheme(APP_SCEME).encodedAuthority(BASE_URL).path(createPath);

        if(flag==true)
        {
           /* for(int j=0;j<params.size();j++)
            {
                Log.e("jj"+j,"==="+params.get());
            }*/

            if (params != null) {
                for (String key : params.keySet()) {
                    if (params.get(key) != null) {
                        Utils.printValue("If part", "===" + params.get(key));
                        builder.appendQueryParameter(key, params.get(key));
                    } else {
                        //   builder.appendPath("");
                        Utils.printValue("else part", "===");

                        // throw new IllegalArgumentException(
                        // "params must not contain any null values");
                    }
                }
            }
        }
        else{
            Utils.printValue("builderString","==="+builder.build().toString());
        }
/*
* NOTE: this will encode url parameters,we got error in email shows into encoded form
* ex: vanshika@cstech.in ===> vanshika%24cstech.in  to not get that
* we consider next method
*
* */
       // String builderString = builder.build().toString().trim();
        /*====2nd method====*/
        String builderString = null;
        try {
            builderString = URLDecoder.decode(builder.build().toString(), "UTF-8");
            Utils.printValue("values","==="+builderString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return builderString;
    }
   /* public static String urlBuilder(String createPath, Map<String, String> params, boolean flag, final String baseUrlType)
    {

        Uri.Builder builder = new Uri.Builder();
if(baseUrlType.trim().equalsIgnoreCase("1")){
        builder.scheme(APP_SCEME).encodedAuthority(BASE_URL).path(createPath);}
        else {
    builder.scheme(APP_SCEME).encodedAuthority(BASE_URL2).path(createPath);
}

        if(flag==true)
        {


            if (params != null) {
                for (String key : params.keySet()) {
                    if (params.get(key) != null) {
                        Utils.printValue("If part", "===" + params.get(key));
                        builder.appendQueryParameter(key, params.get(key));
                    } else {
                     //   builder.appendPath("");
                        Utils.printValue("else part", "===");

                        // throw new IllegalArgumentException(
                        // "params must not contain any null values");
                    }
                }
            }
        }
        else{
            Utils.printValue("builderString","==="+builder.build().toString());
        }

        String builderString = builder.build().toString().trim();
        return builderString;
    }
*/
}
