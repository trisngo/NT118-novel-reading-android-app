package com.example.appmanga;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanga.ExtraFeature.FilterBook;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder>{

    Context context;
    public ArrayList<Book> list, filterList;
    private FilterBook filter;
    public MangaAdapter(Context context, ArrayList<Book> list) {
        this.context = context;
        this.list = list;
        this.filterList = list;
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
            holder.book_category.setText(book.getBookId());
            holder.book_author.setText(book.getAuthor_name());
            String image = book.getThumbnail();
            Picasso.get().load(image).into(holder.thumbnail);
            holder.rank_number.setText("#1");
            holder.fire.setImageResource(R.drawable.ic_baseline_local_fire_department_24);
        }
        if (i == 2) {
            holder.book_title.setText(book.getBook_title());
            holder.book_category.setText(book.getBookId());
            holder.book_author.setText(book.getAuthor_name());
            String image = book.getThumbnail();
            Picasso.get().load(image).into(holder.thumbnail);
            holder.rank_number.setText("#2");
            holder.fire.setImageResource(R.drawable.ic_baseline_local_fire_department_24_1);
        }
        if (i == 3) {
            holder.book_title.setText(book.getBook_title());
            holder.book_category.setText(book.getBookId());
            holder.book_author.setText(book.getAuthor_name());
            String image = book.getThumbnail();
            Picasso.get().load(image).into(holder.thumbnail);
            holder.rank_number.setText("#3");
            holder.fire.setImageResource(R.drawable.ic_baseline_local_fire_department_24_2);
        }
        if(i>3){
            holder.book_title.setText(book.getBook_title());
            holder.book_category.setText(book.getBookId());
            holder.book_author.setText(book.getAuthor_name());
            String image = book.getThumbnail();
            Picasso.get().load(image).into(holder.thumbnail);
            holder.rank_number.setText("#"+ i);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class MangaViewHolder extends RecyclerView.ViewHolder {
        TextView book_title, book_author, book_category,rank_number;
        ImageView thumbnail,fire;
        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);
            book_title = itemView.findViewById(R.id.titleTv);
            book_author = itemView.findViewById(R.id.authorTv);
            book_category = itemView.findViewById(R.id.categoryTv);
            thumbnail= itemView.findViewById(R.id.imgBook);
            rank_number=itemView.findViewById(R.id.rank_number);
            fire= itemView.findViewById(R.id.imagefire);
        }
    }
}
