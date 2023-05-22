package com.example.myapplication;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult;
import com.amplifyframework.auth.options.AuthSignInOptions;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class AmplifyCognito {
    private Context context;

    public AmplifyCognito(Context context) {

        this.context = context;
    }

    //public void signUp(String email, String username, String password) {


    public void signUp(String email, String username, String password) {
        AuthSignUpOptions options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .build();
        Amplify.Auth.signUp(username, password, options,
                result -> {
                    Log.i("AuthQuickStart", "Result: " + result.toString());
                    loadConfirm(username);
                },
                error -> Log.e("AuthQuickStart", "Sign up failed", error));
    }

    public void confirm(String username, String code) {
        Amplify.Auth.confirmSignUp(username, code,
                result -> {
                    Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                    loadLogin();
                },
                error -> Log.e("AuthQuickstart", error.toString()));
    }

    public void signIn(String username, String password) {


        Amplify.Auth.signIn(username, password,
                result ->
                {
                    Log.i("AuthQuickstart", result.isSignedIn() ? "Sign in succeeded" : "Sign in not complete");
                    loadHome(username);

                },
                error -> Log.e("AuthQuickstart", error.toString()));
    }


    public void loadLogin() {
        Intent intent = new Intent(context, SigninActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void loadConfirm(String username) {
        Intent intent = new Intent(context, ConfirmActivity.class);
        intent.putExtra("username", username);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void loadSignup() {
        Intent intent = new Intent(context, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void loadHome(String username) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("username", username);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    AuthSignOutOptions options = AuthSignOutOptions.builder()
            .globalSignOut(true)
            .build();
    public void signOut(){

        Amplify.Auth.signOut(options, signOutResult -> {
            if (signOutResult instanceof AWSCognitoAuthSignOutResult.CompleteSignOut) {
                loadSignup();
            } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.PartialSignOut) {
                // handle partial sign out
            } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.FailedSignOut) {
                // handle failed sign out
            }
    });

}





}
