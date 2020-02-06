package com.jiaao.chatclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jiaao.chatclient.R;
import com.jiaao.chatclient.common.Const;
import com.jiaao.chatclient.common.XMPPUtil;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smackx.carbons.packet.CarbonExtension;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText EditUsername,EditPassword,EditRepassword,EditSever;
    private Button ButtonRegister;
    private  MyHandler myHandler = new MyHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditUsername = (EditText)findViewById(R.id.userword);
        EditPassword = (EditText)findViewById(R.id.password);
        EditRepassword = (EditText)findViewById(R.id.password1);
        EditSever = (EditText)findViewById(R.id.ip);
        ButtonRegister = (Button)findViewById(R.id.button_Register);

        String server = getIntent().getStringExtra("server");   //接收登陆界面的服务器地址
        if(server!=null){
            EditSever.setText(server);
        }
    }

    //校验用户输入信息
    private boolean verify(){
        if ("".equals(EditUsername.getText().toString().trim())||"".equals(EditPassword.getText().toString().trim())||
                "".equals(EditRepassword.getText().toString().trim())||"".equals(EditSever.getText().toString().trim())){
            Toast.makeText(this,"信息未填写。",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!EditPassword.getText().toString().equals(EditRepassword.getText().toString())) {
            Toast.makeText(this,"密码与确认密码不一致。",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            Toast.makeText(RegisterActivity.this,msg.obj.toString(),Toast.LENGTH_LONG).show();
//        }
//    };
    private static class MyHandler extends Handler{
        private Context mContext;
        public MyHandler(Context context){
            mContext = context;
        }
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Toast.makeText(mContext,msg.obj.toString(),Toast.LENGTH_LONG).show();
    }
}
    //注册新用户
    public void createAccount(String server,String username,String password){

        Message msg = new Message();

        try{
            XMPPConnection connection = XMPPUtil.getXMPPConnection(server);   //获得xmpp对象
            Registration registration = new Registration();
            registration.setType(IQ.Type.SET);   //IQ(信息查询)，设置类型
            registration.setTo(connection.getHost());
            Map<String,String> attributes = new HashMap<String, String>();//传递username,password
            attributes.put("username",username);
            attributes.put("password",password);
            registration.setAttributes(attributes);

            //过滤
            PacketFilter filter = new AndFilter(new PacketIDFilter(registration.getPacketID()),
                    new PacketTypeFilter(IQ.class));
            PacketCollector collector = connection.createPacketCollector(filter);
            connection.sendPacket(registration);
            IQ result = (IQ)collector.nextResult(connection.getPacketReplyTimeout());  //超时返回空
            collector.cancel();
            if(result == null){
                throw new Exception("创建用户失败,服务器未响应。");
            }else if(result.getType()==IQ.Type.RESULT){
                msg.obj = "成功创建用户";
                finish();
            }else {
                if (result.getError().toString().toLowerCase().contains("conflict")){
                    throw new Exception("该用户名已存在。");
                }else{
                    throw new Exception("创建新用户失败，未知错误。");
                }
            }

        }catch (Exception e){
            msg.obj = e.getMessage();
        }
        finally {
            myHandler.sendMessage(msg);
        }
    }

    public void onClick_Register(View view){
        if (!verify()){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                createAccount(EditSever.getText().toString(),EditUsername.getText().toString(),
                        EditPassword.getText().toString());
            }
        }).start();
    }


}
