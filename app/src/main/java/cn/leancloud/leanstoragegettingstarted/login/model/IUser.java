package cn.leancloud.leanstoragegettingstarted.login.model;

import com.avos.avoscloud.AVUser;

public interface IUser {
    String getName();
    String getPasswd();
    int checkUserValidity(String name, String passwd);
}
