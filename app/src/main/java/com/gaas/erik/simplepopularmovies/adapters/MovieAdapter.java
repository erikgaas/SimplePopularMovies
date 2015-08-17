package com.gaas.erik.simplepopularmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.gaas.erik.simplepopularmovies.R;
import com.gaas.erik.simplepopularmovies.models.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by erik on 8/16/15.
 */
public class MovieAdapter extends ArrayAdapter<Result> {

    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";

    private Context mContext;
    int layoutResourceId;
    ArrayList<Result> data = null;


    public MovieAdapter(Context context, int layoutResourceId, ArrayList<Result> data) {
        super(context, layoutResourceId, data);
        mContext = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MovieHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MovieHolder();
            holder.imgPoster = (ImageView)row.findViewById(R.id.poster_atom);

            row.setTag(holder);
        }
        else {
            holder = (MovieHolder)row.getTag();
        }
        Result result = data.get(position);
        Picasso.with(mContext).load(BASE_IMAGE_URL + result.getPosterPath()).into(holder.imgPoster);



        return row;
    }

    static class MovieHolder
    {
        ImageView imgPoster;
    }
}
