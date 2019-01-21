package cn.leancloud.leanstoragegettingstarted.setting;

import com.avos.avoscloud.AVUser;

public class MUser  extends AVUser {

    @Override
    protected void processAuthData(AVThirdPartyUserAuth auth) {
        super.processAuthData(auth);
    }
}
