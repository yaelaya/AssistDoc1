package ma.ensa.www.assistdoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ma.ensa.www.assistdoc.R;
import ma.ensa.www.assistdoc.model.Users;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<Users> usersList;
    private Context context;

    // Constructeur de l'adaptateur
    public UserAdapter(List<Users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infler le layout de l'item utilisateur
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // Récupérer l'utilisateur à la position spécifiée
        Users user = usersList.get(position);

        // Mettre à jour les informations dans la vue
        holder.usernameTextView.setText(user.getUseremail());  // Utiliser email comme nom d'utilisateur
        holder.statusTextView.setText(user.getType());  // Utiliser type (Patient, etc.)
    }

    @Override
    public int getItemCount() {
        return usersList.size();  // Retourne le nombre total d'utilisateurs
    }

    // ViewHolder pour chaque item de la RecyclerView
    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView usernameTextView;
        TextView statusTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }
    }
}
