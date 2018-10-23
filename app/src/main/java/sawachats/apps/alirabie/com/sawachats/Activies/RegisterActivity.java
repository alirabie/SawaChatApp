package sawachats.apps.alirabie.com.sawachats.Activies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseAuthHelper;
import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseDataBaseHelper;
import sawachats.apps.alirabie.com.sawachats.R;

public class RegisterActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    TextInputLayout displayNameInput;
    TextInputLayout emailInput;
    TextInputLayout passwordInput;
    Button regBtn;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mToolbar=findViewById(R.id.reg_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpPageUI();


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRegisteration();
            }
        });


    }




    void setUpPageUI(){
        displayNameInput=findViewById(R.id.display_name_input);
        emailInput=findViewById(R.id.email_input);
        passwordInput=findViewById(R.id.password_input);
        regBtn=findViewById(R.id.reg_btn);
    }


    void doRegisteration(){

        final String displayName = displayNameInput.getEditText().getText().toString();
        String email=emailInput.getEditText().getText().toString();
        String password=passwordInput.getEditText().getText().toString();

        if(TextUtils.isEmpty(displayName)){
            displayNameInput.getEditText().setError("*");
            displayNameInput.requestFocus();
        }else if(TextUtils.isEmpty(email)){
            emailInput.getEditText().setError("*");
            emailInput.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            passwordInput.getEditText().setError("*");
            passwordInput.requestFocus();
        }else {

            FireBaseAuthHelper.register(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Map<String,String> userMap =new HashMap<>();
                        userMap.put("name",displayName);
                        userMap.put("status","Hi iam using Sawa Chat");
                        userMap.put("image","default");
                        userMap.put("Active","false");
                        userMap.put("lastSeen","notSet");
                        userMap.put("thump_image","default");
                        FireBaseDataBaseHelper.storeNewUser(FireBaseAuthHelper.getUid(),userMap);
                        startActivity(new Intent(RegisterActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        RegisterActivity.this.finish();
                    }else {
                        Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }



    }
}
