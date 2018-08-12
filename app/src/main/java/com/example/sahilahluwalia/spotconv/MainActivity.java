package com.example.sahilahluwalia.spotconv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
public class MainActivity extends AppCompatActivity
{
    private static final String CLIENT_ID = "82921fab329a4f7e96a9f4abb393e53a";
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "https://spotconv/callback";
    final song current = new song();
    EditText linkText;
    Button checkBtn;
    String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linkText = findViewById(R.id.link);
        checkBtn = findViewById(R.id.button);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = linkText.getText().toString();
                String URI = current.getURI(link);
                current.setURI(URI);
                linkText.setText(URI);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
        authToken = response.getAccessToken();
        System.out.println("&&&&&&&&&&&&" + authToken);
    }

}