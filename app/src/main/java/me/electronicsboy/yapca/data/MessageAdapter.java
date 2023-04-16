package me.electronicsboy.yapca.data;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.util.TempStorage;
import me.electronicsboy.yapca.util.ReportSystem;

public class MessageAdapter extends ArrayAdapter<MessageItem> {
    public MessageAdapter(Context context, int resource, List<MessageItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_chat, parent, false);

        TextView messageTextView =  convertView.findViewById(R.id.messageTextView);
        TextView authorTextView =  convertView.findViewById(R.id.nameTextView);
        Button reportButton = convertView.findViewById(R.id.button);
        Button deleteButton = convertView.findViewById(R.id.delete);

        MessageItem message = getItem(position);

        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(message.getText());
        authorTextView.setText(message.getName());

        reportButton.setEnabled(!(TempStorage.get("USERNAME").equals(message.getName())));
        reportButton.setOnClickListener((v) ->
            ReportSystem.report(message.getID(), message.asHashMap())
        );

        deleteButton.setEnabled(TempStorage.get("USERNAME").equals(message.getName()));
        deleteButton.setOnClickListener((v) -> FirebaseDatabase.getInstance().getReference("Chat/" + TempStorage.get("OPEN_CHAT") + "/" + message.getID()).removeValue());

        return convertView;
    }
}
