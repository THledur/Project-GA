import java.util.List;

public class Reserva {
    private int diaInicio;
    private int diaFim;
    private String cliente;
    private Quarto quarto;
    private char status; // A/C/I/O
    
    // Construtor
    public Reserva(int diaInicio, int diaFim, String cliente, Quarto quarto) {
        this.diaInicio = diaInicio;
        this.diaFim = diaFim;
        this.cliente = cliente;
        this.quarto = quarto;
        this.status = 'A'; // Ativa por padrão
    }
    
    // Getters e Setters
    public int getDiaInicio() { return diaInicio; }
    public void setDiaInicio(int diaInicio) { this.diaInicio = diaInicio; }
    
    public int getDiaFim() { return diaFim; }
    public void setDiaFim(int diaFim) { this.diaFim = diaFim; }
    
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    
    public Quarto getQuarto() { return quarto; }
    public void setQuarto(Quarto quarto) { this.quarto = quarto; }
    
    public char getStatus() { return status; }
    public void setStatus(char status) { 
        if(status == 'A' || status == 'C' || status == 'I' || status == 'O') {
            this.status = status;
        }
    }
    
    // Verifica se a reserva está ativa em uma data específica
    public boolean estaAtivaNaData(int data) {
        return status == 'A' && data >= diaInicio && data <= diaFim;
    }
    
    // Serializar para arquivo
    public String serializar() {
        return diaInicio + "\t" + diaFim + "\t" + cliente + "\t" + 
               quarto.getNumero() + "\t" + status;
    }
    
    // Deserializar do arquivo
    public static Reserva deserializar(String linha, List<Quarto> quartos) {
        String[] partes = linha.split("\t");
        if(partes.length == 5) {
            int diaInicio = Integer.parseInt(partes[0]);
            int diaFim = Integer.parseInt(partes[1]);
            String cliente = partes[2];
            int numeroQuarto = Integer.parseInt(partes[3]);
            char status = partes[4].charAt(0);
            
            // Encontrar o quarto correspondente
            Quarto quarto = null;
            for(Quarto q : quartos) {
                if(q.getNumero() == numeroQuarto) {
                    quarto = q;
                    break;
                }
            }
            
            if(quarto != null) {
                Reserva reserva = new Reserva(diaInicio, diaFim, cliente, quarto);
                reserva.setStatus(status);
                return reserva;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        String statusStr = "";
        switch(status) {
            case 'A': statusStr = "Ativa"; break;
            case 'C': statusStr = "Cancelada"; break;
            case 'I': statusStr = "Check-In"; break;
            case 'O': statusStr = "Check-Out"; break;
        }
        
        return "Cliente: " + cliente + " | Quarto: " + quarto.getNumero() + 
               " | Período: " + diaInicio + " a " + diaFim + " | Status: " + statusStr;
    }
}