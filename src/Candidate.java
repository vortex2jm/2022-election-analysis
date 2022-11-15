import java.util.Date;

public class Candidate implements Comparable<Candidate>{
    
    private int nrCandidato;
    private String nmUrnaCandidato;
    private Date dtNascimento;
    private Boolean cdSitTotTurno;
    private int cdGenero;
    private PoliticalParty party;
    private int qtVotos;

    public Candidate(int nrCandidato, String nmUrnaCandidato, Date dtNascimento,
     Boolean cdSitTotTurno, int cdGenero, PoliticalParty party){

        this.nrCandidato = nrCandidato;
        this.nmUrnaCandidato = nmUrnaCandidato;
        this.dtNascimento = dtNascimento;
        this.cdSitTotTurno = cdSitTotTurno;
        this.cdGenero = cdGenero;
        this.party = party;
        this.qtVotos = 0;
    }


    //===============Setters================================================//
    public void setQtVotos(int qtVotos) {
        this.qtVotos = qtVotos;
    }


    



    //==============================Override methods========================//
    //@Override
    //public String toString() {
    //    return votingName + " (" + candidateParty.getName() + ", " + candidateVotes + " votos)\n";
    //}

    @Override
    public int compareTo(Candidate o) {
        // TODO Auto-generated method stub
        return 0;
    }
}
