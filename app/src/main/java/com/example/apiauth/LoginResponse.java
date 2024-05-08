package com.example.apiauth;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    private Token token;

    @SerializedName("msg")
    private String message;

    public Token getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public static class Token {
        @SerializedName("refresh")
        private String refresh;

        @SerializedName("access")
        private String access;

        public String getRefresh() {
            return refresh;
        }

        public String getAccess() {
            return access;
        }
    }
}
