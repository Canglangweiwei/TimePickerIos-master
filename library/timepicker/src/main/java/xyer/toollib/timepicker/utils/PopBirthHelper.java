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
 * 生日日历日期选择器
 */
@SuppressWarnings("ALL")
public class PopBirthHelper {

    private Context context;
    private PopupWindow mPopupWindow;
    private View mRootView;
    private OnClickOkListener onClickOkListener;

    private List<String> listYear, listMonth, listDay;
    private String year, month, day;

    public PopBirthHelper(Context context) {
        this.context = context;
        mRootView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.picker_birth, null);
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
        listYear = DatePackerUtil.getBirthYearList();
        listMonth = DatePackerUtil.getBirthMonthList();
        listDay = DatePackerUtil.getBirthDay31List();
    }

    private void initView() {
        TextView btnCancel = (TextView) mRootView.findViewById(R.id.btnCancel);
        TextView btnOk = (TextView) mRootView.findViewById(R.id.btnOK);
        final LoopView loopView1 = (LoopView) mRootView.findViewById(R.id.loopView1);
        final LoopView loopView2 = (LoopView) mRootView.findViewById(R.id.loopView2);
        final LoopView loopView3 = (LoopView) mRootView.findViewById(R.id.loopView3);
        loopView1.setList(listYear);
        loopView1.setNotLoop();

        Calendar c = Calendar.getInstance();
        loopView1.setCurrentItem(59);// 定位到今年
        loopView2.setList(listMonth);
        loopView2.setNotLoop();
        loopView2.setCurrentItem(0);

        loopView3.setList(listDay);
        loopView3.setNotLoop();
        loopView3.setCurrentItem(0);

        loopView1.setListener(new LoopListener() {

            @Override
            public void onItemSelect(int item) {
                String select_item = listYear.get(item);
                if (TextUtils.isEmpty(year)) {
                    year = listYear.get(59);
                } else {
                    year = select_item.replace("年", "");
                }
                if (!TextUtils.isEmpty(month) && month.equals("02")) {
                    if (DatePackerUtil.isRunYear(year) && listDay.size() != 29) {
                        listDay = DatePackerUtil.getBirthDay29List();
                        loopView3.setList(listDay);
                        loopView3.setCurrentItem(0);
                    } else if (!DatePackerUtil.isRunYear(year) && listDay.size() != 28) {
                        listDay = DatePackerUtil.getBirthDay28List();
                        loopView3.setList(listDay);
                        loopView3.setCurrentItem(0);
                    }
                }
            }
        });
        loopView2.setListener(new LoopListener() {

            @Override
            public void onItemSelect(int item) {
                String select_item = listMonth.get(item);
                if (TextUtils.isEmpty(month)) {
                    month = "01";
                } else {
                    month = select_item.replace("月", "");
                }
                if (month.equals("02")) {
                    if (!TextUtils.isEmpty(year)
                            && DatePackerUtil.isRunYear(year)
                            && listDay.size() != 29) {
                        listDay = DatePackerUtil.getBirthDay29List();
                        loopView3.setList(listDay);
                        loopView3.setCurrentItem(0);
                    } else if (!TextUtils.isEmpty(year)
                            && !DatePackerUtil.isRunYear(year)
                            && listDay.size() != 28) {
                        listDay = DatePackerUtil.getBirthDay28List();
                        loopView3.setList(listDay);
                        loopView3.setCurrentItem(0);
                    }
                } else if ((month.equals("01")
                        || month.equals("03")
                        || month.equals("05")
                        || month.equals("07")
                        || month.equals("08")
                        || month.equals("10")
                        || month.equals("12"))
                        && listDay.size() != 31) {
                    listDay = DatePackerUtil.getBirthDay31List();
                    loopView3.setList(listDay);
                    loopView3.setCurrentItem(0);
                } else if ((month.equals("04")
                        || month.equals("06")
                        || month.equals("09")
                        || month.equals("11"))
                        && listDay.size() != 30) {
                    listDay = DatePackerUtil.getBirthDay30List();
                    loopView3.setList(listDay);
                    loopView3.setCurrentItem(0);
                }
            }
        });
        loopView3.setListener(new LoopListener() {

            @Override
            public void onItemSelect(int item) {
                String select_item = listDay.get(item);
                if (TextUtils.isEmpty(day)) {
                    day = "01";
                } else {
                    day = select_item.replace("日", "");
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
                onClickOkListener.onClickOk(year + "-" + month + "-" + day);
            }
        });
    }

    /**
     * 显示
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
        void onClickOk(String birthday);
    }
}
