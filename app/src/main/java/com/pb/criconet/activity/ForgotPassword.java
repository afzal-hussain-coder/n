package com.pb.criconet.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


/**
 * Created by Pradeep on 9/6/2016.
 */
public class ForgotPassword extends AppCompatActivity {

    SharedPreferences prefs;
    Button submit;
    ImageView ivBackArrow;
    EditText edttxt_email;
    String email_String;
    RequestQueue queue;
    ProgressWheel progress_wheel;
    ProgressDialog progress_dialog;
    boolean isGPSEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
//        StatusBarGradient.setStatusBarGradiant(ForgotPassword.this);

        prefs = PreferenceManager.getDefaultSharedPreferences(ForgotPassword.this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        submit = (Button) findViewById(R.id.submit);
        queue = Volley.newRequestQueue(ForgotPassword.this);
        progress_wheel =  findViewById(R.id.progress_wheel);
        edttxt_email =  findViewById(R.id.edttxt_email);
        ivBackArrow = findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(view -> finish());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_String = edttxt_email.getText().toString().trim();

                if (!Global.ValidEmail(email_String)) {
                    edttxt_email.setError(Html.fromHtml("<font color='red'>Enter correct emailid</font>"));
                } else {
                    edttxt_email.setError(null);
                    if (Global.isOnline(ForgotPassword.this)) {
                        forgot_task(email_String);
                    } else {
                        Toast.makeText(ForgotPassword.this, R.string.no_internet, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void forgot_task(final String email_String) {
        progress_wheel.setVisibility(View.VISIBLE);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "forget_password",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.e("%s", response);
                        progress_wheel.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                edttxt_email.setText("");
                                Global.msgDialog(ForgotPassword.this, jsonObject.optString("message"));

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(ForgotPassword.this, jsonObject.optString("message"));
                            } else {
                                Global.msgDialog(ForgotPassword.this, "Error in server");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progress_wheel.setVisibility(View.GONE);
                        Global.msgDialog(ForgotPassword.this, "Error from server");
//                Global.msgDialog(Login.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("email", email_String);
//                param.put("device_id", SessionManager.get_devicetoken(prefs));
                param.put("s", "1");

                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }


}