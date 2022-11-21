package br.ufes.edu.jh.domain;
import java.util.ArrayList;
import java.util.List;

public class PoliticalParty implements Comparable<PoliticalParty>{

  private String sg;
  private int legendVotes;
  private int federation;
  private int number;
  private List<Candidate> candidatesList;
  private int position;
  
  public PoliticalParty(int number, String sg, int federation) {
    this.number = number;
    this.sg = sg;
    this.federation = federation;
    this.candidatesList = new ArrayList<>();
  }

  //======================Getters==================================//
  public String getSg() {
    return sg;
  }
  public int getLegendVotes() {
    return legendVotes;
  }
  public int getFederation() {
    return federation;
  }
  public int getNumber() {
    return number;
  }
  public List<Candidate> getCandidatesList() {
    var list = new ArrayList<>(this.candidatesList);
    list.sort(null);
    return list;
  }
  public int getNominalVotes(){
    int total=0;
    for(Candidate c: candidatesList){
      total += c.getQtVotos();    
    }
    return total;
  }
  public int getElectedAmount(){
    int total=0;
    for(Candidate c: candidatesList){
      if(c.getCdSitTotTurno())
        total++;
    }
    return total;
  }
  public int getPosition() {
    return position;
  }
  public int getTotalVotes(){
    return this.legendVotes + getNominalVotes();
  }
  public Candidate mostVotedCandidate(){
    var list = new ArrayList<Candidate>(this.candidatesList);
    list.sort(null);
    
    return list.get(0);
  }

  public Candidate leastVotedCandidate(){
    var list = new ArrayList<Candidate>(this.candidatesList);
    list.sort(null);
    return list.get(list.size() - 1);
  }

  //======================Setters==================================//
  public void setLegendVotes(int legendVotes) {
    this.legendVotes += legendVotes;
  }
  public void setCandidatesList(Candidate candidate) {
    this.candidatesList.add(candidate);
  }
  public void setPosition(int position) {
    this.position = position;
  }

  //================Override======================================//
  @Override
  public int compareTo(PoliticalParty arg0) {
    
    int ownTotal = legendVotes + getNominalVotes();
    int otherTotal = arg0.legendVotes + arg0.getNominalVotes();

    if(ownTotal > otherTotal)
      return -1;
    if(ownTotal < otherTotal)
      return 1;
    if(this.number < arg0.number)
      return -1;
    if(this.number > arg0.number)
      return 1;
    return 0;
  }   
}
