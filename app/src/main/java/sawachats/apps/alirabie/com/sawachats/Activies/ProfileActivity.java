package sawachats.apps.alirabie.com.sawachats.Activies;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseAuthHelper;
import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseDataBaseHelper;
import sawachats.apps.alirabie.com.sawachats.R;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView displayName;
    private TextView statusTv;
    private Button sendFrindReqBtn;
    private String currentStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        currentStatus="notFriends";
        setupUI();



        FireBaseDataBaseHelper.getUserById(getIntent().getStringExtra("userId")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                displayName.setText(dataSnapshot.child("name").getValue().toString());
                statusTv.setText(dataSnapshot.child("status").getValue().toString());
                Picasso.get()
                        .load(dataSnapshot.child("image").getValue().toString())
                        .fit()
                        .centerCrop()
                        .placeholder(R.mipmap.default_user)
                        .into(profilePic);



                FireBaseDataBaseHelper.sendFrindRequest().child(FireBaseAuthHelper.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(getIntent().getStringExtra("userId"))){
                            String reqType=dataSnapshot
                                    .child(getIntent().getStringExtra("userId"))
                                    .child("request_type")
                                    .getValue().toString();

                            if(reqType.equals("received")){
                                currentStatus="req_received";
                                sendFrindReqBtn.setText("ACCEPT FRIEND REQUEST");
                            }else if (reqType.equals("sent")){
                                currentStatus = "req_sent";
                                sendFrindReqBtn.setText("CANCEL FRIEND REQUEST");
                            }
                        }else {

                            FireBaseDataBaseHelper.addtoFrindes().child(FireBaseAuthHelper.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(getIntent().getStringExtra("userId"))){
                                        currentStatus="friends";
                                        sendFrindReqBtn.setText("UNFRIEND");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        sendFrindReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFrindReqBtn.setEnabled(false);
             if(currentStatus.equals("notFriends")) {
    FireBaseDataBaseHelper.sendFrindRequest()
            .child(FireBaseAuthHelper.getUid())
            .child(getIntent().getStringExtra("userId"))
            .child("request_type")
            .setValue("sent")
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        FireBaseDataBaseHelper.sendFrindRequest()
                                .child(getIntent().getStringExtra("userId"))
                                .child(FireBaseAuthHelper.getUid())
                                .child("request_type")
                                .setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                currentStatus = "req_sent";
                                sendFrindReqBtn.setText("CANCEL FRIEND REQUEST");
                            }
                        });
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to send friend request" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    sendFrindReqBtn.setEnabled(true);
                }
            });
}


                //-------------------------------------------------------------------------------------------------

                if(currentStatus.equals("req_sent")){

                    FireBaseDataBaseHelper.sendFrindRequest().child(FireBaseAuthHelper.getUid()).child(getIntent().getStringExtra("userId")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FireBaseDataBaseHelper.sendFrindRequest().child(getIntent().getStringExtra("userId")).child(FireBaseAuthHelper.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    sendFrindReqBtn.setEnabled(true);
                                    currentStatus="notFriends";
                                    sendFrindReqBtn.setText("SEND FRIEND REQUEST");
                                }
                            });
                        }
                    });



                }



                //---------------------------------------------------------------------------

                if(currentStatus.equals("req_received")){


                    final String date = DateFormat.getDateTimeInstance().format(new Date());

                    Map friendsMap =new HashMap();
                    friendsMap.put("friendes/"+FireBaseAuthHelper.getUid()+"/"+getIntent().getStringExtra("userId")+"/date",date);
                    friendsMap.put("friendes/"+getIntent().getStringExtra("userId")+"/"+FireBaseAuthHelper.getUid()+"/date",date);

                    friendsMap.put("frind_requestes/"+FireBaseAuthHelper.getUid()+"/"+getIntent().getStringExtra("userId"),null);
                    friendsMap.put("frind_requestes/"+getIntent().getStringExtra("userId")+"/"+FireBaseAuthHelper.getUid(),null);


                    FirebaseDatabase.getInstance().getReference().updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                            if(databaseError==null){

                                sendFrindReqBtn.setEnabled(true);
                                currentStatus="friends";
                                sendFrindReqBtn.setText("UNFRIEND");

                            }else {
                                Toast.makeText(ProfileActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                            }


                        }
                    });











//                    FireBaseDataBaseHelper.addtoFrindes().child(FireBaseAuthHelper.getUid()).child(getIntent().getStringExtra("userId")).setValue(date).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//
//                            FireBaseDataBaseHelper.addtoFrindes().child(getIntent().getStringExtra("userId")).child(FireBaseAuthHelper.getUid()).setValue(date).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    FireBaseDataBaseHelper.sendFrindRequest().child(FireBaseAuthHelper.getUid()).child(getIntent().getStringExtra("userId")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            FireBaseDataBaseHelper.sendFrindRequest().child(getIntent().getStringExtra("userId")).child(FireBaseAuthHelper.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    sendFrindReqBtn.setEnabled(true);
//                                                    currentStatus="friends";
//                                                    sendFrindReqBtn.setText("UNFRIEND");
//                                                }
//                                            });
//                                        }
//                                    });
//                                }
//                            });
//                        }
//                    });
                }


                //----------------------------------------------------------------------------

                if(currentStatus.equals("friends")){

                    FireBaseDataBaseHelper.addtoFrindes().child(FireBaseAuthHelper.getUid()).child(getIntent().getStringExtra("userId")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FireBaseDataBaseHelper.addtoFrindes().child(getIntent().getStringExtra("userId")).child(FireBaseAuthHelper.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    currentStatus="notFriends";
                                    sendFrindReqBtn.setText("SEND FRIEND REQUEST");
                                }
                            });
                        }
                    });



                }















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

//    @Override
//    protected void onStop() {
//        super.onStop();
//        FireBaseDataBaseHelper
//                .getUserById(FireBaseAuthHelper.getUid()).child("online")
//                .setValue(false);
//    }

    private void setupUI() {
        profilePic=findViewById(R.id.profile_pic);
        displayName=findViewById(R.id.display_name);
        statusTv=findViewById(R.id.status_tv);
        sendFrindReqBtn=findViewById(R.id.send_req_btn);
    }
}
