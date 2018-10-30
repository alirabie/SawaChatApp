package sawachats.apps.alirabie.com.sawachats.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import sawachats.apps.alirabie.com.sawachats.Activies.AllUsersActivity;
import sawachats.apps.alirabie.com.sawachats.Activies.ChatActivity;
import sawachats.apps.alirabie.com.sawachats.Activies.ProfileActivity;
import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseAuthHelper;
import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseDataBaseHelper;
import sawachats.apps.alirabie.com.sawachats.Models.FriendModel;
import sawachats.apps.alirabie.com.sawachats.Models.UserModel;
import sawachats.apps.alirabie.com.sawachats.R;

public class FrindesFragment extends Fragment {

    private RecyclerView friendsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frindes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        friendsList=view.findViewById(R.id.frindes_list);
        friendsList.setHasFixedSize(true);
        friendsList.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<FriendModel> options =
                new FirebaseRecyclerOptions.Builder<FriendModel>()
                        .setQuery(FireBaseDataBaseHelper.addtoFrindes().child(FireBaseAuthHelper.getUid()), FriendModel.class)
                        .build();


        FireBaseDataBaseHelper.addtoFrindes().child(FireBaseAuthHelper.getUid()).keepSynced(true);
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<FriendModel, FrindsViewHolder>(options) {
            @Override
            public FrindsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.friend_item_list, parent, false);
                return new FrindsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final FrindsViewHolder holder, final int position, final FriendModel model) {


                FireBaseDataBaseHelper.getUserById(getRef(position).getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        holder.name.setText(dataSnapshot.child("name").getValue().toString());
                        holder.status.setText(dataSnapshot.child("status").getValue().toString());

                        if(dataSnapshot.child("online").getValue().toString().equals("true")){
                            holder.onlineIndecator.setImageResource(R.drawable.dot_active);
                        }else if (dataSnapshot.child("online").getValue().toString().equals("false")) {
                            holder.onlineIndecator.setImageResource(R.drawable.dot_offline);
                        }
                        Picasso.get()
                                .load(dataSnapshot.child("image").getValue().toString())
                                .placeholder(R.mipmap.default_user)
                                .into(holder.profilePic);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FireBaseDataBaseHelper.getUserById(getRef(position).getKey()).keepSynced(true);

                holder.date.setText(model.getDate());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CharSequence options[] =new CharSequence[]{"Open Profile","Send Message"};
                        AlertDialog.Builder mBulider=new AlertDialog.Builder(getContext());
                        mBulider.setTitle("Select Options");
                        mBulider.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if(i==0){

                                    startActivity(new Intent(getContext(),ProfileActivity.class)
                                            .putExtra("userId",getRef(position).getKey()));


                                }

                                if(i==1){

                                    startActivity(new Intent(getContext(),ChatActivity.class)
                                            .putExtra("userId",getRef(position).getKey()));


                                }

                            }
                        });
                        mBulider.show();

                    }
                });

            }
        };

        adapter.startListening();
        friendsList.setAdapter(adapter);




    }




    public class FrindsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        CircleImageView profilePic;
        ImageView onlineIndecator;
        TextView name,status,date;
        public FrindsViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            profilePic=itemView.findViewById(R.id.user_img);
            onlineIndecator=itemView.findViewById(R.id.online_indicator);
            name=itemView.findViewById(R.id.user_name_tv);
            status=itemView.findViewById(R.id.status_tv);
            date=itemView.findViewById(R.id.date_tv);
        }
    }
}
