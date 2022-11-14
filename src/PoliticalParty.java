import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PoliticalParty {

  private String sg;
  private int legendVotes;
  private List<Candidate> candidatesList;
  private int federation;
  private int number;

  public int getNumber() {
    return number;
  }

  public PoliticalParty(int number, String sg, int federation) {
    this.number = number;
    this.sg = sg;
    this.federation = federation;
    this.candidatesList = new ArrayList<>();
  }

  //========================================================//
  public String getSg() {
    return sg;
  }

  //========================================================//
  public int getLegendVotes() {
    return legendVotes;
  }

  public void setLegendVotes(int legendVotes) {
    this.legendVotes += legendVotes;
  }

  //========================================================//  
  public Collection<Candidate> getCandidatesList() {
    return new ArrayList<>(this.candidatesList);
  }

  public void setCandidatesList(List<Candidate> candidatesList) {
    this.candidatesList = candidatesList;
  }
  
}
