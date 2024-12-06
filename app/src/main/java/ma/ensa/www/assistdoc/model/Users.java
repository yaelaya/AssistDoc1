package ma.ensa.www.assistdoc.model;
import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {
    private long userId;  // Utilisez long si le userId est un nombre
    private String status;
    private String username;
    private String email;
    private String type;
    private String age;
    private String gender;

    // Constructeur par défaut
    public Users() {
        // Firebase nécessite un constructeur par défaut
    }

    // Constructeur avec tous les paramètres
    public Users(long userId, String status, String username, String email, String type, String age, String gender) {
        this.userId = userId;
        this.status = status;
        this.username = username;
        this.email = email;
        this.type = type;
        this.age = age;
        this.gender = gender;
    }

    // Constructeur secondaire pour créer un objet Users à partir d'un Parcel
    protected Users(Parcel in) {
        userId = in.readLong();  // Utilisez readLong() pour récupérer le userId comme long
        status = in.readString();
        username = in.readString();
        email = in.readString();
        type = in.readString();
        age = in.readString();
        gender = in.readString();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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
        return email;
    }

    public void setUseremail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Méthode writeToParcel pour écrire dans le Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(userId);
        dest.writeString(status);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(type);
        dest.writeString(age);
        dest.writeString(gender);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Users> CREATOR = new Parcelable.Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };
}
