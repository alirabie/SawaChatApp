package sawachats.apps.alirabie.com.sawachats.Activies;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;
import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseAuthHelper;
import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseDataBaseHelper;
import sawachats.apps.alirabie.com.sawachats.R;

public class AccountActivity extends AppCompatActivity {

    CircleImageView profilePic;
    TextView name;
    TextView status;
    Button changeStatus;
    Button changeProfileImage;
    public static final int GALLERY_PICK=1;
    public StorageReference mImagesStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setUpUiItems();

        mImagesStorage= FirebaseStorage.getInstance().getReference();
        FireBaseDataBaseHelper.getUserById(FireBaseAuthHelper.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue().toString());
                status.setText(dataSnapshot.child("status").getValue().toString());
                Picasso.get()
                        .load(dataSnapshot.child("image").getValue().toString())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .resize(50, 50)
                        .placeholder(R.mipmap.default_user)
                        .into(profilePic, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get()
                                        .load(dataSnapshot.child("image").getValue().toString())
                                        .resize(50, 50)
                                        .placeholder(R.mipmap.default_user)
                                        .into(profilePic);
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FireBaseDataBaseHelper.getUserById(FireBaseAuthHelper.getUid()).keepSynced(true);



        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(AccountActivity.this);
            }
        });



        changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                builder.setTitle("Change your status");

                final EditText input = new EditText(AccountActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FireBaseDataBaseHelper.getUserById(FireBaseAuthHelper.getUid()).child("status").setValue(input.getText().toString());

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public void onStart() {
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
//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                StorageReference filePath=mImagesStorage.child("Profile_images").child("IMG"+FireBaseAuthHelper.getUid()+".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){

                            FireBaseDataBaseHelper.getUserById(FireBaseAuthHelper.getUid()).child("image").setValue(task.getResult().getDownloadUrl().toString());

                        }else {
                            Log.e("Errorr Upoading images",task.getException().getMessage());
                        }
                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void setUpUiItems() {
        profilePic=findViewById(R.id.profile_img);
        name=findViewById(R.id.username_tv);
        status=findViewById(R.id.status_tv);
        changeProfileImage=findViewById(R.id.change_profile_img_btn);
        changeStatus=findViewById(R.id.change_status_btn);
    }



}
