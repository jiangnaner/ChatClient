package com.jiaao.chatclient.data;

import android.app.Application;

import org.jivesoftware.smack.XMPPConnection;

import java.util.Set;
import java.util.TreeSet;

public class GlobalData extends Application {

    public XMPPConnection xmppConnection;
    public LoginData loginData = new LoginData();
    public Set<String> chatUsers = new TreeSet<String>(); //当前正在聊天的用户
}
