package cn.leancloud.leanstoragegettingstarted.login.presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import cn.leancloud.leanstoragegettingstarted.login.model.IUser;
import cn.leancloud.leanstoragegettingstarted.login.view.IVIew;

public class LoginPresenterCompl implements  IPresenter{

    IUser  iUser;
    IVIew  ivIew;
    Handler handler;
    public LoginPresenterCompl(IVIew  ivIew) {
        this.ivIew = ivIew;
        handler = new Handler(Looper.getMainLooper());
    }


    @Override
    public void login(final String name, final String password) {
        ivIew.setDialogShow(true);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    AVUser.logInInBackground(name, password, new LogInCallback<AVUser>(){
                        @Override
                        public void done( AVUser avUser,  AVException e) {
                            Log.e("liu",""+name+"--"+password);
                            if (ivIew!=null) {
                                ivIew.onLoginResult(avUser,e);
                                ivIew.setDialogShow(false);
                                ivIew.onClearText();
                            }
                        }
                    });

            }
        }, 4000);

    }
}
