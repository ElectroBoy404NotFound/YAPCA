package me.electronicsboy.yapca.data;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import me.electronicsboy.yapca.R;

public class MessageAdapter extends ArrayAdapter<MessageItem> {
    public MessageAdapter(Context context, int resource, List<MessageItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_chat, parent, false);

        TextView messageTextView =  convertView.findViewById(R.id.messageTextView);
        TextView authorTextView =  convertView.findViewById(R.id.nameTextView);

        MessageItem message = getItem(position);

        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(message.getText());
        authorTextView.setText(message.getName());

        return convertView;
    }
}
