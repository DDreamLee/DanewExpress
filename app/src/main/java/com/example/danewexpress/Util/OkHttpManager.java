package com.example.danewexpress.Util;
import android.util.Log;
import com.example.danewexpress.DataObject.Express;
import com.example.danewexpress.DataObject.Pack;
import com.example.danewexpress.DataObject.User;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpManager {

    /*  sendPostRequestOKHttp：根据传入的对象类型动态确定具体调用的更新数据库方法   */

    static void sendPostRequestOKHttp(User u) {
        String url = "http://94.191.56.82/user/update";//资源定位符，下同
        try {
                    OkHttpClient client = new OkHttpClient();//OkHttp客户端对象
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("Userid",u.getUserid())
                            .addFormDataPart("UserName", u.getUsername())
                            .addFormDataPart("Password", u.getPassword())
                            .addFormDataPart("Phone", u.getPhonenum())
                            .addFormDataPart("Address",u.getAddress())
                            .addFormDataPart("Authority",u.getAuthority())
                            .build();//请求体
                    Request request = new Request.Builder()
                            .url(url)
                            .build();//请求对象
                    Response response = client.newCall(request).execute();//客户机发送请求并获得响应
                    String responseData = response.body().string();//获得响应的字符串形式
                    Log.i("Update Users","status: success");
                }catch (Exception e){
                    e.printStackTrace();
                }

        }

    static void sendPostRequestOKHttp(Express ex) {
        String url = "http://94.191.56.82/express/update";
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("ExpressPackageId",ex.getExpressPackageId())
                    .addFormDataPart("NowAddress", ex.getNowAddress())
                    .addFormDataPart("Timestamp", ex.getTimestamp())
                    .addFormDataPart("ExpressmanId", ex.getExpressman().getUserid())
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            Log.i("Update Express","status: success");
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    static void sendPostRequestOKHttp(Pack p) {
        String url = "http://94.191.56.82/package/update";
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("PackageId",p.getPackageId())
                    .addFormDataPart("SenderPhone", p.getSenderPhone())
                    .addFormDataPart("SenderAddress", p.getSenderAddress())
                    .addFormDataPart("ReceiverPhone", p.getReceiverPhone())
                    .addFormDataPart("ReceiverAddress", p.getReceiverAddress())
                    .addFormDataPart("Expenses", p.getExpenses())
                    .addFormDataPart("PayerPhone", p.getPayerPhone())
                    .addFormDataPart("PaymentStatus", p.getPaymentStatus())
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            Log.i("Update Pack","status: success");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /*  sendDeleteUserRequestOKHttp：根据userId删除服务器上的User数据条目   */
    static void sendDeleteUserRequestOKHttp(String userid){
        String url = "http://94.191.56.82/user/delete";
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("UserId",userid)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            Log.i("Update Pack","status: success");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*  sendDeletePackRequestOKHttp：根据packId删除服务器上的Pack数据条目   */
    static void sendDeletePackRequestOKHttp(String packid){
        String url = "http://94.191.56.82/package/delete";
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("PackId",packid)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            Log.i("Update Pack","status: success");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*  sendDeleteExpressRequestOKHttp：根据expressId删除服务器上的Express数据条目   */
    static void sendDeleteExpressRequestOKHttp(String expressId){
        String url = "http://94.191.56.82/express/delete";
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("ExpressId",expressId)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            Log.i("Update Pack","status: success");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*  sendSearchUserRequestOKHttp：根据userid查询服务器上的User数据条目   */
    static String sendSearchUserRequestOKHttp(String userid){
        String url = "http://94.191.56.82/user/search";
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("UserId",userid)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();//以字符串形式返回
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /*  sendSearchPackRequestOKHttp：根据packid查询服务器上的Pack数据条目   */
    static String sendSearchPackRequestOKHttp(String packid){
        String url = "http://94.191.56.82/package/search";
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("PackId",packid)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();//以字符串形式返回
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /*  sendSearchExpressRequestOKHttp：根据expressid查询服务器上的Express数据条目 */
    static String sendSearchExpressRequestOKHttp(String expressid){
        String url = "http://94.191.56.82/package/search";
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("ExpressId",expressid)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();//以字符串形式返回
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

