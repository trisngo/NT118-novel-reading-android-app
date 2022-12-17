package com.example.appmanga.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanga.Book;
import com.example.appmanga.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterHomeBook2 extends RecyclerView.Adapter<AdapterHomeBook2.HolderBook> {
    Context context;
    public ArrayList<Book> list;

    public AdapterHomeBook2(Context context, ArrayList<Book> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterHomeBook2.HolderBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_book_home_2,parent,false);
        return new AdapterHomeBook2.HolderBook(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHomeBook2.HolderBook holder, int position) {
        Book book = list.get(position);
        Log.d("debug",book.getBook_title());
        holder.book_title.setText(book.getBook_title());
        holder.book_category.setText(book.getBookId());

        String image = book.getThumbnail();
        Picasso.get().load(image).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class HolderBook extends RecyclerView.ViewHolder{

        TextView book_title, book_category;
        ImageView thumbnail;

        public HolderBook(@NonNull View itemView) {
            super(itemView);
            book_title = itemView.findViewById(R.id.tv_title);
            book_category = itemView.findViewById(R.id.tv_category);
            thumbnail= itemView.findViewById(R.id.iv_thumbnail);
        }
    }
}
