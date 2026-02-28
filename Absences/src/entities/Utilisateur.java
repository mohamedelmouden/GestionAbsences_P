package entities;

public class Utilisateur {
    private int    id;
    private String username;
    private String motDePasse;
    private String email;

    public Utilisateur(int id, String username, String motDePasse, String email) {
        this.id = id;
        this.username  = username;
        this.motDePasse = motDePasse;
        this.email     = email;
    }

    public int    getId()         { return id;         }
    public String getUsername()   { return username;   }
    public String getMotDePasse() { return motDePasse; }
    public String getEmail()      { return email;      }
}