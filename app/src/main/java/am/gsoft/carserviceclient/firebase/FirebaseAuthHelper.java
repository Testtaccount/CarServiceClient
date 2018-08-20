package am.gsoft.carserviceclient.firebase;

import am.gsoft.carserviceclient.app.App;
import am.gsoft.carserviceclient.R;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.Log;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import java.util.ArrayList;
import java.util.List;

public class FirebaseAuthHelper {

  private static final String TAG = FirebaseAuthHelper.class.getSimpleName();

  public static final int RC_SIGN_IN = 456;
  public static final String EXTRA_FIREBASE_ACCESS_TOKEN = "extra_firebase_access_token";
  private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
  private static final String FIREBASE_TOS_URL = "https://firebase.google.com/terms/";
  private static final String GOOGLE_PRIVACY_POLICY_URL = "https://www.google.com/policies/privacy/";
  private static final String FIREBASE_PRIVACY_POLICY_URL = "https://firebase.google.com/terms/analytics/#7_privacy";

  public void loginByPhone(Activity activity) {
    activity.startActivityForResult(
        AuthUI.getInstance().createSignInIntentBuilder()
            .setTheme(getSelectedTheme())
            .setAvailableProviders(getSelectedProviders())
            .setTosUrl(getSelectedTosUrl())
            .setPrivacyPolicyUrl(getSelectedPrivacyPolicyUrl())
            .setIsSmartLockEnabled(false)
            .build(),
        RC_SIGN_IN);
  }

  public static FirebaseUser getCurrentFirebaseUser() {
    return FirebaseAuth.getInstance().getCurrentUser();
  }


  public static void getIdTokenForCurrentUser(final RequestFirebaseIdTokenCallback callback) {
    if (App.getInstance().isSimulator){
      String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjM4NjlhMTliNjdiNDY3YTQ4NjQ3YWFjNTMxYmUxZmIxZTFlYTI5ZTAifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vY2Fyc2VydmljZWNsaWVudCIsImF1ZCI6ImNhcnNlcnZpY2VjbGllbnQiLCJhdXRoX3RpbWUiOjE1MTU1OTM3NzQsInVzZXJfaWQiOiJwVlRqaVdzSzE1V0RMcVZYa1Z3dERHWUNDZHQyIiwic3ViIjoicFZUamlXc0sxNVdETHFWWGtWd3RER1lDQ2R0MiIsImlhdCI6MTUxNTU5Mzc3NiwiZXhwIjoxNTE1NTk3Mzc2LCJwaG9uZV9udW1iZXIiOiIrMzc0Nzc5Mzk3MzMiLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7InBob25lIjpbIiszNzQ3NzkzOTczMyJdfSwic2lnbl9pbl9wcm92aWRlciI6InBob25lIn19.MGgntVNMubQp3g63VMbL3jlMqvAZGz0TtcCQm5iahtTORVON4kCGKN_0FdN01HNEbMSKrXAFn3Se6vqTgGBgmWhl0AICavEk9oiOv6BnJz5rTXqURH9y9O2K_rXQo6XbWXd4_w-SKA59jWMofCX7qOhas9CYDAM-HmEbBgflDRqnMkzMZC3DIdtuCvXy9ynuXzg7aJuud2BXNUv4HItCSQ28t5mVvwsA-4w6sFk_SEsJsgNAy6eTgwD7jBO_s6GMhRMlFTZVw9QyDX0DtleMDHxIOG24i2rBfUOG2dDn4tYi0h-hdXRVl1EVvfkQQmrtgZ1v4BYC5VIQLR1pJ20Acw";
      Log.v(TAG, "Token got successfully. TOKEN = " + accessToken);
      App.getAppSharedHelper().saveFirebaseToken(accessToken);
      callback.onSuccess(accessToken);
    }else {
      if (getCurrentFirebaseUser() == null) {
        Log.v(TAG, "Getting Token error. ERROR = Current Firebse User is null");
        App.getAppSharedHelper().saveFirebaseToken(null);
        callback.onError(new NullPointerException("Current Firebse User is null"));
        return;
      }

      getCurrentFirebaseUser().getIdToken(true)
          .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            public void onComplete(@NonNull Task<GetTokenResult> task) {
              if (task.isSuccessful()) {
                String accessToken = task.getResult().getToken();
//                    String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjM4NjlhMTliNjdiNDY3YTQ4NjQ3YWFjNTMxYmUxZmIxZTFlYTI5ZTAifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vY2Fyc2VydmljZWNsaWVudCIsImF1ZCI6ImNhcnNlcnZpY2VjbGllbnQiLCJhdXRoX3RpbWUiOjE1MTU1OTM3NzQsInVzZXJfaWQiOiJwVlRqaVdzSzE1V0RMcVZYa1Z3dERHWUNDZHQyIiwic3ViIjoicFZUamlXc0sxNVdETHFWWGtWd3RER1lDQ2R0MiIsImlhdCI6MTUxNTU5Mzc3NiwiZXhwIjoxNTE1NTk3Mzc2LCJwaG9uZV9udW1iZXIiOiIrMzc0Nzc5Mzk3MzMiLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7InBob25lIjpbIiszNzQ3NzkzOTczMyJdfSwic2lnbl9pbl9wcm92aWRlciI6InBob25lIn19.MGgntVNMubQp3g63VMbL3jlMqvAZGz0TtcCQm5iahtTORVON4kCGKN_0FdN01HNEbMSKrXAFn3Se6vqTgGBgmWhl0AICavEk9oiOv6BnJz5rTXqURH9y9O2K_rXQo6XbWXd4_w-SKA59jWMofCX7qOhas9CYDAM-HmEbBgflDRqnMkzMZC3DIdtuCvXy9ynuXzg7aJuud2BXNUv4HItCSQ28t5mVvwsA-4w6sFk_SEsJsgNAy6eTgwD7jBO_s6GMhRMlFTZVw9QyDX0DtleMDHxIOG24i2rBfUOG2dDn4tYi0h-hdXRVl1EVvfkQQmrtgZ1v4BYC5VIQLR1pJ20Acw";
                Log.v(TAG, "Token got successfully. TOKEN = " + accessToken);
                App.getAppSharedHelper().saveFirebaseToken(accessToken);
                callback.onSuccess(accessToken);
              } else {
                Log.v(TAG, "Getting Token error. ERROR = " + task.getException().getMessage());
                callback.onError(task.getException());
              }
            }
          });
    }

  }


  private String getSelectedTosUrl() {
//    if (mUseGoogleTos.isChecked()) {
//      return GOOGLE_TOS_URL;
//    }

    return FIREBASE_TOS_URL;
  }

  private String getSelectedPrivacyPolicyUrl() {
//    if (mUseGooglePrivacyPolicy.isChecked()) {
//      return GOOGLE_PRIVACY_POLICY_URL;
//    }

    return FIREBASE_PRIVACY_POLICY_URL;
  }

  @StyleRes
  private int getSelectedTheme() {
    return R.style.AuthTheme;
  }

  private List<IdpConfig> getSelectedProviders() {
    List<IdpConfig> selectedProviders = new ArrayList<>();

//    if (mUseGoogleProvider.isChecked()) {
//      selectedProviders.add(
//          new IdpConfig.GoogleBuilder().setScopes(getGoogleScopes()).build());
//    }
//
//    if (mUseFacebookProvider.isChecked()) {
//      selectedProviders.add(new IdpConfig.FacebookBuilder()
//          .setPermissions(getFacebookPermissions())
//          .build());
//    }
//
//    if (mUseTwitterProvider.isChecked()) {
//      selectedProviders.add(new IdpConfig.TwitterBuilder().build());
//    }
//
//    if (mUseEmailProvider.isChecked()) {
//      selectedProviders.add(new IdpConfig.EmailBuilder()
//          .setRequireName(mRequireName.isChecked())
//          .setAllowNewAccounts(mAllowNewEmailAccounts.isChecked())
//          .build());
//    }
//
//    if (mUsePhoneProvider.isChecked()) {
      selectedProviders.add(new IdpConfig.PhoneBuilder().build());
//    }

    return selectedProviders;
  }

  public void logout() {
    FirebaseAuth.getInstance().signOut();
    App.getAppSharedHelper().saveFirebaseToken(null);
  }

  public interface RequestFirebaseIdTokenCallback {

    void onSuccess(String accessToken);

    void onError(Exception e);
  }
}
