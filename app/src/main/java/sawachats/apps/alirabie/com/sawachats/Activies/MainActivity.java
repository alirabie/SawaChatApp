package sawachats.apps.alirabie.com.sawachats.Activies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnSuccessListener;

import sawachats.apps.alirabie.com.sawachats.Adapters.CustomPagerAdapter;
import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseAuthHelper;
import sawachats.apps.alirabie.com.sawachats.FireBaseUtils.FireBaseDataBaseHelper;
import sawachats.apps.alirabie.com.sawachats.Fragments.ChatsFragment;
import sawachats.apps.alirabie.com.sawachats.Fragments.FrindesFragment;
import sawachats.apps.alirabie.com.sawachats.Fragments.RequestesFragment;
import sawachats.apps.alirabie.com.sawachats.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabs;
    CustomPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar=findViewById(R.id.hometoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("SAWA CHAT");
        setUpTabsUI();

    }




    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(FireBaseAuthHelper.activeSession()==null){
         sendToStartPage();
        }else {

            FireBaseDataBaseHelper
                    .getUserById(FireBaseAuthHelper.getUid()).child("online")
                    .setValue(true);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(FireBaseAuthHelper.activeSession()!=null) {
            FireBaseDataBaseHelper
                    .getUserById(FireBaseAuthHelper.getUid()).child("online")
                    .setValue(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            FireBaseDataBaseHelper
                    .getUserById(FireBaseAuthHelper.getUid()).child("online")
                    .setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FireBaseAuthHelper.logOut();
                    sendToStartPage();
                }
            });

            return true;
        }else if(id == R.id.action_allusers){
            startActivity(new Intent(MainActivity.this,AllUsersActivity.class));
            return true;
        }else if(id==R.id.action_account_settings){
            startActivity(new Intent(MainActivity.this,AccountActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void sendToStartPage() {
        startActivity(new Intent(MainActivity.this,StartUpActivity.class));
        finish();
    }



    public void setUpTabsUI(){
        mViewPager=findViewById(R.id.pager);
        mTabs=findViewById(R.id.tabs);
        mPagerAdapter=new CustomPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(new RequestesFragment(),"Requests");
        mPagerAdapter.addFragment(new ChatsFragment(),"Chats");
        mPagerAdapter.addFragment(new FrindesFragment(),"Friends");
        mViewPager.setAdapter(mPagerAdapter);
        mTabs.setupWithViewPager(mViewPager);
    }
}
