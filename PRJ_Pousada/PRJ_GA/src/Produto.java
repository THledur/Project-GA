
public class Produto {
    private int codigo;
    private String nome;
    private float valor;
    
    // Construtor
    public Produto(int codigo, String nome, float valor) {
        this.codigo = codigo;
        this.nome = nome;
        this.valor = valor;
    }
    
    // Getters e Setters
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public float getValor() { return valor; }
    public void setValor(float valor) { 
        if(valor >= 0) this.valor = valor; 
    }
    
    // Serializar para arquivo
    public String serializar() {
        return codigo + "\t" + nome + "\t" + valor;
    }
    
    // Deserializar do arquivo
    public static Produto deserializar(String linha) {
        String[] partes = linha.split("\t");
        if(partes.length == 3) {
            int codigo = Integer.parseInt(partes[0]);
            String nome = partes[1];
            float valor = Float.parseFloat(partes[2]);
            return new Produto(codigo, nome, valor);
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Código: " + codigo + " | Produto: " + nome + " | Valor: R$ " + valor;
    }
}