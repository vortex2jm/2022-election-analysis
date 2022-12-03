package br.ufes.edu.jh.domain;

import java.util.ArrayList;
import java.util.List;

public class PoliticalParty implements Comparable<PoliticalParty> {

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

  // ======================Getters==================================//
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

  public int getPosition() {
    return position;
  }

  public int getTotalVotes() {
    return this.legendVotes + getNominalVotes();
  }

  // ======================Setters==================================//
  public void setLegendVotes(int legendVotes) {
    this.legendVotes += legendVotes;
  }

  public void setCandidatesList(Candidate candidate) {
    this.candidatesList.add(candidate);
  }

  public void setPosition(int position) {
    this.position = position;
  }

  // ======================another get methods======================//
  /**
   * @return Lista de candidatos do partido, ordenados
   */
  public List<Candidate> getCandidatesList() {
    var list = new ArrayList<>(this.candidatesList);
    list.sort(null);
    return list;
  }

  // ======================================//
  /**
   * @return número de votos nominais
   */
  public int getNominalVotes() {
    int total = 0;
    for (Candidate c : candidatesList) {
      total += c.getQtVotos();
    }
    return total;
  }

  // ======================================//
  /**
   * @return a quantidade de candidatos do partido que foram eleitos
   */
  public int getElectedAmount() {
    int total = 0;
    for (Candidate c : candidatesList) {
      if (c.getCdSitTotTurno())
        total++;
    }
    return total;
  }

  // ======================================//
  /**
   * @return o candidato mais votado do partido
   */
  public Candidate mostVotedCandidate() {
    var list = new ArrayList<Candidate>(this.candidatesList);
    list.sort(null);
    return list.get(0);
  }

  // ======================================//
  /**
   * @return o candidato menos votado do partido
   */
  public Candidate leastVotedCandidate() {
    var list = new ArrayList<Candidate>(this.candidatesList);
    list.sort(null);
    return list.get(list.size() - 1);
  }

  // ================Override======================================//
  @Override
  public int compareTo(PoliticalParty arg0) {
    int ownTotal = legendVotes + getNominalVotes();
    int otherTotal = arg0.legendVotes + arg0.getNominalVotes();

    if (ownTotal > otherTotal)
      return -1;
    if (ownTotal < otherTotal)
      return 1;
    if (this.number < arg0.number)
      return -1;
    if (this.number > arg0.number)
      return 1;
    return 0;
  }
}
