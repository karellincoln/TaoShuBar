package com.cn.ciao.taoshubar;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

import java.util.ArrayList;

public class LoginFace extends AppCompatActivity implements LoginDefaultFragment.OnFragmentInteractionListener
,LoginRegisterFragment.OnFragmentInteractionListener{

    public final static String ARG_TO_WELCOME_PAGE ="com.ciao.intent_action.welcomepage";
    AVUser user;
    Intent intentToWelcomePage;
    ArrayList<Fragment> loginFragmentList;
    FragmentPagerAdapter fpa;
    LoginDefaultFragment loginDefaultFragment;
    LoginRegisterFragment loginRegisterFragment;
    Button loginInButton,registerButton,confirmRegisterButton;
    EditText registerAccount,registerPassword,loginAccount,loginPassword;
    ViewPager loginViewPager;

    @Override
    public void onFragmentInteraction(Uri uri) {
        confirmRegisterButton = loginRegisterFragment.getConfirmRegisterButton();
        registerAccount = loginRegisterFragment.getRegisterAccount();
        registerPassword=loginRegisterFragment.getRegisterPassword();
        confirmRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password;
                username = registerAccount.getText().toString();
                password = registerPassword.getText().toString();
                user.setUsername(username);
                user.setPassword(password);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                        }
                    }
                });
                AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if (e == null) {
                            intentToWelcomePage =new Intent(LoginFace.this,WelcomeActivity.class);
                            intentToWelcomePage.putExtra("AVuser",avUser);
                            LoginFace.this.startActivity(intentToWelcomePage);
                        }
                    }
                });
            }
        });
    }

    private void testcloud(){
        AVOSCloud.initialize(this, "YAlBJEQoKiSByjAsT6cttQtt-gzGzoHsz", "Cs4mXLQcVJ9qdDx2m8zSNeTz");
    }
    private void initViewAction(){
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewPager.setCurrentItem(1,true);
            }
        });
        loginInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username,password;
                username = loginAccount.getText().toString();
                password=loginPassword.getText().toString();
                AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if(e==null){
                            intentToWelcomePage =new Intent(LoginFace.this,WelcomeActivity.class);
                            intentToWelcomePage.putExtra("AVuser",avUser);
                            LoginFace.this.startActivity(intentToWelcomePage);
                        }else{
                            Toast.makeText(LoginFace.this,"Wrong Account or Password",Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        });
    }
    private void initViews(){
        loginInButton = (Button)findViewById(R.id.login_button);
        registerButton = (Button)findViewById(R.id.register_button);
        loginAccount =(EditText) findViewById(R.id.user_account_input);
        loginPassword=(EditText) findViewById(R.id.user_password_input);
        loginViewPager =(ViewPager)findViewById(R.id.register_field_pager);
        loginDefaultFragment =new LoginDefaultFragment();
        loginRegisterFragment =new LoginRegisterFragment();

        loginFragmentList =new ArrayList<Fragment>(2);
        loginFragmentList.add(loginDefaultFragment);
        loginFragmentList.add(loginRegisterFragment);
        fpa = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return loginFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return loginFragmentList.size();
            }
        };
        loginViewPager.setAdapter(fpa);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_face);
        testcloud();
        user =new AVUser();
        initViews();
        initViewAction();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_face, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
