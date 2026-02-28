package entities;

public class Employe {
    private int    id;
    private String nom;
    private String departement;
    private String poste;

    // Constructeur sans id (pour ajout)
    public Employe(String nom, String departement, String poste) {
        this.nom          = nom;
        this.departement  = departement;
        this.poste        = poste;
    }

    // Constructeur complet (pour modification / lecture BDD)
    public Employe(int id, String nom, String departement, String poste) {
        this.id           = id;
        this.nom          = nom;
        this.departement  = departement;
        this.poste        = poste;
    }

    // ── Getters ──────────────────────────────────────────────────────────
    public int    getId()           { return id;           }
    public String getNom()          { return nom;          }
    public String getDepartement()  { return departement;  }
    public String getPoste()        { return poste;        }

    // ── Setters ──────────────────────────────────────────────────────────
    public void setId(int id)                    { this.id          = id;          }
    public void setNom(String nom)               { this.nom         = nom;         }
    public void setDepartement(String dep)       { this.departement = dep;         }
    public void setPoste(String poste)           { this.poste       = poste;       }

    // Affichage dans JComboBox
    @Override
    public String toString() { return nom + " (" + departement + ")"; }
}