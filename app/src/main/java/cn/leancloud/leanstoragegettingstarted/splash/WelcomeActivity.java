package cn.leancloud.leanstoragegettingstarted.splash;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import cn.leancloud.leanstoragegettingstarted.LoginActivity;
import cn.leancloud.leanstoragegettingstarted.R;
import cn.leancloud.leanstoragegettingstarted.splash.mi_animotion.BaseActivityMi;
import cn.leancloud.leanstoragegettingstarted.splash.mi_animotion.Configure;
import cn.leancloud.leanstoragegettingstarted.splash.mi_animotion.DateAdapter;
import cn.leancloud.leanstoragegettingstarted.splash.mi_animotion.DragGrid;
import cn.leancloud.leanstoragegettingstarted.splash.mi_animotion.ScrollLayout;


public class WelcomeActivity extends BaseActivityMi {


    /** GridView.
     * 模仿小米动画
     *
     * */
    private LinearLayout linear;
    private RelativeLayout relate;
    private DragGrid gridView;
    private ScrollLayout lst_views;
    private ImageView runImage, delImage;
    LinearLayout.LayoutParams param;

    TranslateAnimation left, right;
    Animation up, down;

    public static final int PAGE_SIZE = 1;
    ArrayList<DragGrid> gridviews = new ArrayList<DragGrid>();

    ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();// 全部数据的集合集lists.size()==countpage;
    ArrayList<String> lstDate = new ArrayList<String>();// 每一页的数据

    SensorManager sm;
    SensorEventListener lsn;
    boolean isClean = false;
    Vibrator vibrator;
    int rockCount = 0;

    @Override
    public void setView() {
        setContentView(R.layout.activity_splash_mi_);
        for (int i = 0; i < 4; i++) {
            lstDate.add("" + i);
        }

    }
    public static boolean isLast = false ;
    public static int whichPotion = 0 ;
    @Override
    public void initView() {

        relate = (RelativeLayout) findViewById(R.id.relate);
        lst_views = (ScrollLayout) findViewById(R.id.views);
        Configure.init(WelcomeActivity.this);
        param = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.FILL_PARENT);
        param.rightMargin = 20;
        param.leftMargin = 20;
        param.topMargin = 10;
        param.bottomMargin =10;
        if (gridView != null) {
            lst_views.removeAllViews();
        }

        initData();

        for (int i = 0; i < Configure.countPages; i++) {
            lst_views.addView(addGridView(i));
        }

        lst_views.setPageListener(new ScrollLayout.PageListener() {
            @Override
            public void page(int page) {
                whichPotion = page;
                  setCurPage(page);
                  if(page==3){
                      isLast = true ;
//                      ((DateAdapter) ((gridviews.get(Configure.curentPage))
//                              .getAdapter())).setButton(true);
                      new Handler().postDelayed(new Runnable() {
                          @Override
                          public void run() {
                              Intent intent  = new Intent(WelcomeActivity.this,LoginActivity.class);
                              startActivity(intent);
                              finish();
                          }
                      },1000);
                  }
            }
        });

        runImage = (ImageView) findViewById(R.id.run_image);
        runAnimation();

        relate.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                System.out.println("LongClick");
                return false;
            }
        });
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lsn = new SensorEventListener() {
            @SuppressLint("MissingPermission")
            public void onSensorChanged(SensorEvent e) {
                if (e.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    if (!isClean && rockCount >= 10) {
                        isClean = true;
                        rockCount = 0;
                        vibrator.vibrate(100);
                        CleanItems();
                        return;
                    }
                    float newX = e.values[SensorManager.DATA_X];
                    float newY = e.values[SensorManager.DATA_Y];
                    float newZ = e.values[SensorManager.DATA_Z];
                    // if ((newX >= 18 || newY >= 20||newZ >= 20 )&&rockCount<4)
                    // {
                    if ((newX >= 18 || newY >= 20 || newZ >= 20)
                            && rockCount % 2 == 0) {
                        rockCount++;
                        return;
                    }
                    if ((newX <= -18 || newY <= -20 || newZ <= -20)
                            && rockCount % 2 == 1) {
                        rockCount++;
                        return;
                    }

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // TODO Auto-generated method stub

            }
        };

        sm.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_GAME);


        new Thread(new Runnable() {
            @Override
            public void run() {
                testTags();
            }
        }).start();
    }

    @Override
    public void setListener() {
        // TODO Auto-generated method stub

    }

    public void initData() {
        Configure.countPages = (int) Math.ceil(lstDate.size()
                / (float) PAGE_SIZE);

        lists = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < Configure.countPages; i++) {
            lists.add(new ArrayList<String>());
            for (int j = PAGE_SIZE * i; j < (PAGE_SIZE * (i + 1) > lstDate
                    .size() ? lstDate.size() : PAGE_SIZE * (i + 1)); j++)
                lists.get(i).add(lstDate.get(j));
        }
        boolean isLast = true;
        for (int i = lists.get(Configure.countPages - 1).size(); i < PAGE_SIZE; i++) {
            if (isLast) {
                lists.get(Configure.countPages - 1).add(null);
                isLast = false;
            } else
                lists.get(Configure.countPages - 1).add("none");
        }
    }

    public void CleanItems() {
        lstDate = new ArrayList<String>();
        for (int i = 0; i < lists.size(); i++) {
            for (int j = 0; j < lists.get(i).size(); j++) {
                if (lists.get(i).get(j) != null
                        && !lists.get(i).get(j).equals("none")) {
                    lstDate.add(lists.get(i).get(j).toString());
                    System.out.println("-->" + lists.get(i).get(j).toString());
                }
            }
        }
        System.out.println(lstDate.size());
        initData();
        lst_views.removeAllViews();
        gridviews = new ArrayList<DragGrid>();
        for (int i = 0; i < Configure.countPages; i++) {
            lst_views.addView(addGridView(i));
        }
        isClean = false;
        lst_views.snapToScreen(0);
        //
    }

    public int getFristNonePosition(ArrayList<String> array) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) != null && array.get(i).toString().equals("none")) {
                return i;
            }
        }
        return -1;
    }

    public int getFristNullPosition(ArrayList<String> array) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) == null) {
                return i;
            }
        }
        return -1;
    }

    @SuppressLint("ResourceType")
    public LinearLayout addGridView(int i) {
        // if (lists.get(i).size() < PAGE_SIZE)
        // lists.get(i).add(null);

        linear = new LinearLayout(WelcomeActivity.this);
        gridView = new DragGrid(WelcomeActivity.this);
        gridView.setAdapter(new DateAdapter(WelcomeActivity.this, lists.get(i)));
        gridView.setNumColumns(1);
        gridView.setHorizontalSpacing(0);
        gridView.setVerticalSpacing(0);
        final int ii = i;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {
                // TODO Auto-generated method stub

            }
        });
        gridView.setSelector(R.animator.mi_laucher_grid_light);
        gridView.setPageListener(new DragGrid.G_PageListener() {
            @Override
            public void page(int cases, int page) {
                switch (cases) {
                    case 0:// 滑动页面
                        lst_views.snapToScreen(page);
                        setCurPage(page);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Configure.isChangingPage = false;
                            }
                        }, 800);
                        break;
                    case 1:// 删除按钮上来
                        delImage.setBackgroundResource(R.drawable.mi_laucher_del);
                        delImage.setVisibility(View.VISIBLE);
                        delImage.startAnimation(up);
                        break;
                    case 2:// 删除按钮变深
                        delImage.setBackgroundResource(R.drawable.mi_laucher_del_check);
                        Configure.isDelDark = true;
                        break;
                    case 3:// 删除按钮变淡
                        delImage.setBackgroundResource(R.drawable.mi_laucher_del);
                        Configure.isDelDark = false;
                        break;
                    case 4:// 删除按钮下去
                        delImage.startAnimation(down);
                        break;
                    case 5:// 松手动作
                        delImage.startAnimation(down);
                        // Configure.isDelRunning = false;
                        lists.get(Configure.curentPage).add(Configure.removeItem,
                                null);
                        lists.get(Configure.curentPage).remove(
                                Configure.removeItem + 1);
                        ((DateAdapter) ((gridviews.get(Configure.curentPage))
                                .getAdapter())).notifyDataSetChanged();
                        break;
                }
            }
        });
        gridView.setOnItemChangeListener(new DragGrid.G_ItemChangeListener() {
            @Override
            public void change(int from, int to, int count) {
                String toString = (String) lists.get(
                        Configure.curentPage - count).get(from);

                lists.get(Configure.curentPage - count).add(from,
                        (String) lists.get(Configure.curentPage).get(to));
                lists.get(Configure.curentPage - count).remove(from + 1);
                lists.get(Configure.curentPage).add(to, toString);
                lists.get(Configure.curentPage).remove(to + 1);

                ((DateAdapter) ((gridviews.get(Configure.curentPage - count))
                        .getAdapter())).notifyDataSetChanged();
                ((DateAdapter) ((gridviews.get(Configure.curentPage))
                        .getAdapter())).notifyDataSetChanged();
            }
        });
                gridviews.add(gridView);
                linear.addView(gridviews.get(i), param);
                return linear;
    }
//
    public void runAnimation() {
        down = AnimationUtils.loadAnimation(WelcomeActivity.this,
                R.anim.mi_laucher_del_down);
        up = AnimationUtils.loadAnimation(WelcomeActivity.this,
                R.anim.mi_laucher_del_up);
        down.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                // TODO Auto-generated method stub
                delImage.setVisibility(View.GONE);
            }
        });

        right = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, -1f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
                0f);
        left = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
                0f, Animation.RELATIVE_TO_PARENT, 0f);
        right.setDuration(25000);
        left.setDuration(25000);
        right.setFillAfter(true);
        left.setFillAfter(true);

        right.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                runImage.startAnimation(left);
            }
        });
        left.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                runImage.startAnimation(right);
            }
        });
        runImage.startAnimation(right);
    }

    public void setCurPage(final int page) {
        Animation a = AnimationUtils.loadAnimation(WelcomeActivity.this,
                R.anim.mi_laucher_scale_in);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
            }
        });

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        sm.unregisterListener(lsn);
    }

    public void setButton(boolean b,buttonListener listener){
        listener.setVisible();
    }
    public interface  buttonListener{
        void setVisible();
    }

    private   StringBuffer  sb = new StringBuffer().append("Qian Zhiya, the company's founder1 and CEO, said on Thursday during a press conference that they will have more than 4,500 stores across the country this year, which will put them ahead of Starbucks. They are also aiming to beat the American chain's sales.\n" +
            " \n" +
            "Qian also said that the chain will continue to offer promotions2 so they can quickly grow their market share, despite a reported deficit3 of 857 million yuan (125 million US dollars) in the first nine months of last year.\n" +
            " \n" +
            "After another round of financing in December that brought in around 200 million U.S. dollars, Luckin Coffee is now worth an estimated 2.2 billion U.S. dollars.\n" +
            " \n" +
            "The first Luckin Coffee shop opened in January 2018. By the end of the year, the company had 2,073 stores in 22 cities across China.\n");

    private   void testTags(){

        Random ran1 = new Random(700);
        Log.e("liu1","sb-length-"+sb.length());
        Map<Integer,String> hashMap = new HashMap<>();
        long   startAdd = SystemClock.currentThreadTimeMillis();
        for (int x = 0;x<1000;x++){
           int temp =  ran1.nextInt(700);
            hashMap.put(x,sb.substring(temp,temp+50));
            Log.e("liu1","temp-"+temp);
        }
        long   startEnd = SystemClock.currentThreadTimeMillis();
        //持续时间
        long  continuedTime = startEnd -startAdd;
        Log.e("liu1","time--"+continuedTime);
        //创建随机数字

        if(hashMap.keySet().size()>0) {
            long start = SystemClock.currentThreadTimeMillis();

            Iterator index = hashMap.keySet().iterator();
            while (index.hasNext()) {
                int i = (int) index.next();
                String value = hashMap.get(i);
                Log.e("liu1", ": " + value);

            }
            long end = SystemClock.currentThreadTimeMillis();
            Log.e("liu1", "sss" + (end - start));
        }
    }
}