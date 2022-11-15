import java.util.Date;

public class Candidate implements Comparable<Candidate>{
    
    private int nrCandidato;
    private String nmUrnaCandidato;
    private Date dtNascimento;
    private Boolean cdSitTotTurno;
    private int cdGenero;
    private PoliticalParty party;
    private int qtVotos;
    private int position;

    public Candidate(int nrCandidato, String nmUrnaCandidato, Date dtNascimento,
     Boolean cdSitTotTurno, int cdGenero, PoliticalParty party){

        this.nrCandidato = nrCandidato;
        this.dtNascimento = dtNascimento;
        this.cdSitTotTurno = cdSitTotTurno;
        this.cdGenero = cdGenero;
        this.party = party;
        this.qtVotos = 0;

        if(this.party.getFederation() != -1){
            this.nmUrnaCandidato = "*" + nmUrnaCandidato;
            return;
        }
        this.nmUrnaCandidato = nmUrnaCandidato;
    }

    //===============Setters================================================//
    public void setQtVotos(int qtVotos) {
        this.qtVotos += qtVotos;
    }
    public void setPosition(int position) {
        this.position = position;
    }

    //===============Getters================================================//
    public int getNrCandidato() {
        return nrCandidato;
    }
    public Boolean getCdSitTotTurno() {
        return cdSitTotTurno;
    }
    public int getQtVotos() {
        return qtVotos;
    }
    public Date getDtNascimento() {
        return dtNascimento;
    }
    public int getCdGenero() {
        return cdGenero;
    }
    public int getPosition() {
        return position;
    }

    //==============Override================================================//
    @Override
    public int compareTo(Candidate o) {
        if(this.qtVotos < o.getQtVotos())
            return 1;
        if(this.qtVotos > o.getQtVotos())
            return -1;
        if(this.dtNascimento.compareTo(o.getDtNascimento()) < 0)
            return -1;
        if(this.dtNascimento.compareTo(o.getDtNascimento()) > 0){
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return this.nmUrnaCandidato + " (" + this.party.getSg() + ", " + this.qtVotos + " votos)";
    }
}
