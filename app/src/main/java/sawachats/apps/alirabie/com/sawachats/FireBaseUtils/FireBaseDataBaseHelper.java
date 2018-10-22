package sawachats.apps.alirabie.com.sawachats.FireBaseUtils;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.ResultSet;

/**
 * Created by Eng Ali on 10/22/2018.
 */

public class FireBaseDataBaseHelper {

    public static  final String USERS = "users";

    public static void storeNewUser (String uid,Object data){
        FirebaseDatabase.getInstance().getReference().child(USERS).child(uid).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Log.e("Database err" , task.getException().getMessage());
                }
            }
        });
    }

    public static DatabaseReference getUserById (String uid){
        return   FirebaseDatabase.getInstance().getReference().child(USERS).child(uid);
    }


}
