package com.example.appmanga.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanga.Model.Notify;
import com.example.appmanga.Model.comment;
import com.example.appmanga.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.HolderNotify>{
    Context context;
    public ArrayList<Notify> list;
    private notifyClickListener listener;

    public NotifyAdapter(Context context, ArrayList<Notify> list, notifyClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener=listener;
    }

    @NonNull
    @Override
    public NotifyAdapter.HolderNotify onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_notify,parent,false);
        return new NotifyAdapter.HolderNotify(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyAdapter.HolderNotify holder, int position) {
        Notify notify = list.get(position);
        holder.notify_title.setText(notify.getTitle());
        holder.notify_content.setText(notify.getContent());

        Date now = new Date();
        Date received_time = new Date(notify.getReceivedTime());
        long diff = now.getTime() - received_time.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            holder.received_time.setText(String.valueOf(days) + " ngày trước");
        }
        else if (hours > 0) {
            holder.received_time.setText(String.valueOf(hours) + " giờ trước");
        }
        else if (minutes > 0) {
            holder.received_time.setText(String.valueOf(minutes) + " phút trước");
        }
        else{
            holder.received_time.setText(String.valueOf(seconds) + " giây trước");
        }

        int i = position;
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onItemClick(list.get(i));
                if (notify.getSeen() == 0){
                    holder.notify_title.setTypeface(null, Typeface.BOLD);
                    holder.notify_content.setTypeface(null, Typeface.BOLD);
                    holder.received_time.setTypeface(null, Typeface.BOLD);
                }
                else {
                    holder.notify_title.setTypeface(null, Typeface.NORMAL);
                    holder.notify_content.setTypeface(null, Typeface.NORMAL);
                    holder.received_time.setTypeface(null, Typeface.NORMAL);
                }
                return true;
            }
        });

        if (notify.getSeen() == 0){
            holder.notify_title.setTypeface(null, Typeface.BOLD);
            holder.notify_content.setTypeface(null, Typeface.BOLD);
            holder.received_time.setTypeface(null, Typeface.BOLD);
        }
        else {
            holder.notify_title.setTypeface(null, Typeface.NORMAL);
            holder.notify_content.setTypeface(null, Typeface.NORMAL);
            holder.received_time.setTypeface(null, Typeface.NORMAL);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderNotify extends RecyclerView.ViewHolder{
        TextView notify_title, notify_content, received_time;
        CardView cardView;

        public HolderNotify(@NonNull View itemView) {
            super(itemView);
            notify_title = itemView.findViewById(R.id.tv_notify_title);
            notify_content = itemView.findViewById(R.id.tv_notify_content);
            received_time = itemView.findViewById(R.id.tv_received_time);
            cardView = itemView.findViewById(R.id.cv_notify);
        }
    }
}
