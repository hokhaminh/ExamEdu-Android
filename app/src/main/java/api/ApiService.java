package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import models.AccessToken;
import models.LoginBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {


    @POST("Authentication/login")
    Call<AccessToken> login(@Body LoginBody body);

    @POST("Token/refresh")
    Call<AccessToken> refresh();

    @GET("Module")
    Call<JsonObject> moduleGet();





//    @GET("posts")
//    Call<PostResponse> posts();
}
