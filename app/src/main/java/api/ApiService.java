package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import models.AccessToken;
import models.ExamQuestion;
import models.ExamSchedule;
import models.LoginBody;
import models.MarkReport;
import models.ModuleResponse;
import models.ResponseDTO;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {


    @POST("Authentication/login")
    Call<AccessToken> login(@Body LoginBody body);

    @POST("Authentication/logout")
    Call<ResponseDTO> logout();

    @POST("Token/refresh")
    Call<AccessToken> refresh();

    @GET("Module")
    Call<JsonObject> moduleGet();

    @GET("student/markReport/{studentId}/{moduleId}")
    Call<List<MarkReport>> markReportGet(@Path("studentId") int studentId,@Path("moduleId") int moduleId);

    @GET("ExamQuestions/{examId}")
    Call<ExamQuestion> examQuestionGet(@Path("examId") int examId, @Query("studentId") int studentId);
    @GET("Exam/{studentId}")
    Call<ExamSchedule> getSchedule(@Path("studentId") String studentId);

    @GET("Module/{studentId}")
    Call<ModuleResponse> moduleGetByStudentId(@Path("studentId") int studentId, @Query("pageSize")int pageSize);




//    @GET("posts")
//    Call<PostResponse> posts();
}
