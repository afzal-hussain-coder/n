package com.pb.criconet.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.MultipartRequest;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.models.FBUser;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by Pradeep on 9/6/2016.
 */
public class Signup extends AppCompatActivity {

    private static final int CAMERA_REQUESTid = 2015;
    private static final int PICK_IMAGEid = 100;
    SharedPreferences prefs;
    LinearLayout click_signin;
    Button btn_login;
    TextView terms;
    TextView tvFemale;
    TextView tvMale;
    private ImageView ivMale;
    private ImageView ivFemale;
    EditText edttxt_email, edttxt_password, edttxt_fname, edttxt_address;
    TextInputLayout til_edttxt_email, til_edttxt_password,  til_edttxt_fname,  til_edttxt_address;
    String email_String, password_String, fname_String, address_String,gender="";
    RequestQueue queue;
    private ConstraintLayout container;
    ProgressDialog progress;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    String imagepath = "";
    private LinearLayout llMale,llFemale;
    //    CallbackManager callbackManager;
    Cursor cursor;
    int columnindex, i;
    Uri URIid = null;
    Uri selectedImageid, mCapturedImageURIid;
    String file_pathid = "", image_pathid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
        container=findViewById(R.id.container);

        prefs = PreferenceManager.getDefaultSharedPreferences(Signup.this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        progress = new ProgressDialog(Signup.this);
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();



//        callbackManager = CallbackManager.Factory.create();


        llMale = findViewById(R.id.llMale);
        tvMale = findViewById(R.id.tvMale);
        ivFemale = findViewById(R.id.ivFemale);
        ivMale = findViewById(R.id.ivMale);
        llFemale = findViewById(R.id.llFemale);
        tvFemale = findViewById(R.id.tvFemale);
        click_signin = (LinearLayout) findViewById(R.id.click_signin);
        btn_login = (Button) findViewById(R.id.btn_login);
        queue = Volley.newRequestQueue(Signup.this);
        edttxt_fname = (EditText) findViewById(R.id.edttxt_fname);
        edttxt_email = (EditText) findViewById(R.id.edttxt_email);
        edttxt_password = (EditText) findViewById(R.id.edttxt_password);
        edttxt_address = (EditText) findViewById(R.id.edttxt_address);
        terms = (TextView) findViewById(R.id.terms);

        String html = "By creating a account you agree to our <b><a href=\"https://criconetonline.com/terms/terms\">Terms of Use</a></b>";
        Spanned result = HtmlCompat.fromHtml(html,0);
        terms.setText(result);
        terms.setMovementMethod(LinkMovementMethod.getInstance());

//        Linkify.addLinks(terms, Linkify.WEB_URLS);


        til_edttxt_fname = (TextInputLayout) findViewById(R.id.til_edttxt_fname);
        til_edttxt_address = (TextInputLayout) findViewById(R.id.til_edttxt_address);

        llFemale.setOnClickListener(view -> {
            gender="Female";
            llMale.setBackgroundResource(R.color.white);
            llFemale.setBackgroundResource(R.color.colorPrimary);
            tvFemale.setTextColor(getResources().getColor(R.color.white));
            tvMale.setTextColor(getResources().getColor(R.color.gray));
            ivFemale.setImageResource(R.drawable.female_white);
            ivMale.setImageResource(R.drawable.male_gray);
        });
        llMale.setOnClickListener(view -> {
            gender="Male";
            llMale.setBackgroundResource(R.color.colorPrimary);
            llFemale.setBackgroundResource(R.color.white);
            tvMale.setTextColor(getResources().getColor(R.color.white));
            tvFemale.setTextColor(getResources().getColor(R.color.gray));
            ivFemale.setImageResource(R.drawable.female_gray);
            ivMale.setImageResource(R.drawable.male_white);
        });

       /* profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });*/

        click_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_String = edttxt_email.getText().toString().trim();
                password_String = edttxt_password.getText().toString().trim();
                fname_String = edttxt_fname.getText().toString().trim();
                if (!Global.validateLength(fname_String, 5)) {
                    Global.showSnackbar(container,"Name must be at least 5 characters");
                } else if (!Global.ValidEmail(email_String)) {
                    Global.showSnackbar(container,"Enter correct Emailid");
                } else if (gender.isEmpty()) {
                    Global.showSnackbar(container,"Select Gender");
                } else if (password_String.length() < 6) {
                    Global.showSnackbar(container,"Password must be at least 6 characters");
                } else {
                    if (Global.isOnline(Signup.this)) {
                        signup_task();
                    } else {
                        Toast.makeText(Signup.this, R.string.no_internet, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Signup.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                SessionManager.save_devicetoken(prefs, newToken);
                Log.e("newToken", newToken);
            }
        });
//
//        TextWatcher tw = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (editable != null && !editable.toString().equalsIgnoreCase("")) {
//                    // Checking editable.hashCode() to understand which edittext is using right now
//                    if (edttxt_email.getText().hashCode() == editable.hashCode()) {
//                        til_edttxt_email.setError(null);
//                    } else if (edttxt_password.getText().hashCode() == editable.hashCode()) {
//                        til_edttxt_password.setError(null);
//                    } else if (edttxt_fname.getText().hashCode() == editable.hashCode()) {
//                        edttxt_fname.setError(null);
//                    }
//                }
//            }
//        };
//        edttxt_email.addTextChangedListener(tw);
//        edttxt_password.addTextChangedListener(tw);
//        edttxt_fname.addTextChangedListener(tw);

    }

    private void createAccount(final String email, final String password) {
        progress.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            progress.dismiss();
                            Log.d("task data", task.getException().toString());
                            Toast.makeText(Signup.this, "Sign Up Failed : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        SessionManager.save_firebaseId(prefs, user.getUid());
        writeNewUser(user.getUid(), fname_String, user.getEmail());
    }

    private void writeNewUser(String userId, String sname, String semail) {
        FBUser FBUser = new FBUser(sname, semail, userId);
        mDatabase.child("users").child(userId).setValue(FBUser);

        signup_task(Global.URL, imagepath);
    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Signup.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    openCameraid();
                } else if (items[item].equals("Gallery")) {
                    openGalleryid();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openCameraid() {
        try {
            String fileName = "profile.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURIid = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURIid);
            startActivityForResult(intent, CAMERA_REQUESTid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openGalleryid() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGEid);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGEid) {
            try {
                selectedImageid = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                cursor = getContentResolver().query(selectedImageid, filePathColumn, null, null, null);
                cursor.moveToFirst();
                columnindex = cursor.getColumnIndex(filePathColumn[0]);
                file_pathid = cursor.getString(columnindex);
                // Log.e("Attachment Path:", attachmentFile);
                URIid = Uri.parse("file://" + file_pathid);
                imagepath = file_pathid;

                cursor.close();
                Log.e("TAG", "1234789" + imagepath);

                if (resultCode == 0) {
//                    dialog_camera.dismiss();
                } else {
//                    dialog_camera.dismiss();
                    System.out.println("cccccccc   " + imagepath);
                    //profile_image.setImageURI(Uri.parse(imagepath));
//                    try {
//                        Glide.with(Signup.this).load(Uri.parse(imagepath)).into(profile_image);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    textViewidproof.setText(image_pathid);
                }

            } catch (Exception e) {

            }

        } else if (requestCode == CAMERA_REQUESTid) {
            try {
                String[] projection = {MediaStore.Images.Media.DATA};
                @SuppressWarnings("deprecation")
                Cursor cursor = managedQuery(mCapturedImageURIid, projection, null, null, null);
                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String capturedImageFilePath = cursor.getString(column_index_data);
                imagepath = capturedImageFilePath;

                if (resultCode == 0) {
//                    dialog_camera.dismiss();
                } else {
//                    dialog_camera.dismiss();
//                    profile_image.setImageURI(Uri.parse(imagepath));
                   // Glide.with(Signup.this).load(imagepath).into(profile_image);
//                    textViewidproof.setText(image_pathid);
                    System.out.println("cccccccc   " + imagepath);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
//        try {
//            callbackmanager.onActivityResult(requestCode, resultCode, data);
//        } catch (Exception e) {
////            e.printStackTrace();
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public void signup_task(String url, String path) {
        try {
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("action", new StringBody("registration"));
            entity.addPart("firstName", new StringBody(fname_String));
            entity.addPart("emailId", new StringBody(email_String));
            entity.addPart("password", new StringBody(password_String));
            entity.addPart("address", new StringBody(address_String));
            entity.addPart("gender", new StringBody(gender));
            entity.addPart("deviceToken", new StringBody(SessionManager.get_devicetoken(prefs)));
            entity.addPart("firebaseDeviceTocken", new StringBody(SessionManager.get_devicetoken(prefs)));
            entity.addPart("firebaseId", new StringBody(SessionManager.get_firebaseId(prefs)));
            if (!(path.equals("") || path == null)) {
                File file = new File(path);
                FileBody fileBody = new FileBody(file);
                entity.addPart("profile", fileBody);
            }
            Log.e("updateimage: ", entity.toString());
            progress.show();
            MultipartRequest req = new MultipartRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        progress.dismiss();
                        System.out.println(response);
                        JSONObject jsonObject2, jsonObject = new JSONObject(response);

                        if (jsonObject.getString("status").equals("Success")) {
                            jsonObject2 = jsonObject.getJSONObject("response");
                            SessionManager.save_user_id(prefs, jsonObject2.getString("id"));
                            SessionManager.save_name(prefs, jsonObject2.getString("firstName"));
                            SessionManager.save_emailid(prefs, jsonObject2.getString("emailId"));
                            SessionManager.save_sex(prefs, jsonObject2.getString("gender"));
                            SessionManager.save_password(prefs, jsonObject2.getString("password2"));
                            SessionManager.save_address(prefs, jsonObject2.getString("address"));

                            SessionManager.save_check_login(prefs, true);
                            SessionManager.save_image(prefs, jsonObject2.getString("image"));

                            Intent intent = new Intent(Signup.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
//                            finish();


                        } else if (jsonObject.getString("status").equalsIgnoreCase("Fail")) {
                            Global.msgDialog(Signup.this, jsonObject.getString("msg"));
                        } else {
                            Global.msgDialog(Signup.this, "Error in server");
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    error.printStackTrace();
                    Global.msgDialog(Signup.this, "Error from Server");
                }
            }, entity);

            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            queue.add(req);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void signup_task() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "user_registration",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.tag("login response").e("%s", response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                SessionManager.save_user_id(prefs, jsonObject.optString("user_id"));
                                SessionManager.save_session_id(prefs, jsonObject.optString("session_id"));

                                SessionManager.save_name(prefs, fname_String);
                                SessionManager.save_emailid(prefs, email_String);
                                SessionManager.save_password(prefs, password_String);
                                SessionManager.save_session_id(prefs, jsonObject.optString("session_id"));
                                SessionManager.save_check_login(prefs, true);
                                SessionManager.save_sex(prefs, jsonObject.optString("gender"));
                                Intent intent = new Intent(Signup.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(Signup.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(Signup.this, "Error in server");
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
                        Global.msgDialog(Signup.this, "Error from server");
//                Global.msgDialog(Signup.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                //            "username:dfordharma123
//            password:dharma@123
//            email:dfordharma12@gmail.com
//            confirm_password:dharma@123
//            s:1"
                param.put("username", fname_String);
                param.put("email", email_String);
                param.put("password", password_String);
                param.put("confirm_password", password_String);
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

    public void gps_on() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Signup.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Turn On Location Services ");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        startActivityForResult(intent, 14);
                    }
                });
        alertDialog.setNegativeButton("Not Now",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();

    }

}