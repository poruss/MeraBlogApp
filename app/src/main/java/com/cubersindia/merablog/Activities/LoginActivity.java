package com.cubersindia.merablog.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cubersindia.merablog.Constants.Constants;
import com.cubersindia.merablog.R;
import com.cubersindia.merablog.Session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText username,userPassword;
    private TextView forgotPasswordTv,dontHaveAccountTv;
    private ProgressBar progressBar1;
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeUi();
    }

    private void initializeUi() {
        username = (EditText)findViewById(R.id.username);
        userPassword = (EditText)findViewById(R.id.userPassword);
        forgotPasswordTv = (TextView)findViewById(R.id.forgotPasswordTv);
        dontHaveAccountTv = (TextView)findViewById(R.id.dontHaveAccountTv);
        progressBar1 = (ProgressBar)findViewById(R.id.progressBar1);
        loginBtn = (Button) findViewById(R.id.loginBtn);
    }

    public void signup(View view) {
        startActivity(new Intent(LoginActivity.this,SignupActivity.class));
        finish();
    }

    public void submitLogin(View view) {

        final String usernamee = username.getText().toString();
        final String password = userPassword.getText().toString();
        if (usernamee.equals("")){
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        }else   if (password.equals("")){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }else{
            progressBar1.setVisibility(View.VISIBLE);
            loginBtn.setEnabled(false);
            StringRequest loginRequest = new StringRequest(Request.Method.POST, Constants.loginApi, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressBar1.setVisibility(View.GONE);
                    loginBtn.setEnabled(true);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")){
                            String userId = jsonObject.getString("userId");
                            new SessionManager(getApplicationContext()).saveUserId(userId);
                            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("login","login");
                    params.put("username",usernamee);
                    params.put("userpassword",password);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(loginRequest);
        }

    }
}
