package com.shiji.png.pat.app.domain.service;

import android.text.TextUtils;

import com.shiji.png.pat.app.R;
import com.shiji.png.pat.app.domain.InvalidPreferenceException;
import com.shiji.png.pat.app.domain.model.PrefInfo;

/**
 * @author bruce.wu
 * @date 2018/9/4
 */
public class ValidateService {

    public static void validate(PrefInfo prefInfo) throws InvalidPreferenceException {
        if (TextUtils.isEmpty(prefInfo.getMerId())) {
            throw new InvalidPreferenceException(R.string.invalid_merchant_id);
        }
        if (TextUtils.isEmpty(prefInfo.getRvcId())) {
            throw new InvalidPreferenceException(R.string.invalid_revenue_center_id);
        }
        if (TextUtils.isEmpty(prefInfo.getTerId())) {
            throw new InvalidPreferenceException(R.string.invalid_terminal_id);
        }
        if (TextUtils.isEmpty(prefInfo.getPosMerId())) {
            throw new InvalidPreferenceException(R.string.invalid_pos_mer_id);
        }
        if (TextUtils.isEmpty(prefInfo.getPosRvcId())) {
            throw new InvalidPreferenceException(R.string.invalid_pos_rvc_id);
        }
        if (TextUtils.isEmpty(prefInfo.getApiGateway())) {
            throw new InvalidPreferenceException(R.string.invalid_api_gateway);
        }
        if (TextUtils.isEmpty(prefInfo.getApiUser())) {
            throw new InvalidPreferenceException(R.string.invalid_api_user);
        }
        if (TextUtils.isEmpty(prefInfo.getApiBearer())) {
            throw new InvalidPreferenceException(R.string.invalid_api_signature);
        }
        if (TextUtils.isEmpty(prefInfo.getApiPassword())) {
            throw new InvalidPreferenceException(R.string.invalid_api_password);
        }
    }

}
