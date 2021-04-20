package com.pb.criconet.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.models.UserData;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pradeep on 9/6/2016.
 */
@SuppressLint("LogNotTimber")
public class Login extends AppCompatActivity {

    SharedPreferences prefs;
    TextView forgot_password;
    LinearLayout click_signup;
    Button btn_login;
    EditText edttxt_email, edttxt_password;
    String email_String, password_String;
    RequestQueue queue;
    ProgressWheel progress_wheel;
    Location mylocation;
    String personid, personemail, personName, personPhotoUrl, logintype = "";
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    SignInButton gmail_login;
    LoginButton fb_login;
    private ConstraintLayout container;
    private FusedLocationProviderClient mFusedLocationClient;
    private CallbackManager callbackmanager;

    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarGradient.setStatusBarGradiant(Login.this);
        setContentView(R.layout.activity_login_screen);
        prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
        container=findViewById(R.id.container);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Login.this);


//               Facebook SignIn
        callbackmanager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
////                Toast.makeText(Login.this,Base64.encodeToString(md.digest(), Base64.DEFAULT),Toast.LENGTH_LONG).show();
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//        } catch (NoSuchAlgorithmException e) {
//        }
// /////////////////////////////////////////////////////////
//          Google SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
// /////////////////////////////////////////////////////////

        fb_login = (LoginButton) findViewById(R.id.fb_login);
        gmail_login = (SignInButton) findViewById(R.id.gmail_login);

        click_signup = (LinearLayout) findViewById(R.id.click_signup);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        queue = Volley.newRequestQueue(Login.this);
        progress_wheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        edttxt_email = (EditText) findViewById(R.id.edttxt_email);
        edttxt_password = (EditText) findViewById(R.id.edttxt_password);


        click_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SessionManager.get_select_type(prefs).equals("M")) {
                    Intent intent = new Intent(Login.this, Signup.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Login.this, Signup.class);
                    startActivity(intent);
                }
            }
        });
        forgot_password.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, ForgotPassword.class);/**/
            startActivity(intent);
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_String = edttxt_email.getText().toString().trim();
                password_String = edttxt_password.getText().toString().trim();
                if (!Global.validateName(email_String)) {
                    Global.showSnackbar(container,"Enter correct EmailId / Name");
                } else if (!Global.validateName(password_String)) {
                    Global.showSnackbar(container,"Enter correct password");
                } else {
                    if (Global.isOnline(Login.this)) {
                        login_task(email_String, password_String);
                    } else {
                        Toast.makeText(Login.this, R.string.no_internet, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Login.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                SessionManager.save_devicetoken(prefs, newToken);
                Log.e("newToken", newToken);
            }
        });

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && !editable.toString().equalsIgnoreCase("")) {
                    // Checking editable.hashCode() to understand which edittext is using right now
                    if (edttxt_email.getText().hashCode() == editable.hashCode()) {
                        edttxt_email.setError(null);
                    } else if (edttxt_password.getText().hashCode() == editable.hashCode()) {
                        edttxt_password.setError(null);

                    }
                }
            }
        };
        edttxt_email.addTextChangedListener(tw);
        edttxt_password.addTextChangedListener(tw);

        gmail_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        fb_login.setReadPermissions(Arrays.asList("email", "public_profile"));
        // If you are using in a fragment, call loginButton.setFragment(this);
        callbackmanager = CallbackManager.Factory.create();
        fb_login.registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("FB Login Button:", "login Success");
                performFbLoginOrSignUp(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                System.out.println("Login details = ");
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });
    }

    private void googleSignIn() {
//        Auth.GoogleSignInApi.signOut(mGoogleSignInClient);
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            boolean gps_enabled = false;
                            boolean network_enabled = false;

                            try {
                                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            } catch (Exception ex) {
                            }

                            try {
                                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                            } catch (Exception ex) {
                            }

                            if (!gps_enabled && !network_enabled) {
//                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                startActivity(intent);
                                new AlertDialog.Builder(Login.this)
                                        .setMessage(R.string.gps_network_not_enabled)
                                        .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                            }
                                        })
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                finish();
                                            }
                                        })
                                        .show();
                            } else {
                                mFusedLocationClient.getLastLocation().addOnSuccessListener(Login.this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location != null) {
                                            // Logic to handle location object
                                            mylocation = location;
//                                        Toast.makeText(Login.this, "" + mylocation.getLatitude() + " - " + mylocation.getLongitude(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            mylocation = new Location("DummyLocation");
                                            mylocation.setLatitude(0.0);
                                            mylocation.setLongitude(0.0);
                                        }
                                    }
                                });
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            Global.showSettingsDialog(Login.this);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();
    }

    public void login_task(final String email_String, final String password_String) {
        progress_wheel.setVisibility(View.VISIBLE);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "user_login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("login reponse", "" + response);
                        progress_wheel.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                SessionManager.save_user_id(prefs, jsonObject.optString("user_id"));
                                SessionManager.save_session_id(prefs, jsonObject.optString("session_id"));
                                SessionManager.save_name(prefs, email_String);
                                SessionManager.save_emailid(prefs, email_String);
                                SessionManager.save_password(prefs, password_String);
//                                SessionManager.save_session_id(prefs, jsonObject.optString("session_id"));
//                                SessionManager.save_image(prefs, jsonObject.optString("i"));
                                SessionManager.save_check_login(prefs, true);

                                UserData userData = UserData.fromJson(jsonObject.optJSONObject("data"));
                                SessionManager.save_user_id(prefs, userData.getUser_id());
                                SessionManager.save_name(prefs, userData.getUsername());
                                SessionManager.save_emailid(prefs, userData.getEmail());
                                SessionManager.save_sex(prefs, userData.getGender());
                                SessionManager.save_address(prefs, userData.getAddress());
                                SessionManager.save_image(prefs, userData.getAvatar());
                                SessionManager.save_cover(prefs, userData.getCover());
                                SessionManager.save_profiletype(prefs, userData.getProfile_type());

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(Login.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(Login.this, "Error in server");
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
                        Global.msgDialog(Login.this, "Error from server");
//                Global.msgDialog(Login.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("username", email_String);
                param.put("password", password_String);
                param.put("device_id", SessionManager.get_devicetoken(prefs));
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

    public void signup_tasksocial(String url, final String name_string, final String email_String, final String logintype, final String personid, final String image) {
        progress_wheel.setVisibility(View.VISIBLE);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress_wheel.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                SessionManager.save_user_id(prefs, jsonObject.optString("user_id"));
                                SessionManager.save_session_id(prefs, jsonObject.optString("session_id"));
                                SessionManager.save_check_login(prefs, true);

                                UserData userData = UserData.fromJson(jsonObject.optJSONObject("data"));
                                SessionManager.save_user_id(prefs, userData.getUser_id());
                                SessionManager.save_name(prefs, userData.getUsername());
                                SessionManager.save_emailid(prefs, userData.getEmail());
                                SessionManager.save_sex(prefs, userData.getGender());
                                SessionManager.save_address(prefs, userData.getAddress());
                                SessionManager.save_image(prefs, userData.getAvatar());
                                SessionManager.save_cover(prefs, userData.getCover());

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(Login.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(Login.this, "Error in server");
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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//                "provider:Facebook
//                identifier:134464588258955
//                first_name:Kundan
//                last_name:Singh
//                email:criconetonline12@gmail.com
//                description:
//                gender:
//                profileURL:
//                photoURL:https://graph.facebook.com/134464588258955/picture?width=150&height=150
//                device_id:23243432dfdfdfdf
//                device:android"

                param.put("provider", logintype);
                param.put("identifier", personid);
                param.put("first_name", name_string);
                param.put("email", email_String);
                param.put("device", "android");

                param.put("device_id", SessionManager.get_devicetoken(prefs));
                param.put("deviceToken", SessionManager.get_devicetoken(prefs));
                param.put("firebaseDeviceTocken", SessionManager.get_devicetoken(prefs));
                param.put("firebaseId", SessionManager.get_firebaseId(prefs));
                param.put("photoURL", image);

//                param.put("socialMediaProfile", location_string);
//                param.put("phone", phone_string);
//                param.put("address", location_string);

                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Signed in successfully, show authenticated UI.
                updateUI(account);
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                updateUI(null);
            }
        } else {
            try {
                callbackmanager.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            String name = account.getDisplayName();
            String imageurl = "";
            try {
                Uri uri = account.getPhotoUrl();
                if (uri != null)
                    imageurl = new URL(account.getPhotoUrl().toString()).toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            String email = account.getEmail();
            String id = account.getId();
            String logintype = "Google";

            Log.e(TAG, "updateUI name: " + name);
            Log.e(TAG, "updateUI imageurl: " + imageurl);
            Log.e(TAG, "updateUI email: " + email);
            Log.e(TAG, "updateUI id: " + id);

            signup_tasksocial(Global.URL + "social_login", name, email, logintype, id,imageurl);

        } else {
            Toast.makeText(this, "SignIn failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void performFbLoginOrSignUp(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        // TODO Auto-generated method stub
                        if (response.getError() != null) {
                            // handle error
                            System.out.println("ERROR");
                        } else {
                            System.out.println("Success");
                            try {
                                String jsonresult = String.valueOf(json);
                                System.out.println("JSON Result" + jsonresult);
                                String personid = json.getString("id");
                                String personemail = json.optString("email");
                                String personName = json.getString("first_name");
                                System.out.println("Login details = " + personid + " " + personemail + " " + personName);
                                String personPhotoUrl = "https://graph.facebook.com/" + personid + "/picture?type=large";
                                String logintype = "Facebook";

                                signup_tasksocial(Global.URL + "social_login", personName, personemail, logintype, personid,personPhotoUrl);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void onFblogin() {
        callbackmanager = CallbackManager.Factory.create();
        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        // TODO Auto-generated method stub
                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            try {
                                                String jsonresult = String.valueOf(json);

                                                System.out.println("JSON Result" + jsonresult);
                                                personid = json.optString("id");
                                                personemail = json.optString("email");
                                                personName = json.optString("first_name");
                                                System.out.println("Login details = " + personid + " " + personemail + " " + personName);
                                                personPhotoUrl = "https://graph.facebook.com/" + personid + "/picture?type=large";
                                                logintype = "FACEBOOK";

//                                                signup_tasksocial(Global.URL, personName, personemail, logintype, personid);
//                                                createAccount(personemail, personemail);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name,last_name,link,email,picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("Login details = ");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        error.printStackTrace();
                    }
                });
    }


}

