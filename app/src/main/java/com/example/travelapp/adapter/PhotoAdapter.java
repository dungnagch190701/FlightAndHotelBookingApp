package com.example.travelapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.travelapp.R;

import java.util.List;

public class PhotoAdapter extends PagerAdapter {

    private Context context;
    private List<String> imageURL;

    public PhotoAdapter(Context context, List<String> imageURL) {
        this.context = context;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_photo,container,false);
        ImageView imageView = view.findViewById(R.id.image);
        String url = imageURL.get(position);
        if (url!= null){
            Glide.with(context).load(url).into(imageView);
        }

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if (imageURL!=null){
            return imageURL.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
