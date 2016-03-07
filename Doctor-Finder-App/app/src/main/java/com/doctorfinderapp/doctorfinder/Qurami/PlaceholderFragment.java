package com.doctorfinderapp.doctorfinder.Qurami;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.doctorfinderapp.doctorfinder.R;
import com.qurami.android.link.QuramiLink;

public class PlaceholderFragment extends Fragment {

    EditText editTextOfficeLink;
    Button openOfficeButton;

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qurami, container, false);

        editTextOfficeLink = (EditText) rootView.findViewById(R.id.editTextOfficeLink);

        openOfficeButton = (Button) rootView.findViewById(R.id.openOfficeButton);
        openOfficeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openQuramiOffice();
            }
        });

        return rootView;
    }

    private void openQuramiOffice() {
        FragmentActivity contextActivity = getActivity();
        String officeLink = editTextOfficeLink.getText().toString();

        QuramiLink.openQuramiOfficeWithOfficeLink(contextActivity, officeLink);
    }
}