package cn.leancloud.leanstoragegettingstarted.splash.mi_animotion;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import cn.leancloud.leanstoragegettingstarted.MainActivity;
import cn.leancloud.leanstoragegettingstarted.R;
import cn.leancloud.leanstoragegettingstarted.splash.WelcomeActivity;


public class DateAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> lstDate;
	private ImageView txtAge;
    private Button bt_go ;
    private  boolean selected = false;
	public DateAdapter(Context mContext, ArrayList<String> list) {
		this.context = mContext;
		lstDate = list;
		selected = WelcomeActivity.isLast;
	}

	@Override
	public int getCount() {
		return lstDate.size();
	}

	@Override
	public Object getItem(int position) {
		return lstDate.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void exchange(int startPosition, int endPosition) {
		Object endObject = getItem(endPosition);
		Object startObject = getItem(startPosition);
		lstDate.add(startPosition, (String) endObject);
		lstDate.remove(startPosition + 1);
		lstDate.add(endPosition, (String) startObject);
		lstDate.remove(endPosition + 1);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.activity_splash_mi_item, null);
		txtAge = (ImageView) convertView.findViewById(R.id.txt_userAge);
		bt_go = (Button) convertView.findViewById(R.id.bt_go);

        if(WelcomeActivity.whichPotion==3){
            txtAge.setBackgroundResource(R.drawable.timg2);
        }
		if(lstDate.get(position)==null){
			//txtAge.setText("+");
			//txtAge.setBackgroundResource(R.drawable.mi_laucher_red);

		}
		else if(lstDate.get(position).equals("none")){

		}else {

		}

		return convertView;
	}

	public void setButton(boolean b){
		if(b){
			bt_go.setVisibility(View.VISIBLE);
			Log.e("liuheng","aaa");
		}else {
			bt_go.setVisibility(View.GONE);
			Log.e("liuheng","ccc");
		}
			notifyDataSetChanged();
			Log.e("liuheng","bbbbbbb");
	}
}
