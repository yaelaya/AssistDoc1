package ma.ensa.www.assistdoc.model;
import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {

    private String userid;         // ID unique de l'utilisateur
    private String status;         // Statut de l'utilisateur (en ligne, hors ligne, etc.)
    private String username;       // Nom d'utilisateur affiché
    private String useremail;      // Adresse email de l'utilisateur
    private String type;
    private String age;  // Ajoutez un champ pour l'âge
    private String gender;  // Ajoutez un champ pour le genre

    public Users(){
        //need firebase
    }
    // Constructeur par défaut
    public Users(String userid, String status, String imageUrl, String username, String useremail, boolean hasNewMessage) {
        this.userid = userid;
        this.status = status;
        this.username = username;
        this.useremail = useremail;
        this.age = age;
        this.gender = gender;
    }

    // Constructeur secondaire pour créer un objet Users à partir d'un Parcel
    protected Users(Parcel in) {
        userid = in.readString();
        status = in.readString();
        username = in.readString();
        useremail = in.readString();
    }

    // Getter et Setter pour les propriétés (optionnels selon votre besoin)
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Écrit les propriétés de l'utilisateur dans le Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeString(status);
        dest.writeString(username);
        dest.writeString(useremail);
    }

    // Méthode nécessaire pour l'interface Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    // Créateur pour la classe Parcelable
    public static final Parcelable.Creator<Users> CREATOR = new Parcelable.Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in); // Création d'une instance Users à partir d'un Parcel
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size]; // Création d'un tableau d'objets Users
        }
    };
}
