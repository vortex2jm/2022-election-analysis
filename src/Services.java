public class Services {
    
    public static int validateArgs(String args) throws Exception{
        if(args == "--estadual")
            return 7;
        if(args == "--federal")
            return 6;
        throw new Exception("Argumento inválido");
    }
    
}
