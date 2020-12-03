package com.rancho.msgshare.textmsg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rancho.msgshare.BaseActivity;
import com.rancho.msgshare.R;
import com.rancho.msgshare.common.CommonConstant;
import com.rancho.msgshare.entity.CommonResult;
import com.rancho.msgshare.entity.TextMsg;
import com.rancho.msgshare.entity.TextMsgResult;
import com.rancho.msgshare.entity.User;
import com.rancho.msgshare.entity.Version;
import com.rancho.msgshare.ui.adapter.TextMsgAdapter;
import com.rancho.msgshare.utils.HttpUtil;
import com.rancho.msgshare.utils.JsonUtil;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TextMsgMainActivity extends BaseActivity implements View.OnClickListener {

    private List<TextMsg> data = new ArrayList<>();

    private TextMsgAdapter textMsgAdapter;
    private RecyclerView recyclerView;

    private User currentUser;
    private Version localVersion;

    void initData() {
        data = LitePal.where("userId = ?", String.valueOf(getCurrentId())).find(TextMsg.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_msg_main);
        FloatingActionButton addTextMsgBtn = findViewById(R.id.new_text_msg_btn);
        addTextMsgBtn.setOnClickListener(this);

        recyclerView = findViewById(R.id.text_msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        initData();
        textMsgAdapter = new TextMsgAdapter(data);
        recyclerView.setAdapter(textMsgAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("start", "进入onStart");
        updateUserStatus(getCurrentId());
    }

    /**
     * 保持用户登录状态
     * @param id
     */
    private void updateUserStatus(int id) {
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("网络错误");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final CommonResult result = JsonUtil.getObject(response.body().string(), CommonResult.class);
                if (result.getCode() != 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast(result.getMsg());
                        }
                    });
                } else {
                    refreshData();
                }
            }
        };
        HttpUtil.get(CommonConstant.HOST_URL + CommonConstant.USER_STATUS_RES_URL + id, callback);
    }

    /**
     * 获取最新数据
     */
    private void refreshData() {
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("网络错误");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final CommonResult result = JsonUtil.getObject(response.body().string(), CommonResult.class);

                if (result.getCode() == 0) {
                    final String newestVersion = result.getData().toString();
                    String local = getLocalVersion();

                    if (local == null || !local.equals(newestVersion)) {
                        Callback processData = new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showToast("网络错误");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final TextMsgResult dataResult = JsonUtil.getObject(response.body().string(), TextMsgResult.class);

                                List<TextMsg> textMsgList = dataResult.getData();
                                data.clear();
                                data.addAll(textMsgList);

                                for (TextMsg textMsg : textMsgList) {
                                    String idStr = String.valueOf(textMsg.getMsgId());
                                    TextMsg oldData = LitePal.where("msgId = ?", idStr).findFirst(TextMsg.class);
                                    if (oldData == null) {
                                        textMsg.save();
                                    } else {
                                        textMsg.updateAll("msgId = ?", idStr);
                                    }
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textMsgAdapter.notifyDataSetChanged();
                                    }
                                });
                                updateLocalVersion(newestVersion);
                            }
                        };

                        HttpUtil.get(CommonConstant.HOST_URL + CommonConstant.TEXT_MSG_RES_URL, processData);
                    } else {
                        Log.d("version check", "当前为最新版本，不拉取数据");
                    }
                } else {
                    Log.d("get version result.getCode()!=0", result.getMsg());
                }
            }
        };

        HttpUtil.get(CommonConstant.HOST_URL + CommonConstant.TEXT_VERSION_RES_URL, callback);
    }

    private int getCurrentId() {
        if (currentUser == null) {
            currentUser = LitePal.findFirst(User.class);
        }
        return currentUser.getUserId();
    }

    private String getLocalVersion() {
        if (localVersion == null) {
            localVersion = LitePal.where("userId = ?", String.valueOf(getCurrentId())).findFirst(Version.class);
        }
        return localVersion == null ? null : localVersion.getVersion();
    }

    private void updateLocalVersion(String newestVersion) {
        if (localVersion == null) {
            localVersion = new Version();
            localVersion.setUserId(currentUser.getUserId());
        }
        localVersion.setVersion(newestVersion);
        localVersion.save();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, TextMsgDetailActivity.class);
        intent.putExtra("msgId", CommonConstant.NEW_MSG_ID);
        intent.putExtra("content", "");
        startActivity(intent);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}