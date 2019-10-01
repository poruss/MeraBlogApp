package com.cubersindia.merablog.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class SignupActivity extends AppCompatActivity {

    private EditText name,username,userPassword,userConfPassword;
    private Button signupBtn;private ProgressBar progressBar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initializeUi();
    }

    private void initializeUi() {
        name = (EditText)findViewById(R.id.name);
        username = (EditText)findViewById(R.id.username);
        userPassword = (EditText)findViewById(R.id.userPassword);
        userConfPassword = (EditText)findViewById(R.id.userConfPassword);
        signupBtn = (Button)findViewById(R.id.signupBtn);
        progressBar1 = (ProgressBar)findViewById(R.id.progressBar1);
    }

    public void login(View view) {
        startActivity(new Intent(SignupActivity.this,LoginActivity.class));
        finish();

    }

    public void submitRegister(View view) {
        String name_ = name.getText().toString();
        String username_ = username.getText().toString();
        String userPassword_ = userPassword.getText().toString();
        String userConfPassword_ = userConfPassword.getText().toString();
        if (name_.equals("")){
            Toast.makeText(this,"Please enter name",Toast.LENGTH_SHORT).show();
        }else  if (username_.equals("")){
            Toast.makeText(this,"Please enter username",Toast.LENGTH_SHORT).show();
        }else  if (userPassword_.equals("")){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
        }else  if (userConfPassword_.equals("")){
            Toast.makeText(this,"Please enter confirm password",Toast.LENGTH_SHORT).show();
        }else if (!userPassword_.equals(userConfPassword_)){
            Toast.makeText(this,"Both password not matched",Toast.LENGTH_SHORT).show();
        }else{
            makeUserSignup(name_,username_,userPassword_);
        }

    }

    private void makeUserSignup(final String name, final String usernamee, final String userPassword) {
        progressBar1.setVisibility(View.VISIBLE);
        signupBtn.setEnabled(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.signupApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("2")){
                        Toast.makeText(getApplicationContext(),"Username already exists", Toast.LENGTH_SHORT).show();
                        username.setText("");
                    }else if (status.equals("1")){
                        String userId = jsonObject.getString("userId");
                        new SessionManager(getApplicationContext()).saveUserId(userId);
                        startActivity(new Intent(SignupActivity.this,HomeActivity.class));
                        finish();
                    }
                    progressBar1.setVisibility(View.GONE);
                    signupBtn.setEnabled(true);
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
                params.put("register","register");
                params.put("name",name);
                params.put("password",userPassword);
                params.put("username",usernamee);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
