package com.example.myfacebookloginapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfacebookloginapp.R;
import com.example.myfacebookloginapp.api.service.MyClient;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FbLoginActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private ImageView displayImage;
    private TextView displayName,emailId;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button1);
        displayName = findViewById(R.id.profile_name);
        emailId = findViewById(R.id.profile_email);
        displayImage = findViewById(R.id.profile_pic);

        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                AccessToken accessToken=loginResult.getAccessToken();
                userLoginInformation(accessToken);
                getToken();
                insertData();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }
static String email,name,token;
    private void userLoginInformation(AccessToken newAccessToken)
    {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response)
            {
                try {
                    name = object.getString("name");
                    email = object.getString("email");
                    String image = object.getJSONObject("picture").getJSONObject("data").getString("url");//"https://graph.facebook.com/"+id+ "/picture?type=normal";
                    insertData();
                    emailId.setText(email);
                    displayName.setText(name);

                    insertData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","id,name,email,picture.width(200)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getToken()
    {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener
                (FbLoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String newToken = instanceIdResult.getToken();
                        Log.e("newToken", newToken);
                        token=newToken;
                    }
                });
    }

    private void insertData() {
        Call<ResponseBody> call= MyClient.getInstance().getMyApi().insertdata(email,token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}