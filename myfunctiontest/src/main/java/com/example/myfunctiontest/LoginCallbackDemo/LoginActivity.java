package com.example.myfunctiontest.LoginCallbackDemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfunctiontest.R;

/**
 * author: Administrator
 * created on: 2016/12/14 16:29
 * description:
 */

public class LoginActivity extends Activity {


    private EditText name,pwd;
    private Button login;
    private userrequest userrequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlogin);
        name = (EditText) findViewById(R.id.username);
        pwd = (EditText) findViewById(R.id.userpwd);
        login = (Button) findViewById(R.id.login);
        userrequest = new userrequest(this);
        login.setOnClickListener(new loginclick());
    }

    class loginclick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            userrequest.login(name.getText().toString(),pwd.getText().toString(),new userrequest.loginListener(){
                @Override
                public void loginsuccess() {
                    ToastUtil.showShortToast(LoginActivity.this,"登录成功");
                }

                @Override
                public void loginerr() {
                    ToastUtil.showShortToast(LoginActivity.this,"登录失败");
                }

                @Override
                public void disconnect() {
                    ToastUtil.showShortToast(LoginActivity.this,"网络未连接");
                }
            });
        }
    }
}
