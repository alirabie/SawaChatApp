package sawachats.apps.alirabie.com.sawachats.Activies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseAuthHelper;
import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseDataBaseHelper;
import sawachats.apps.alirabie.com.sawachats.Models.UserModel;
import sawachats.apps.alirabie.com.sawachats.R;

public class AllUsersActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView usersList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        setupUI();


    }



    @Override
    protected void onStart() {
        super.onStart();
        FireBaseDataBaseHelper
                .getUserById(FireBaseAuthHelper.getUid()).child("online")
                .setValue(true);

        FirebaseRecyclerOptions<UserModel> options =
                new FirebaseRecyclerOptions.Builder<UserModel>()
                        .setQuery(FireBaseDataBaseHelper.getAllUsers(), UserModel.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<UserModel, UserViewHolder>(options) {
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_item_layout, parent, false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(UserViewHolder holder, final int position, final UserModel model) {

                holder.name.setText(model.getName());
                holder.status.setText(model.getStatus());
                Picasso.get().load(model.getImage()).placeholder(R.mipmap.default_user).into(holder.profilePic);
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(AllUsersActivity.this,ProfileActivity.class)
                                .putExtra("userId",getRef(position).getKey()));
                    }
                });

            }
        };

        adapter.startListening();
        usersList.setAdapter(adapter);

    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        FireBaseDataBaseHelper
//                .getUserById(FireBaseAuthHelper.getUid()).child("online")
//                .setValue(false);
//    }

    void setupUI(){
        usersList=findViewById(R.id.users_list);
        usersList.setHasFixedSize(true);
        usersList.setLayoutManager(new LinearLayoutManager(this));
        mToolbar=findViewById(R.id.users_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }




    public class UserViewHolder extends RecyclerView.ViewHolder{
        View mView;
        CircleImageView profilePic;
        TextView name,status;
        public UserViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            profilePic=itemView.findViewById(R.id.user_img);
            name=itemView.findViewById(R.id.user_name_tv);
            status=itemView.findViewById(R.id.status_tv);
        }
    }
}
