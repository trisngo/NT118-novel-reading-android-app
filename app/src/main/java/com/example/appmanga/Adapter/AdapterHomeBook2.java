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

import java.text.DecimalFormat;
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
        holder.book_likes.setText(prettyCount(book.getLikes()));
        holder.book_views.setText(prettyCount(book.getViews()));
        String dsc = book.getBook_description();
        int i =position;
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
        return list.size() > 10 ? 10 : list.size();
    }

    public static class HolderBook extends RecyclerView.ViewHolder{

        TextView book_title, book_category, book_likes, book_views;
        CardView cardView;
        ImageView thumbnail;

        public HolderBook(@NonNull View itemView) {
            super(itemView);
            book_title = itemView.findViewById(R.id.tv_title);
            book_category = itemView.findViewById(R.id.tv_category);
            book_likes = itemView.findViewById(R.id.tv_likes_number);
            book_views = itemView.findViewById(R.id.tv_views);
            thumbnail= itemView.findViewById(R.id.iv_thumbnail);
            cardView = itemView.findViewById(R.id.parent_layout1);
        }
    }

    public String prettyCount(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }
}
