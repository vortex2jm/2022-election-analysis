import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Services {

    public static int validateArgs(String args) throws Exception {
        if (args.compareTo("--estadual") == 0)
            return 7;
        if (args.compareTo("--federal") == 0)
            return 6;
        throw new Exception("Argumento inválido");
    }

    public static BufferedReader createBuffer(String args) throws Exception {
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(new File(args)));
            return buffer;
        } catch (Exception e) {
            throw e;
        }
    }

    public static Boolean candidateIsValid(String cdCargo, String cdDetalhesSituacaoCand, int type) {
        int cdC = Integer.parseInt(cdCargo);
        int cdD = Integer.parseInt(cdDetalhesSituacaoCand);
        if (cdC == type && (cdD == 2 || cdD == 16))
            return true;
        return false;
    }

    //Overload
    public static Boolean candidateIsValid(String cdCargo, int type, String nrVotavel){
        int cdC = Integer.parseInt(cdCargo);
        int nrV = Integer.parseInt(nrVotavel);
        if(cdC == type && nrV != 95 && nrV != 96 && nrV != 97 && nrV != 98)
            return true;
        return false;
    }

    public static PoliticalParty updateParties(Election election, String[] data) {

        int nrPartido = Integer.parseInt(data[27]);
        String sgPartido = data[28];
        int nrFederacao = Integer.parseInt(data[30]);

        if (election.parties.containsKey(nrPartido)) {
            return election.parties.get(nrPartido);
        }

        PoliticalParty p = new PoliticalParty(nrPartido, sgPartido, nrFederacao);
        election.parties.put(nrPartido, p);
        return p;
    }

    public static String[] dataFormatter(String line) {
        String[] currentData = line.split(";");
        for (int i = 0; i < currentData.length; i++) {
            currentData[i] = currentData[i].replaceAll("\"", "");
        }
        return currentData;
    }

    public static void updateCandidates(Election election, String[] data, PoliticalParty party) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dtNsc = dateFormat.parse(data[42]);
        Boolean situation = isElectedCandidate(data[56]);
        int nrCandidato = Integer.parseInt(data[16]);
        String nmUrnaCandidato = data[18];
        int cdGenero = Integer.parseInt(data[45]);

        Candidate cand = new Candidate(nrCandidato, nmUrnaCandidato, dtNsc, situation, cdGenero, party); 
        election.candidates.put(nrCandidato, cand);
        party.setCandidatesList(cand);
    }

    public static Boolean isElectedCandidate(String sit) {
        int situation = Integer.parseInt(sit);
        if (situation == 2 || situation == 3)
            return true;
        return false;
    }

    public static void processCandidatesFile(BufferedReader bufferCandidates, Election election)
            throws Exception {
        String currentLine;
        String[] currentData;
        PoliticalParty party;

        bufferCandidates.readLine(); // Eliminando linha de legenda
        while ((currentLine = bufferCandidates.readLine()) != null) {

            // Formatando os dados
            currentData = Services.dataFormatter(currentLine);

            // Atualizando lista de candidatos e partidos
            if (Services.candidateIsValid(currentData[13], currentData[24], election.getType())) {
                party = Services.updateParties(election, currentData);
                Services.updateCandidates(election, currentData, party);
            }
        }
    }

    public static void processVotesFile(BufferedReader bufferVotes, Election election) throws Exception {

        String currentLine;
        String[] currentData;

        bufferVotes.readLine();
        while ((currentLine = bufferVotes.readLine()) != null) {

            currentData = dataFormatter(currentLine);

            if(candidateIsValid(currentData[17], election.getType(), currentData[19])){
                processCandidatesVotes(election, currentData);
            }
        }

    }

    public static void processCandidatesVotes(Election election, String[] data){
        int nrVotavel = Integer.parseInt(data[19]);
        int qtVotos = Integer.parseInt(data[21]);

        if(election.candidates.containsKey(nrVotavel)){
            election.candidates.get(nrVotavel).setQtVotos(qtVotos);
            election.setNominalVotes(qtVotos);
            return;
        }
        if(election.parties.containsKey(nrVotavel)){
            election.parties.get(nrVotavel).setLegendVotes(qtVotos);
            election.setLegendVotes(qtVotos);
        }       
    }

    public static void generateReports(Election election){

        System.out.println("Número de vagas: "+election.electedAmount());
        System.out.print("\n");
        
        //===============================================================//
        String category="";
        if(election.getType() == 6)
            category = "federais";
        else if(election.getType() == 7)
            category = "estaduais";
        System.out.printf("Deputados %s eleitos:\n", category);
        
        //===============================================================//
        for(Candidate c: election.electedCandidates()){
            System.out.printf("%d - ",c.getPosition());
            System.out.println(c);
        }
        System.out.print("\n");
        
        //===============================================================//
        System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        for(Candidate c: election.getBestCandidates()){
            System.out.printf("%d - ", c.getPosition());
            System.out.println(c);
        }
        System.out.print("\n");

        //===============================================================//
        System.out.println("Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");
        System.out.println("(com sua posição no ranking de mais votados)");
        for(Candidate c: election.electedIfMajorElection()){
            System.out.printf("%d - ", c.getPosition());
            System.out.println(c);
        }
        System.out.print("\n");

        //===============================================================//
        System.out.println("Eleitos, que se beneficiaram do sistema proporcional:");
        System.out.println("(com sua posição no ranking de mais votados)");
        for(Candidate c: election.electedByProportional()){
            System.out.printf("%d - ", c.getPosition());
            System.out.println(c);
        }
        System.out.print("\n");

        //===============================================================//
        System.out.println("Votação dos partidos e número de candidatos eleitos:");
        for(PoliticalParty p: election.getParties()){
            System.out.printf("%d - %s - %d, %d votos (%d nominais e %d de legenda), %d candidatos eleitos\n",
            p.getPosition(), p.getSg(), p.getNumber(), p.getTotalVotes(),
             p.getNominalVotes(), p.getLegendVotes(), p.getElectedAmount());
        }
        System.out.print("\n");
        
        // Linkar candidato na lista de partido e atualizar os votos do partido
        // Criar funções do partido
        // Adicionar função de faixa etária por data via argumentos
    }
}
