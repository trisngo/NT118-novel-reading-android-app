package com.example.appmanga;

import android.annotation.SuppressLint;
import android.widget.Filter;


import com.example.appmanga.Adapter.AdapterBook;

import java.util.ArrayList;

public class FilterBook extends Filter {

    ArrayList<Book> filterList;
    //adapter in which filter need to be implemented
    AdapterBook adapterBook;

    public FilterBook(ArrayList<Book> filterList, AdapterBook adapterBook) {
        this.filterList = filterList;
        this.adapterBook = adapterBook;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();
            ArrayList<Book> filteredModels = new ArrayList<>();
            for(int i = 0; i<filterList.size(); i++){
                if (filterList.get(i).getBook_title().toUpperCase().contains(constraint) || filterList.get(i).getAuthor_name().toUpperCase().contains(constraint)) {
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;
        }else{
            results.count = filterList.size();
            results.values = filterList;
        }

        return results;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterBook.list = (ArrayList<Book>)results.values;
    //notify changes
        adapterBook.notifyDataSetChanged();
    }
}
