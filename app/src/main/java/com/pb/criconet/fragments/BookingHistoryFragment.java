package com.pb.criconet.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CCResource;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.activity.CallActivity;
import com.pb.criconet.adapters.BookingHistoryAdapter;
import com.pb.criconet.models.BookingHistory;
import com.pb.criconet.models.CoachAccept;
import com.pb.criconet.models.DateSlotes;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class BookingHistoryFragment extends Fragment implements BookingHistoryAdapter.clickCallback{
    View rootView;
    private RecyclerView coachlist;
    private RequestQueue queue;
    private SharedPreferences prefs;
    private ProgressDialog progressDialog;

    public static BookingHistoryFragment newInstance() {
        return new BookingHistoryFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.coach_fragment, container, false);


        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        queue = Volley.newRequestQueue(getActivity());

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText("Booking History");

        coachlist=rootView.findViewById(R.id.coachlist);
        coachlist.setHasFixedSize(true);
        coachlist.setLayoutManager(new LinearLayoutManager(getActivity()));


        if (Global.isOnline(getActivity())) {
            getBookingHistory();
        } else {
            Global.showDialog(getActivity());
        }



    }
    private void getBookingHistory() {
        progressDialog = Global.getProgressDialog(getActivity(), CCResource.getString(getActivity(), R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_booking_history", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Global.dismissDialog(progressDialog);
                Gson gson = new Gson();
                BookingHistory modelArrayList = gson.fromJson(response, BookingHistory.class);
                coachlist.setAdapter(new BookingHistoryAdapter(getActivity(),modelArrayList.getData(),BookingHistoryFragment.this));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.dismissDialog(progressDialog);
                Global.msgDialog(getActivity(), "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    @Override
    public void buttonClick(String action,String booking_id) {
        if(action.equalsIgnoreCase("join")){
            Intent intent=new Intent(getActivity(), CallActivity.class);
            startActivity(intent);
        }else {
            validateAction(booking_id,action);
        }
    }

    private void validateAction(String booking_id,String action) {
        progressDialog = Global.getProgressDialog(getActivity(), CCResource.getString(getActivity(), R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "booking_action", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Global.dismissDialog(progressDialog);
                Gson gson = new Gson();
                CoachAccept modelArrayList = gson.fromJson(response, CoachAccept.class);
                Toast.makeText(getActivity(),modelArrayList.getData().getMessage() , Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.dismissDialog(progressDialog);
                Global.msgDialog(getActivity(), "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("action", action);
                param.put("booking_id", booking_id);
                param.put("s", SessionManager.get_session_id(prefs));
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

}
