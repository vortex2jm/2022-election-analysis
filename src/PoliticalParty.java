import java.util.ArrayList;
import java.util.List;

public class PoliticalParty {

  private String sg;
  private int legendVotes;
  private int federation;
  private int number;
  private List<Candidate> candidatesList;
  
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
    return new ArrayList<>(this.candidatesList);
  }

  //======================Setters==================================//
  public void setLegendVotes(int legendVotes) {
    this.legendVotes += legendVotes;
  }
  public void setCandidatesList(Candidate candidate) {
    this.candidatesList.add(candidate);
  }   
}
