package com.shiji.png.pat.app.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.shiji.png.pat.app.R;
import com.shiji.png.pat.app.domain.model.PrefInfo;
import com.shiji.png.pat.app.functional.EmptyObserver;
import com.shiji.png.pat.app.mvp.BaseActivity;
import com.shiji.png.pat.app.router.ARouter;
import com.shiji.png.pat.app.router.Autowired;
import com.shiji.png.pat.app.router.Route;
import com.shiji.png.pat.app.spat.Transform;
import com.shiji.png.pat.app.ui.dialog.DialogService;
import com.shiji.png.pat.app.ui.dialog.ProcessDialogService;
import com.shiji.png.pat.printer.PrinterLoader;
import com.shiji.png.pat.printer.model.PrintingCheck;
import com.shiji.png.pat.spat.service.ApiConfig;
import com.shiji.png.pat.spat.service.SpatClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Route(path = RoutePath.ACTIVITY_PRINT_PREVIEW)
public class PrintPreviewActivity extends BaseActivity {

    private static final String TAG = "PrintPreviewActivity";

    private PrintingCheck printingCheck;

    @Autowired(name = RoutePath.EXTRA_TABLE_NO, required = true)
    String tableNo;
    @Autowired(name = RoutePath.EXTRA_CHECK_NO, required = true)
    String checkNo;
    @Autowired(name = RoutePath.EXTRA_RECEIPT)
    boolean receipt;

    @BindView(R.id.img_preview)
    ImageView imgPreview;
    @BindView(R.id.btn_print)
    Button btnPrint;

    @OnClick(R.id.btn_cancel)
    void onClickCancel() {
        finish();
    }
    @OnClick(R.id.btn_print)
    void onClickPrint() {
        final int receiptFlag = getAppPreferences().getReceiptTypeAsInt();
        Observable.create(emitter -> {
            try {
                PrinterLoader.discover().print(receiptFlag, printingCheck, getCurrency());
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> btnPrint.setEnabled(false))
                .doOnError(tr -> Log.e(TAG, tr.getMessage(), tr))
                .doOnError(tr -> new DialogService(this).alert(R.string.print_task_failed))
                .doOnError(tr -> btnPrint.setEnabled(true))
                .doOnComplete(() -> new DialogService(this).alert(R.string.print_completed))
                .doOnComplete(() -> btnPrint.setEnabled(true))
                .subscribe(new EmptyObserver<>());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_preview);

        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadPrintingCheck();
    }

    private void loadPrintingCheck() {
        final PrefInfo prefInfo = getAppPreferences();
        final ApiConfig config = prefInfo.getApiConfig();
        final DialogService dialogService = new DialogService(this)
                .positive(this::finish);
        final ProcessDialogService processDialogService = new ProcessDialogService(this);
        SpatClient client = SpatClient.builder().config(config).build();
        client.getPrintCheck(tableNo, checkNo, prefInfo.getReceiptTypeAsInt())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> processDialogService.show())
                .doOnNext(dto -> {
                    if (!dto.approved()) {
                        dialogService.alert(dto.getErrorMessage());
                        return;
                    }
                    handlePrintCheck(Transform.getPrintingCheck(dto.getData()));
                })
                .doOnComplete(processDialogService::hide)
                .doOnError(tr -> processDialogService.hide())
                .doOnError(tr -> Log.e(TAG, "getPrintCheck", tr))
                .doOnError(dialogService::alert)
                .subscribe(new EmptyObserver<>());
    }

    private void handlePrintCheck(PrintingCheck printingCheck) {
        btnPrint.setEnabled(true);
        this.printingCheck = printingCheck;
        int receiptFlag = getAppPreferences().getReceiptTypeAsInt();
        Bitmap bitmap = PrinterLoader.discover().preview(receiptFlag, printingCheck, getCurrency());
        imgPreview.setImageBitmap(bitmap);
    }

}
