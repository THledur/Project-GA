import java.util.*;

public class Quarto {
    private int numero;
    private char categoria; // S/M/P
    private float diaria;
    private List<Integer> consumo; // códigos dos produtos consumidos
    
    // Construtor
    public Quarto(int numero, char categoria, float diaria) {
        this.numero = numero;
        this.categoria = categoria;
        this.diaria = diaria;
        this.consumo = new ArrayList<>();
    }
    
    // Getters e Setters
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    
    public char getCategoria() { return categoria; }
    public void setCategoria(char categoria) { 
        if(categoria == 'S' || categoria == 'M' || categoria == 'P') {
            this.categoria = categoria;
        }
    }
    
    public float getDiaria() { return diaria; }
    public void setDiaria(float diaria) { 
        if(diaria >= 0) this.diaria = diaria; 
    }
    
    public List<Integer> getConsumo() { return consumo; }
    
    // Métodos de consumo
    public void adicionaConsumo(int codigoProduto) {
        consumo.add(codigoProduto);
    }
    
    public String listaConsumo(List<Produto> produtos) {
        StringBuilder sb = new StringBuilder();
        for(int codigo : consumo) {
            for(Produto p : produtos) {
                if(p.getCodigo() == codigo) {
                    sb.append(p.toString()).append("\n");
                    break;
                }
            }
        }
        return sb.toString();
    }
    
    public float valorTotalConsumo(List<Produto> produtos) {
        float total = 0;
        for(int codigo : consumo) {
            for(Produto p : produtos) {
                if(p.getCodigo() == codigo) {
                    total += p.getValor();
                    break;
                }
            }
        }
        return total;
    }
    
    public void limpaConsumo() {
        consumo.clear();
    }
    
    // Serializar para arquivo
    public String serializar() {
        StringBuilder sb = new StringBuilder();
        sb.append(numero).append("\t")
          .append(categoria).append("\t")
          .append(diaria).append("\t");
        
        // Serializar lista de consumo
        for(int i = 0; i < consumo.size(); i++) {
            sb.append(consumo.get(i));
            if(i < consumo.size() - 1) sb.append(",");
        }
        
        return sb.toString();
    }
    
    // Deserializar do arquivo
    public static Quarto deserializar(String linha) {
        String[] partes = linha.split("\t");
        if(partes.length >= 3) {
            int numero = Integer.parseInt(partes[0]);
            char categoria = partes[1].charAt(0);
            float diaria = Float.parseFloat(partes[2]);
            
            Quarto quarto = new Quarto(numero, categoria, diaria);
            
            // Deserializar consumo se existir
            if(partes.length > 3 && !partes[3].isEmpty()) {
                String[] consumos = partes[3].split(",");
                for(String codigo : consumos) {
                    if(!codigo.isEmpty()) {
                        quarto.adicionaConsumo(Integer.parseInt(codigo));
                    }
                }
            }
            
            return quarto;
        }
        return null;
    }
    
    @Override
    public String toString() {
        String cat = "";
        switch(categoria) {
            case 'S': cat = "Standard"; break;
            case 'M': cat = "Master"; break;
            case 'P': cat = "Premium"; break;
        }
        return "Quarto " + numero + " | Categoria: " + cat + " | Diária: R$ " + diaria;
    }
}