package com.jiaao.chatclient;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jiaao.chatclient.data.DataWarehouse;
import com.jiaao.chatclient.data.LoginData;

import org.jivesoftware.smack.packet.Message;

public class ChatActivity extends ParentActivity {

    private String mUser;  //登陆用户名
    private String mName;  //别名
    private String mServerName;   //@服务名
    private LoginData mLoginData;

    private EditText mEditTextChatText; //聊天文本框
    private ListView mListViewChatList; //显示聊天记录控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat );
         mLoginData = DataWarehouse.getGlobalData(this).loginData;
         mUser = getIntent().getStringExtra("userName");
         mName = getIntent().getStringExtra("nickName");
         mServerName = mXMPPConnection.getServiceName();
         mEditTextChatText = (EditText)findViewById(R.id.editText_chat_text);
         mListViewChatList = (ListView)findViewById(R.id.ListView_ChatList);
    }

    public void onClick_Send(View view){
        String text = mEditTextChatText.getText().toString().trim();
        if(!"".equals(text)){
            //账号+@服务名
            Message msg = new Message(mUser+"@"+mServerName,Message.Type.chat);
            msg.setBody(text);
        }

    }

}
