package com.doctorfinderapp.doctorfinder;

import android.app.AlertDialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.adapter.PersonAdapter;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.util.Log;


import java.util.List;
import java.util.ArrayList;


public class UserProfileActivity extends AppCompatActivity {

    private  AlertDialog.Builder alert;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PersonAdapter mAdapter;
    private Button segnala;
    private String USER_EMAIL;
    private int index;
    private List<ParseObject> user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //retrieve index from Activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            index = extras.getInt("index");
        }

        //scrolling
        setContentView(R.layout.activity_scrolling_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //floating button for report problems
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert = new AlertDialog.Builder(UserProfileActivity.this);
                alert.setTitle("Hai bisogno di aiuto?");
                alert.setMessage("Se stai riscontrando problemi con il tuo profilo, o " +
                        "hai problemi con un dottore, clicca sul pulsante posto in basso, segnala problema, " +
                        " e spiegaci il tuo problema. Saremo a tua disposizione per aiutarti!");
                alert.setPositiveButton("Ho capito", null);
                alert.show();
            }
        });

        /*

        segnala = (Button) findViewById(R.id.segnala) ;
        segnala.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                alert.setTitle("Inviaci una mail!");
                alert.setMessage("Scrivi una mail a info@doctorfinderapp.com spiegando " +
                        "dettagliatamente il tuo problema. Saremo felici di aiutarti");
                alert.setPositiveButton("Invia", null);
                alert.setNegativeButton("Cancella",null);
                alert.show();
           }
        });

        //bugga parse
        user = GlobalVariable.USER;

        ParseObject USERTHIS = user.get(index);
        Log.d("USERTHIS", USER_EMAIL);
        USER_EMAIL = USERTHIS.getString("email");

        TextView email = (TextView) findViewById(R.id.emaillino);
*/
        }


        //UserImage Baccerino uno di noi

    private void getUserImage(ParseUser user){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserPhoto");
        query.whereEqualTo("username", user.getEmail());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject userPhoto, ParseException e) {
                //userphoto exists

                if (userPhoto == null) {
                    Log.d("userphoto", "isnull");

                } else {
                    ParseFile file = (ParseFile) userPhoto.get("profilePhoto");
                    file.getDataInBackground(new GetDataCallback() {
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                // data has the bytes for the resume
                                //data is the image in array byte
                                //must change image on profile
                                GlobalVariable.UserPropic = BitmapFactory.decodeByteArray(data, 0, data.length);
                                Log.d("Userphoto", "downloaded");

                                RoundedImageView mImg = (RoundedImageView) findViewById(R.id.userino);
                                mImg.setImageBitmap(GlobalVariable.UserPropic);
                                //iv.setImageBitmap(bitmap );

                            } else {
                                // something went wrong
                                Log.d("UserPhoto ", "problem download image");
                            }
                        }
                    });

                }
            }
        });

    }

}

