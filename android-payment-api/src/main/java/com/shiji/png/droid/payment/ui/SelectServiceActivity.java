package com.shiji.png.droid.payment.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shiji.png.droid.payment.R;
import com.shiji.png.payment.LoadServiceException;
import com.shiji.png.payment.ServiceInfo;
import com.shiji.png.payment.ServiceManager;
import com.shiji.png.payment.rx.RxServiceObservable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

public class SelectServiceActivity extends AppCompatActivity {

    public static void start(Context context, String sourceId, String amount) {
        Intent intent = new Intent(context, SelectServiceActivity.class);
        intent.putExtra(EXTRA_SOURCE_ID, sourceId);
        if (!TextUtils.isEmpty(amount)) {
            intent.putExtra(EXTRA_AMOUNT, amount);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private static final Logger logger = LoggerFactory.getLogger(SelectServiceActivity.class);

    private static final String EXTRA_SOURCE_ID = "sourceId";
    private static final String EXTRA_AMOUNT = "amount";

    private String sourceId;
    private RecyclerView recyclerView;

    private List<ServiceInfo> services;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        error(new RuntimeException("Cancelled"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);

        getWindow().addFlags(FLAG_KEEP_SCREEN_ON);

        recyclerView = findViewById(R.id.list);

        this.sourceId = getIntent().getStringExtra(EXTRA_SOURCE_ID);
        if (TextUtils.isEmpty(sourceId)) {
            finish();
            return;
        }

        this.services = ServiceManager.getServiceList();
        if (services.isEmpty()) {
            error(new LoadServiceException("empty services"));
            return;
        }

        if (services.size() == 1) {
            complete(services.get(0));
            return;
        }

        initAmount();

        startTimer();

        setupList();
    }

    private void initAmount() {
        String amount = getIntent().getStringExtra(EXTRA_AMOUNT);
        logger.info("amount={}", amount);
        if (TextUtils.isEmpty(amount)) {
            return;
        }
        TextView lblAmount = findViewById(R.id.lbl_amount);
        lblAmount.setText(amount);
        findViewById(R.id.panel_amount).setVisibility(View.VISIBLE);
    }

    private Disposable disposable;

    private void startTimer() {
        disposable = Observable.timer(30, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(i -> error(new RuntimeException("Select timeout")))
                .subscribe();
    }

    private void stopTimer() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }

    private void error(Throwable throwable) {
        Emitter<ServiceInfo> emitter = RxServiceObservable.getRegistry().find(sourceId);
        emitter.onError(throwable);
        finish();

        stopTimer();
    }

    private void complete(ServiceInfo serviceInfo) {
        Emitter<ServiceInfo> emitter = RxServiceObservable.getRegistry().find(sourceId);
        emitter.onNext(serviceInfo);
        emitter.onComplete();
        finish();

        stopTimer();
    }

    private void setupList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private final RecyclerView.Adapter<ItemViewHolder> adapter = new RecyclerView.Adapter<ItemViewHolder>() {
        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_payment_service_item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return services.size();
        }
    };

    class ItemViewHolder extends RecyclerView.ViewHolder {

        Button button;

        ItemViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.item_name);
        }

        void bind(int position) {
            final ServiceInfo serviceInfo = services.get(position);
            button.setText(serviceInfo.getDisplayText());
            button.setOnClickListener(v -> complete(serviceInfo));
        }
    }
}
