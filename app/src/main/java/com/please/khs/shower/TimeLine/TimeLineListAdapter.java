/*package com.example.khs.shower.TimeLine;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.khs.shower.R;

import java.util.ArrayList;

public class TimeLineListAdapter extends RecyclerView.Adapter<TimeLineListAdapter.ViewHolder>{

    Context context;
    ArrayList<TimeLine> memoList; //찬반 카드

    public TimeLineListAdapterr(Context context, ArrayList<TimeLine> memoList) {
        this.context = context;
        this.memoList = memoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //recycler view에 반복될 아이템 레이아웃 연결, 슬픈감정파랑/좋은감정핑크
        //if (감정수치가 4이하이면 슬픈감정(파랑))
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_timeline_blue,null);
        //elseif(감정수치가 5이상이면 좋은감정(핑크))


        /*int proConSum = v.findViewById(R.id.pro).getWidth() + v.findViewById(R.id.con).getWidth();

        TextView proTV = (TextView) v.findViewById(R.id.pro);
        TextView conTV = (TextView) v.findViewById(R.id.con);

        LinearLayout proConBar = (LinearLayout) v.findViewById(R.id.proConBar);

        Log.d("test","pro");

        v.findViewById(R.id.pro).setLayoutParams(new LinearLayout.LayoutParams(,20));
        v.findViewById(R.id.con).setLayoutParams(new LinearLayout.LayoutParams(,20));*/

  /*    return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  //  /** 정보 및 이벤트 처리는 이 메소드에서 구현 **/
   /* @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TimeLine memolist = memoList.get(position);

        final String url = memoList.url;
        final String time =
        Intent i = new Intent(context, TimeLineDetailActivity.class);

        i.putExtra("time", memoList.time);
        i.putExtra("memoUrl", agDag.url);

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);//!
    }

    @Override
    public int getItemCount() {
        return this.memoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time;
        TextView tv_memo;
        ConstraintLayout tv_proConBar;
        ImageView iv_image;

        public ViewHolder(View v) {
            super(v);
            tv_time = (TextView) v.findViewById(R.id.timeText);
            tv_memo = (TextView) v.findViewById(R.id.memoText);
            tv_proConBar = (LinearLayout) v.findViewById(R.id.proConBar);
            iv_image = (ImageView) v.findViewById(R.id.battleImage);

            cv = (CardView) v.findViewById(R.id.cv);
        }
    }
}*/
