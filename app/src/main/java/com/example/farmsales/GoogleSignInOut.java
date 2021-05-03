package com.example.farmsales;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class GoogleSignInOut {
    GoogleSignInClient signInClient;
    public GoogleSignInOut(GoogleSignInClient signInClient){
        this.signInClient = signInClient;
    }

    public GoogleSignInOut() {
    }

    public GoogleSignInClient getSignInClient() {
        return signInClient;
    }
}
