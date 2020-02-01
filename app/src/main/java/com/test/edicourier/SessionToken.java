package com.test.edicourier;

import android.content.Context;

import com.test.edicourier.model.Token;

public class SessionToken {

    private static final SessionToken ourInstance = new SessionToken();

    public static SessionToken getInstance() {
        return ourInstance;
    }

    private SessionToken() {
    }

    private static String token;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        SessionToken.token = token;
    }

    public String getNewToken(Context context) {
        String login = StorageService.getData(context, Constants.APP_PREFERENCES_LOGIN);
        String password = StorageService.getData(context, Constants.APP_PREFERENCES_PASSWORD);
        NetworkEngine.getInstance().loadToken(login, password, new TypedNetworkCallback<Token>() {
            @Override
            public void onLoad(Token token) {
                setToken(token.getToken());
            }

            @Override
            public void onError(String errorString) {
                setToken(null);
            }
        });
        return getToken();
    }
}
