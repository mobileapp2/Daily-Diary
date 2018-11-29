package in.oriange.dailydiary.utilities;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebServiceCalls {

    public static String JSONAPICall(String methodName, String jsonAnsString) {
        String serverResponse = "[]";
        try {

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build();

            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, jsonAnsString);
            Request request = new Request.Builder()
                    .url(ApplicationConstants.PARENTAPI + "" + methodName)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            serverResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

    public static String FORMDATAAPICall(String methodName, List<ParamsPojo> list) {
        String serverResponse = "[]";
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(ApplicationConstants.PARENTAPI + "" + methodName).newBuilder();

            for (int i = 0; i < list.size(); i++)
                urlBuilder.addQueryParameter(list.get(i).getParam_Key(),
                        list.get(i).getParam_Value());

            String url = urlBuilder.build().toString();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build();

            ;
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            serverResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

}
