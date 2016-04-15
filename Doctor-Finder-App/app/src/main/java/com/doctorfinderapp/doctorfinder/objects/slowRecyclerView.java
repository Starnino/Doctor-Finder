package com.doctorfinderapp.doctorfinder.objects;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by francesco on 15/04/16.
 */
public class slowRecyclerView extends RecyclerView{


    Context context;

    public slowRecyclerView(Context context) {
        super(context);
        this.context = context;
    }


    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityY *= 0.7;
        return super.fling(velocityX, velocityY);
    }
}
