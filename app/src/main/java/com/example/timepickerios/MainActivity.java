package com.example.timepickerios;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.timepickerios.utils.DatePackerUtil;
import com.example.timepickerios.utils.PopBirthHelper;
import com.example.timepickerios.utils.PopDateHelper;
import com.example.timepickerios.utils.PopOneHelper;

import butterknife.Bind;
import butterknife.OnClick;

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
    protected int initLayoutId() {
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

        popOneHelper.setListItem(DatePackerUtil.getTimeAllList());
        popOneHelper.setOnClickOkListener(new PopOneHelper.OnClickOkListener() {
            @Override
            public void onClickOk(String str) {
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3})
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
        }
    }
}
