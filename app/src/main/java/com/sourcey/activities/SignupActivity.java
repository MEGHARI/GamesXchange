package com.sourcey.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.Bind;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_firsName) EditText firstNameText;
    @Bind(R.id.input_lastName) EditText lastNameText;
    @Bind(R.id.input_address) EditText addressText;
    @Bind(R.id.input_email) EditText emailText;
    @Bind(R.id.input_password) EditText passwordText;
    @Bind(R.id.input_reEnterPassword) EditText reEnterPasswordText;
    @Bind(R.id.btn_signup) Button signupButton;
    @Bind(R.id.link_login) TextView loginLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }
        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String address = addressText.getText().toString();
        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();

        JSONObject signup = new JSONObject();
        try {
            signup.put("firstName",firstName);
            signup.put("lastName",lastName);
            signup.put("address",address);
            signup.put("mail",email);
            signup.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                "http://192.168.43.222:8080/DarProject/user/register",
                signup,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.has("error")){
                            try {
                                progressDialog.dismiss();
                                signupButton.setEnabled(true);
                                String message = response.getJSONObject("error").optString("message");
                                Toast.makeText(getBaseContext(),message,Toast.LENGTH_LONG).show();

                            }catch (JSONException e){}

                        }  else {
                            Toast.makeText(getBaseContext(), "Inscription réussite", Toast.LENGTH_LONG).show();
                            Toast.makeText(getBaseContext(), "Vous venez de recevoir un code de confirmation", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), ConfirmSubscriptionActivity.class);
                            intent.putExtra(ConfirmSubscriptionActivity.mailAndPassword,email+","+password);
                            Toast.makeText(getBaseContext(),"l'utilisateur doit confirmer l'inscription", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }


                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        signupButton.setEnabled(true);
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError errorParse = new VolleyError(new String(error.networkResponse.data));
                            error = errorParse;

                           JSONObject json;
                            try {
                                json = new JSONObject(error.getMessage());
                                JSONArray errors = json.getJSONArray("errors");
                                for(int i =0;i<errors.length();++i){
                                    JSONObject js = new JSONObject();
                                    js = errors.getJSONObject(i);
                                    switch (js.optInt("code")){
                                        case 1:
                                            firstNameText.setError("check your first name");
                                            break;
                                        case 2:
                                            lastNameText.setError("check your last name");
                                            break;
                                        case 3:
                                            addressText.setError("check your address");
                                            break;
                                        case 4:
                                            emailText.setError("check your mail");
                                            break;
                                        case 5:
                                            passwordText.setError("check your password");
                                            break;
                                        case 6:
                                            reEnterPasswordText.setError("check your password");
                                            break;

                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //loginButton.setEnabled(true);
                            progressDialog.dismiss();

                        }
                    }
                }


        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);

    }


    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "signup failed", Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String address = addressText.getText().toString();
        String email =emailText.getText().toString();
        String password = passwordText.getText().toString();
        String reEnterPassword = reEnterPasswordText.getText().toString();

        if (firstName.isEmpty() || firstName.length() < 3) {
            firstNameText.setError("at least 3 characters");
            valid = false;
        } else {
            firstNameText.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            lastNameText.setError("at least 3 characters");
            valid = false;
        } else {
            lastNameText.setError(null);
        }

        if (address.isEmpty()) {
            addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordText.setError("greater than 6 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (!(reEnterPassword.equals(password))) {
            reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            reEnterPasswordText.setError(null);
        }

        return valid;
    }
}