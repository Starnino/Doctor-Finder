package com.doctorfinderapp.doctorfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctorfinderapp.doctorfinder.Class.Doctor;
import com.doctorfinderapp.doctorfinder.Class.Feedback;
import com.doctorfinderapp.doctorfinder.DoctorProfileActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.functions.Util;

import java.util.List;

/**
 * Created by fedebyes on 05/03/16.
 */
public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {


    @Override
    public FeedbackAdapter.FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_item, parent, false);
        return new FeedbackViewHolder(v);

    }

    @Override
    public void onBindViewHolder(FeedbackAdapter.FeedbackViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return feedbacklist.size();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        FeedbackViewHolder(View itemView) {
            super(itemView);
        }
    }

    List<Feedback> feedbacklist;

    public FeedbackAdapter(List<Feedback> feedbacks) {
        super();
        this.feedbacklist = feedbacks;
    }


}
