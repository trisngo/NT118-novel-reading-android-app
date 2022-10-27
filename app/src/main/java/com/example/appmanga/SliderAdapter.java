package com.example.appmanga;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    public SliderAdapter(Context context) {
        this.context = context;
    }
    int images[] = {
            R.drawable.biasach1b,
            R.drawable.anh1,
            R.drawable.comment1,
    };

    int headings[] = {
            R.string.frist_text_title,
            R.string.second_text_title,
            R.string.third_text_title,
    };

    int descrip[] = {
            R.string.frist_text_des,
            R.string.second_text_des,
            R.string.third_text_des,
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_slides,container,false);
        ImageView imageView = view.findViewById(R.id.image);
        TextView heading =view.findViewById((R.id.bigtext));
        TextView des =view.findViewById((R.id.smalltext));

        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);
        des.setText(descrip[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
        //super.destroyItem(container, position, object);
    }
}

