import java.util.Scanner;

public class Main {
    private static Pousada pousada;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        pousada = new Pousada("Pousada do Vale", "(51) 99999-9999");
        
        System.out.println("=== SISTEMA DE GERENCIAMENTO DE POUSADA ===");
        pousada.carregarDados();
        
        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Digite a opção desejada: ");
            processarOpcao(opcao);
        } while(opcao != 0);
        
        scanner.close();
    }
    
    private static void exibirMenu() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1 - Consultar disponibilidade");
        System.out.println("2 - Consultar reserva");
        System.out.println("3 - Realizar reserva");
        System.out.println("4 - Cancelar reserva");
        System.out.println("5 - Realizar check-in");
        System.out.println("6 - Realizar check-out");
        System.out.println("7 - Registrar consumo");
        System.out.println("8 - Salvar dados");
        System.out.println("0 - Sair");
    }
    
    private static void processarOpcao(int opcao) {
        switch(opcao) {
            case 1:
                consultarDisponibilidade();
                break;
            case 2:
                consultarReserva();
                break;
            case 3:
                realizarReserva();
                break;
            case 4:
                cancelarReserva();
                break;
            case 5:
                realizarCheckIn();
                break;
            case 6:
                realizarCheckOut();
                break;
            case 7:
                registrarConsumo();
                break;
            case 8:
                pousada.salvarDados();
                break;
            case 0:
                System.out.println("Encerrando sistema...");
                pousada.salvarDados(); // Salva automaticamente ao sair
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }
    
    private static void consultarDisponibilidade() {
        System.out.println("\n--- CONSULTAR DISPONIBILIDADE ---");
        int data = lerInteiro("Digite a data (número do dia): ");
        int numeroQuarto = lerInteiro("Digite o número do quarto: ");
        
        String resultado = pousada.consultarDisponibilidade(data, numeroQuarto);
        System.out.println(resultado);
    }
    
    private static void consultarReserva() {
        System.out.println("\n--- CONSULTAR RESERVA ---");
        System.out.println("Deixe em branco os campos que não deseja filtrar");
        
        Integer data = lerInteiroOpcional("Digite a data (número do dia): ");
        String nomeCliente = lerStringOpcional("Digite o nome do cliente: ");
        Integer numeroQuarto = lerInteiroOpcional("Digite o número do quarto: ");
        
        String resultado = pousada.consultarReserva(data, nomeCliente, numeroQuarto);
        System.out.println(resultado);
    }
    
    private static void realizarReserva() {
        System.out.println("\n--- REALIZAR RESERVA ---");
        int diaInicio = lerInteiro("Digite o dia de início: ");
        int diaFim = lerInteiro("Digite o dia de fim: ");
        String cliente = lerString("Digite o nome do cliente: ");
        int numeroQuarto = lerInteiro("Digite o número do quarto: ");
        
        String resultado = pousada.realizarReserva(diaInicio, diaFim, cliente, numeroQuarto);
        System.out.println(resultado);
    }
    
    private static void cancelarReserva() {
        System.out.println("\n--- CANCELAR RESERVA ---");
        String nomeCliente = lerString("Digite o nome do cliente: ");
        
        String resultado = pousada.cancelarReserva(nomeCliente);
        System.out.println(resultado);
    }
    
    private static void realizarCheckIn() {
        System.out.println("\n--- REALIZAR CHECK-IN ---");
        String nomeCliente = lerString("Digite o nome do cliente: ");
        
        String resultado = pousada.realizarCheckIn(nomeCliente);
        System.out.println(resultado);
    }
    
    private static void realizarCheckOut() {
        System.out.println("\n--- REALIZAR CHECK-OUT ---");
        String nomeCliente = lerString("Digite o nome do cliente: ");
        
        String resultado = pousada.realizarCheckOut(nomeCliente);
        System.out.println(resultado);
    }
    
    private static void registrarConsumo() {
        System.out.println("\n--- REGISTRAR CONSUMO ---");
        String nomeCliente = lerString("Digite o nome do cliente: ");
        
        // Mostrar produtos disponíveis
        String produtos = pousada.registrarConsumo(nomeCliente);
        System.out.println(produtos);
        
        if(!produtos.contains("Nenhum check-in")) {
            int codigoProduto = lerInteiro("Digite o código do produto: ");
            String resultado = pousada.adicionarConsumo(nomeCliente, codigoProduto);
            System.out.println(resultado);
        }
    }
    
    // Métodos auxiliares para leitura de dados
    private static int lerInteiro(String mensagem) {
        while(true) {
            try {
                System.out.print(mensagem);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido!");
            }
        }
    }
    
    private static Integer lerInteiroOpcional(String mensagem) {
        System.out.print(mensagem);
        String input = scanner.nextLine();
        if(input.isEmpty()) return null;
        
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido, ignorando filtro...");
            return null;
        }
    }
    
    private static String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }
    
    private static String lerStringOpcional(String mensagem) {
        System.out.print(mensagem);
        String input = scanner.nextLine();
        return input.isEmpty() ? null : input;
    }
}