package com.shiji.png.droid.icbc.simulator.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hisense.pos.cardreader.CardReader;
import com.hisense.pos.cardreader.IcCardSearchCallback;
import com.hisense.pos.cardreader.MagCardResult;
import com.hisense.pos.cardreader.MagCardSearchCallback;
import com.hisense.pos.cardreader.RfCardSearchCallback;
import com.hisense.pos.emv.AidInfo;
import com.hisense.pos.emv.StartAppData;
import com.hisense.pos.emvproc.EmvProc;
import com.hisense.pos.emvproc.ICCardResult;
import com.hisense.pos.ic.IcCard;
import com.shiji.png.droid.icbc.simulator.R;
import com.shiji.png.droid.icbc.simulator.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Falcon.cao
 * @date 2019/1/22
 */
public class BankCardsActivity extends AppCompatActivity implements IcCardSearchCallback, RfCardSearchCallback, MagCardSearchCallback {

    private EditText lblCardNo;
    private Button btnConfirm;
    private volatile String cardNo;

    private String amount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_cards_payment);

        amount = getIntent().getStringExtra("AMT");
        ((TextView) findViewById(R.id.lbl_amt)).setText(amount);

        lblCardNo = findViewById(R.id.lbl_card_no);
        lblCardNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cardNo = lblCardNo.getText().toString().trim();
                if(!StringUtils.isNullOrEmpty(cardNo)&&StringUtils.isInteger(cardNo)&&cardNo.length()>=13) {
                    btnConfirm.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnConfirm = findViewById(R.id.btn_confirm);

        reader = CardReader.getInstance();
        reader.setIcCardSearchCallback(BankCardsActivity.this);     // 插卡异步回调
        reader.setMagCardSearchCallback(BankCardsActivity.this);    // 刷卡异步回调
        reader.setRfCardSearchCallback(BankCardsActivity.this);     // 挥卡异步回调

        reader.searchCard(CardReader.MAGSLOT, 120000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent.putExtra("PAN", "");
            intent.putExtra("ENTRY", 1L);
            setResult(RESULT_OK, intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onClickConfirm(View view) {
        Intent intent = new Intent();
        intent.putExtra("PAN", lblCardNo.getText().toString());
        intent.putExtra("ENTRY", 1L);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    private void tips(final String tips) {
        Log.v("Bank Cards", tips);
    }

    private void onAddingAids(EmvProc e) {
        AidInfo fi = new AidInfo();
        fi.setTermAppVer("008C".getBytes());
        fi.setMaxTargetPer((byte) 0);
        fi.setDDOL("9F3704".getBytes());
        fi.setTDOL("9F02065F2A029A039C0195059F3704".getBytes());
        fi.setMerchName("VSDC_TEST_APP1".getBytes());
        fi.setAID("A0000000032010".getBytes());
        tips("aid first : " + e.getEmvL2().addAid(fi));

        AidInfo si = new AidInfo();
        si.setTermAppVer("0002".getBytes());
        si.setMaxTargetPer((byte) 0);
        si.setDDOL("9F3704".getBytes());
        si.setTDOL("9F02065F2A029A039C0195059F3704".getBytes());
        si.setMerchName("MCC_APP1".getBytes());
        si.setAID("A0000000041010".getBytes());
        tips("aid second : " + e.getEmvL2().addAid(si));

        e.getEmvL2().initTermData();
        e.getEmvL2().initEmvLib();
        e.getEmvL2().initTLV();

    }

    private void onReadingPAN(EmvProc e) {
        e.getEmvL2().initTermData();

        List list = new ArrayList();
        tips("start select : " + e.getEmvL2().startAppSelection(0, 0, list));
        tips("final select : " + e.getEmvL2().finalSelect(0, list));

        StartAppData app = new StartAppData();
        tips("get proc option : " + e.getEmvL2().getProcOption(app, list));
        tips("reading app data : " + e.getEmvL2().readAppData());


    }

    private CardReader reader;
    private Handler handler = new Handler();

    @Override
    public void onSearchResult(int code, MagCardResult result) {
        if (code == 0 && !TextUtils.isEmpty(result.getPAN())) {
            runOnUiThread(() -> {
                cardNo = result.getPAN();
                String maskedPan = "000000******0000";
                if (cardNo.length() > 10) {
                    maskedPan = cardNo.substring(0, 6) + "******" + cardNo.substring(cardNo.length() - 4);
                }
                lblCardNo.setText(maskedPan);
                btnConfirm.setEnabled(true);
            });
        }
    }

    @Override
    public void onSearchResult(int code, int type) {
        final String tip = "ret : " + code + " result : " + type;
        runOnUiThread(() -> {
            Toast.makeText(BankCardsActivity.this, tip, Toast.LENGTH_SHORT).show();
        });
        if ((type == CardReader.ACARD || type == CardReader.BCARD) && code == 0) {

        }
    }

    @Override
    public void onSearchResult(int code) {
        tips("search result : " + code);
        if (code != 0) {
            return;
        }
        IcCard ic = new IcCard();
        if (ic.Icc_Detect((byte) 0, (byte) 0) != IcCard.ICC_OK) {
            tips("icc detect not ok");
            return;
        }
        if (ic.Icc_Init((byte) 0, (byte) 0, new byte[200]) == IcCard.ICC_OK) {
            tips("icc power up okay");
            onDisposing(ic);        // Power Up
        }
        ic.Icc_Close((byte) 0);     // Power Down
    }

    private void onDisposing(IcCard ic) {
        EmvProc proc = EmvProc.getInstance();
        onAddingAids(proc);

        ICCardResult result = proc.getICCardResult();
        tips(result.toString());
    }
}