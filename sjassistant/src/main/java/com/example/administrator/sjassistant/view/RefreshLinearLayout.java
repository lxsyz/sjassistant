package com.example.administrator.sjassistant.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;

/**
 * Created by Administrator on 2016/6/29.
 */
public class RefreshLinearLayout extends LinearLayout {

    private Context mContext;
    private View mHeadView;
    private TextView mTv;

    private int mHeadViewHeight = 70;//单位dp
    private int mStartX;
    private int mStartY;
    private int mEndX;
    private int mEndY;
    private int dx;
    private int dy;

    /**
     * 是否正在刷新标志位
     */
    private boolean isRefresh;

    public RefreshLinearLayout(Context context) {
        this(context,null);
    }

    public RefreshLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        forceVertival();
        addHeadView();
        onComplete();
    }

    /**
     * 强制设置垂直方向
     */
    private void forceVertival() {
        if (getOrientation() == LinearLayout.HORIZONTAL){
            setOrientation(LinearLayout.VERTICAL);
        }
    }

    /**
     * 增加头布局文件
     */
    private void addHeadView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mHeadView = inflater.inflate(R.layout.list_refresh_head,null);
        mHeadViewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) mHeadViewHeight, mContext.getResources().getDisplayMetrics());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mHeadViewHeight);
        mHeadView.setLayoutParams(params);
        this.addView(mHeadView);
        mTv = (TextView)(mHeadView.findViewById(R.id.state_tv));
    }

    /**
     * 隐藏HeadView
     */
    public void onComplete(){
        setHeadViewPaddingTop(mHeadViewHeight);
    }

    /**
     *
     * @param paddingTop 隐藏的高度
     */
    private void setHeadViewPaddingTop(int paddingTop){
        if(paddingTop > mHeadViewHeight){
            new IllegalArgumentException("paddingTop must < HeadViewHeight ");
        }
        setPadding(getPaddingLeft(),-paddingTop,getPaddingRight(),getPaddingBottom());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) event.getX();
                mStartY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mEndX = (int) event.getX();
                mEndY = (int) event.getY();
                dx = Math.abs(mEndX - mStartX);
                dy = mEndY - mStartY;
                if (0 < dy && dy < mHeadViewHeight && dy>dx){
                    setHeadViewPaddingTop(mHeadViewHeight - dy );
                    mTv.setText("下拉加载数据");
                }else if(dy >= mHeadViewHeight && dy>dx){
                    setHeadViewPaddingTop(0);
                    isRefresh = true;
                    mTv.setText("松开即可刷新");
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isRefresh) {
                    setHeadViewPaddingTop(0);
                    mTv.setText("正在刷新");
                    if (mIRefreshListener != null){
                        mIRefreshListener.onBegin();
                        mIRefreshListener.onLoading();
                        mIRefreshListener.onEnd();
                    }
                    isRefresh = false;
                } else {
                    onComplete();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private IRefreshListener mIRefreshListener;

    public interface IRefreshListener{
        void onBegin();
        void onLoading();
        void onEnd();
    }

    public void setIRefreshListener(IRefreshListener mIRefreshListener) {
        this.mIRefreshListener = mIRefreshListener;
    }
}
