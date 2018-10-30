package sawachats.apps.alirabie.com.sawachats.Activies;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseAuthHelper;
import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseDataBaseHelper;
import sawachats.apps.alirabie.com.sawachats.GetTimeAgo;
import sawachats.apps.alirabie.com.sawachats.R;

public class ChatActivity extends AppCompatActivity {

    private String chatUserId;
    private Toolbar mToolbar;
    private TextView userNameTv,lastSeenTv;
    private CircleImageView profilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mToolbar=findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        final ActionBar mActionBar=getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = mLayoutInflater.inflate(R.layout.chat_custom_bar,null);
        mActionBar.setCustomView(actionBarView);
        userNameTv=findViewById(R.id.user_name_tv);
        lastSeenTv=findViewById(R.id.last_seen_tv);
        profilePic=findViewById(R.id.profile_pic);


        chatUserId=getIntent().getStringExtra("userId");

        FireBaseDataBaseHelper.getUserById(chatUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userNameTv.setText(dataSnapshot.child("name").getValue().toString());
                Picasso.get().load(dataSnapshot.child("image").getValue().toString())
                        .placeholder(R.mipmap.default_user)
                        .fit()
                        .into(profilePic);

                if(dataSnapshot.child("online").getValue().toString().equals("true")){
                    lastSeenTv.setText("Online");
                }else {

                    GetTimeAgo getTimeAgo =new GetTimeAgo();
                    long lastTime = Long.parseLong(dataSnapshot.child("online").getValue().toString());
                    lastSeenTv.setText(getTimeAgo.getTimeAgo(lastTime,getApplicationContext()));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FireBaseDataBaseHelper
                .getUserById(FireBaseAuthHelper.getUid()).child("online")
                .setValue(true);
    }
}
