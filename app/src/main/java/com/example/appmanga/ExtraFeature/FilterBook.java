package com.example.appmanga.ExtraFeature;

import android.annotation.SuppressLint;
import android.widget.Filter;


import com.example.appmanga.Adapter.SearchAdapter;
import com.example.appmanga.Model.Book;

import java.util.ArrayList;

public class FilterBook extends Filter {

    ArrayList<Book> filterList;
    //adapter in which filter need to be implemented
    SearchAdapter adapterBook;

    public FilterBook(ArrayList<Book> filterList, SearchAdapter adapterBook) {
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
                if (filterList.get(i).getBook_title().toUpperCase().contains(constraint)
                        || filterList.get(i).getAuthor_name().toUpperCase().contains(constraint)
                        || filterList.get(i).getCategories().toUpperCase().contains(constraint)


                ) {
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
