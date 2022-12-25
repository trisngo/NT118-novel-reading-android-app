package com.example.appmanga.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanga.Book;
import com.example.appmanga.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterHomeBook2 extends RecyclerView.Adapter<AdapterHomeBook2.HolderBook> {
    Context context;
    public ArrayList<Book> list;
    private clickListener listener;

    public AdapterHomeBook2(Context context, ArrayList<Book> list,clickListener listener) {
        this.context = context;
        this.list = list;
        this.listener=listener;
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
        holder.book_title.setText(book.getBook_title());
        holder.book_category.setText(book.getCategories());
        holder.book_likes.setText(String.valueOf(book.getLikes()));
        int i =position;
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(list.get(i));
            }
        });
        String image = book.getThumbnail();
        Picasso.get().load(image).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return list.size() > 10 ? 10 : list.size();
    }

    public static class HolderBook extends RecyclerView.ViewHolder{

        TextView book_title, book_category, book_likes;
        CardView cardView;
        ImageView thumbnail;

        public HolderBook(@NonNull View itemView) {
            super(itemView);
            book_title = itemView.findViewById(R.id.tv_title);
            book_category = itemView.findViewById(R.id.tv_category);
            book_likes = itemView.findViewById(R.id.tv_likes_number);
            thumbnail= itemView.findViewById(R.id.iv_thumbnail);
            cardView = itemView.findViewById(R.id.parent_layout1);
        }
    }
}
