package com.xiaoan.times.fingerprinttest;

import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_sign, btn_cancel;
    private TextView tv;
    private MyFingerCallBack callBack;
    private FingerPrintHelper manageer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_sign = (Button) findViewById(R.id.button);
        btn_cancel = (Button) findViewById(R.id.button2);
        tv = (TextView) findViewById(R.id.textView);
        btn_sign.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_cancel.setEnabled(false);
        initFingerPrint();
    }

    private void initFingerPrint() {

        manageer = new FingerPrintHelper(this, "com.xiaoan.times.fingerprinttest.fingerprint_authentication_key");
        if (manageer.checkSuopprtFingerPrint() != FingerPrintHelper.TYPE_SUCCESS) {
            btn_sign.setEnabled(false);
            return;
        }
        callBack = new MyFingerCallBack();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            //开启指纹识别
            try {
                setResultInfo("正在识别...", Color.BLACK);
                manageer.startFingerPrint(callBack);
                btn_sign.setEnabled(false);
                btn_cancel.setEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v.getId() == R.id.button2) {
            try {
                //取消本次识别
                btn_cancel.setEnabled(false);
                btn_sign.setEnabled(true);
                setResultInfo("取消识别", Color.RED);
                manageer.stopFingerPrint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setResultInfo(String result, int color) {
        tv.setText(result);
        tv.setTextColor(color);
    }

    public void exitApp(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            manageer.stopFingerPrint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyFingerCallBack extends FingerprintManagerCompat.AuthenticationCallback {
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            //发生不可恢复的错误，比如传感器异常
            setResultInfo("发生了不可预知的错误！", Color.RED);
            Log.e("print", "errMsgId = " + errMsgId + ",  errString = " + errString);
            try {
                manageer.stopFingerPrint();
                btn_cancel.setEnabled(false);
                btn_sign.setEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAuthenticationFailed() {
            //认证失败，表示本次采集的指纹信息和之前注册的指纹信息不一致
            setResultInfo("指纹信息不一致,请重试", Color.RED);
//            btn_sign.setEnabled(true);
//            btn_cancel.setEnabled(false);
            //其实这个时候，传感器还是在工作的。知道你录入正确指纹。好像有五次机会.
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            //发生可以恢复的错误。比如手指离开的太快，还没采集结束
            switch (helpMsgId) {
                case FingerprintManager.FINGERPRINT_ACQUIRED_GOOD:
                    setResultInfo("", Color.YELLOW);
                    break;
                case FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY:
                    setResultInfo("指纹图像太嘈杂由于在传感器上可疑或检测到的污垢", Color.YELLOW);
                    break;
                case FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT:
                    setResultInfo("皮肤太干", Color.YELLOW);
                    break;
                case FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL:
                    setResultInfo("只检测到一个局部指纹图像", Color.YELLOW);
                    break;
                case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST:
                    setResultInfo("指纹图像是不完整的，由于快速运动。", Color.YELLOW);
                    break;
                case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW:
                    setResultInfo("指纹图像是不可读的", Color.YELLOW);
                    break;
            }
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
            tv.setText("指纹认证成功！！");
            tv.setTextColor(Color.GREEN);
            btn_sign.setEnabled(true);
            btn_cancel.setEnabled(false);
        }
    }
}
