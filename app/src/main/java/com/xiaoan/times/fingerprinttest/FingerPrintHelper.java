package com.xiaoan.times.fingerprinttest;

import android.app.KeyguardManager;
import android.content.Context;
import android.graphics.Color;
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
        if (!managerCompat.isHardwareDetected()) {
            return TYPE_NO_HARDWAREDETECTED;
        }
        if (!keyguardManager.isDeviceSecure()) {
            return TYPE_NO_DEVICESECURE;
        }
        if (!managerCompat.hasEnrolledFingerprints()) {
            return TYPE_NO_ENROLLEDFINGERPRINTS;
        }
        if (Build.VERSION.SDK_INT < 23) {
            return TYPE_NO_SDKLEVEL;
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

    class MyFingerCallBack extends FingerprintManagerCompat.AuthenticationCallback {
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            //发生不可恢复的错误，比如传感器异常

        }

        @Override
        public void onAuthenticationFailed() {
            //认证失败，表示本次采集的指纹信息和之前注册的指纹信息不一致

        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            //发生可以恢复的错误。比如手指离开的太快，还没采集结束
//            switch (helpMsgId) {
//                case FingerprintManager.FINGERPRINT_ACQUIRED_GOOD:
//                    break;
//                case FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY:
//                    setResultInfo("指纹图像太嘈杂由于在传感器上可疑或检测到的污垢", Color.YELLOW);
//                    break;
//                case FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT:
//                    setResultInfo("皮肤太干", Color.YELLOW);
//                    break;
//                case FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL:
//                    setResultInfo("只检测到一个局部指纹图像", Color.YELLOW);
//                    break;
//                case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST:
//                    setResultInfo("指纹图像是不完整的，由于快速运动。", Color.YELLOW);
//                    break;
//                case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW:
//                    setResultInfo("指纹图像是不可读的", Color.YELLOW);
//                    break;
//            }
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            //认证成功
//            try {
//                result.getCryptoObject().getCipher().doFinal();
//                //只有没有发生异常的情况才表示认证完全成功
//                tv.setText("指纹认证成功！！");
//                tv.setTextColor(Color.GREEN);
//                btn_sign.setEnabled(true);
//                btn_cancel.setEnabled(false);
//            } catch (IllegalBlockSizeException e) {
//                e.printStackTrace();
//                tv.setText("指纹认证失败！！发生拦截！！危险！！");
//                tv.setTextColor(Color.RED);
//            } catch (BadPaddingException e) {
//                e.printStackTrace();
//                tv.setText("指纹认证失败！！发生拦截！！危险！！");
//                tv.setTextColor(Color.RED);
//            }

        }
    }
}

