package com.pb.criconet.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.models.CoachDetails;
import com.pb.criconet.models.OrderCreate;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityCheckoutDetails extends AppCompatActivity implements PaymentResultListener {

    private AppCompatActivity mActivity = ActivityCheckoutDetails.this;
    private static final String TAG = PaymentActivity.class.getSimpleName();
    private RequestQueue queue;
    private SharedPreferences prefs;
    private CoachDetails modelArrayList;
    private OrderCreate ordercreate;
    private CircleImageView ivProfileImage;
    private TextView tvCoachName;
    private TextView tvCoacTitle;
    private TextView tvPrice;
    private TextView tvOfferPrice;
    private TextView tvDate;
    private TextView tvtime;
    private TextView tvSubtotal;
    private TextView tvTax;
    private TextView tvTotalAmount;
    private TextView btnPaynow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_details);
        modelArrayList = (CoachDetails) getIntent().getSerializableExtra("key");
        ordercreate = (OrderCreate) getIntent().getSerializableExtra("key1");

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
        ivProfileImage=findViewById(R.id.ivProfileImage);
        tvCoachName=findViewById(R.id.tvCoachName);
        tvCoacTitle=findViewById(R.id.tvCoacTitle);
        tvPrice=findViewById(R.id.tvPrice);
        tvOfferPrice=findViewById(R.id.tvOfferPrice);
        tvDate=findViewById(R.id.tvDate);
        tvtime=findViewById(R.id.tvtime);
        tvSubtotal=findViewById(R.id.tvSubtotal);
        tvTax=findViewById(R.id.tvTax);
        tvTotalAmount=findViewById(R.id.tvTotalAmount);
        btnPaynow=findViewById(R.id.btnPaynow);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText("Checkout");
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);

        if (modelArrayList!=null && ordercreate!=null) {
            Glide.with(mActivity).load(modelArrayList.getData().getAvatar())
                    .into(ivProfileImage);
            tvCoachName.setText(modelArrayList.getData().getFirstName()+" "+modelArrayList.getData().getLastName());
            tvCoacTitle.setText(modelArrayList.getData().getProfileTitle());
            tvPrice.setText("Price: " + "\u20B9" + modelArrayList.getData().getPrice().getCoachPrice());
            tvTax.setText("Taxes: " + "\u20B9" + modelArrayList.getData().getPrice().getTaxesAmount());
            tvTotalAmount.setText("Total payble amount: " + "\u20B9" + modelArrayList.getData().getPrice().getWithTaxesAmount());
            tvSubtotal.setText("Subtotal : " + "\u20B9" + modelArrayList.getData().getPrice().getPaymentPrice());
            tvDate.setText(ordercreate.getPaymentOption().getSessionDate());
            tvtime.setText(ordercreate.getPaymentOption().getSessionTime());
            if(modelArrayList.getData().getIsOffer().equalsIgnoreCase("Yes")) {
                tvOfferPrice.setVisibility(View.VISIBLE);
                tvOfferPrice.setText("Offer price: " + "\u20B9" + modelArrayList.getData().getPrice().getPaymentPrice());
            }else {
                tvOfferPrice.setVisibility(View.GONE);
            }

        }

        btnPaynow.setOnClickListener(view -> {
            startPayment();
        });

        Checkout.preload(getApplicationContext());

    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", ordercreate.getPaymentOption().getName());
            options.put("description",ordercreate.getPaymentOption().getDescription());
            options.put("send_sms_hash",true);
            options.put("allow_rotation", false);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", modelArrayList.getData().getAvatar());
            options.put("currency", "INR");
            options.put("amount",ordercreate.getPaymentOption().getAmount());

            JSONObject preFill = new JSONObject();
            preFill.put("email", ordercreate.getPaymentOption().getPrefill().getEmail());
            preFill.put("contact", ordercreate.getPaymentOption().getPrefill().getContact());

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

}