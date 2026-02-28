package entities;

import java.util.Date;

public class Absence {
    private int    id;
    private Date   dateDebut;
    private Date   dateFin;
    private int    idEmploye;
    private int    idType;

    // ── Champs enrichis (remplis par le JOIN dans AbsenceDAO) ─────────────
    private String nomEmploye;
    private String departementEmploye;   // ← REMPLACE prenomEmploye
    private String posteEmploye;          // ← REMPLACE emailEmploye
    private String libelleType;
    private int    nbJours;

    // ── Constructeurs ──────────────────────────────────────────────────────

    // Pour ajouter (sans id)
    public Absence(Date dateDebut, Date dateFin, int idEmploye, int idType) {
        this.dateDebut  = dateDebut;
        this.dateFin    = dateFin;
        this.idEmploye  = idEmploye;
        this.idType     = idType;
    }

    // Pour modifier (avec id)
    public Absence(int id, Date dateDebut, Date dateFin, int idEmploye, int idType) {
        this.id         = id;
        this.dateDebut  = dateDebut;
        this.dateFin    = dateFin;
        this.idEmploye  = idEmploye;
        this.idType     = idType;
    }

    // ── Getters ───────────────────────────────────────────────────────────
    public int    getId()                   { return id;                   }
    public Date   getDateDebut()            { return dateDebut;            }
    public Date   getDateFin()              { return dateFin;              }
    public int    getIdEmploye()            { return idEmploye;            }
    public int    getIdType()               { return idType;               }
    public String getNomEmploye()           { return nomEmploye;           }
    public String getDepartementEmploye()   { return departementEmploye;   }  // ← NOUVEAU
    public String getPosteEmploye()         { return posteEmploye;          }  // ← NOUVEAU
    public String getLibelleType()          { return libelleType;          }
    public int    getNbJours()              { return nbJours;              }

    // ── Setters ───────────────────────────────────────────────────────────
    public void setId(int id)                               { this.id                   = id;                   }
    public void setDateDebut(Date d)                        { this.dateDebut            = d;                    }
    public void setDateFin(Date d)                          { this.dateFin              = d;                    }
    public void setIdEmploye(int idEmploye)                 { this.idEmploye            = idEmploye;            }
    public void setIdType(int idType)                       { this.idType               = idType;               }
    public void setNomEmploye(String nom)                   { this.nomEmploye           = nom;                  }
    public void setDepartementEmploye(String dep)           { this.departementEmploye   = dep;                  }  // ← NOUVEAU
    public void setPosteEmploye(String poste)               { this.posteEmploye         = poste;                }  // ← NOUVEAU
    public void setLibelleType(String libelle)              { this.libelleType          = libelle;              }
    public void setNbJours(int n)                           { this.nbJours              = n;                    }
}