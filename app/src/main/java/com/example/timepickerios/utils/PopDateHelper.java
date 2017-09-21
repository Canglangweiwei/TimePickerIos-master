package com.example.timepickerios.utils;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.timepickerios.R;
import com.example.timepickerios.picker.LoopListener;
import com.example.timepickerios.picker.LoopView;

@SuppressWarnings("ALL")
public class PopDateHelper {

    private Context context;
    private PopupWindow pop;
    private View view;
    private OnClickOkListener onClickOkListener;

    private List<String> listDate, listTime;
    private String date, time;
    private int year, month, day;

    public PopDateHelper(Context context) {
        this.context = context;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.picker_date, null);
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        initPop();
        initData();
        initView();
    }

    private void initPop() {
        pop.setAnimationStyle(android.R.style.Animation_InputMethod);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnOk = (Button) view.findViewById(R.id.btnOK);
        final LoopView loopView1 = (LoopView) view.findViewById(R.id.loopView1);
        final LoopView loopView2 = (LoopView) view.findViewById(R.id.loopView2);
        loopView1.setIsViewYear(false);// 不显示年
        loopView1.setList(listDate);
        loopView1.setNotLoop();
        loopView1.setCurrentItem(0);

        loopView2.setList(listTime);
        loopView2.setNotLoop();
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
                pop.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onClickOkListener.onClickOk(date, time);
                    }
                }, 200);
            }
        });
    }

    /**
     * 显示
     *
     * @param view
     */
    public void show(View view) {
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 隐藏监听
     *
     * @param onDismissListener
     */
    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        pop.setOnDismissListener(onDismissListener);
    }

    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    public interface OnClickOkListener {
        void onClickOk(String date, String time);
    }
}
