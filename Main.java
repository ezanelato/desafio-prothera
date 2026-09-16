import model.Funcionario;
import java.math.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.format.*;
import java.text.*;

public class Main {
    public static void main(String[] args) {
        // formatacao de data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        //formatacao de numero
        NumberFormat numberFormat = NumberFormat.getNumberInstance( new Locale("pt", "BR"));
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        // ------ INSERIR FUNCIONARIOS ------
        List<Funcionario> funcionarios = new ArrayList<>();
        funcionarios.add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), "Operador", new BigDecimal("2009.44")));
        funcionarios.add(new Funcionario("Joao", LocalDate.of(1990, 5, 12), "Operador", new BigDecimal("2284.38")));
        funcionarios.add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), "Coordenador", new BigDecimal("9836.14")));
        funcionarios.add(new Funcionario("Miguel", LocalDate.of(1998, 10, 14), "Diretor", new BigDecimal("19119.88")));
        funcionarios.add(new Funcionario("Alice", LocalDate.of(1995, 1, 5), "Recepcionista", new BigDecimal("2234.68")));
        funcionarios.add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), "Operador", new BigDecimal("1582.72")));
        funcionarios.add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), "Contador", new BigDecimal("4071.84")));
        funcionarios.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), "Gerente", new BigDecimal("3017.45")));
        funcionarios.add(new Funcionario("Heloisa", LocalDate.of(2003, 5, 24), "Eletricista", new BigDecimal("1605.85")));
        funcionarios.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), "Gerente", new BigDecimal("2799.93")));

        // ------ REMOVER JOAO ------
        funcionarios.removeIf(funcionario -> funcionario.getNome().equals("Joao"));
        
        // ------ MOSTRAR FUNCIONARIOS E INFORMACOES PESSOAIS ------
        for (Funcionario funcionario : funcionarios) {
            System.out.println("Nome: " + funcionario.getNome());
            System.out.println("Data de Nascimento: " + formatter.format(funcionario.getDataNascimento()));
            System.out.println("Funcao: " + funcionario.getFuncao());
            System.out.println("Salario: " + numberFormat.format(funcionario.getSalario()));
            System.out.println("--------------------------------");
        }

        // ------ FUNCIONARIOS RECEBEM AUMENTO DE 10% ------
        for (Funcionario funcionario : funcionarios) {
            funcionario.setSalario(funcionario.getSalario().multiply(new BigDecimal("1.10")));
        }

        // ------ MOSTRAR FUNCIONARIOS E INFORMACOES PESSOAIS ------
        for (Funcionario funcionario : funcionarios) {
            System.out.println("Nome: " + funcionario.getNome());
            System.out.println("Salario: " + numberFormat.format(funcionario.getSalario()));
            System.out.println("--------------------------------");
        }

        // ------ MAP FUNCIONARIOS POR FUNCAO ------
        Map<String, List<Funcionario>> funcionariosPorFuncao = funcionarios.stream()
            .collect(Collectors.groupingBy(Funcionario::getFuncao));
        //mostrar funcionarios por funcao
        for (Map.Entry<String, List<Funcionario>> entry : funcionariosPorFuncao.entrySet()) {
            System.out.println("Funcao: " + entry.getKey());
            for (Funcionario funcionario : entry.getValue()) {
                System.out.println("Nome: " + funcionario.getNome());
            }
            System.out.println("--------------------------------");
        }

        // ------ MOSTRAR FUNCIONARIOS POR ANIVERSARIANTES ENTRE OUTUBRO E DEZEMBRO ------
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getDataNascimento().getMonthValue() >= 10 && funcionario.getDataNascimento().getMonthValue() <= 12) {
                System.out.println("Aniversariantes de outubro e dezembro:");
                System.out.println("Nome: " + funcionario.getNome());
                System.out.println("Data de Nascimento: " + formatter.format(funcionario.getDataNascimento()));
                System.out.println("--------------------------------");
            }
        }

        // ------ MOSTRAR IDADE MAIOR PARA MENOR ------
        funcionarios.sort(Comparator.comparing(Funcionario::getDataNascimento));
        for (Funcionario funcionario : funcionarios) {
            System.out.println("Nome: " + funcionario.getNome());
            // idade = data atual - data de nascimento
            System.out.println("Idade: " + (LocalDate.now().getYear() - funcionario.getDataNascimento().getYear()));
            System.out.println("--------------------------------");
        }
        
        // ------ MOSTRAR FUNCIONARIOS POR ORDEM ALFABETICA ------
        funcionarios.sort(Comparator.comparing(Funcionario::getNome));
        for (Funcionario funcionario : funcionarios) {
            System.out.println("Nome: " + funcionario.getNome());
            System.out.println("--------------------------------");
        }

        // ------ SOMA DOS SALARIOS ------
        BigDecimal somaDosSalarios = funcionarios.stream()
            .map(Funcionario::getSalario)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Soma dos salarios: " + numberFormat.format(somaDosSalarios));
        System.out.println("--------------------------------");

        // ------ EQUIVALENCIA DO SALARIO MINIMO ------
        BigDecimal salarioMinimo = new BigDecimal("1212.00");
        for (Funcionario funcionario : funcionarios) {
            System.out.println("Nome: " + funcionario.getNome());
            System.out.println("Salario: " + numberFormat.format(funcionario.getSalario()));
            
            //equivalencia
            BigDecimal equivalencia = funcionario.getSalario().divide(salarioMinimo, 2, RoundingMode.HALF_UP);
            System.out.println("Equivalencia: " + equivalencia+"x");
            System.out.println("--------------------------------");
        }
    }
}