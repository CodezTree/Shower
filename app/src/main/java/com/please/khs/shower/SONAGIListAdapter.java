package com.please.khs.shower;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SONAGIListAdapter extends RecyclerView.Adapter<SONAGIListAdapter.ViewHolder> {
    Context context;
    ArrayList<MemoData> memoList;

    public SONAGIListAdapter(Context context, ArrayList<MemoData> memoList) {
        this.context = context;
        this.memoList = memoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_timeline, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MemoData memData = memoList.get(position);

        holder.memo_text.setText(memData.memo);
        holder.time_text.setText(memData.time.substring(11, 19));
        if (memData.emotion <= 4) {
            holder.right_punc.setImageResource(R.drawable.double_downpyo_blue);
            holder.left_punc.setImageResource(R.drawable.double_downpyo_blue);
            holder.timeline_image.setImageResource(R.drawable.timeline_blue);
        } else {
            holder.right_punc.setImageResource(R.drawable.double_downpyo_pink);
            holder.left_punc.setImageResource(R.drawable.double_downpyo_pink);
            holder.timeline_image.setImageResource(R.drawable.timeline_pink);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView memo_text;
        TextView time_text;
        ImageView timeline_image;
        ImageView left_punc;
        ImageView right_punc;

        public ViewHolder(View v) {
            super(v);

            memo_text = v.findViewById(R.id.memoText);
            time_text = v.findViewById(R.id.timeText);
            timeline_image = v.findViewById(R.id.timelineimg);
            left_punc = v.findViewById(R.id.leftPunc);
            right_punc = v.findViewById(R.id.rightPunc);
        }
    }

    @Override
    public int getItemCount() {
        return this.memoList.size();
    }
}
