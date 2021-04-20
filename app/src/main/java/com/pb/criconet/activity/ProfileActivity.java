package com.pb.criconet.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.Utills.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity {
    private LinearLayout llFollowers;
    private LinearLayout llFolloweing;
    private LinearLayout llFolloweingRequest;
    private TextView tvNumFollowers;
    private TextView tvNumFollowing;
    private TextView tvNumFollowingRequest;
    private ImageView ivEditProfile;
    private SharedPreferences prefs;
    private AppCompatActivity mActivity;
    private CircleImageView profile_pic;
    private ImageView cover_img;
    private TextView txt_nav_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mActivity= ProfileActivity.this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText("Profile");
        profile_pic=findViewById(R.id.profile_pic);
        cover_img=findViewById(R.id.cover_img);
        txt_nav_name=findViewById(R.id.txt_nav_name);

        ivEditProfile = findViewById(R.id.ivEditProfile);
        llFollowers = findViewById(R.id.llFollowers);
        llFolloweing = findViewById(R.id.llFolloweing);
        llFolloweingRequest = findViewById(R.id.llFolloweingRequest);
        tvNumFollowers = findViewById(R.id.tvNumFollowers);
        tvNumFollowing = findViewById(R.id.tvNumFollowing);
        tvNumFollowingRequest = findViewById(R.id.tvNumFollowingRequest);
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);

        ivEditProfile.setOnClickListener(view -> {
            startActivity(new Intent(mActivity,EditProfile.class));
        });


        if (SessionManager.get_image(prefs) != null && !SessionManager.get_image(prefs).isEmpty()) {
            Glide.with(mActivity).load(SessionManager.get_image(prefs)).dontAnimate().into(profile_pic);
            Glide.with(mActivity).load(SessionManager.get_cover(prefs)).dontAnimate().into(cover_img);
            txt_nav_name.setText(SessionManager.get_name(prefs));
        }

        navigationController.navigatoFollowers();


        llFollowers.setOnClickListener(view1 -> {
            navigationController.navigatoFollowers();
        });
        llFolloweing.setOnClickListener(view1 -> {
            navigationController.navigatoFollowing();
        });
        llFolloweingRequest.setOnClickListener(view1 -> {
            navigationController.navigatoFollowingRequest();
        });
    }


    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }
}
