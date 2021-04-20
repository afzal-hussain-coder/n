package com.pb.criconet.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pb.criconet.activity.CoachDetailsActivity;
import com.pb.criconet.R;
import com.pb.criconet.models.CoachList;

import java.util.List;

public class CoachListAdapter extends RecyclerView.Adapter<CoachListAdapter.MyViewHolder> {

    private Context context;
    private List<CoachList.Datum> mdata;
    public CoachListAdapter(List<CoachList.Datum> data,Context context) {
        this.context = context;
        this.mdata=data;
    }

    @Override
    public CoachListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_coch_list, parent, false);
        CoachListAdapter.MyViewHolder vh = new CoachListAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CoachListAdapter.MyViewHolder holder, final int position) {
        // set the data in items

        Glide.with(context).load(mdata.get(position).getAvatar())
                .into(holder.image);
        holder.tvCoachName.setText(mdata.get(position).getName());
        holder.tvCoacTitle.setText(mdata.get(position).getProfileTitle());
        holder.tvDiscription.setText(mdata.get(position).getAboutCoachProfile());
        holder.tvPrice.setText("Price: "+"\u20B9" +mdata.get(position).getChargeAmount());

        holder.btnDetails.setOnClickListener(view -> {
            Intent intent =new Intent(context,CoachDetailsActivity.class);
            intent.putExtra("coachId",mdata.get(position).getUserId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView btnDetails;
        TextView tvCoacTitle;
        TextView tvCoachName;
        TextView tvDiscription;
        TextView tvPrice;
        ImageView image;

        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            btnDetails=itemView.findViewById(R.id.btnDetails);
            tvCoacTitle=itemView.findViewById(R.id.tvCoacTitle);
            tvCoachName=itemView.findViewById(R.id.tvCoachName);
            tvDiscription=itemView.findViewById(R.id.tvDiscription);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            image=itemView.findViewById(R.id.image);

        }
    }
}