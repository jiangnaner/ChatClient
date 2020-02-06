package com.jiaao.chatclient.common;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

//连接服务器
public class XMPPUtil {
    //获取连接
    public static XMPPConnection getXMPPConnection(String server, int port)
    {
        try{
           // XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
            ConnectionConfiguration config = new ConnectionConfiguration(server,port);
            config.setReconnectionAllowed(true);
            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            config.setSendPresence(true);
            SASLAuthentication.supportSASLMechanism("PLAIN",0);//验证
            XMPPConnection connection = new XMPPTCPConnection(config,null);
            connection.connect();
            return connection;
        }catch(Exception e)
        {
         }
        return null;                               //创建不成功则返回空
    }
    public static XMPPConnection getXMPPConnection(String server){   //默认端口号
        return getXMPPConnection(server,5222);
    }
}
