package com.rancho.msgshare.user;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.rancho.msgshare.R;
import com.rancho.msgshare.BaseActivity;
import com.rancho.msgshare.entity.CommonResult;
import com.rancho.msgshare.utils.HttpUtil;
import com.rancho.msgshare.utils.JsonUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserLoginActivity extends BaseActivity implements View.OnClickListener {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginBtn;
    Button signUpBtn;
    ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginBtn = findViewById(R.id.log_in);
        signUpBtn = findViewById(R.id.sign_up);
        loadingProgressBar = findViewById(R.id.loading);

        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);

        //用户已登录时，直接跳转
        if(verifyUserStatus()){
           // Intent view = new Intent(UserLoginActivity.this,TextMsgMainActivity.class);
         //   startActivity(view);
        }
    }

    /**
     * 校验用户登录状态
     * @return 是否已登录
     */
    private boolean verifyUserStatus(){
        //TODO 检查本地是否有id存在
        return true;
    }


    private boolean logIn(String username,String password){
        //TODO 请求后端，进行登录，保存id

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.log_in: {

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                Log.d("--------", username + "  " + password);

                HttpUtil.get("http://www.baidu.com", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("====",response.body().string());
                    }
                });

             //   CommonResult result= JsonUtil.getObject("{\"id\": 123,\n" +
               //         "\t\"msg\": \"计算机测试班级\" }",CommonResult.class);




                if(logIn(username,password)){

                }else{
                   // Toast.makeText(getApplicationContext(), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.sign_up: {
                break;
            }
        }
    }
}