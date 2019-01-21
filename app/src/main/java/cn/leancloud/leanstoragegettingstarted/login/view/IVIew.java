package cn.leancloud.leanstoragegettingstarted.login.view;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;

public interface IVIew {
     void onClearText();
     void onLoginResult(AVUser avUser, AVException e);
     void setDialogShow(boolean b);
}
