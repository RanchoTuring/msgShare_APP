package com.rancho.msgshare.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rancho.msgshare.R;
import com.rancho.msgshare.common.CommonConstant;
import com.rancho.msgshare.entity.TextMsg;
import com.rancho.msgshare.textmsg.TextMsgDetailActivity;

import java.util.List;

public class TextMsgAdapter extends RecyclerView.Adapter<TextMsgAdapter.ViewHolder> {

    private Context mContext;
    private List<TextMsg> textMsgList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView msgContent;
        TextView msgUTime;
        TextView msgSource;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            msgContent = itemView.findViewById(R.id.msg_content);
            msgUTime = itemView.findViewById(R.id.msg_time);
            msgSource = itemView.findViewById(R.id.msg_source);
        }
    }

    public TextMsgAdapter(List<TextMsg> textMsgs) {
        textMsgList = textMsgs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        if (null == mContext) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.text_msg_item, parent, false);

        final ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                TextMsg textMsg = textMsgList.get(position);
                Intent intent = new Intent(view.getContext(), TextMsgDetailActivity.class);
                intent.putExtra("msgId", textMsg.getMsgId());
                intent.putExtra("content", textMsg.getContent());
                intent.putExtra("utime", textMsg.getUtime());
                view.getContext().startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextMsg textMsg = textMsgList.get(position);
        holder.msgContent.setText(getProfile(textMsg.getContent()));
        holder.msgUTime.setText(textMsg.getUtime());
        holder.msgSource.setText(textMsg.getSource());
    }

    private String getProfile(String content) {
        if (content.length() > CommonConstant.PROFILE_MAX_LENGTH) {
            return content.substring(0, CommonConstant.PROFILE_MAX_LENGTH) + "...";
        }
        return content;
    }

    @Override
    public int getItemCount() {
        return textMsgList.size();
    }
}
