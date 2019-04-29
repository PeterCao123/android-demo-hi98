package com.shiji.png.pat.app.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.shiji.png.pat.app.BuildConfig;
import com.shiji.png.pat.app.R;
import com.shiji.png.pat.app.mvp.BaseActivity;
import com.shiji.png.pat.app.router.Route;

@Route(path = RoutePath.ACTIVITY_ABOUT)
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView lblVersion = findViewById(R.id.lbl_version);
        lblVersion.setText("v" + BuildConfig.VERSION_NAME);
    }
}
