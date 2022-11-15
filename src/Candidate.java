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

    //Implementar getters e setters

    //===============Setters================================================//
    public void setQtVotos(int qtVotos) {
        this.qtVotos = qtVotos;
    }

    //===============Getters================================================//
    public int getNrCandidato() {
        return nrCandidato;
    }


    @Override
    public int compareTo(Candidate o) {
        
        //Implementar parâmetros de comparação

        return 0;
    }
}
