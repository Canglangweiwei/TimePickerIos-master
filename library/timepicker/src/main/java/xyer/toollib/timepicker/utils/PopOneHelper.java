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
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xyer.toollib.timepicker.LoopListener;
import xyer.toollib.timepicker.LoopView;
import xyer.toollib.timepicker.R;

/**
 * 自定义时间选择器
 */
@SuppressWarnings("ALL")
public class PopOneHelper {

    private Context context;
    private PopupWindow mPopupWindow;
    private View mRootView;
    private OnClickOkListener onClickOkListener;

    private List<String> listItem;
    private String _item;

    public PopOneHelper(Context context) {
        this.context = context;
        mRootView =
                ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.picker_one, null);
        mPopupWindow = new PopupWindow(mRootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        initPopupWindow();
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

    private void initView() {
        TextView btnCancel = (TextView) mRootView.findViewById(R.id.btnCancel);
        TextView btnOk = (TextView) mRootView.findViewById(R.id.btnOK);
        final LoopView loopView = (LoopView) mRootView.findViewById(R.id.loopView);
        if (null == listItem) {
            listItem = new ArrayList<String>();
        }
        loopView.setList(listItem);
        loopView.setNotLoop();
        loopView.setCurrentItem(0);

        loopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                if (TextUtils.isEmpty(_item)) {
                    _item = listItem.get(0);
                } else {
                    _item = listItem.get(item);
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
                onClickOkListener.onClickOk(_item);
            }
        });
    }

    /**
     * 显示
     *
     * @param view
     */
    public void show(View view) {
        if (null == listItem || listItem.size() <= 0) {
            Toast.makeText(context, "请初始化您的数据", Toast.LENGTH_LONG).show();
            return;
        }
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

    public void setListItem(List<String> listItem) {
        this.listItem = listItem;
        // 页面加载
        initView();
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
        void onClickOk(String str);
    }
}
