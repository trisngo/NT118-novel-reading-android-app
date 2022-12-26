package com.example.appmanga.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanga.Book;
import com.example.appmanga.ExtraFeature.FilterBook;
import com.example.appmanga.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder>{

    Context context;
    private clickListener listener;
    public ArrayList<Book> list, filterList;
    private FilterBook filter;
    public MangaAdapter(Context context, ArrayList<Book> list,clickListener listener) {
        this.context = context;
        this.list = list;
        this.filterList = list;
        this.listener=listener;

    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_books_layout,parent,false);
        return new MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        Book book = list.get(position);
        int i=position+1;
        if (i == 1) {
            holder.book_title.setText(book.getBook_title());
            holder.book_category.setText("#" + book.getCategories());
            holder.book_author.setText(book.getAuthor_name());
            String image = book.getThumbnail();
            Picasso.get().load(image).into(holder.thumbnail);
            holder.rank_number.setText("TOP1");
            holder.bg_number_rank.setBackgroundResource(R.drawable.bg_num_rank);
        }
        if (i == 2) {
            holder.book_title.setText(book.getBook_title());
            holder.book_category.setText("#" + book.getCategories());
            holder.book_author.setText(book.getAuthor_name());
            String image = book.getThumbnail();
            Picasso.get().load(image).into(holder.thumbnail);
            holder.rank_number.setText("TOP2");
            holder.bg_number_rank.setBackgroundResource(R.drawable.bg_num_rank);
        }
        if (i == 3) {
            holder.book_title.setText(book.getBook_title());
            holder.book_category.setText("#" + book.getCategories());
            holder.book_author.setText(book.getAuthor_name());
            String image = book.getThumbnail();
            Picasso.get().load(image).into(holder.thumbnail);
            holder.rank_number.setText("TOP3");
            holder.bg_number_rank.setBackgroundResource(R.drawable.bg_num_rank);
        }
        if(i>3){
            holder.book_title.setText(book.getBook_title());
            holder.book_category.setText("#" + book.getCategories());
            holder.book_author.setText(book.getAuthor_name());
            String image = book.getThumbnail();
            Picasso.get().load(image).into(holder.thumbnail);
            holder.rank_number.setText("TOP"+ i);
        }
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(list.get(i-1));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class MangaViewHolder extends RecyclerView.ViewHolder {
        TextView book_title, book_author, book_category,rank_number;
        ImageView thumbnail;
        RelativeLayout bg_number_rank;
        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);
            book_title = itemView.findViewById(R.id.titleTv);
            book_author = itemView.findViewById(R.id.authorTv);
            book_category = itemView.findViewById(R.id.categoryTv);
            thumbnail= itemView.findViewById(R.id.imgBook);
            rank_number=itemView.findViewById(R.id.rank_number);
            bg_number_rank= itemView.findViewById((R.id.backgournd_number_ranking));
        }
    }
}
