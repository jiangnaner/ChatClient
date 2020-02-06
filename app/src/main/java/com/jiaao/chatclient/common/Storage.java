package com.jiaao.chatclient.common;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {                     //存储密码
    private final  static  String STORAGE_FILE_NAME="chatclient.config";

    public static void putString(Context context, String key, String value){     //存字符串
        SharedPreferences sharedPreferences =getSharedPreferences(context);
        sharedPreferences.edit().putString(key, value).commit();//存
    }

    public  static String getString(Context context,String key,String... defaultValue){  //取
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String dv = "";
        for (String v:defaultValue){
            dv = v;
            break;
        }
        return sharedPreferences.getString(key,dv);
    }

    public static void putBoolean(Context context,String key,boolean value){
        SharedPreferences sharedPreferences =getSharedPreferences(context);
        sharedPreferences.edit().putBoolean(key, value).commit();//存
    }
    public  static Boolean getBoolean(Context context,String key,Boolean... defaultValue){  //取
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        Boolean dv =false;
        for (Boolean v:defaultValue){
            dv = v;
            break;
        }
        return sharedPreferences.getBoolean(key,dv);
    }







    private static SharedPreferences getSharedPreferences(Context context){        //
        return context.getSharedPreferences(STORAGE_FILE_NAME,Context.MODE_PRIVATE);
    }
}
