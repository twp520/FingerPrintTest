package com.xiaoan.times.fingerprinttest;

import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

/**
 * Created by tianweiping on 2017/11/8.
 */

public class FingerPrintHelper {

    public static final int TYPE_NO_HARDWAREDETECTED = 0; //没有指纹设备
    public static final int TYPE_NO_ENROLLEDFINGERPRINTS = 1; //没有指纹信息
    public static final int TYPE_NO_DEVICESECURE = 2; //没有开启屏幕密码
    public static final int TYPE_NO_SDKLEVEL = 3; //要求Android SDK >=23
    public static final int TYPE_SUCCESS = 4; //没有开启屏幕密码

    private Context mContext;
    private final FingerprintManagerCompat managerCompat; //指纹管理器
    private CancellationSignal cancel; //取消指纹识别
    private KeyguardManager keyguardManager;
    private String keyName;

    public FingerPrintHelper(Context context, String keyName) {
        this.mContext = context;
        this.keyName = keyName;
        managerCompat = FingerprintManagerCompat.from(context);
        keyguardManager = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
    }

    /**
     * 检查是否可开启指纹
     *
     * @return 检查结果
     */
    public int checkSuopprtFingerPrint() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return TYPE_NO_SDKLEVEL;
        }
        if (!keyguardManager.isDeviceSecure()) {
            return TYPE_NO_DEVICESECURE;
        }

        if (!managerCompat.isHardwareDetected()) {
            return TYPE_NO_HARDWAREDETECTED;
        }

        if (!managerCompat.hasEnrolledFingerprints()) {
            return TYPE_NO_ENROLLEDFINGERPRINTS;
        }
        return TYPE_SUCCESS;
    }

    /**
     * 开启指纹识别
     */
    public void startFingerPrint(FingerprintManagerCompat.AuthenticationCallback callback) throws Exception {
        if (checkSuopprtFingerPrint() != TYPE_SUCCESS)
            throw new SecurityException("系统不支持指纹识别，请检查");
        CryptoObjectHelper cryptoObjectHelper = new CryptoObjectHelper(keyName);
        if (cancel == null)
            cancel = new CancellationSignal();
        managerCompat.authenticate(cryptoObjectHelper.buildCryptoObject(), 0, cancel, callback, null);
    }

    /**
     * 停止指纹识别
     */
    public void stopFingerPrint() throws Exception {
        if (cancel != null)
            cancel.cancel();
        cancel = null;
    }

    public String switchHelpCode(int helpMsgId) {
        String msg = "";
        switch (helpMsgId) {
            case FingerprintManager.FINGERPRINT_ACQUIRED_GOOD:
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY:
                msg = "指纹图像太嘈杂由于在传感器上可疑或检测到的污垢";
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT:
                msg = "皮肤太干";
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL:
                msg = "只检测到一个局部指纹图像";
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST:
                msg = "指纹图像是不完整的，由于快速运动。";
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW:
                msg = "指纹图像是不可读的";
                break;
        }
        return msg;
    }

}

