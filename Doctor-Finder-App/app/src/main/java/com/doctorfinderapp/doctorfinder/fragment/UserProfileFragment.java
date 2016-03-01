package com.doctorfinderapp.doctorfinder.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.parse.ParseObject;

/**
 * Created by giovanni on 2/24/16.
 */
public class UserProfileFragment extends Fragment {

    private static ParseObject user;
    private TextView nome;
    private TextView cognome;
    private TextView email;
    private TextView ddn;
    private TextView facebookid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //@Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //user =  ParseObject.
        //nome = (TextView) container.findViewById(R.id.tvNumber1);
        //nome.setText(user.get);
    //}

}
