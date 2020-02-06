package com.jiaao.chatclient.data;

import android.app.Application;

import org.jivesoftware.smack.XMPPConnection;

public class GlobalData extends Application {

    public XMPPConnection xmppConnection;
    public LoginData loginData = new LoginData();

}
