package com.pb.criconet.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.activity.BookingDetails;
import com.pb.criconet.models.BookingHistory;

import java.util.List;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.MyViewHolder> {

    private Context context;
    private List<BookingHistory.Datum> data;
    private clickCallback callback;
    public BookingHistoryAdapter(Context context,List<BookingHistory.Datum> data,clickCallback callback) {
        this.context = context;
        this.data = data;
        this.callback = callback;
    }

    @Override
    public BookingHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_booking_history, parent, false);
        BookingHistoryAdapter.MyViewHolder vh = new BookingHistoryAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(BookingHistoryAdapter.MyViewHolder holder, final int position) {
        // set the data in items

        Glide.with(context).load(data.get(position).getAvatar())
                .into(holder.ivProfileImage);
        holder.tvCoachName.setText(data.get(position).getName());
        holder.tvBookingDateTime.setText(data.get(position).getBookingSlotDate());
        holder.tvTime.setText(data.get(position).getBookingSlotTxt());

        holder.constraint.setOnClickListener(view -> {
            Intent intent=new Intent(context, BookingDetails.class);
            intent.putExtra("MyClass", data.get(position));
            context.startActivity(intent);
        });

        if(data.get(position).getBtn1()!=null&&!data.get(position).getBtn1().equalsIgnoreCase("")){
            holder.btn1.setText(data.get(position).getBtn1());
            holder.btn1.setVisibility(View.VISIBLE);
        }else {
            holder.btn1.setVisibility(View.GONE);
        }

        if(data.get(position).getBtn2()!=null&&!data.get(position).getBtn2().equalsIgnoreCase("")){
            holder.btn2.setText(data.get(position).getBtn2());
            holder.btn2.setVisibility(View.VISIBLE);
        }else {
            holder.btn2.setVisibility(View.GONE);
        }
        holder.btn1.setOnClickListener(view -> {
            if(holder.btn1.getText().toString().equalsIgnoreCase("Accept")){
                callback.buttonClick("accept",data.get(position).getId());

            }else if(holder.btn1.getText().toString().equalsIgnoreCase("Join")){
                callback.buttonClick("join",data.get(position).getId());

            }else if(holder.btn1.getText().toString().equalsIgnoreCase("Cancel")){
                callback.buttonClick("cancel",data.get(position).getId());

            }else if(holder.btn1.getText().toString().equalsIgnoreCase("Deny")){
                callback.buttonClick("deny",data.get(position).getId());

            }
        });

        holder.btn2.setOnClickListener(view -> {
            if(holder.btn2.getText().toString().equalsIgnoreCase("Accept")){
                callback.buttonClick("accept",data.get(position).getId());

            }else if(holder.btn2.getText().toString().equalsIgnoreCase("Join")){
                callback.buttonClick("join",data.get(position).getId());

            }else if(holder.btn2.getText().toString().equalsIgnoreCase("Cancel")){
                callback.buttonClick("cancel",data.get(position).getId());

            }else if(holder.btn2.getText().toString().equalsIgnoreCase("Deny")){
                callback.buttonClick("deny",data.get(position).getId());

            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvCoachName;
        TextView tvTime;
        TextView tvBookingDateTime;
        TextView btn1;
        TextView btn2;
        ConstraintLayout constraint;
        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            ivProfileImage=itemView.findViewById(R.id.ivProfileImage);
            tvCoachName=itemView.findViewById(R.id.tvCoachName);
            tvTime=itemView.findViewById(R.id.tvTime);
            tvBookingDateTime=itemView.findViewById(R.id.tvBookingDateTime);
            constraint=itemView.findViewById(R.id.constraint);
            btn1=itemView.findViewById(R.id.btn1);
            btn2=itemView.findViewById(R.id.btn2);

        }
    }

    public interface clickCallback{
        public void buttonClick(String action,String booking_id);
    }
}