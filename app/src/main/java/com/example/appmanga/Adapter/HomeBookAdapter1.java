package com.example.appmanga.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanga.Model.Book;
import com.example.appmanga.R;
import com.example.appmanga.intro_manga_before_read;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeBookAdapter1 extends RecyclerView.Adapter<HomeBookAdapter1.HolderBook> {
    Context context;
    public ArrayList<Book> list;
    private clickListener listener;

    public HomeBookAdapter1(Context context, ArrayList<Book> list, clickListener listener) {
        this.context = context;
        this.list = list;
        this.listener=listener;
    }

    @NonNull
    @Override
    public HomeBookAdapter1.HolderBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_book_home_1,parent,false);
        return new HomeBookAdapter1.HolderBook(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBookAdapter1.HolderBook holder, int position) {
        Book book = list.get(position);
        holder.book_title.setText(book.getBook_title());
        holder.book_description.setText(book.getBook_description());
        holder.book_category.setText(book.getCategories());
        String dsc = book.getBook_description();
        int i = position;
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(list.get(i));
                Intent intent = new Intent(context, intro_manga_before_read.class);
                intent.putExtra("book_id", book.getBookId());
                intent.putExtra("dsc",dsc);
                context.startActivity(intent);
            }
        });

        String image = book.getThumbnail();
        Picasso.get().load(image).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return list.size() > 3 ? 3 : list.size();
    }

    public static class HolderBook extends RecyclerView.ViewHolder{

        TextView book_title, book_description, book_category;
        CardView cardView;
        ImageView thumbnail;

        public HolderBook(@NonNull View itemView) {
            super(itemView);
            book_title = itemView.findViewById(R.id.tv_title);
            book_description = itemView.findViewById(R.id.tv_description);
            book_category = itemView.findViewById(R.id.tv_category);
            thumbnail= itemView.findViewById(R.id.iv_thumbnail);
            cardView = itemView.findViewById(R.id.parent_layout1);
        }
    }
}