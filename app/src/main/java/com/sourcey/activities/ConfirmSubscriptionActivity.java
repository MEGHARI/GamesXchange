package com.sourcey.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.Bind;

public class ConfirmSubscriptionActivity extends AppCompatActivity {
    private static final String TAG = "ConfirmSubscriptionActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static String mailAndPassword= " , ";


    @Bind(R.id.input_code) EditText codeText;
    @Bind(R.id.btn_login) Button loginButton;
    @Bind(R.id.link_signup) TextView signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmsubscription);
        ButterKnife.bind(this);
        mailAndPassword = getIntent().getStringExtra(mailAndPassword);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                confirm();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void confirm() {
        if (!validate()) {
            onConfirmSuccess();
            return;
        }

        loginButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(ConfirmSubscriptionActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = mailAndPassword.split(",")[0];
        String password = mailAndPassword.split(",")[1];
        String code = codeText.getText().toString();

        JSONObject request = new JSONObject();

        try {
            request.put("mail", email);
            request.put("password", password);
            request.put("code",code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                "http://192.168.43.222:8080/DarProject/user/confirmCode",
                request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getBaseContext(), response.toString(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        // On complete call either onLoginSuccess or onLoginFailed
                                        onConfirmSuccess();
                                        progressDialog.dismiss();
                                    }
                                }, 3000);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError errorParse = new VolleyError(new String(error.networkResponse.data));
                            error = errorParse;
                            JSONObject json;
                            try {
                                json = new JSONObject(error.getMessage());
                                Toast.makeText(getBaseContext(), json.getJSONObject("error").optString("message","mail ou mot de passe invalide"), Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            loginButton.setEnabled(true);
                            progressDialog.dismiss();

                        }
                    }
                }


        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);


    }


    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onConfirmSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onConfirmFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String code= codeText.getText().toString();


        if (code.isEmpty()) {
            codeText.setError("check your code");
            valid = false;
        } else {
            codeText.setError(null);
        }

        return valid;
    }
}
