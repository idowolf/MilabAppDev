package com.idowolf.awesomelistapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by idowo on 12/11/2017.
 */

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {

    private List<String> mDataSet;
    private Context mContext;

    public MyListAdapter(Context context, List<String> dataSet) {
        this.mContext = context;
        this.mDataSet = dataSet;
    }

    @Override
    public MyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.list_entry, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyListAdapter.ViewHolder viewHolder, int position) {
        int resourceId = mContext.getResources().getIdentifier(mDataSet.get(position), "drawable", mContext.getPackageName());
        String input = mDataSet.get(position);
        viewHolder.textView.setText(String.format("%s%s", input.substring(0, 1).toUpperCase(), input.substring(1)));
        viewHolder.imageView.setImageResource(resourceId);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.personImage);
            textView = (TextView) view.findViewById(R.id.personName);
        }
    }
}
