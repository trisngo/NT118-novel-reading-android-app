package com.example.appmanga.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanga.Book;
import com.example.appmanga.FilterBook;
import com.example.appmanga.R;
//import com.example.appmanga.databinding.ItemBookBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterBook extends RecyclerView.Adapter<AdapterBook.HolderBook>{
    Context context;
    public ArrayList<Book> list, filterList;
    //private ItemBookBinding binding;
    private FilterBook filter;

    public AdapterBook(Context context, ArrayList<Book> list) {
        this.context = context;
        this.list = list;
        this.filterList = list;

    }


    @NonNull
    @Override
    public HolderBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_book,parent,false);
        return new HolderBook(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBook holder, int position) {
        Book book = list.get(position);
        holder.book_title.setText(book.getBook_title());
        holder.book_category.setText(book.getBookId());
        holder.book_author.setText(book.getAuthor_name());
        String image = book.getThumbnail();
        Picasso.get().load(image).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterBook(filterList, this);
        }
        return filter;
    }

    public static class HolderBook extends RecyclerView.ViewHolder{

        TextView book_title, book_author, book_category;
        ImageView thumbnail;


        public HolderBook(@NonNull View itemView) {
            super(itemView);
            book_title = itemView.findViewById(R.id.titleTv);
            book_author = itemView.findViewById(R.id.authorTv);
            book_category = itemView.findViewById(R.id.categoryTv);
            thumbnail= itemView.findViewById(R.id.imgBook);

        }
    }
}



