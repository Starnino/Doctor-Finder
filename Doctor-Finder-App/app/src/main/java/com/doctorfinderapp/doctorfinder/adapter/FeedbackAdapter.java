package com.doctorfinderapp.doctorfinder.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.R;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by fedebyes on 05/03/16.
 */
public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {


    List<ParseObject> feedbacklist;

    public FeedbackAdapter(List<ParseObject> feedbacks) {
        this.feedbacklist = feedbacks;
    }

    @Override
    public FeedbackAdapter.FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_item, parent, false);
        return new FeedbackViewHolder(v);

    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {

        TextView feedback_text;

        FeedbackViewHolder(View itemView) {
            super(itemView);
            feedback_text = (TextView) itemView.findViewById(R.id.feedback_text);
        }
    }


    @Override
    public void onBindViewHolder(FeedbackAdapter.FeedbackViewHolder holder, int position) {
        String text = "";
        for (int i = 0; i < 100; i++) {
            text += "porco dio" + position + " ";
        }
        holder.feedback_text.setText(text);
    }

    @Override
    public int getItemCount() {
        Log.d("Feedback", "" + feedbacklist.size());
        return feedbacklist.size();
    }



}
