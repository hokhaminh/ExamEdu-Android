package Token;

import android.content.SharedPreferences;

import models.AccessToken;

public class TokenManager {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static TokenManager INSTANCE = null;

    private TokenManager(SharedPreferences prefs){
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

    public static synchronized TokenManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null){
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }

    public void saveToken(AccessToken token){
        editor.putString("ACCESS_TOKEN", token.getAccessToken()).commit();
//        editor.putString("REFRESH_TOKEN", token.getRefreshToken()).commit();
        editor.putString("ACCOUNT_ID", token.getAccountId()).commit();
    }



    public void deleteToken(){
        editor.remove("ACCESS_TOKEN").commit();
//        editor.remove("REFRESH_TOKEN").commit();
    }

    public AccessToken getToken(){
        AccessToken token = new AccessToken();
        token.setAccessToken(prefs.getString("ACCESS_TOKEN", null));
        token.setAccountId(prefs.getString("ACCOUNT_ID", null));
//        token.setRefreshToken(prefs.getString("REFRESH_TOKEN", null));
        return token;
    }

}
