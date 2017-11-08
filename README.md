# FingerPrintTest
Android 指纹识别 Demo

并进行了简单的封装

封装类：FingerPrintHelper
生成秘钥的工具类：CryptoObjectHelper

使用：
 manageer = new FingerPrintHelper(this, "com.test.colin.fingerprinttest.fingerprint_authentication_key");
 
构造方法有两个参数：第一个是 Context，第二个是生成秘钥的Key的名字。建议使用包名+fingerprint_authentication_key
首先先使用checkSuopprtFingerPrint方法检查设备是否支持指纹识别
包括：手机是否有指纹识别设备，手机是否设置了屏幕密码，手机是否有录入好的指纹信息，SDK是否大于23
开启指纹识别使用： manageer.startFingerPrint(callBack);
其中参数callback是系统的回调类
说明一下每个回调方法的作用

 //发生不可恢复的错误，比如传感器异常
public void onAuthenticationError(int errMsgId, CharSequence errString);

//认证失败，表示本次采集的指纹信息和之前注册的指纹信息不一致
//其实这个时候，传感器还是在工作的。直到你录入正确指纹。好像有五次机会.
 public void onAuthenticationFailed();
 
 //发生可以恢复的错误。比如手指离开的太快，还没采集结束
 //其实这个时候，传感器还是在工作的。
 public void onAuthenticationHelp(int helpMsgId, CharSequence helpString);
 
 其中 helpMsgId我翻译了一下
 
//指纹图像太嘈杂由于在传感器上可疑或检测到的污垢
FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY:

//皮肤太干
FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT:

//只检测到一个局部指纹图像                 
FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL:

//指纹图像是不完整的，由于快速运动。
FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST:

//指纹图像是不可读的
FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW:
                   


 //认证成功
 public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result);

 停止指纹识别使用： manageer.stopFingerPrint();
