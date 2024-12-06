package ma.ensa.www.assistdoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import ma.ensa.www.assistdoc.R;


public class UserAdapter extends RecyclerView.ViewHolder {

    public TextView usernameTextView;
    public TextView emailTextView;

    public UserAdapter(@NonNull View itemView) {
        super(itemView);
        usernameTextView = itemView.findViewById(R.id.usernameTextView);
        emailTextView = itemView.findViewById(R.id.emailTextView);
    }
}

