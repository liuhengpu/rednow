package cn.leancloud.leanstoragegettingstarted;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVAnalytics;

/**
 * Created by BinaryHB on 16/9/13.
 */
public class GettingStartedApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    AVOSCloud.initialize(this,"4wPGimaIcvju8DStL1uD0yCt-gzGzoHsz", "O47HB4isfVKHLbqDVelf5opy");
    AVOSCloud.setDebugLogEnabled(true);
    AVAnalytics.enableCrashReport(this, true);
  }
}
