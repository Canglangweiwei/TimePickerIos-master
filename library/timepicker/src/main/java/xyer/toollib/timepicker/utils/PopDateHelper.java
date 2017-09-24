package xyer.toollib.timepicker.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import xyer.toollib.timepicker.LoopListener;
import xyer.toollib.timepicker.LoopView;
import xyer.toollib.timepicker.R;

/**
 * 自定义日期选择器
 */
@SuppressWarnings("ALL")
public class PopDateHelper {

    private Context context;
    private PopupWindow mPopupWindow;
    private View mRootView;
    private OnClickOkListener onClickOkListener;

    private List<String> listDate, listTime;
    private String date, time;
    private int year, month, day;

    public PopDateHelper(Context context) {
        this.context = context;
        mRootView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.picker_date, null);
        mPopupWindow = new PopupWindow(mRootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        initPopupWindow();
        initData();
        initView();
    }

    private void initPopupWindow() {
        mPopupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            // 在dismiss中恢复透明度
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                ((Activity) context).getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        listDate = DatePackerUtil.getDateList();
        listTime = DatePackerUtil.getTimeAllList();
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    private void initView() {
        TextView btnCancel = (TextView) mRootView.findViewById(R.id.btnCancel);
        TextView btnOk = (TextView) mRootView.findViewById(R.id.btnOK);
        final LoopView loopView1 = (LoopView) mRootView.findViewById(R.id.loopView1);
        final LoopView loopView2 = (LoopView) mRootView.findViewById(R.id.loopView2);
        loopView1.setIsViewYear(false);// 不显示年
        loopView1.setList(listDate);
        // 设置不循环
//        loopView1.setNotLoop();
        loopView1.setCurrentItem(0);

        loopView2.setList(listTime);
        // 设置不循环
//        loopView2.setNotLoop();
        loopView2.setCurrentItem(0);

        loopView1.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                String select_date = listDate.get(item);
                if (TextUtils.isEmpty(date)) {
                    date = year + "-" + month + "-" + day;
                } else {
                    date = select_date.substring(0, select_date.indexOf("日")).replace("年", "-").replace("月", "-");
                }
            }
        });
        loopView2.setListener(new LoopListener() {

            @Override
            public void onItemSelect(int item) {
                if (TextUtils.isEmpty(time)) {
                    time = listTime.get(0);
                } else {
                    time = listTime.get(item);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                onClickOkListener.onClickOk(date, time);
            }
        });
    }

    /**
     * 显示
     *
     * @param view
     */
    public void show(View view) {
        // 产生背景变暗效果
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = 0.4f;
        ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity) context).getWindow().setAttributes(lp);
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    /**
     * 隐藏监听
     *
     * @param onDismissListener
     */
    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        mPopupWindow.setOnDismissListener(onDismissListener);
    }

    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    public interface OnClickOkListener {
        void onClickOk(String date, String time);
    }
}
