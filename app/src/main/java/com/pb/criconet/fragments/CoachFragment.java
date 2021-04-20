package com.pb.criconet.fragments;

import android.app.ProgressDialog;
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
import androidx.lifecycle.ViewModelProvider;
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
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CCResource;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.adapters.CoachListAdapter;
import com.pb.criconet.models.CoachList;
import com.pb.criconet.models.NewPostModel;
import com.pb.criconet.models.UserIdInput;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


public class CoachFragment extends Fragment {
    View rootView;
    private ProgressDialog progressDialog;
    private SharedPreferences prefs;
    private RequestQueue queue;
    private RecyclerView recyclerView;

    public static CoachFragment newInstance() {
        return new CoachFragment();
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
        mTitle.setText("Coach List");

        recyclerView=rootView.findViewById(R.id.coachlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        if (Global.isOnline(getActivity())) {
            getCoachList();
        } else {
            Global.showDialog(getActivity());
        }

    }


    private void getCoachList() {
        progressDialog = Global.getProgressDialog(getActivity(), CCResource.getString(getActivity(), R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_coach_lists", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Global.dismissDialog(progressDialog);
                Gson gson = new Gson();
                CoachList modelArrayList = gson.fromJson(response, CoachList.class);

                if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    recyclerView.setAdapter(new CoachListAdapter(modelArrayList.getData(),getActivity()));

                } else {
                    Toast.makeText(getActivity(), modelArrayList.getErrors().getErrorText(), Toast.LENGTH_SHORT).show();
                }

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

}
