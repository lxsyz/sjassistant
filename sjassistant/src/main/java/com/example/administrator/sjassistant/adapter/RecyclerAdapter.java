package com.example.administrator.sjassistant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.util.Constant;
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

    private Context mContext;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_person,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (getItemCount() == 10) {
            holder.iv.setVisibility(View.GONE);
        } else {
            holder.iv.setVisibility(View.VISIBLE);
        }

        if (position == getItemCount() - 1) {
            holder.iv.setImageResource(R.drawable.add02);
            holder.tv.setText("添加人员");
            holder.itemView.setTag("add");

        } else {
            if (position == 0) {
                String imgUrl =  Constant.SERVER_URL + "images/" + mDatas.get(0).getUserCode() + ".jpg";
                Glide.with(mContext).load(imgUrl)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .error(R.drawable.customer_de).into(holder.iv);
            } else if (mDatas.get(position).getUserCode() != null) {
                String imgUrl = Constant.SERVER_URL + "images/" + mDatas.get(position).getUserCode() + ".jpg";
                Glide.with(mContext).load(imgUrl)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .error(R.drawable.customer_de).into(holder.iv);
            } else {
                holder.iv.setImageResource(R.drawable.customer_de);
            }

            holder.tv.setText(mDatas.get(position).getLinkName());
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

    public void update(List<Person> data) {
        this.mDatas = data;
        notifyDataSetChanged();
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
