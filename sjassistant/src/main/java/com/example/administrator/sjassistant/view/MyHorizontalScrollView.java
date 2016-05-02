package com.example.administrator.sjassistant.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.HorizontalScrollViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyHorizontalScrollView extends HorizontalScrollView implements
		OnClickListener
{
	
	private Context context;


	/**
	 * 条目点击时的回调
	 * 
	 * 
	 */
	public interface OnItemClickListener
	{
		void onClick(View view, int pos);
	}


	private OnItemClickListener mOnClickListener;

	private static final String TAG = "MyHorizontalScrollView";

	/**
	 * HorizontalListView中的LinearLayout
	 */
	private LinearLayout mContainer = (LinearLayout)getChildAt(0);


	/**
	 * 当前第一个View
	 */
	private View mFirstView;
	/**
	 * 数据适配器
	 */
	private HorizontalScrollViewAdapter mAdapter;

	/**
	 * 保存View与位置的键值对
	 */
	private Map<View, Integer> mViewPos = new HashMap<View, Integer>();

	
	private List<String> dataList = new ArrayList<String>();
	
	public MyHorizontalScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context=context;

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mContainer = (LinearLayout) getChildAt(0);
	}


	/**
	 * 初始化数据，设置数据适配器
	 * 
	 * @param mAdapter
	 */
	public void initDatas(HorizontalScrollViewAdapter mAdapter)
	{
		mContainer = (LinearLayout) getChildAt(0);
		mContainer.removeAllViews();
		mViewPos.clear();
		this.mAdapter = mAdapter;
		mContainer = (LinearLayout) getChildAt(0);
		if (this.mAdapter.getCount() == 0) {
			return;
		}
		
		// 获得适配器中第一个View
		// final View view = mAdapter.getView(0, null, mContainer);
		//mContainer.addView(view);

		for (int i = 0; i < mAdapter.getCount(); i++)
		{
			View view = mAdapter.getView(i, null, mContainer);
			view.setOnClickListener(this);
			mContainer.addView(view);
			mViewPos.put(view, i);
		}
		//初始化第一屏幕的元素
		//initFirstScreenChildren(mCountOneScreen);
	}


	/**
	 * 加载第一屏的View
	 *
	 */
//	public void initFirstScreenChildren(int mCountOneScreen)
//	{
//		mContainer = (LinearLayout) getChildAt(0);
//		mContainer.removeAllViews();
//		mViewPos.clear();
//		Log.d("initfirst", mContainer.getChildCount()+" ");
//		//scrollBy(-mScreenWitdh / 2, 0);
//		int temp = mCountOneScreen;
//		if (mCountOneScreen > mAdapter.getCount()) {
//			temp = mAdapter.getCount();
//
//			for (int i = 0; i < temp; i++)
//			{
//				View view = mAdapter.getView(i, null, mContainer);
//				view.setOnClickListener(this);
//				mContainer.addView(view);
//				mViewPos.put(view, i);
//				mCurrentIndex = i;
//				rightTempIndex = i;
//			}
//			//初始化后面的空数据
//			for (int j = temp;j < mCountOneScreen;j++) {
//				View emptyView = mAdapter.getEmptyView(j, null, mContainer);
//				//view.setOnClickListener(this);
//				mContainer.addView(emptyView);
//				mViewPos.put(emptyView, j);
//				mCurrentIndex = j;
//				rightTempIndex = j;
//				emptyViewSum++;
//			}
//		}
//
//	}

	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		return super.onTouchEvent(ev);
	}

	@Override
	public void onClick(View v)
	{
		if (mOnClickListener != null)
		{

			TextView time = (TextView)v.findViewById(R.id.item_data);
			time.setTextColor(Color.rgb(126, 166, 191));
			mOnClickListener.onClick(v, mViewPos.get(v));

		}
	}

	
	public void setOnItemClickListener(OnItemClickListener mOnClickListener)
	{
		this.mOnClickListener = mOnClickListener;
	}
	public void clearView () {
		mViewPos.clear();
	}

}
