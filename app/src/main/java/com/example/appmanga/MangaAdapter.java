package com.example.appmanga;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder>{

    private List<Manga> listmanga;
    public MangaAdapter(List<Manga> ls) {
        listmanga = ls;
    }
    public void setData(List<Manga> list){
        this.listmanga=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_books_layout,parent,false);
        return new MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
                int value_ranking= position+1;
                Manga manga =listmanga.get(position);
                holder.img_bool.setImageResource(manga.getImg());
                holder.tvName.setText(manga.getName());
                holder.tvRank.setText(manga.getRanking()+" "+value_ranking);
    }

    @Override
    public int getItemCount() {
        return listmanga.size();
    }

    public class MangaViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvRank;
        private ImageView img_bool;
        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName =itemView.findViewById(R.id.tvName);
            tvRank=itemView.findViewById(R.id.tvNumberRanking);
            img_bool=itemView.findViewById((R.id.img_book));
        }
    }
}
