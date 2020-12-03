package com.rancho.msgshare.user;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rancho.msgshare.R;
import com.rancho.msgshare.BaseActivity;
import com.rancho.msgshare.common.CommonConstant;
import com.rancho.msgshare.entity.CommonResult;
import com.rancho.msgshare.entity.HttpParam;
import com.rancho.msgshare.entity.User;
import com.rancho.msgshare.textmsg.TextMsgMainActivity;
import com.rancho.msgshare.utils.HttpUtil;
import com.rancho.msgshare.utils.JsonUtil;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;
import org.litepal.crud.LitePalSupport;

import java.io.IOException;
import java.util.List;

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

        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);

        //用户已登录时，直接跳转
        if (verifyUserStatus()) {
            Intent intent = new Intent(UserLoginActivity.this, TextMsgMainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 校验用户登录状态
     *
     * @return 是否已登录
     */
    private boolean verifyUserStatus() {
        List<User> userList = LitePal.findAll(User.class);
        if (userList == null || userList.isEmpty()) {
            return false;
        }
        return true;
    }


    private void logIn(final String username, String password) {
        HttpParam paramUsername = new HttpParam(CommonConstant.PARAM_USERNAME, username);
        HttpParam paramPassword = new HttpParam(CommonConstant.PARAM_PASSWORD, password);

        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("login", e.getMessage());
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("网络错误");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final CommonResult result = JsonUtil.getObject(response.body().string(), CommonResult.class);

                if (result.getCode() == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            User user = new User();
                            //TODO object类型反序列化后，被判断为double。后续需要优化后端返回值类型
                            user.setUserId(((Double) result.getData()).intValue());
                            user.setName(username);
                            user.save();
                            showToast("登录成功");
                            Intent intent = new Intent(UserLoginActivity.this, TextMsgMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast(result.getMsg());
                        }
                    });
                }
            }
        };
        HttpUtil.get(CommonConstant.HOST_URL + CommonConstant.USER_RES_URL, callback, paramUsername, paramPassword);
    }

    private void signUp(String username, String password) {


    }

    @Override
    public void onClick(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        switch (view.getId()) {
            case R.id.log_in: {
                //   CommonResult result= JsonUtil.getObject("{\"id\": 123,\n" +
                //         "\t\"msg\": \"计算机17-2\" }",CommonResult.class);
                logIn(username, password);
            }
            case R.id.sign_up: {
                signUp(username, password);
                break;
            }
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}