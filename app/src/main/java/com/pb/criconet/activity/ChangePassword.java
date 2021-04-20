package com.pb.criconet.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


public class ChangePassword extends AppCompatActivity {

    private TextView change_pass;
    private EditText old_pass, new_pass, conf_pass;
    private SharedPreferences prefs;
    private RequestQueue queue;
    private View rootView;
    private String TAG = "ChangePassword";
    private ProgressDialog progress;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //  // Inflate the layout for this fragment
//        rootView = inflater.inflate(R.layout.change_password, container, false);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarGradient.setStatusBarGradiant(ChangePassword.this);
        setContentView(R.layout.change_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView toolbartext = (TextView) toolbar.findViewById(R.id.toolbartext);
        toolbartext.setText(R.string.change_password);

//        Toolbar toolbar = (Toolbar) ChangePassword.this.findViewById(R.id.toolbar);
//        TextView mTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
//        mTitle.setText("CHANGE PASSWORD");

        progress = new ProgressDialog(ChangePassword.this);
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);

        queue = Volley.newRequestQueue(ChangePassword.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(ChangePassword.this);
        old_pass = (EditText) findViewById(R.id.edttxt_old_pass);
        new_pass = (EditText) findViewById(R.id.edttxt_new_pass);
        conf_pass = (EditText) findViewById(R.id.edttxt_conf_pass);
        change_pass = (TextView) findViewById(R.id.change_pass);

        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMethod();
            }
        });
//        return rootView;
    }

    public void changeMethod() {
        if (old_pass.getText().toString().length() < 1) {
            Toast.makeText(ChangePassword.this, "Please Enter Current Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (new_pass.getText().toString().length() < 1) {
            Toast.makeText(ChangePassword.this, "Please Enter new Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (conf_pass.getText().toString().length() < 1) {
            Toast.makeText(ChangePassword.this, "Please Re-Enter new Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (old_pass.getText().toString().equals(new_pass.getText().toString())) {
            Toast.makeText(ChangePassword.this, "Passwords Must Not Be Same As Current Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!conf_pass.getText().toString().equalsIgnoreCase(new_pass.getText().toString())) {
            Toast.makeText(ChangePassword.this, "Passwords Must Be Same", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Global.isOnline(ChangePassword.this)) {
            changePassword();
        } else {
            Global.showDialog(ChangePassword.this);
        }
    }

    private void changePassword() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "update_user_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.e(response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                SessionManager.save_password(prefs, new_pass.getText().toString());
                                Global.msgDialog(ChangePassword.this, "Password Changed Successfully");
                                old_pass.setText("");
                                new_pass.setText("");
                                conf_pass.setText("");
                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(ChangePassword.this, jsonObject.optString("message"));
                            } else {
                                Global.msgDialog(ChangePassword.this, "Error in server");
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
                        progress.dismiss();
                        Global.msgDialog(ChangePassword.this, "Error from server");
//                Global.msgDialog(ChangePassword.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//                type:  change_password
//                current_password:
//                new_password:"

                param.put("type", "change_password");
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("current_password",old_pass .getText().toString());
                param.put("new_password", new_pass.getText().toString());
                param.put("s", SessionManager.get_session_id(prefs));

                Timber.e("%s", param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);


    }


    public void FireBasechangePassword() {
        progress.show();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider.getCredential(SessionManager.get_emailid(prefs), old_pass.getText().toString().trim());
// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progress.dismiss();
                            user.updatePassword(new_pass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Password updated Fire Base");
                                        Global.msgDialog(ChangePassword.this, "Password Changed Sucessfully");
                                        old_pass.setText("");
                                        new_pass.setText("");
                                        conf_pass.setText("");

                                    } else {
                                        Global.msgDialog(ChangePassword.this, "Please check your password");
                                        Log.d(TAG, "Error password not updated");
                                    }
                                }
                            });
                        } else {
                            progress.dismiss();
                            Global.msgDialog(ChangePassword.this, "Change Password Failed");
                            Log.d(TAG, "Error auth failed");
                        }
                    }
                });

    }

}
