package com.shiji.png.droid.payment.message;

import android.os.Parcel;
import android.os.Parcelable;

import com.shiji.png.payment.util.Json;

/**
 * @author bruce.wu
 * @date 2018/9/6
 */
public class RequestPayload implements Parcelable {

    private static final String EMPTY_OBJECT = "{}";

    private String head;

    private String body;

    public RequestPayload(Object body) {
        init(EMPTY_OBJECT, Json.getEncoder().encode(body));
    }

    private RequestPayload(Parcel in) {
        init(in.readString(), in.readString());
    }

    private void init(String head, String body) {
        this.head = head;
        this.body = body;
    }

    public <T> T getHead(Class<T> type) {
        return Json.getDecoder().decode(head, type);
    }

    public <T> T getBody(Class<T> type) {
        return Json.getDecoder().decode(body, type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(head);
        dest.writeString(body);
    }

    public static final Creator<RequestPayload> CREATOR = new Creator<RequestPayload>() {
        @Override
        public RequestPayload createFromParcel(Parcel source) {
            return new RequestPayload(source);
        }

        @Override
        public RequestPayload[] newArray(int size) {
            return new RequestPayload[size];
        }
    };
}
