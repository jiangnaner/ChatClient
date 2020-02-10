package com.jiaao.chatclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jiaao.chatclient.common.Const;
import com.jiaao.chatclient.common.Storage;
import com.jiaao.chatclient.common.XMPPUtil;
import com.jiaao.chatclient.data.DataWarehouse;
import com.jiaao.chatclient.data.LoginData;

import org.jivesoftware.smack.XMPPConnection;

public class LoginActivity extends AppCompatActivity implements Const {

    private EditText Editusername,Editpassword,Editipsever;
    private TextView Register;
    private LoginData mLoginData;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Editusername = (EditText) findViewById(R.id.userword);
        Editpassword = (EditText)findViewById(R.id.password);
        Editpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());//隐藏密码
        Editipsever  = (EditText)findViewById(R.id.ip);
        Register = (TextView)findViewById(R.id.Register);
        mLoginData = DataWarehouse.getGlobalData(this).loginData;

        mLoginData.username = Storage.getString(this,KEY_USERNAME);
        mLoginData.password = Storage.getString(this,KEY_PASSWORD);   //读取密码
        mLoginData.loginServer = Storage.getString(this,KEY_LOGIN_SERVER);

        Editusername.setText(mLoginData.username);
        Editpassword.setText(mLoginData.password);   //默认填充密码
        Editipsever.setText(mLoginData.loginServer);


          //注册点击事件
        final SpannableString spanString = new SpannableString("注册账号");
        spanString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                //点击的响应事件
                Intent intent2 = new Intent(LoginActivity.this,RegisterActivity.class);
                String server = Editipsever.getText().toString();
                if (!"".equals(server)){
                    intent2.putExtra("server",server);
                }
                startActivity(intent2);
            }
        }, 0, spanString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#e9e9e9"));
        spanString.setSpan(foregroundColorSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        Register.append(spanString);
        Register.setMovementMethod(LinkMovementMethod.getInstance());



        new Thread(new Runnable() {
            public void run() {
            if (login()) {
                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        }
        }).start();

    }

    //用于在线程中登陆的方法
    private boolean login(){
        try {
            XMPPConnection connection = XMPPUtil.getXMPPConnection(mLoginData.loginServer);
            if (connection == null){
                throw new Exception("聊天服务器连接失败");
            }
            connection.login(mLoginData.username,mLoginData.password);
            DataWarehouse.setXMPPConnection(this,connection);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //登录按钮的单击事件
    public void onClick_Login(View view){

        mLoginData.username = Editusername.getText().toString();
        mLoginData.password = Editpassword.getText().toString();
        mLoginData.loginServer = Editipsever.getText().toString();

        //存储登录信息
//        Storage.putString(this,KEY_USERNAME,mLoginData.username);
//        Storage.putString(this,KEY_PASSWORD,mLoginData.password);          //默认保存密码
//        Storage.putString(this,KEY_LOGIN_SERVER,mLoginData.loginServer);


        new Thread(new Runnable() {
            @Override
            public void run() {
                if (login()){                                     //登陆成功
                    Storage.putString(LoginActivity.this,KEY_USERNAME,mLoginData.username);
                    Storage.putString(LoginActivity.this,KEY_PASSWORD,mLoginData.password);          //默认保存密码
                    Storage.putString(LoginActivity.this,KEY_LOGIN_SERVER,mLoginData.loginServer);
                    Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
                    startActivity(intent);
                    finish();
                }else {                                           //失败
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this," 账号或密码错误。",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }
}
