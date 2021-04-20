package com.pb.criconet.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
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
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CCResource;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.event.SlotId;
import com.pb.criconet.models.CoachDetails;
import com.pb.criconet.models.DateSlotes;
import com.pb.criconet.models.OrderCreate;
import com.pb.criconet.models.TimeSlot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class CoachDetailsActivity extends BaseActivity {

    List<EventDay> events = new ArrayList<>();
    private String year;
    private String month;
    private String dateGot = "";
    private AppCompatActivity mActivity = CoachDetailsActivity.this;
    private TextView tvCoachName;
    private TextView tvCoacTitle;
    private TextView tvPrice;
    private TextView tvOfferPrice;
    private TextView tvDiscription;
    private CircleImageView ivProfileImage;
    private RequestQueue queue;
    private SharedPreferences prefs;
    private ProgressDialog progressDialog;
    private String coachId;
    private TextView btnCalender;
    private Calendar[] days;
    private String mslotId = "";
    private CalendarView datePicker;
    private CoachDetails modelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_details);
        coachId = getIntent().getStringExtra("coachId");
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

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(R.string.coach_details);
        prefs = PreferenceManager.getDefaultSharedPreferences(CoachDetailsActivity.this);
        queue = Volley.newRequestQueue(CoachDetailsActivity.this);


        tvCoachName = findViewById(R.id.tvCoachName);
        tvCoacTitle = findViewById(R.id.tvCoacTitle);
        tvPrice = findViewById(R.id.tvPrice);
        tvOfferPrice = findViewById(R.id.tvPrice);
        tvDiscription = findViewById(R.id.tvDiscription);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        btnCalender = findViewById(R.id.btnCalender);
        datePicker = findViewById(R.id.calendarView);
        btnCalender.setOnClickListener(view -> {
            if (dateGot.equalsIgnoreCase("")) {
                Toast.makeText(mActivity, "Please Select Date", Toast.LENGTH_SHORT).show();
            } else if (mslotId.equalsIgnoreCase("")) {
                Toast.makeText(mActivity, "Please Select Time Slote", Toast.LENGTH_SHORT).show();
            } else {
                BookCoach();
            }
        });


        year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
        month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());


        // disable dates before today
        // Calendar min = Calendar.getInstance();
        //min.add(Calendar.DAY_OF_MONTH, -1);
        //datePicker.setMinimumDate(min);


        datePicker.setOnDayClickListener(eventDay -> {
            dateGot = Global.getDateGot(eventDay.getCalendar().getTime().toString());
            if (Global.isOnline(mActivity)) {
                getDateSlote(dateGot, coachId);
            } else {
                Global.showDialog(mActivity);
            }
        });
        if (Global.isOnline(mActivity)) {
            getCoachDetails();
            getCoachDete();
        } else {
            Global.showDialog(mActivity);
        }


        datePicker.setOnForwardPageChangeListener(() -> {
            year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
            month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());
            getCoachDete();
        });

        datePicker.setOnPreviousPageChangeListener(() -> {
            year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
            month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());
            getCoachDete();
        });

    }

    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }

    private void getCoachDetails() {
        progressDialog = Global.getProgressDialog(mActivity, CCResource.getString(mActivity, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_coach_details", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Global.dismissDialog(progressDialog);
                Gson gson = new Gson();
                modelArrayList = gson.fromJson(response, CoachDetails.class);

                if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    Glide.with(mActivity).load(modelArrayList.getData().getAvatar())
                            .into(ivProfileImage);
                    tvCoachName.setText(modelArrayList.getData().getFirstName() + " " + modelArrayList.getData().getLastName());
                    tvCoacTitle.setText(modelArrayList.getData().getProfileTitle());
                    tvPrice.setText("Price: " + "\u20B9" + modelArrayList.getData().getChargeAmount());
                    if (modelArrayList.getData().getIsOffer().equalsIgnoreCase("Yes")) {
                        tvOfferPrice.setVisibility(View.VISIBLE);
                        tvOfferPrice.setText("Price: " + "\u20B9" + modelArrayList.getData().getPrice().getPaymentPrice());
                    } else {
                        tvOfferPrice.setVisibility(View.GONE);
                    }

                    tvDiscription.setText(modelArrayList.getData().getAboutCoachProfile());
                } else {
                    Toast.makeText(mActivity, modelArrayList.getErrors().getErrorText(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.dismissDialog(progressDialog);
                Global.msgDialog(mActivity, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coach_id", coachId);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getCoachDete() {
        //progressDialog = Global.getProgressDialog(mActivity, CCResource.getString(mActivity, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_coach_available_date", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Global.dismissDialog(progressDialog);
                Gson gson = new Gson();

                if (!response.contains("false")) {
                    DateSlotes modelArrayList = gson.fromJson(response, DateSlotes.class);

                    if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                        days = new Calendar[modelArrayList.getData().size()];
                        for (int i = 0; i < modelArrayList.getData().size(); i++) {

                            try {
                                days[i] = Global.convertStringToCalendar(modelArrayList.getData().get(i));
                                events.add(new EventDay(Global.convertStringToCalendar(modelArrayList.getData().get(i)), Global.getThreeDots(mActivity)));
                                datePicker.setEvents(events);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toast.makeText(mActivity, modelArrayList.getErrors().getErrorText(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //    Global.dismissDialog(progressDialog);
                Global.msgDialog(mActivity, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coach_id", coachId);
                param.put("month", month);
                param.put("year", year);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getDateSlote(String dateGot, String coachId) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_coach_date_slot", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                TimeSlot modelArrayList = gson.fromJson(response, TimeSlot.class);
                if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    navigationController.showTimePreode(modelArrayList);
                } else {
                    Toast.makeText(mActivity, modelArrayList.getErrors().getErrorText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.msgDialog(mActivity, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coach_id", coachId);
                param.put("date", dateGot);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void BookCoach() {
        progressDialog = Global.getProgressDialog(mActivity, CCResource.getString(mActivity, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "create_booking_order", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Global.dismissDialog(progressDialog);
                Gson gson = new Gson();
                OrderCreate ordercreate = gson.fromJson(response, OrderCreate.class);
                if (ordercreate != null && ordercreate.getStatus() == 200) {

                    Toast.makeText(mActivity, "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mActivity, ActivityCheckoutDetails.class);
                    intent.putExtra("key", (Serializable) modelArrayList);
                    intent.putExtra("key1", (Serializable) ordercreate);
                    startActivity(intent);
                    finish();

                } else {
                    if(ordercreate!=null)
                    Toast.makeText(mActivity, ordercreate.getErrors().getErrorText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.dismissDialog(progressDialog);
                Global.msgDialog(mActivity, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coach_id", coachId);
                param.put("booking_slot_date", dateGot);
                param.put("booking_slot_id", mslotId);
                param.put("booking_amount", modelArrayList.getData().getPrice().getCoachPrice());
                param.put("payment_amount", modelArrayList.getData().getPrice().getPaymentPrice());
                param.put("taxes_amount", modelArrayList.getData().getPrice().getTaxesAmount());
                param.put("offer_id", modelArrayList.getData().getPrice().getOfferId());
                param.put("cuurency_code", "INR");
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SlotId event) {
        this.mslotId = event.slotId;
        btnCalender.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}

