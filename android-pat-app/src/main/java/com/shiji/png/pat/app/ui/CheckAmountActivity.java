package com.shiji.png.pat.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.shiji.png.pat.app.R;
import com.shiji.png.pat.app.mvp.BaseActivity;
import com.shiji.png.pat.app.domain.persistence.CheckStore;
import com.shiji.png.pat.app.router.ARouter;
import com.shiji.png.pat.app.router.Route;
import com.shiji.png.pat.model.CheckInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = RoutePath.ACTIVITY_CHECK_AMOUNT)
public class CheckAmountActivity extends BaseActivity {

    private static final String TAG = "CheckAmountActivity";

    @BindView(R.id.txt_txn_amt)
    TextView txtTxnAmt;

    @OnClick(R.id.btn_print_check)
    void onClickPrintCheck() {
        CheckInfo checkInfo = new CheckStore(getApplicationContext()).load();
        ARouter.getInstance()
                .build(RoutePath.ACTIVITY_PRINT_PREVIEW)
                .withString(RoutePath.EXTRA_TABLE_NO, checkInfo.getTableNo())
                .withString(RoutePath.EXTRA_CHECK_NO, checkInfo.getCheckNo())
                .navigation();
    }
    @OnClick(R.id.btn_proceed)
    void onClickProceed() {
        ARouter.getInstance().build(RoutePath.ACTIVITY_CARD_TYPE).navigation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_amount);

        ButterKnife.bind(this);

        logoInto(R.id.img_logo);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        init();
    }

    private void init() {
        CheckInfo checkInfo = new CheckStore(getApplicationContext()).load();
        if (checkInfo == null) {
            throw new NullPointerException("checkInfo");
        }
        Log.d(TAG, "init: " + checkInfo.toString());
        setAmountView(txtTxnAmt, checkInfo.getAmt());
    }
}
