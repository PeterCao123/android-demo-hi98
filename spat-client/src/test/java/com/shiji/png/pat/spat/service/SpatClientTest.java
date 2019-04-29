package com.shiji.png.pat.spat.service;

import com.shiji.png.pat.spat.dto.ApiValidationDTO;
import com.shiji.png.pat.spat.dto.ChecksDTO;
import com.shiji.png.pat.spat.dto.GetConfigurationDTO;
import com.shiji.png.pat.spat.dto.GetLogoDTO;
import com.shiji.png.pat.spat.dto.GetPrintCheckDTO;
import com.shiji.png.pat.spat.dto.PaymentsDTO;
import com.shiji.png.pat.spat.dto.UserValidationDTO;
import com.shiji.png.pat.spat.model.PaymentModel;
import com.shiji.png.pat.spat.model.ReceiptFlag;
import com.shiji.png.pat.spat.model.UserValidationModel;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;

import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;

/**
 * @author bruce.wu
 * @date 2018/9/3
 */
public class SpatClientTest {

    /*private final ApiConfig config = new ApiConfig("000000000010189",
            "000000000000065",
            "1",
            "2042",
            "239",
            "https://png-dev-api-pat-cn01.shijicloud.com:443/sps-pat/api/V1/",
            "MacauTestUser",
            "05981DEA7608188B6D2BC1A2F6E4E273844E1738A6FBD62C94B8136682E38C60",
            "F9CBLP7LG6DQCSKY",
            5000, 50000);*/

    private final ApiConfig config = new ApiConfig("000000000010191",
            "000000000000067",
            "1",
            "0001",
            "0001",
            "https://png-dev-api-pat-cn01.shijicloud.com:443/sps-pat/api/V1/",
            "BruceTest",
            "1BCC1312FF6628FF6E39A8367BA72CD63CE50EA8EDE16C5DBC5F5E11B256F31D",
            "JHEYDIXG2TG8JS7I",
            5000, 50000);

    @Before
    public void setUp() {
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        SpatClient.logHttp();
    }

    @Test
    public void userValidation() {
        SpatClient client = SpatClient.builder()
                .config(config)
                .build();
        UserValidationModel model = UserValidationModel.builder()
                .operator("1")
                .password("1")
                .build();
        UserValidationDTO dto = client.userValidation(model)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.trampoline())
                .blockingSingle();
        assertEquals("00", dto.getRespCode());
    }

    @Ignore
    @Test
    public void payments() {
        SpatClient client = SpatClient.builder()
                .config(config)
                .build();
        PaymentModel model = PaymentModel.builder()
                .merId(config.getMerId())
                .rvcId(config.getRvcId())
                .terId(config.getTerId())
                .merRef(UUID.randomUUID().toString())
                .maskedPan("000000")
                .source("Pay@Table")
                .refNo(UUID.randomUUID().toString())
                .build();
        PaymentsDTO dto = client.payments("2", "0048", model, false)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.trampoline())
                .blockingSingle();
        assertEquals("00", dto.getRespCode());
    }

    @Ignore
    @Test
    public void getChecksByTableNo() {
        SpatClient client = SpatClient.builder()
                .config(config)
                .build();
        ChecksDTO dto = client.getChecksByTableNo("22")
                .subscribeOn(Schedulers.io())
                .blockingSingle();
        assertEquals("00", dto.getRespCode());
    }

    @Ignore
    @Test
    public void getChecksByCheckNo() {
        SpatClient client = SpatClient.builder()
                .config(config)
                .build();
        ChecksDTO dto = client.getChecksByCheckNo("0048")
                .subscribeOn(Schedulers.io())
                .blockingSingle();
        assertEquals("00", dto.getRespCode());
    }

    @Ignore
    @Test
    public void getPrintCheck() {
        SpatClient client = SpatClient.builder()
                .config(config)
                .build();
        GetPrintCheckDTO dto = client.getPrintCheck("2", "0048")
                .subscribeOn(Schedulers.io())
                .blockingSingle();
        assertEquals("00", dto.getRespCode());
    }

    @Ignore
    @Test
    public void getPrintCheck_formatted() {
        SpatClient client = SpatClient.builder()
                .config(config)
                .build();
        GetPrintCheckDTO dto = client.getPrintCheck("2", "0048", ReceiptFlag.Formatted)
                .subscribeOn(Schedulers.io())
                .blockingSingle();
//        System.out.println(dto);
        assertEquals("00", dto.getRespCode());
    }

    @Ignore
    @Test
    public void getPrintCheck_printLines() {
        SpatClient client = SpatClient.builder()
                .config(config)
                .build();
        GetPrintCheckDTO dto = client.getPrintCheck("2", "0048", ReceiptFlag.PrintLines)
                .subscribeOn(Schedulers.io())
                .blockingSingle();
//        System.out.println(dto);
        assertEquals("00", dto.getRespCode());
    }

    @Test
    public void getLogo() {
        SpatClient client = SpatClient.builder()
                .config(config)
                .build();
        GetLogoDTO dto = client.getLogo()
                .subscribeOn(Schedulers.io())
                .blockingSingle();
        assertEquals("00", dto.getRespCode());
    }

    @Test
    public void getConfiguration() {
        SpatClient client = SpatClient.builder()
                .config(config)
                .build();
        GetConfigurationDTO dto = client.getConfiguration()
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> System.out.println("getConfiguration: doOnComplete1"))
                .doOnComplete(() -> System.out.println("getConfiguration: doOnComplete2"))
                .doOnComplete(() -> System.out.println("getConfiguration: doOnComplete3"))
                .blockingSingle();
        assertEquals("00", dto.getRespCode());
    }

    @Test
    public void apiValidation() {
        SpatClient client = SpatClient.builder()
                .config(config)
                .build();
        ApiValidationDTO dto = client.apiValidation()
                .subscribeOn(Schedulers.io())
                .blockingSingle();
        assertEquals("00", dto.getRespCode());
    }

    @Test
    public void buildPaymentsHeaders() {
        String tableNo = "111";
        String checkNo = "1018150441";
        String refNo = "00002251";
        String acqTxnDateTime = "2018-10-18 11:51:06";
        Map<String, String> headers =  SpatClient.builder()
                .config(config)
                .build()
                .builderHeaders(tableNo, checkNo, "00", refNo, acqTxnDateTime);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
//            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }

}