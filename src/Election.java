import java.util.ArrayList;
import java.util.List;

public class Election {
    
    // Estes dados poderão ser acessados diretamente somente pela classe Services
    protected List<Candidate> candidates = new ArrayList<>();
    protected List<PoliticalParty> parties = new ArrayList<>();
    
    //===================Getters===========================//
    public List<PoliticalParty> getPoliticalParties() {
        return new ArrayList<>(parties);
    }
    public List<Candidate> getCandidates() {
        return new ArrayList<>(candidates);
    }
    
    //==================Setters============================//
    public void addCandidate(Candidate candidate){
        this.candidates.add(candidate);
    }
    public void addParty(PoliticalParty party){
        this.parties.add(party);
    }

}
