package com.xiang.pulltorefrelistviewproject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReFlashListView extends ListView implements AbsListView.OnScrollListener {
    View head;//顶部布局文件
    int headHeight;//顶部布局文件的高度
    int firstVisibleItem;//当前第一个可见item 位置
    boolean isRemark;//标记按下时在list view 最顶端
    int startY; //按下时候的y 值
    int start;// 当前状态
    final int NONE = 0;//正常状态
    final int PULL = 1;//提示下拉状态
    final int RELESE = 2;//松开释放状态
    final int REDLASGIBG = 3;//刷新状态
    int scrollState;//list view 当前滚动状态
    reflashListener anInterface;

    public ReFlashListView(Context context) {
        super(context);
        initView(context);
    }

    public ReFlashListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ReFlashListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    //初始化顶部布局文件
    private void initView(Context context) {
        head = LayoutInflater.from(context).inflate(R.layout.head_layout, null);
        measureView(head);
        headHeight = head.getMeasuredHeight();
        topPading(-headHeight);//注意添加的为高度发负值
        this.addHeaderView(head);
        //滚动监听
        this.setOnScrollListener(this);
    }

    //设置布局上边距
    private void topPading(int topPading) {
        head.setPadding(head.getPaddingLeft(), topPading, head.getPaddingRight(), head.getPaddingBottom());
    }

    //通知父布局栈用的宽高
    private void measureView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, params.width);
        int height;
        int tempHeight = params.height;
        //高度不为零需要填充，为o 不需要
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    //手势b
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_DOWN:
                //挡按下在第一item 时候 需要做标记

                if (firstVisibleItem == 0) {
                    isRemark = true;
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (start == RELESE) {
                    start = REDLASGIBG;
                    reflashViewByStart();
                    //需要加载新数据
                    anInterface.onRefalsh();

                } else if (start == PULL) {
                    start = NONE;
                    isRemark = false;
                    reflashViewByStart();
                }
                ;
                break;
        }
        return super.onTouchEvent(ev);
    }

    //判断一定过程的操作
    private void onMove(MotionEvent ev) {
        if (isRemark) {
            return;
        }
        //获取当前移动到的位置   移动的距离 //控制头部布局一点点出来
        int tempy = (int) ev.getY();
        int space = tempy - startY;
        int topPadding = space - headHeight;
        switch (start) {
            case NONE:
                //移动距离大于0；状态改变在移动
                if (space > 0) {
                    start = PULL;
                }
                reflashViewByStart();
                break;
            case PULL:
                topPading(topPadding);
                //移动距离大于一定数时候，并 正在滚动状态
                if (space > headHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    start = RELESE;
                }
                reflashViewByStart();
                break;
            case RELESE:
                topPading(topPadding);
                if (space < headHeight + 30) {
                    start = PULL;
                    reflashViewByStart();
                } else if (space <= 0) {
                    start = NONE;
                    isRemark = false;
                    reflashViewByStart();
                }
                break;
        }

    }


    //根据当前状态改变界面显示
    private void reflashViewByStart() {
        //  获得头部布局内容
        ImageView arrow = (ImageView) head.findViewById(R.id.arrow);
        TextView tip = (TextView) head.findViewById(R.id.tip);
        ProgressBar progress = (ProgressBar) head.findViewById(R.id.progress);
        RotateAnimation animation = new RotateAnimation(0, 180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f
        );
        animation.setDuration(500);
        animation.setFillAfter(true);
        RotateAnimation animation1 = new RotateAnimation(180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f
        );
        animation1.setDuration(500);
        animation1.setFillAfter(true);
        switch (start) {
            case NONE:
                topPading(-headHeight);
                break;
            case PULL:
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                tip.setText("下拉可以刷新");
                arrow.clearAnimation();
                arrow.setAnimation(animation);
                break;
            case RELESE:
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                tip.setText("松开可以刷新");
                arrow.clearAnimation();
                arrow.setAnimation(animation1);
                break;
            case REDLASGIBG:
                topPading(50);
                arrow.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                tip.setText("正在刷新");
                arrow.clearAnimation();
                break;
        }
    }

    //获取玩数据 需要更新时间
    public void reflashComplete() {
        start = NONE;
        reflashViewByStart();
        isRemark = false;
        TextView lasttime = (TextView) head.findViewById(R.id.lastup_data_time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = format.format(date);
        lasttime.setText(time);
    }

    //刷新数据的接口
    public interface reflashListener {
        void onRefalsh();

    }

    public void setInterface(reflashListener anInterface) {
        this.anInterface = anInterface;

    }

}
