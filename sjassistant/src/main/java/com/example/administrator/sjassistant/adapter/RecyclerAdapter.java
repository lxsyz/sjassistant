package com.example.administrator.sjassistant.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements View.OnClickListener {
    private List<Person> mDatas = new ArrayList<Person>();

    public RecyclerAdapter(List mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            holder.iv.setImageResource(R.drawable.add02);
            holder.tv.setText("添加人员");
            holder.itemView.setTag("add");

        } else {
            holder.iv.setImageResource(R.drawable.customer_de);
            Log.d("position",position+" ");
            holder.itemView.setTag(mDatas.get(position));
        }


    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            onClickListener.onClick(v, v.getTag());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView iv;
        TextView tv;
        public MyViewHolder(View view)
        {
            super(view);

            iv = (CircleImageView)view.findViewById(R.id.iv);

            tv = (TextView)view.findViewById(R.id.username);
        }

    }


    public void addPerson(Person person) {
        mDatas.add(0,person);
        notifyItemInserted(0);
    }

    public void removePerson(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public void removePerson(Person person) {
        for (int i = 0;i < mDatas.size() - 1;i++) {
            if (mDatas.get(i).equals(person)) {
                mDatas.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }

    }

    interface OnItemLongClickListener {
        void onLongClick(View v);
    }

    public interface OnItemClickListener {
        void onClick(View v,Object object);
    }

    private OnItemClickListener onClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnClickListener(OnItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public OnItemClickListener getOnClickListener() {
        return onClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }


}
