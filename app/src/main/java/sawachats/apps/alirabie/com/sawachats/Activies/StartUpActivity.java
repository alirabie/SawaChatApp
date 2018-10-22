package sawachats.apps.alirabie.com.sawachats.Activies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sawachats.apps.alirabie.com.sawachats.R;

public class StartUpActivity extends AppCompatActivity  {

    Button goLoginPage;
    Button goRegisterPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        setUpUI();


        goLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartUpActivity.this,LoginActivity.class));
            }
        });

        goRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartUpActivity.this,RegisterActivity.class));
            }
        });


    }








    //Setup UI
    public void setUpUI () {
        goLoginPage=findViewById(R.id.go_login_btn);
        goRegisterPage=findViewById(R.id.go_register_btn);
    }



}
