package api;

import android.content.Context;
import android.content.Intent;

import com.example.examedu_android.MainActivity;
import com.example.examedu_android.PostActivity;

import Token.TokenManager;

public class CheckToken {

    static ApiService service;
    static TokenManager tokenManager;

    public static ApiService check(Context context){

    //Lấy Token ra
    tokenManager = TokenManager.getInstance(context.getSharedPreferences("prefs",context.MODE_PRIVATE));

    //check nếu ko có token sẽ chuyển về trang login
    if(tokenManager.getToken() == null){
        context.startActivity(new Intent(context, MainActivity.class));
        return null;
    }

    //Đây là lúc nó sẽ nhận vào token để tự động nhét vào header
    return service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
}

}
