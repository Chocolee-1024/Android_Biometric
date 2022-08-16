package com.example.biometric;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private androidx.biometric.BiometricManager biometricManager;
    private BiometricPrompt biometricPrompt;
    private androidx.biometric.BiometricPrompt.PromptInfo.Builder promptInfo;
    private Button finger_bt;
    private Button pin_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        finger_bt=(Button) findViewById(R.id.button);
        pin_bt=(Button) findViewById(R.id.button2);
        //建立BiometricManager
        biometricManager = androidx.biometric.BiometricManager.from(this);
        //呼叫判斷是否能用生物辨識的方法
        checkBiometricSupport();
        //呼叫建立BiometricPrompt的方法
        createBiometricPrompt();

    }
    //叫出指紋辨識
    public void OnFingerClick(View view){
        buildFingerPrint();
        biometricPrompt.authenticate(promptInfo.build());
    }
    //叫出Pin碼辨識
    public void OnPinClick(View view){
        buildPinPrint();
        biometricPrompt.authenticate(promptInfo.build());
    }
    //建立指紋辨識的視窗
    private void buildFingerPrint() {
        promptInfo = new androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setAllowedAuthenticators(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .setTitle("驗證")
                .setSubtitle("請輸入指紋")
                .setNegativeButtonText("取消");
    }
    //建立Pin辨識的視窗
    private void buildPinPrint() {
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                //設定驗證(DEVICE_CREDENTIAL 為最基本PIN、圖形等、BIOMETRIC_WEAK、BIOMETRIC_STRONG(最高等級)(BIOMETRIC表生物辨識逼本也有指紋))
                .setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .setTitle("PIN")
                .setSubtitle("請輸入PIN");
    }
    //判斷API版本後，如果能使用就會跑下面判斷是否有生物辨識系統。
    private void checkBiometricSupport() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)){
                //此裝置可以使用生物辨識
                case androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS:
                    Log.e("TAG", "App can authenticate using biometrics.");
                    break;
                //此裝置無法使用生物辨識
                case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    Log.e("TAG", "No biometric features available on this device.");
                    break;
                //生物識別功能目前不可用
                case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    Log.e("TAG", "Biometric features are currently unavailable.");
                    break;
                //用戶尚未建立生物辨識帳號資料
                case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    Log.e("TAG", "The user hasn't associated any biometric credentials with their account.");
                    break;
                //使否可以使用生物辨識(已棄用)
                default:
                    throw new IllegalStateException("Unexpected value: "+biometricManager.canAuthenticate());
            }
        }
    }
    private void createBiometricPrompt() {
        //設置驗證CallBack
        biometricPrompt = new androidx.biometric.BiometricPrompt(this, ContextCompat.getMainExecutor(this), new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.e("GOGO", errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.e("GOGO", "成功");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.e("GOGO", "失敗");
            }
        });


    }
}