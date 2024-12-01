package ma.ensa.www.assistdoc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.www.assistdoc.R;
import ma.ensa.www.assistdoc.Utils;
import ma.ensa.www.assistdoc.entities.Messages;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private List<Messages> listOfMessages = new ArrayList<>();

    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == RIGHT) {
            view = inflater.inflate(R.layout.chatitemright, parent, false);
        } else {
            view = inflater.inflate(R.layout.chatitemleft, parent, false);
        }

        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Messages message = listOfMessages.get(position);

        holder.messageText.setVisibility(View.VISIBLE);
        holder.timeOfSent.setVisibility(View.VISIBLE);

        holder.messageText.setText(message.getMessage());
        if (message.getTime() != null) {
            holder.timeOfSent.setText(message.getTime().substring(0, 5));
        }
    }

    @Override
    public int getItemCount() {
        return listOfMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages message = listOfMessages.get(position);
        return Utils.getUidLoggedIn().equals(message.getSender()) ? RIGHT : LEFT;
    }

    public void setList(List<Messages> newList) {
        this.listOfMessages = newList;
        notifyDataSetChanged(); // Notifie RecyclerView de la mise à jour des données
    }

    static class MessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeOfSent;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.show_message);
            timeOfSent = itemView.findViewById(R.id.timeView);
        }
    }
}
