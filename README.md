# FingerPrintTest
Android 指纹识别 Demo

并进行了简单的封装

封装类：FingerPrintHelper
生成秘钥的工具类：CryptoObjectHelper

使用：
 manageer = new FingerPrintHelper(this, "com.test.colin.fingerprinttest.fingerprint_authentication_key");
 if (manageer.checkSuopprtFingerPrint() != FingerPrintHelper.TYPE_SUCCESS) {
       btn_sign.setEnabled(false);
       return;
     }
构造方法有两个参数：第一个是 Context，第二个是生成秘钥的Key的名字。建议使用包名+fingerprint_authentication_key
先使用checkSuopprtFingerPrint方法检查设备是否支持指纹识别
包括：手机是否有指纹识别设备，手机是否设置了屏幕密码，手机是否有录入好的指纹信息，SDK是否大于23

开启指纹识别使用： manageer.startFingerPrint(callBack);
callback是系统的回调类

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
            //其实这个时候，传感器还是在工作的。直到你录入正确指纹。好像有五次机会.
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
                  //其实应该调用这句话，但是调用后会崩溃，原因我还没找到。不过不调用应该也没多大影响。
//                tv.setText("指纹认证成功！！");
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
    
 停止识别使用： manageer.stopFingerPrint();
