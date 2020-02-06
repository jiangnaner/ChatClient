package com.jiaao.chatclient.data;


import android.content.Context;

import org.jivesoftware.smack.XMPPConnection;

//获取全局变量
public class DataWarehouse {

    public static GlobalData getGlobalData(Context context){
        return (GlobalData)context.getApplicationContext();
    }

    public static XMPPConnection getXMPPConnection(Context context){
        return  getGlobalData(context).xmppConnection;
    }

    public static void setXMPPConnection(Context context,XMPPConnection connection){
        getGlobalData(context).xmppConnection = connection;
    }

}
