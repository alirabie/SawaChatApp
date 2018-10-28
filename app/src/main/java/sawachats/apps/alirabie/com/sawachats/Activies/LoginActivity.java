package sawachats.apps.alirabie.com.sawachats.Activies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.iid.FirebaseInstanceId;

import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseAuthHelper;
import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseDataBaseHelper;
import sawachats.apps.alirabie.com.sawachats.R;

public class LoginActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    TextInputLayout emailInput;
    TextInputLayout passwordInput;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mToolbar=findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpPageUI();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });



    }





    void setUpPageUI(){
        emailInput=findViewById(R.id.email_input);
        passwordInput=findViewById(R.id.password_login_input);
        loginBtn=findViewById(R.id.login_btn);
    }


    void doLogin(){
        String email=emailInput.getEditText().getText().toString();
        String password=passwordInput.getEditText().getText().toString();

       if(TextUtils.isEmpty(email)){
            emailInput.getEditText().setError("*");
            emailInput.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            passwordInput.getEditText().setError("*");
            passwordInput.requestFocus();
        }else {
           Log.e("usr",email);
           Log.e("password",password);

            FireBaseAuthHelper.login(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        String deviceToken = FirebaseInstanceId.getInstance().getToken();
                        FireBaseDataBaseHelper.getAllUsers().child(FireBaseAuthHelper.getUid()).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(LoginActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                LoginActivity.this.finish();
                            }
                        });

                    }else {
                        Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }



    }


}
