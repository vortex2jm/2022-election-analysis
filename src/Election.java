import java.util.HashMap;
import java.util.Map;

public class Election {
    
    // Estes dados poderão ser acessados diretamente somente pela classe Services
    protected Map<Integer, Candidate> candidates = new HashMap<>();
    protected Map<Integer, PoliticalParty> parties = new HashMap<>();

    //Aqui ficarão os métodos que retornarão as estatísticas das eleições
}   
