package com.example.timepickerios;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.OnClick;
import xyer.toollib.timepicker.utils.DatePackerUtil;
import xyer.toollib.timepicker.utils.PopBirthHelper;
import xyer.toollib.timepicker.utils.PopDateHelper;
import xyer.toollib.timepicker.utils.PopOneHelper;

@SuppressWarnings("ALL")
public class MainActivity extends BaseActivity {

    @Bind(R.id.btn1)
    Button btn1;
    @Bind(R.id.btn2)
    Button btn2;
    @Bind(R.id.btn3)
    Button btn3;

    private PopBirthHelper popBirthHelper;
    private PopDateHelper popDateHelper;
    private PopOneHelper popOneHelper;

    @Override
    protected int loadLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initUi() {
        popBirthHelper = new PopBirthHelper(this);
        popDateHelper = new PopDateHelper(this);
        popOneHelper = new PopOneHelper(this);
    }

    @Override
    protected void initDatas() {
        popOneHelper.setListItem(DatePackerUtil.getTimeAllList());
    }

    @Override
    protected void initListener() {
        popBirthHelper.setOnClickOkListener(new PopBirthHelper.OnClickOkListener() {
            @Override
            public void onClickOk(String birthday) {
                Toast.makeText(MainActivity.this, birthday, Toast.LENGTH_LONG).show();
            }
        });

        popDateHelper.setOnClickOkListener(new PopDateHelper.OnClickOkListener() {
            @Override
            public void onClickOk(String date, String time) {
                Toast.makeText(MainActivity.this, date + " " + time, Toast.LENGTH_LONG).show();
            }
        });

        popOneHelper.setOnClickOkListener(new PopOneHelper.OnClickOkListener() {
            @Override
            public void onClickOk(String str) {
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.button})
    void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                popBirthHelper.show(btn1);
                break;
            case R.id.btn2:
                popDateHelper.show(btn2);
                break;
            case R.id.btn3:
                popOneHelper.show(btn3);
                break;
            case R.id.button:
                Toast.makeText(this, "事件没有被分发下去", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (popBirthHelper != null && popBirthHelper.isShowing()) {
            return false;
        } else if (popDateHelper != null && popDateHelper.isShowing()) {
            return false;
        } else if (popOneHelper != null && popOneHelper.isShowing()) {
            return false;
        }
        return super.dispatchTouchEvent(event);
    }
}
