package com.pb.criconet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;
import com.pb.criconet.models.TimeSlot;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.MyViewHolder> {
    private Context context;
    private List<TimeSlot.Datum> data;
    private timeSelect callback;
    public int mSelectedItem = -1;

    public TimeAdapter(Context context,List<TimeSlot.Datum> data,timeSelect callback) {
        this.context = context;
        this.data = data;
        this.callback = callback;
    }

    @Override
    public TimeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_time, parent, false);
        TimeAdapter.MyViewHolder vh = new TimeAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TimeAdapter.MyViewHolder holder, final int position) {
        // set the data in items
        holder.textView.setText(data.get(position).getSlotValue());
       // holder.radio.setChecked(position == mSelectedItem);

        holder.radio.setOnClickListener(view -> {
            callback.getSlotId(data.get(position).getSlotId());
            data.get(position).setActive(true);
           // mSelectedItem = holder.getAdapterPosition();
           // notifyDataSetChanged();
        });

          if (data.get(position).getIs_selected()!=null&&data.get(position).getIs_selected().equalsIgnoreCase("1")){
              holder.radio.setChecked(true);
          }else if (data.get(position).getIs_booked()!=null&&data.get(position).getIs_booked().equalsIgnoreCase("1")){
              holder.radio.setChecked(true);
          }else {
              holder.radio.setChecked(false);
          }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RadioButton radio;

        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            textView=itemView.findViewById(R.id.textView);
            radio=itemView.findViewById(R.id.radio);

        }
    }

    public interface timeSelect{
        public void getSlotId(String sloteId);
    }
}