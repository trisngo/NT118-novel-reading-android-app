package com.example.appmanga.Adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanga.Model.Book;
import com.example.appmanga.Model.comment;
import com.example.appmanga.R;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.commentAdapterViewHolder> {
    Context context;
    public ArrayList<comment> list_comment;


    public commentAdapter(Context context,ArrayList<comment> list_comment) {
        this.context = context;
        this.list_comment = list_comment;
    }
    @NonNull
    @Override
    public commentAdapter.commentAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return new commentAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull commentAdapter.commentAdapterViewHolder holder, int position) {
        comment comment = list_comment.get(position);
        holder.user_name.setText(comment.user_comment);
        holder.content_comment.setText(comment.content_comment);
    }
    @Override
    public int getItemCount() {
        return list_comment.size();
    }

    public class commentAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView user_name, content_comment;
        public commentAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.name_user_comment);
            content_comment=itemView.findViewById(R.id.content_comment);
        }
    }
}
