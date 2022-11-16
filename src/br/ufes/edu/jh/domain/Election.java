package br.ufes.edu.jh.domain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufes.edu.jh.util.comparators.PartyComparatorByCandidate;

public class Election {
    
    // Separar os packages posteriormente
    // Estes dados poderão ser acessados diretamente somente pela classe Services
    private Map<Integer, Candidate> candidates = new HashMap<>();
    private Map<Integer, PoliticalParty> parties = new HashMap<>();

    private int nominalVotes = 0;
    private int legendVotes = 0;

    private int type;

    public Election(int type){
        this.type = type;
    }
    
    //==============Getters======================//
    public int getNominalVotes() {
        return nominalVotes;
    }
    public int getLegendVotes() {
        return legendVotes;
    }
    public int getType() {
        return type;
    }
    public Map<Integer, Candidate> getCandidatesMap() {
        return candidates;
    }
    public Map<Integer, PoliticalParty> getPartiesMap(){
        return parties;
    }

    //==============Setters======================//
    public void setNominalVotes(int nominalVotes) {
        this.nominalVotes += nominalVotes;
    }
    public void setLegendVotes(int legendVotes) {
        this.legendVotes += legendVotes;
    }
    public void addCandidate(int key, Candidate value) {
        this.candidates.put(key, value);
    }
    public void addPartie(int key, PoliticalParty value) {
        this.parties.put(key, value);
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
        for(int i=0; i<result.size(); i++){
            result.get(i).setPosition(i+1);
        }
        return result;
    }

    public List<Candidate> getAllCandidates(){
        var c = new ArrayList<Candidate>(candidates.values());
        c.sort(null);
        for(int i=0; i<c.size(); i++){
            c.get(i).setPosition(i+1);
        }
        return c;
    }

    public List<Candidate> getBestCandidates(){
        var c = new ArrayList<Candidate>();
        for(int i=0; i<electedAmount(); i++){
            c.add(getAllCandidates().get(i));
        }
        c.sort(null);
        return c;
    }

    public List<Candidate> electedIfMajorElection(){
        var mjEl = new ArrayList<Candidate>();
        for(Candidate c: getBestCandidates()){
            if(!electedCandidates().contains(c))
                mjEl.add(c);
        }
        mjEl.sort(null);
        return mjEl;
    }

    public List<Candidate> electedByProportional(){
        var elctdProp = new ArrayList<Candidate>();
        for(Candidate c: electedCandidates()){
            if(!getBestCandidates().contains(c))
                elctdProp.add(c);
        }
        elctdProp.sort(null);
        return elctdProp;
    }

    public List<PoliticalParty> getParties(){
        var p = new ArrayList<PoliticalParty>(parties.values());
        p.sort(null);
        for(int i=0; i<p.size(); i++){
            p.get(i).setPosition(i+1);
        }        
        return p;
    }

    public List<PoliticalParty> getPartiesOrderedByCandidates(){
        var pList = new ArrayList<PoliticalParty>();
        for(PoliticalParty p: getParties()){
            if(p.getTotalVotes() > 0){
                pList.add(p);
            }
        }
        pList.sort(new PartyComparatorByCandidate());
        for(int i=0; i<pList.size(); i++){
            pList.get(i).setPosition(i+1);
        } 
        return pList;
    }
}   
