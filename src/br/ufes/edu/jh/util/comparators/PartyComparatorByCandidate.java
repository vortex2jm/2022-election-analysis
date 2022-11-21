package br.ufes.edu.jh.util.comparators;

import java.util.Comparator;

import br.ufes.edu.jh.domain.PoliticalParty;

public class PartyComparatorByCandidate implements Comparator<PoliticalParty>{

    @Override
    public int compare(PoliticalParty arg0, PoliticalParty arg1) {


        //first rule: vote count
        if(arg0.mostVotedCandidate().getQtVotos() > arg1.mostVotedCandidate().getQtVotos())
            return -1;
        if(arg0.mostVotedCandidate().getQtVotos() < arg1.mostVotedCandidate().getQtVotos())
            return 1;
        //second rule: birth date
        if(arg0.mostVotedCandidate().getDtNascimento().compareTo(arg1.mostVotedCandidate().getDtNascimento()) < 0)
            return -1;
        if(arg0.mostVotedCandidate().getDtNascimento().compareTo(arg1.mostVotedCandidate().getDtNascimento()) > 0)
            return 1;
        //third rule: voting number
        if(arg0.mostVotedCandidate().getParty().getNumber() < arg1.mostVotedCandidate().getParty().getNumber())
            return -1;
        if(arg0.mostVotedCandidate().getParty().getNumber() < arg1.mostVotedCandidate().getParty().getNumber())
            return 1;
        
        return 0;
    }    
}
