package com.pb.criconet.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.models.PageModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


public class Pages extends AppCompatActivity {
    SharedPreferences prefs;
    ListView friends_list;
    EditText etxt_search;
    TextView notfound;
    ArrayList<PageModel> arrayList_new, modelArrayList;
    Friends_Adapter adapter;
    ProgressDialog progress;
    RequestQueue queue;

    // For pagination.
    int page = 1;
    private int visibleThreshold = 10;
    private int previousTotal = 0;
    private boolean loading = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

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
        mTitle.setText(R.string.my_pages);

        prefs = PreferenceManager.getDefaultSharedPreferences(Pages.this);
        queue = Volley.newRequestQueue(Pages.this);
        progress = new ProgressDialog(Pages.this);
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);

        friends_list = (ListView) findViewById(R.id.friends_list);
        etxt_search = (EditText) findViewById(R.id.etxt_search);
        notfound = (TextView) findViewById(R.id.notfound);
        notfound.setText("Sorry No Page Found");
        modelArrayList = new ArrayList<>();

        etxt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Timber.e(String.valueOf(s));
                if (s.length() > 0) {
//                    searchFriends(s.toString());
                    arrayList_new = new ArrayList<>();
                    for (int i = 0; i < modelArrayList.size(); i++) {
                        if (modelArrayList.get(i).getPage_title().toLowerCase().contains(s.toString().toLowerCase())) {
                            arrayList_new.add(modelArrayList.get(i));
                        }
                    }
                    adapter = new Friends_Adapter(Pages.this, arrayList_new);
                    friends_list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (arrayList_new.size() == 0) {
                        notfound.setVisibility(View.VISIBLE);
                    } else {
                        notfound.setVisibility(View.GONE);
                    }
                } else {
                    adapter = new Friends_Adapter(Pages.this, modelArrayList);
                    friends_list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (modelArrayList.size() == 0) {
                        notfound.setVisibility(View.VISIBLE);
                    } else {
                        notfound.setVisibility(View.GONE);
                    }
//                    RefreshList();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                try {
//                    if (arrayList_new.isEmpty()) {
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });


//        friends_list.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (loading) {
//                    if (totalItemCount > previousTotal) {
//                        loading = false;
//                        previousTotal = totalItemCount;
//                        page++;
//                    }
//                }
//                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
//                    // I load the next page of gigs using a background task,
//                    // but you can call any function here.
//                    getFriends();
//                    loading = true;
//                }
//            }
//        });

        friends_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String search = etxt_search.getText().toString();
                if (search.length() > 0) {
                    Intent intent = new Intent(Pages.this, PagesDetails.class);
                    intent.putExtra("page_id", arrayList_new.get(position).getPage_id());
//                    intent.putExtra("friendStatus", arrayList_new.get(position).getFriendStatus());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Pages.this, PagesDetails.class);
                    intent.putExtra("page_id", modelArrayList.get(position).getPage_id());
//                    intent.putExtra("friendStatus", modelArrayList.get(position).getFriendStatus());
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        etxt_search.setText("");
        page = 1;
        visibleThreshold = 10;
        previousTotal = 0;
        loading = true;

        RefreshList();
    }

    public void RefreshList() {
        page = 1;
        visibleThreshold = 10;
        previousTotal = 0;
        loading = true;

        modelArrayList = new ArrayList<>();
        adapter = new Friends_Adapter(Pages.this, modelArrayList);
        friends_list.setAdapter(adapter);
        if (Global.isOnline(Pages.this)) {
            getFriends();
            System.out.println("xxxxxxxxxxxxxxxx Search xxxxxxxxxxxxxxx");
        } else {
            Global.showDialog(Pages.this);
        }
    }

    public void getFriends() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "my_pages",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.e("response:  %s", response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                ArrayList<PageModel> object = PageModel.fromJson(jsonObject.getJSONArray("data"));
                                modelArrayList.addAll(object);
                                adapter.notifyDataSetChanged();
                                if (modelArrayList.size() == 0) {
                                    notfound.setVisibility(View.VISIBLE);
                                } else {
                                    notfound.setVisibility(View.GONE);
                                }

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(Pages.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(Pages.this, "Error in server");
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
                        Global.msgDialog(Pages.this, "Error from server");
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    public void createPage() {
        dialog.dismiss();
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "create_page",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.e("response:  %s", response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                RefreshList();
//                                PageModel page = PageModel.fromJson(jsonObject.getJSONObject("data"));
//                                Intent intent = new Intent(Pages.this, EditPage.class);
//                                intent.putExtra("page_id", page.getPage_id());
//                                startActivity(intent);

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
//                                Global.msgDialog(Pages.this, jsonObject.optJSONObject("errors").optString("error_text"));
                                Global.msgDialog(Pages.this, jsonObject.optString("message"));
                            } else {
                                Global.msgDialog(Pages.this, "Error in server");
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
                        Global.msgDialog(Pages.this, "Error from server");
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//                "user_id:1735
//                s:4d9076ab6b1b5254aa8de046c65fd63a5908ee22134451b41ee035cc8eaa0aa8d1de0443860666293f78fa1cdb0e2fda88c2a935950ffdf1
//                page_name:create news
//                page_title:pageeae
//                phone:7532866377
//                page_description:Page description
//                website:https://mywebsite.com
//                company:Criconet online
//                address:Gurgaon

                param.put("user_id", SessionManager.get_user_id(prefs));
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("page_title", page_name);
                param.put("page_name", page_name);
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    String page_name, page_title;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                addPageDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    Dialog dialog;

    private void addPageDialog() {
        dialog = new Dialog(Pages.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_page_dialog);
        final EditText et_page_name = (EditText) dialog.findViewById(R.id.page_name);
        final EditText et_page_desc = (EditText) dialog.findViewById(R.id.page_desc);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_name = et_page_name.getText().toString().trim();
                if (!Global.validateLength(page_name, 5)) {
                    et_page_name.setError(Html.fromHtml("<font color='red'>Page Name must be at least 5 characters.</font>"));
                } else {
                    et_page_name.setError(null);
                    createPage();
                }
            }
        });

        dialog.show();
    }

    public static class Friends_Adapter extends BaseAdapter {
        Activity context;
        ViewHolder holder = null;
        LayoutInflater inflater;
        ArrayList<PageModel> arrayList_image;

        Friends_Adapter(Activity mcontext, ArrayList<PageModel> chatname_list1) {
            // TODO Auto-generated constructor stub
            context = mcontext;
            arrayList_image = chatname_list1;
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrayList_image.size();
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

        @SuppressLint("ViewHolder")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.page_adapter_item, null);
                holder = new ViewHolder();
                holder.relative_dash = (RelativeLayout) convertView.findViewById(R.id.relative_dash);
                holder.user_image = (ImageView) convertView.findViewById(R.id.user_image);
                holder.user_status = (ImageView) convertView.findViewById(R.id.user_status);
                holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
                holder.user_address = (TextView) convertView.findViewById(R.id.user_address);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.list_anim_side);
                holder.relative_dash.startAnimation(scaleUp);
            }

            holder.user_name.setText(arrayList_image.get(position).getPage_title());
//            holder.user_address.setText(arrayList_image.get(position).getAddress());
            Glide.with(context).load(arrayList_image.get(position).getAvatar()).into(holder.user_image);
            holder.user_status.setVisibility(View.VISIBLE);
            holder.user_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditPage.class);
                    intent.putExtra("page_id", arrayList_image.get(position).getPage_id());
                    context.startActivity(intent);
                }
            });

            return convertView;
        }

        private class ViewHolder {
            ImageView user_image, user_status;
            TextView user_name, user_address;
            RelativeLayout relative_dash;
        }
    }

}
