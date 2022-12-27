package com.example.appmanga.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanga.Model.Book;

import com.example.appmanga.databinding.ItemBookBinding;
import com.example.appmanga.intro_manga_before_read;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReadingAdapter extends RecyclerView.Adapter<ReadingAdapter.HolderBookReading>{

    private final Context context;
    private final ArrayList<Book> bookArrayList;

    private ItemBookBinding binding;

    public ReadingAdapter(Context context, ArrayList<Book> bookArrayList) {
        this.context = context;
        this.bookArrayList = bookArrayList;
    }

    @NonNull
    @Override
    public HolderBookReading onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemBookBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderBookReading(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBookReading holder, int position) {
        Book model = bookArrayList.get(position);

        loadBookDetail(model, holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, intro_manga_before_read.class);
                intent.putExtra("book_id", model.getBookId());
                context.startActivity(intent);
            }
        });
    }

    private void loadBookDetail(Book model, HolderBookReading holder) {
        String book_id = model.getBookId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("books");
        ref.child(book_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String booktitle = ""+snapshot.child("book_title").getValue().toString();
                        String bookAuthor = ""+snapshot.child("author_name").getValue().toString();
                        String category = ""+snapshot.child("categories").getValue().toString();
                        String url = ""+snapshot.child("thumbnail").getValue().toString();

                        //
                        model.setBook_title(booktitle);
                        model.setAuthor_name(bookAuthor);
                        model.setCategories(category);
                        model.setThumbnail(url);
                        //
                        holder.titleTv.setText(booktitle);
                        holder.authorTv.setText(bookAuthor);
                        holder.categoryTv.setText(category);
                        //
                        String image = model.getThumbnail();
                        Picasso.get().load(image).into(holder.thumbnail);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }

    class HolderBookReading extends RecyclerView.ViewHolder{

        TextView titleTv, authorTv, categoryTv ;
        ImageView thumbnail;

        public HolderBookReading(@NonNull View itemView) {
            super(itemView);
            titleTv = binding.titleTv;
            authorTv = binding.authorTv;
            categoryTv = binding.categoryTv;
            thumbnail = binding.imgBook;
        }
    }
}
