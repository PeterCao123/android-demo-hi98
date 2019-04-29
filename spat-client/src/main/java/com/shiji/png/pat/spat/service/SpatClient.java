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
import com.shiji.png.pat.spat.util.Base64;
import com.shiji.png.pat.spat.util.StrongSslSocketFactory;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author bruce.wu
 * @date 2018/9/3
 */
public class SpatClient {

    interface Service {

        @POST("merchants/{merId}/rvcs/{rvcId}/userValidation")
        Observable<UserValidationDTO> userValidation(@HeaderMap Map<String, String> headers,
                                                     @Path("merId") String merId,
                                                     @Path("rvcId") String rvcId,
                                                     @Body UserValidationModel body);

        @POST("merchants/{merId}/rvcs/{rvcId}/tables/{tableNo}/checks/{checkNo}/payments")
        Observable<PaymentsDTO> payments(@HeaderMap Map<String, String> headers,
                                         @Path("merId") String merId,
                                         @Path("rvcId") String rvcId,
                                         @Path("tableNo") String tableNo,
                                         @Path("checkNo") String checkNo,
                                         @Body PaymentModel body,
                                         @Header("x-spat-reUpload") boolean retry);

        @GET("merchants/{merId}/rvcs/{rvcId}/tables/{tableNo}/checks")
        Observable<ChecksDTO> getChecksByTableNo(@HeaderMap Map<String, String> headers,
                                                 @Path("merId") String merId,
                                                 @Path("rvcId") String rvcId,
                                                 @Path("tableNo") String tableNo);

        @GET("merchants/{merId}/rvcs/{rvcId}/checks/{checkNo}/checks")
        Observable<ChecksDTO> getChecksByCheckNo(@HeaderMap Map<String, String> headers,
                                                 @Path("merId") String merId,
                                                 @Path("rvcId") String rvcId,
                                                 @Path("checkNo") String checkNo);

        @GET("merchants/{merId}/rvcs/{rvcId}/tables/{tableNo}/checks/{checkNo}/getPrintCheck")
        Observable<GetPrintCheckDTO> getPrintCheck(@HeaderMap Map<String, String> headers,
                                                   @Path("merId") String merId,
                                                   @Path("rvcId") String rvcId,
                                                   @Path("tableNo") String tableNo,
                                                   @Path("checkNo") String checkNo,
                                                   @Header("x-spat-receiptType") int receiptType);

        @GET("merchants/{merId}/rvcs/{rvcId}/getLogo")
        Observable<GetLogoDTO> getLogo(@HeaderMap Map<String, String> headers,
                                       @Path("merId") String merId,
                                       @Path("rvcId") String rvcId);

        @GET("merchants/{merId}/rvcs/{rvcId}/getConfiguration")
        Observable<GetConfigurationDTO> getConfiguration(@HeaderMap Map<String, String> headers,
                                                         @Path("merId") String merId,
                                                         @Path("rvcId") String rvcId);

        @POST("merchants/{merId}/rvcs/{rvcId}/apiValidation")
        Observable<ApiValidationDTO> apiValidation(@HeaderMap Map<String, String> headers,
                                                   @Path("merId") String merId,
                                                   @Path("rvcId") String rvcId,
                                                   @Body Object body);

    }

    private static boolean LOG_HTTP = false;

    public static void logHttp() {
        LOG_HTTP = true;
    }

    public static Builder builder() {
        return new Builder();
    }

    private final ApiConfig config;
    private final Service service;

    private SpatClient(ApiConfig config, Service service) {
        this.config = config;
        this.service = service;
    }

    public Observable<UserValidationDTO> userValidation(UserValidationModel model) {
        Map<String, String> headers = builderHeaders(model.getOperator(), model.getPassword());
        return service.userValidation(headers, config.getMerId(), config.getRvcId(), model);
    }

    public Observable<PaymentsDTO> payments(String tableNo, String checkNo, PaymentModel model, boolean retry) {
        Map<String, String> headers = builderHeaders(tableNo, checkNo,
                model.getRespCode(), model.getRefNo(), model.getAcqTxnDatetime());
        return service.payments(headers, config.getMerId(), config.getRvcId(), tableNo, checkNo, model, retry);
    }

    public Observable<ChecksDTO> getChecksByTableNo(String tableNo) {
        Map<String, String> headers = builderHeaders(tableNo);
        return service.getChecksByTableNo(headers, config.getMerId(), config.getRvcId(), tableNo);
    }

    public Observable<ChecksDTO> getChecksByCheckNo(String checkNo) {
        Map<String, String> headers = builderHeaders(checkNo);
        return service.getChecksByCheckNo(headers, config.getMerId(), config.getRvcId(), checkNo);
    }

    public Observable<GetPrintCheckDTO> getPrintCheck(String tableNo, String checkNo) {
        return getPrintCheck(tableNo, checkNo, ReceiptFlag.CheckDetails);
    }

    public Observable<GetPrintCheckDTO> getPrintCheck(String tableNo, String checkNo, int receiptType) {
        Map<String, String> headers = builderHeaders(checkNo);
        return service.getPrintCheck(headers, config.getMerId(), config.getRvcId(), tableNo, checkNo, receiptType);
    }

    public Observable<GetLogoDTO> getLogo() {
        Map<String, String> headers = builderHeaders();
        return service.getLogo(headers, config.getMerId(), config.getRvcId());
    }

    public Observable<GetConfigurationDTO> getConfiguration() {
        Map<String, String> headers = builderHeaders();
        return service.getConfiguration(headers, config.getMerId(), config.getRvcId());
    }

    public Observable<ApiValidationDTO> apiValidation() {
        Map<String, String> headers = builderHeaders(config.getPosMerId(), config.getPosRvcId());
        return service.apiValidation(headers, config.getMerId(), config.getRvcId(), new Object());
    }

    Map<String, String> builderHeaders(String...fields) {
        String timestamp = timestamp();
        String reqTrace = reqTrace();
        StringBuilder sb = new StringBuilder(String.format("%s|%s|%s|%s|%s|%s",
                config.getMerId(), config.getRvcId(), config.getUser(), config.getPassword(), reqTrace, timestamp));
        for (String field : fields) {
            sb.append("|").append(field);
        }
        String signedFields = sb.toString();
        Map<String, String> headers = new HashMap<>();
        headers.put("x-spat-api", config.getUser());
        headers.put("x-spat-reqTrace", reqTrace);
        headers.put("bearer", config.getSignature());
        headers.put("x-spat-timestamp", timestamp);
        headers.put("x-spat-posMerId", config.getPosMerId());
        headers.put("x-spat-posRvcId", config.getPosRvcId());
        headers.put("x-spat-sign", sign(signedFields));
        return headers;
    }

    private static String timestamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
    }

    private static String reqTrace() {
        int traceNo = new Random().nextInt(89999999) + 10000000;
        return Integer.toString(traceNo);
    }

    private String sign(String fields) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(fields.getBytes("utf-8"));
            byte[] digest = md.digest();
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception e) {
            throw new RuntimeException("sign error", e);
        }
    }

    public static class Builder {

        private ApiConfig config;
        private Service service;

        private Builder() {
        }

        public Builder config(ApiConfig config) {
            this.config = config;
            return this;
        }

        public Builder service(Service service) {
            this.service = service;
            return this;
        }

        public SpatClient build() {
            if (service == null) {
                service = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .baseUrl(fixBaseUrl(config.getGateway()))
                        .client(client())
                        .build()
                        .create(Service.class);
            }
            return new SpatClient(config, service);
        }

        private String fixBaseUrl(String url) {
            return  url.endsWith("/") ? url : (url + "/");
        }

        private OkHttpClient client() {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .sslSocketFactory(StrongSslSocketFactory.create())
                    .connectTimeout(config.getConnectTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS);
            if (LOG_HTTP) {
                builder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
            }
            return builder.build();
        }

    }

}
