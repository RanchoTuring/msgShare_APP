package com.rancho.msgshare.textmsg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rancho.msgshare.R;
import com.rancho.msgshare.common.CommonConstant;
import com.rancho.msgshare.entity.CommonResult;
import com.rancho.msgshare.entity.HttpParam;
import com.rancho.msgshare.entity.TextMsg;
import com.rancho.msgshare.utils.HttpUtil;
import com.rancho.msgshare.utils.JsonUtil;

import org.litepal.LitePal;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TextMsgDetailActivity extends AppCompatActivity implements View.OnClickListener {


    private TextMsg textMsg;
    private EditText editText;

    abstract class BaseCallback implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            showToastOnUiThread("网络错误");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_msg_detail);
        editText = findViewById(R.id.msg_edit_text);
        Button saveBtn = findViewById(R.id.save_text_msg);
        Button delBtn = findViewById(R.id.delete_text_msg);
        saveBtn.setOnClickListener(this);
        delBtn.setOnClickListener(this);
        Intent intent = getIntent();

        // mockData(intent);
        getTextMsgFromIntent(intent);

        editText.setText(textMsg.getContent());
    }


    private void mockData(Intent intent) {
        textMsg = new TextMsg();
        textMsg.setUserId(123);
        textMsg.setMsgId(3111);
        textMsg.setContent("啊实打实大苏打阿松大啊大苏打阿松大阿松大阿松大阿松大");
    }

    /**
     * 从Intent中获取相关的信息
     *
     * @param intent
     */
    private void getTextMsgFromIntent(Intent intent) {
        textMsg = new TextMsg();
        textMsg.setMsgId(intent.getIntExtra("msgId", 0));
        textMsg.setContent(intent.getStringExtra("content"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_text_msg: {
                String newText = editText.getText().toString();
                //新建的笔记
                if (textMsg.getMsgId() == CommonConstant.NEW_MSG_ID) {
                    addTextMsg(newText);
                } else {
                    updateTextMsg(textMsg.getMsgId(), newText);
                }

                break;
            }
            case R.id.delete_text_msg: {
                deleteTextMsg(textMsg.getMsgId());
                break;
            }
        }
    }

    private void showToastOnUiThread(final String msg) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void addTextMsg(String text) {
        HttpParam paramMsgContent = new HttpParam(CommonConstant.PARAM_MSG_CONTENT, text);
        HttpParam paramDevice = new HttpParam(CommonConstant.PARAM_DEVICE, CommonConstant.DEVICE_TYPE_APP);
        Callback callback = new BaseCallback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final CommonResult result = JsonUtil.getObject(response.body().string(), CommonResult.class);
                if (result.getCode() == 0) {
                    showToastOnUiThread("保存成功");
                } else {
                    showToastOnUiThread(result.getMsg());
                }
            }
        };
        HttpUtil.post(CommonConstant.HOST_URL + CommonConstant.TEXT_MSG_RES_URL,
                callback, paramMsgContent, paramDevice);
    }

    private void updateTextMsg(int msgId, String text) {
        HttpParam paramMsgId = new HttpParam(CommonConstant.PARAM_MSG_ID, String.valueOf(msgId));
        HttpParam paramMsgContent = new HttpParam(CommonConstant.PARAM_MSG_CONTENT, text);
        HttpParam paramDevice = new HttpParam(CommonConstant.PARAM_DEVICE, CommonConstant.DEVICE_TYPE_APP);

        Callback callback = new BaseCallback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final CommonResult result = JsonUtil.getObject(response.body().string(), CommonResult.class);
                if (result.getCode() == 0) {
                    showToastOnUiThread("保存成功");
                } else {
                    showToastOnUiThread(result.getMsg());
                }
            }
        };
        HttpUtil.put(CommonConstant.HOST_URL + CommonConstant.TEXT_MSG_RES_URL,
                callback, paramMsgId, paramMsgContent, paramDevice);
    }

    private void deleteTextMsg(int msgId) {
        HttpParam param = new HttpParam(CommonConstant.PARAM_MSG_ID, String.valueOf(msgId));
        Callback callback = new BaseCallback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final CommonResult result = JsonUtil.getObject(response.body().string(), CommonResult.class);
                if (result.getCode() == 0) {
                    LitePal.deleteAll(TextMsg.class, "msgId = ?", String.valueOf(textMsg.getMsgId()));
                    showToastOnUiThread("删除成功");
                } else {
                    showToastOnUiThread(result.getMsg());
                }
            }
        };
        HttpUtil.delete(CommonConstant.HOST_URL + CommonConstant.TEXT_MSG_RES_URL, callback, param);
    }


}