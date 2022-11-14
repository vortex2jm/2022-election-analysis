import java.util.Date;

public class Candidate implements Comparable<Candidate>{
    

    private int nrCandidato;
    private String nmUrnaCandidato;
    private Date dtNascimento;
    private Boolean cdSitTotTurno;
    private int cdGenero;
    private PoliticalParty party;
    private int qtVotos;

    
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
