package br.ufes.edu.jh.domain;

import java.time.LocalDate;

public class Candidate implements Comparable<Candidate> {

    private int nrCandidato;
    private String nmUrnaCandidato;
    private String nmTipoDestinoVotos;
    private LocalDate dtNascimento;
    private boolean cdSitTotTurno;
    private int cdGenero;
    private PoliticalParty party;
    private int qtVotos;
    private int electedPosition;
    private int geralPosition;

    public Candidate(int nrCandidato, String nmUrnaCandidato, String nmTipoDestinoVotos, LocalDate dtNascimento,
            boolean cdSitTotTurno, int cdGenero, PoliticalParty party) {
        this.nrCandidato = nrCandidato;
        this.dtNascimento = dtNascimento;
        this.cdSitTotTurno = cdSitTotTurno;
        this.cdGenero = cdGenero;
        this.party = party;
        this.qtVotos = 0;
        this.nmUrnaCandidato = nmUrnaCandidato;
        this.nmTipoDestinoVotos = nmTipoDestinoVotos;
    }

    // ===============Setters================================================//
    public void setQtVotos(int qtVotos) {
        this.qtVotos += qtVotos;
    }

    public void setElectedPosition(int position) {
        this.electedPosition = position;
    }

    public void setGeralPosition(int geralPosition) {
        this.geralPosition = geralPosition;
    }

    // ===============Getters================================================//
    public int getNrCandidato() {
        return nrCandidato;
    }

    public boolean getCdSitTotTurno() {
        return cdSitTotTurno;
    }

    public int getQtVotos() {
        return qtVotos;
    }

    public LocalDate getDtNascimento() {
        return dtNascimento;
    }

    public int getCdGenero() {
        return cdGenero;
    }

    public int getElectedPosition() {
        return electedPosition;
    }

    public PoliticalParty getParty() {
        return party;
    }

    public String getNmUrnaCandidato() {
        return nmUrnaCandidato;
    }

    public String getNmTipoDestinoVotos() {
        return nmTipoDestinoVotos;
    }

    public int getGeralPosition() {
        return geralPosition;
    }

    // ==============Override================================================//
    @Override
    public int compareTo(Candidate o) {
        if (this.qtVotos < o.getQtVotos())
            return 1;
        if (this.qtVotos > o.getQtVotos())
            return -1;
        if (this.dtNascimento.compareTo(o.getDtNascimento()) < 0)
            return -1;
        if (this.dtNascimento.compareTo(o.getDtNascimento()) > 0) {
            return 1;
        }
        return 0;
    }

    // ==================================//
    @Override
    public String toString() {
        return this.nmUrnaCandidato + " (" + this.party.getSg() + ", " + this.qtVotos + " votos)";
    }
}
