package com.pb.criconet.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.MytextviewBold;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.adapters.MenuAdapter;
import com.pb.criconet.models.Drawer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;


public class MainActivity extends BaseActivity {

    public static BottomNavigation bottomNavigation;
    private FragmentManager manager;
    private ListView list_nav;
    private TextView txt_nav_name;
    private ImageView loc;
    private MenuAdapter menuadapter;
    private ActionBarDrawerToggle toggle;
    private SharedPreferences prefs;
    private RequestQueue queue;
    private ProgressDialog progress;
    private CircleImageView profile_pic;
    private ImageView cover;
    private ArrayList<Drawer> text;
    private AppCompatActivity mActivity;
    private DrawerLayout drawer;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mActivity = MainActivity.this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager = getSupportFragmentManager();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        queue = Volley.newRequestQueue(mActivity);
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);

        profile_pic = findViewById(R.id.profile_pic);
        cover = findViewById(R.id.cover_img);
        txt_nav_name = (MytextviewBold) findViewById(R.id.txt_nav_name);
        bottomNavigation = findViewById(R.id.BottomNavigation);

        list_nav = findViewById(R.id.list_nav);
        text = new ArrayList<>();
        text.add(new Drawer(getString(R.string.home), false, R.drawable.ic_home));

        profile_pic.setOnClickListener(view -> {
            Intent intent = new Intent(mActivity, ProfileActivity.class);
            startActivity(intent);
        });


        if (SessionManager.get_profiletype(prefs).equalsIgnoreCase("Coach")) {
            text.add(new Drawer(getString(R.string.avility), false, R.drawable.ic_page));
        }
        text.add(new Drawer(getString(R.string.pages), false, R.drawable.ic_page));
        text.add(new Drawer(getString(R.string.booking_history), false, R.drawable.ic_booking_history));
        // text.add(new Drawer(getString(R.string.Followers),false,R.drawable.ic_follower));
        //text.add(new Drawer(getString(R.string.Followings),false,R.drawable.ic_following));
        //text.add(new Drawer(getString(R.string.following_request),true,R.drawable.ic_following_request));
        text.add(new Drawer(getString(R.string.edit_profile), false, R.drawable.ic_edit_profile));
        text.add(new Drawer(getString(R.string.change_pass), false, R.drawable.ic_change_password));
        text.add(new Drawer(getString(R.string.blocked_users), false, R.drawable.ic_block_user));
        text.add(new Drawer(getString(R.string.setting), false, R.drawable.ic_settings_applications));
        text.add(new Drawer(getString(R.string.logout), false, R.drawable.ic_logout));


        menuadapter = new MenuAdapter(mActivity, text);
        list_nav.setAdapter(menuadapter);
        list_nav.addFooterView(new View(mActivity), null, true);


        progress = new ProgressDialog(mActivity);
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we don't want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);

                if (SessionManager.get_image(prefs) != null && !SessionManager.get_image(prefs).isEmpty())
                    Glide.with(mActivity).load(SessionManager.get_image(prefs)).dontAnimate().into(profile_pic);

                Glide.with(mActivity).load(SessionManager.get_cover(prefs)).dontAnimate().into(cover);
                txt_nav_name.setText(SessionManager.get_name(prefs));
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        list_nav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updatedisplay(position);
            }
        });

        navigationController.navigateToHomeFragment();
        bottomNavigation.setMenuItemSelectionListener(
                new BottomNavigation.OnMenuItemSelectionListener() {
                    @Override
                    public void onMenuItemSelect(int id, int i1, boolean b) {
                        switch (id) {
                            case R.id.bbn_home:
                                navigationController.navigateToHomeFragment();
                                break;
                            case R.id.bbn_live:
                                navigationController.navigatoLiveMatchesFragment();
                                break;
                            case R.id.bbn_rec:
                                navigationController.navigatoRecMatchesFragment();
                                break;
                            case R.id.bbn_setting:
                                navigationController.navigatoCoachFragment();
                                break;
                        }

                    }

                    @Override
                    public void onMenuItemReselect(int i, int i1, boolean b) {
//                        Toast.makeText(mActivity, "Reselected", Toast.LENGTH_SHORT).show();
                    }
                }

        );

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("type").equalsIgnoreCase("live_streaming")) {
                bottomNavigation.setSelectedIndex(1, true);
            }
        }

        socialLink();

    }

    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }

    private void socialLink() {
        ImageView fb = findViewById(R.id.fb);
        ImageView twitter = findViewById(R.id.twitter);
        ImageView instagram = findViewById(R.id.instagram);
        ImageView linkedin = findViewById(R.id.linkedin);

        fb.setOnClickListener(v -> {
            String url = "https://www.facebook.com/criconetonline";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        twitter.setOnClickListener(v -> {
            String url = "https://twitter.com/criconetonline";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        instagram.setOnClickListener(v -> {
            String url = "https://www.instagram.com/criconet/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        linkedin.setOnClickListener(v -> {
            String url = "https://www.linkedin.com/company/criconetonline/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

    }

    @Override
    public void onBackPressed() {

        if (manager.getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("");
            alertDialog.setMessage("Do you really want to Exit?");
            alertDialog.setPositiveButton("Yes",
                    (dialog, which) -> finish());
            alertDialog.setNegativeButton("No",
                    (dialog, which) -> {
                    });
            alertDialog.show();
        }
    }


    public void updatedisplay(int position) {
        Intent intent;
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (text.get(position).getTitle().equalsIgnoreCase("Pavilion")) {
            navigationController.navigateToHomeFragment();
        } else if (text.get(position).getTitle().equalsIgnoreCase("Slot")) {
            navigationController.navigatoMenuFragment(true);
        } else if (text.get(position).getTitle().equalsIgnoreCase("Pages")) {
            intent = new Intent(mActivity, Pages.class);
            startActivity(intent);
        } else if (text.get(position).getTitle().equalsIgnoreCase("Booking History")) {
            navigationController.navigatoBookingFragment();
        } else if (text.get(position).getTitle().equalsIgnoreCase("Followers")) {
            navigationController.navigatoFollowers();
        } else if (text.get(position).getTitle().equalsIgnoreCase("Following")) {

            navigationController.navigatoFollowing();
        } else if (text.get(position).getTitle().equalsIgnoreCase("Following Request")) {
            navigationController.navigatoFollowingRequest();
        } else if (text.get(position).getTitle().equalsIgnoreCase("Edit Profile")) {
            //navigationController.navigatoMenuFragment(false);
            startActivity(new Intent(mActivity, EditProfile.class));

        } else if (text.get(position).getTitle().equalsIgnoreCase("Change Password")) {
            intent = new Intent(mActivity, ChangePassword.class);
            startActivity(intent);
        } else if (text.get(position).getTitle().equalsIgnoreCase("Blocked User")) {
            intent = new Intent(mActivity, BlockedUsers.class);
            startActivity(intent);
        } else if (text.get(position).getTitle().equalsIgnoreCase("Settings")) {
            intent = new Intent(mActivity, Settings.class);
            startActivity(intent);
        } else if (text.get(position).getTitle().equalsIgnoreCase("Logout")) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("");
            alertDialog.setMessage("Do you really want to logout?");
            alertDialog.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
//                                logout();
                            SessionManager.dataclear(prefs);
                            SessionManager.save_check_agreement(prefs, true);
                            Intent intent = new Intent(mActivity, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            alertDialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            alertDialog.show();
        }

    }

    public void logout() {
        RequestQueue queue = Volley.newRequestQueue(mActivity);
        final JSONObject json = new JSONObject();
        try {
            json.put("action", "logout");
            json.put("user_id", SessionManager.get_user_id(prefs));
            Log.e(" data  : ", "" + json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progress.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Global.URL, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("login reponse", "" + response);
//                        {"status":"Success"}
                        progress.dismiss();
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.getString("status").equals("Success")) {
                                SessionManager.dataclear(prefs);
                                SessionManager.save_check_agreement(prefs, true);
                                Intent intent = new Intent(mActivity, Login.class);
                                startActivity(intent);
                                finish();
                            } else if (jsonObject.getString("status").equals("Fail")) {
                                Global.msgDialog(mActivity, jsonObject.getString("msg"));
                            } else {
                                Global.msgDialog(mActivity, "Error in server");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progress.dismiss();
                Global.msgDialog(mActivity, "Internet connection is slow");
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);
    }


}
