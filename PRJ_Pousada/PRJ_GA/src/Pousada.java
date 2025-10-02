import java.io.*;
import java.util.*;

public class Pousada {
    private String nome;
    private String contato;
    private List<Quarto> quartos;
    private List<Reserva> reservas;
    private List<Produto> produtos;
    
    // Construtor
    public Pousada(String nome, String contato) {
        this.nome = nome;
        this.contato = contato;
        this.quartos = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.produtos = new ArrayList<>();
    }
    
    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }
    
    public List<Quarto> getQuartos() { return quartos; }
    public List<Reserva> getReservas() { return reservas; }
    public List<Produto> getProdutos() { return produtos; }
    
    // Métodos de negócio
    
    // 1. Consultar disponibilidade
    public String consultarDisponibilidade(int data, int numeroQuarto) {
        Quarto quarto = encontrarQuarto(numeroQuarto);
        if(quarto == null) {
            return "Quarto não encontrado!";
        }
        
        // Verificar se há reservas ativas para este quarto na data
        for(Reserva reserva : reservas) {
            if(reserva.getQuarto().getNumero() == numeroQuarto && 
               reserva.estaAtivaNaData(data)) {
                return "Quarto " + numeroQuarto + " não está disponível na data " + data;
            }
        }
        
        return "Quarto disponível!\n" + quarto.toString();
    }
    
    // 2. Consultar reserva
    public String consultarReserva(Integer data, String nomeCliente, Integer numeroQuarto) {
        List<Reserva> resultados = new ArrayList<>();
        
        for(Reserva reserva : reservas) {
            boolean match = true;
            
            if(data != null && !reserva.estaAtivaNaData(data)) {
                match = false;
            }
            if(nomeCliente != null && !reserva.getCliente().equalsIgnoreCase(nomeCliente)) {
                match = false;
            }
            if(numeroQuarto != null && reserva.getQuarto().getNumero() != numeroQuarto) {
                match = false;
            }
            if(reserva.getStatus() != 'A') {
                match = false;
            }
            
            if(match) {
                resultados.add(reserva);
            }
        }
        
        if(resultados.isEmpty()) {
            return "Nenhuma reserva encontrada com os parâmetros informados.";
        }
        
        StringBuilder sb = new StringBuilder();
        for(Reserva r : resultados) {
            sb.append(r.toString()).append("\n");
        }
        return sb.toString();
    }
    
    // 3. Realizar reserva
    public String realizarReserva(int diaInicio, int diaFim, String cliente, int numeroQuarto) {
        // Verificar se cliente já tem reserva ativa
        for(Reserva reserva : reservas) {
            if(reserva.getCliente().equalsIgnoreCase(cliente) && 
               (reserva.getStatus() == 'A' || reserva.getStatus() == 'I')) {
                return "Cliente já possui uma reserva ativa ou em check-in!";
            }
        }
        
        Quarto quarto = encontrarQuarto(numeroQuarto);
        if(quarto == null) {
            return "Quarto não encontrado!";
        }
        
        // Verificar disponibilidade do quarto no período
        for(int data = diaInicio; data <= diaFim; data++) {
            for(Reserva reserva : reservas) {
                if(reserva.getQuarto().getNumero() == numeroQuarto && 
                   reserva.estaAtivaNaData(data)) {
                    return "Quarto não disponível no período solicitado!";
                }
            }
        }
        
        Reserva novaReserva = new Reserva(diaInicio, diaFim, cliente, quarto);
        reservas.add(novaReserva);
        return "Reserva realizada com sucesso!\n" + novaReserva.toString();
    }
    
    // 4. Cancelar reserva
    public String cancelarReserva(String nomeCliente) {
        for(Reserva reserva : reservas) {
            if(reserva.getCliente().equalsIgnoreCase(nomeCliente) && 
               reserva.getStatus() == 'A') {
                reserva.setStatus('C');
                return "Reserva cancelada com sucesso!";
            }
        }
        return "Nenhuma reserva ativa encontrada para o cliente: " + nomeCliente;
    }
    
    // 5. Realizar check-in
    public String realizarCheckIn(String nomeCliente) {
        for(Reserva reserva : reservas) {
            if(reserva.getCliente().equalsIgnoreCase(nomeCliente) && 
               reserva.getStatus() == 'A') {
                reserva.setStatus('I');
                int dias = reserva.getDiaFim() - reserva.getDiaInicio() + 1;
                float totalDiarias = dias * reserva.getQuarto().getDiaria();
                
                return "Check-in realizado!\n" +
                       reserva.toString() + "\n" +
                       "Dias reservados: " + dias + "\n" +
                       "Total diárias: R$ " + totalDiarias;
            }
        }
        return "Nenhuma reserva ativa encontrada para o cliente: " + nomeCliente;
    }
    
    // 6. Realizar check-out
    public String realizarCheckOut(String nomeCliente) {
        for(Reserva reserva : reservas) {
            if(reserva.getCliente().equalsIgnoreCase(nomeCliente) && 
               reserva.getStatus() == 'I') {
                
                int dias = reserva.getDiaFim() - reserva.getDiaInicio() + 1;
                float totalDiarias = dias * reserva.getQuarto().getDiaria();
                float totalConsumo = reserva.getQuarto().valorTotalConsumo(produtos);
                float totalFinal = totalDiarias + totalConsumo;
                
                String resultado = "Check-out realizado!\n" +
                                  reserva.toString() + "\n" +
                                  "Dias reservados: " + dias + "\n" +
                                  "Total diárias: R$ " + totalDiarias + "\n" +
                                  "Consumos:\n" + reserva.getQuarto().listaConsumo(produtos) +
                                  "Total consumos: R$ " + totalConsumo + "\n" +
                                  "Total final: R$ " + totalFinal;
                
                reserva.setStatus('O');
                reserva.getQuarto().limpaConsumo();
                
                return resultado;
            }
        }
        return "Nenhum check-in ativo encontrado para o cliente: " + nomeCliente;
    }
    
    // 7. Registrar consumo
    public String registrarConsumo(String nomeCliente) {
        // Encontrar reserva em check-in
        Reserva reservaCheckIn = null;
        for(Reserva reserva : reservas) {
            if(reserva.getCliente().equalsIgnoreCase(nomeCliente) && 
               reserva.getStatus() == 'I') {
                reservaCheckIn = reserva;
                break;
            }
        }
        
        if(reservaCheckIn == null) {
            return "Nenhum check-in ativo encontrado para o cliente: " + nomeCliente;
        }
        
        // Mostrar produtos disponíveis
        StringBuilder sb = new StringBuilder();
        sb.append("Produtos disponíveis:\n");
        for(Produto produto : produtos) {
            sb.append(produto.toString()).append("\n");
        }
        
        return sb.toString();
    }
    
    public String adicionarConsumo(String nomeCliente, int codigoProduto) {
        // Encontrar reserva em check-in
        Reserva reservaCheckIn = null;
        for(Reserva reserva : reservas) {
            if(reserva.getCliente().equalsIgnoreCase(nomeCliente) && 
               reserva.getStatus() == 'I') {
                reservaCheckIn = reserva;
                break;
            }
        }
        
        if(reservaCheckIn == null) {
            return "Nenhum check-in ativo encontrado para o cliente: " + nomeCliente;
        }
        
        // Verificar se produto existe
        Produto produto = null;
        for(Produto p : produtos) {
            if(p.getCodigo() == codigoProduto) {
                produto = p;
                break;
            }
        }
        
        if(produto == null) {
            return "Produto não encontrado!";
        }
        
        reservaCheckIn.getQuarto().adicionaConsumo(codigoProduto);
        return "Consumo registrado: " + produto.getNome() + " - R$ " + produto.getValor();
    }
    
    // 8. Salvar dados
    public void salvarDados() {
        try {
            // Salvar quartos
            PrintWriter writer = new PrintWriter(new FileWriter("data/quarto.txt"));
            for(Quarto quarto : quartos) {
                writer.println(quarto.serializar());
            }
            writer.close();
            
            // Salvar reservas (apenas ativas e em check-in)
            writer = new PrintWriter(new FileWriter("data/reserva.txt"));
            for(Reserva reserva : reservas) {
                if(reserva.getStatus() == 'A' || reserva.getStatus() == 'I') {
                    writer.println(reserva.serializar());
                }
            }
            writer.close();
            
            // Salvar pousada
            writer = new PrintWriter(new FileWriter("data/pousada.txt"));
            writer.println(nome + "\t" + contato);
            writer.close();
            
            // Salvar produtos
            writer = new PrintWriter(new FileWriter("data/produto.txt"));
            for(Produto produto : produtos) {
                writer.println(produto.serializar());
            }
            writer.close();
            
            System.out.println("Dados salvos com sucesso!");
            
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }
    
    // Carregar dados
    public void carregarDados() {
        try {
            // Carregar produtos primeiro
            BufferedReader reader = new BufferedReader(new FileReader("data/produto.txt"));
            String linha;
            while((linha = reader.readLine()) != null) {
                Produto produto = Produto.deserializar(linha);
                if(produto != null) {
                    produtos.add(produto);
                }
            }
            reader.close();
            
            // Carregar quartos
            reader = new BufferedReader(new FileReader("data/quarto.txt"));
            while((linha = reader.readLine()) != null) {
                Quarto quarto = Quarto.deserializar(linha);
                if(quarto != null) {
                    quartos.add(quarto);
                }
            }
            reader.close();
            
            // Carregar pousada
            reader = new BufferedReader(new FileReader("data/pousada.txt"));
            if((linha = reader.readLine()) != null) {
                String[] partes = linha.split("\t");
                if(partes.length == 2) {
                    nome = partes[0];
                    contato = partes[1];
                }
            }
            reader.close();
            
            // Carregar reservas (último, pois precisa dos quartos)
            reader = new BufferedReader(new FileReader("data/reserva.txt"));
            while((linha = reader.readLine()) != null) {
                Reserva reserva = Reserva.deserializar(linha, quartos);
                if(reserva != null) {
                    reservas.add(reserva);
                }
            }
            reader.close();
            
            System.out.println("Dados carregados com sucesso!");
            
        } catch (IOException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage() + 
                             "\nIniciando com dados padrão...");
            inicializarDadosPadrao();
        }
    }
    
    // Inicializar dados padrão se arquivos não existirem
    private void inicializarDadosPadrao() {
        // Adicionar alguns quartos
        quartos.add(new Quarto(101, 'S', 150.0f));
        quartos.add(new Quarto(102, 'S', 150.0f));
        quartos.add(new Quarto(201, 'M', 250.0f));
        quartos.add(new Quarto(202, 'P', 350.0f));
        
        // Adicionar produtos
        produtos.add(new Produto(1, "Água Mineral", 5.0f));
        produtos.add(new Produto(2, "Refrigerante", 8.0f));
        produtos.add(new Produto(3, "Cerveja", 10.0f));
        produtos.add(new Produto(4, "Sanduíche", 15.0f));
        produtos.add(new Produto(5, "Chocolate", 7.0f));
    }
    
    // Método auxiliar para encontrar quarto
    private Quarto encontrarQuarto(int numero) {
        for(Quarto quarto : quartos) {
            if(quarto.getNumero() == numero) {
                return quarto;
            }
        }
        return null;
    }
}