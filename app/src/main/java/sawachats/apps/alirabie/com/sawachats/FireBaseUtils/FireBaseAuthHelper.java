package sawachats.apps.alirabie.com.sawachats.FireBaseUtils;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Eng Ali on 10/22/2018.
 */

public class FireBaseAuthHelper {


    public static Task<AuthResult> register(String user , String password){
        return FirebaseAuth.getInstance().createUserWithEmailAndPassword(user,password);
    }



    public static  Task<AuthResult> login (String email,String password){
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password);
    }



    public static void logOut(){
        FirebaseAuth.getInstance().signOut();
    }



    public static FirebaseUser activeSession(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    public static String getUid (){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


}
