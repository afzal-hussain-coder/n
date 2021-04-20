package com.pb.criconet.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pb.criconet.activity.Play_Live_Stream_Single;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.models.LiveStreamingModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class LiveMatches extends Fragment {
    private SharedPreferences prefs;

    private View rootView;
    private ListView weeklist;
    private ArrayList<LiveStreamingModel> data;
    private Week_Adapter adapter;
    private ProgressDialog progress;
    private RequestQueue queue;
    private TextView notfound;

    public static LiveMatches newInstance() {
        return new LiveMatches();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.live_matches, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText("Live Matches");

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading");
        progress.setCancelable(false);

        weeklist = (ListView) rootView.findViewById(R.id.week_list);
        notfound = (TextView) rootView.findViewById(R.id.notfound);



        weeklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Fragment fragment = new WeekExcersiceList();
//                Bundle bundle = new Bundle();
//                bundle.putString("day", data.get(position).getDay());
//                bundle.putString("id", data.get(position).getId());
//                fragment.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getHelp();
    }

    private void getHelp() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_live_streaming_lists",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                data = new ArrayList<>();
                                data = LiveStreamingModel.fromJson(jsonObject.getJSONArray("data"));
                                if (data.size() == 0) {
                                    notfound.setVisibility(View.VISIBLE);
                                } else {
                                    notfound.setVisibility(View.GONE);
                                    adapter = new Week_Adapter(getActivity(), data);
                                    weeklist.setAdapter(adapter);
                                }


                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(getActivity(), "Error in server");
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
                        Global.msgDialog(getActivity(), "Error from server");
//                Global.msgDialog(getActivity(), "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));

                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public static class Week_Adapter extends BaseAdapter {
        Activity context;
        Week_Adapter.ViewHolder holder = null;
        LayoutInflater inflater;
        ArrayList<LiveStreamingModel> arrayList_image;

        Week_Adapter(Activity mcontext, ArrayList<LiveStreamingModel> chatname_list1) {
            // TODO Auto-generated constructor stub
            context = mcontext;
            arrayList_image = chatname_list1;
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrayList_image.size();
//            return 10;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        private class ViewHolder {
            ImageView roundedImageView, play;
            TextView ground_name, team_name, location;
            Button play_icon;
            ConstraintLayout relative_dash;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.stream_adapter, null);
                holder = new Week_Adapter.ViewHolder();
                holder.relative_dash = convertView.findViewById(R.id.relative_dash);
                holder.roundedImageView = (ImageView) convertView.findViewById(R.id.roundedImageView);
                holder.ground_name = (TextView) convertView.findViewById(R.id.ground_name);
//                holder.team_name = (TextView) convertView.findViewById(R.id.team_name);
                holder.location = (TextView) convertView.findViewById(R.id.location);
                holder.play = (ImageView) convertView.findViewById(R.id.logo_imageview);
//                holder.play_icon = (Button) convertView.findViewById(R.id.play_icon);
                convertView.setTag(holder);
            } else {
                holder = (Week_Adapter.ViewHolder) convertView.getTag();
                Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.list_anim_side);
                holder.relative_dash.startAnimation(scaleUp);
            }
            holder.ground_name.setText(arrayList_image.get(position).getTitle());
//            holder.team_name.setText(arrayList_image.get(position).getTeam_name());
            holder.location.setText(arrayList_image.get(position).getDesc());
            Glide.with(context).load(arrayList_image.get(position).getCover()).dontAnimate()
                    .into(holder.play);
//            try {
//                Glide.with(context).load(arrayList_image.get(position).get())
//                        .apply(new RequestOptions().placeholder(R.drawable.app_icon).dontAnimate())
//                        .into(holder.roundedImageView);
//            } catch (Exception e) {
//                holder.roundedImageView.setImageResource(R.drawable.app_icon);
//            }

            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, Play_Stream.class);
//                    Intent intent = new Intent(context, Play_Live_Stream.class);
                    Intent intent = new Intent(context, Play_Live_Stream_Single.class);
                    intent.putExtra("data", LiveStreamingModel.toJson(arrayList_image.get(position)).toString());
                    context.startActivity(intent);
                }
            });

            return convertView;
        }
    }
}
