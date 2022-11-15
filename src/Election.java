import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Election {
    
    // Separar os packages posteriormente
    // Estes dados poderão ser acessados diretamente somente pela classe Services
    protected Map<Integer, Candidate> candidates = new HashMap<>();
    protected Map<Integer, PoliticalParty> parties = new HashMap<>();

    private int nominalVotes = 0;
    private int legendVotes = 0;
    
    //==============Getters======================//
    public int getNominalVotes() {
        return nominalVotes;
    }
    public int getLegendVotes() {
        return legendVotes;
    }

    //==============Setters======================//
    public void setNominalVotes(int nominalVotes) {
        this.nominalVotes += nominalVotes;
    }
    public void setLegendVotes(int legendVotes) {
        this.legendVotes += legendVotes;
    }

    //========================================================================//
    public int electedAmount(){
        int result = 0;
        var c = new ArrayList<Candidate>(candidates.values());
        for(Candidate cand: c){
            if(cand.getCdSitTotTurno())
                result++;
        }
        return result;
    }   

    public List<Candidate> electedCandidates(){
        var c = new ArrayList<Candidate>(candidates.values());
        var result = new ArrayList<Candidate>();
        for(Candidate cand: c){
            if(cand.getCdSitTotTurno()){
                result.add(cand);
            }
        }
        result.sort(null);
        return result;
    }
}   
