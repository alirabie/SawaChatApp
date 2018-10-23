package sawachats.apps.alirabie.com.sawachats;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseAuthHelper;
import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseDataBaseHelper;

/**
 * Created by Eng Ali on 10/23/2018.
 */

public class SawaChat extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Picasso.Builder builder =new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso bulit =builder.build();
        bulit.setIndicatorsEnabled(true);
        bulit.setLoggingEnabled(true);
        Picasso.setSingletonInstance(bulit);

        if(FireBaseAuthHelper.activeSession()!=null) {
            FireBaseDataBaseHelper.getUserById(FireBaseAuthHelper.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        FireBaseDataBaseHelper
                                .getUserById(FireBaseAuthHelper.getUid()).child("online")
                                .onDisconnect()
                                .setValue(false);


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
