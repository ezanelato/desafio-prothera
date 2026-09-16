import model.Funcionario;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    private final DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final NumberFormat formatoNumero;
    private final BigDecimal salarioMinimo = new BigDecimal("1212.00");

    private final List<Funcionario> lista;
    private final List<Funcionario> comAumento;
    private final Map<String, List<Funcionario>> porFuncao;
    private final List<Funcionario> aniversariantes;
    private final List<Funcionario> porIdade;
    private final List<Funcionario> alfabetica;
    private final BigDecimal somaDosSalarios;

    private Main() {
        // formatacao de numero (data ja fica pronta no atributo formatoData la em cima)
        formatoNumero = NumberFormat.getNumberInstance(Locale.forLanguageTag("pt-BR"));
        formatoNumero.setMinimumFractionDigits(2);
        formatoNumero.setMaximumFractionDigits(2);

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
        this.lista = copiar(funcionarios); // guarda a lista aqui pra sobrar pro imprimirConsole/gerarJson usarem depois

        // ------ FUNCIONARIOS RECEBEM AUMENTO DE 10% ------
        for (Funcionario funcionario : funcionarios) {
            funcionario.setSalario(funcionario.getSalario().multiply(new BigDecimal("1.10")));
        }
        this.comAumento = copiar(funcionarios);

        // ------ MAP FUNCIONARIOS POR FUNCAO ------
        this.porFuncao = funcionarios.stream()
            .collect(Collectors.groupingBy(Funcionario::getFuncao, LinkedHashMap::new, Collectors.toList()));

        // ------ FUNCIONARIOS ANIVERSARIANTES ENTRE OUTUBRO E DEZEMBRO ------
        this.aniversariantes = funcionarios.stream()
            .filter(f -> f.getDataNascimento().getMonthValue() >= 10 && f.getDataNascimento().getMonthValue() <= 12)
            .collect(Collectors.toList());

        // ------ ORDENAR POR IDADE (MAIOR PARA MENOR) ------
        this.porIdade = copiar(funcionarios);
        this.porIdade.sort(Comparator.comparing(Funcionario::getDataNascimento));

        // ------ ORDENAR POR ORDEM ALFABETICA ------
        this.alfabetica = copiar(funcionarios);
        this.alfabetica.sort(Comparator.comparing(Funcionario::getNome));

        // ------ SOMA DOS SALARIOS ------
        this.somaDosSalarios = funcionarios.stream()
            .map(Funcionario::getSalario)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static Main calcular() {
        return new Main();
    }

    public void imprimirConsole() {
        // ------ MOSTRAR FUNCIONARIOS E INFORMACOES PESSOAIS ------
        for (Funcionario funcionario : lista) {
            System.out.println("Nome: " + funcionario.getNome());
            System.out.println("Data de Nascimento: " + formatoData.format(funcionario.getDataNascimento()));
            System.out.println("Funcao: " + funcionario.getFuncao());
            System.out.println("Salario: " + formatoNumero.format(funcionario.getSalario()));
            System.out.println("--------------------------------");
        }

        // ------ MOSTRAR FUNCIONARIOS COM AUMENTO DE 10% ------
        for (Funcionario funcionario : comAumento) {
            System.out.println("Nome: " + funcionario.getNome());
            System.out.println("Salario: " + formatoNumero.format(funcionario.getSalario()));
            System.out.println("--------------------------------");
        }

        // ------ MOSTRAR FUNCIONARIOS POR FUNCAO ------
        for (Map.Entry<String, List<Funcionario>> entry : porFuncao.entrySet()) {
            System.out.println("Funcao: " + entry.getKey());
            for (Funcionario funcionario : entry.getValue()) {
                System.out.println("Nome: " + funcionario.getNome());
            }
            System.out.println("--------------------------------");
        }

        // ------ MOSTRAR FUNCIONARIOS ANIVERSARIANTES ENTRE OUTUBRO E DEZEMBRO ------
        for (Funcionario funcionario : aniversariantes) {
            System.out.println("Aniversariantes de outubro e dezembro:");
            System.out.println("Nome: " + funcionario.getNome());
            System.out.println("Data de Nascimento: " + formatoData.format(funcionario.getDataNascimento()));
            System.out.println("--------------------------------");
        }

        // ------ MOSTRAR IDADE MAIOR PARA MENOR ------
        for (Funcionario funcionario : porIdade) {
            System.out.println("Nome: " + funcionario.getNome());
            // idade = data atual - data de nascimento
            System.out.println("Idade: " + idade(funcionario));
            System.out.println("--------------------------------");
        }

        // ------ MOSTRAR FUNCIONARIOS POR ORDEM ALFABETICA ------
        for (Funcionario funcionario : alfabetica) {
            System.out.println("Nome: " + funcionario.getNome());
            System.out.println("--------------------------------");
        }

        // ------ SOMA DOS SALARIOS ------
        System.out.println("Soma dos salarios: " + formatoNumero.format(somaDosSalarios));
        System.out.println("--------------------------------");

        // ------ EQUIVALENCIA DO SALARIO MINIMO ------
        for (Funcionario funcionario : alfabetica) {
            System.out.println("Nome: " + funcionario.getNome());
            System.out.println("Salario: " + formatoNumero.format(funcionario.getSalario()));
            // equivalencia
            System.out.println("Equivalencia: " + equivalencia(funcionario) + "x");
            System.out.println("--------------------------------");
        }
    }

    public String gerarJson() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"lista\":").append(listaCompletaJson(lista)).append(",");
        json.append("\"aumento\":").append(salariosJson(comAumento)).append(",");
        json.append("\"porFuncao\":").append(porFuncaoJson()).append(",");
        json.append("\"aniversariantes\":").append(aniversariantesJson()).append(",");
        json.append("\"porIdade\":").append(idadeJson()).append(",");
        json.append("\"alfabetica\":").append(nomesJson(alfabetica)).append(",");
        json.append("\"somaSalarios\":").append(texto(formatoNumero.format(somaDosSalarios))).append(",");
        json.append("\"salarioMinimo\":").append(texto(formatoNumero.format(salarioMinimo))).append(",");
        json.append("\"equivalencia\":").append(equivalenciaJson());
        json.append("}");
        return json.toString();
    }

    private List<Funcionario> copiar(List<Funcionario> origem) {
        List<Funcionario> copia = new ArrayList<>();
        for (Funcionario funcionario : origem) {
            copia.add(new Funcionario(
                funcionario.getNome(),
                funcionario.getDataNascimento(),
                funcionario.getFuncao(),
                funcionario.getSalario()
            ));
        }
        return copia;
    }

    private int idade(Funcionario funcionario) {
        return LocalDate.now().getYear() - funcionario.getDataNascimento().getYear();
    }

    private BigDecimal equivalencia(Funcionario funcionario) {
        return funcionario.getSalario().divide(salarioMinimo, 2, RoundingMode.HALF_UP);
    }

    private String listaCompletaJson(List<Funcionario> funcionarios) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < funcionarios.size(); i++) {
            Funcionario f = funcionarios.get(i);
            if (i > 0) {
                json.append(",");
            }
            json.append("{")
                .append("\"nome\":").append(texto(f.getNome())).append(",")
                .append("\"dataNascimento\":").append(texto(formatoData.format(f.getDataNascimento()))).append(",")
                .append("\"funcao\":").append(texto(f.getFuncao())).append(",")
                .append("\"salario\":").append(texto(formatoNumero.format(f.getSalario())))
                .append("}");
        }
        json.append("]");
        return json.toString();
    }

    private String salariosJson(List<Funcionario> funcionarios) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < funcionarios.size(); i++) {
            Funcionario f = funcionarios.get(i);
            if (i > 0) {
                json.append(",");
            }
            json.append("{")
                .append("\"nome\":").append(texto(f.getNome())).append(",")
                .append("\"salario\":").append(texto(formatoNumero.format(f.getSalario())))
                .append("}");
        }
        json.append("]");
        return json.toString();
    }

    private String porFuncaoJson() {
        StringBuilder json = new StringBuilder("[");
        int i = 0;
        for (Map.Entry<String, List<Funcionario>> entry : porFuncao.entrySet()) {
            if (i++ > 0) {
                json.append(",");
            }
            json.append("{")
                .append("\"funcao\":").append(texto(entry.getKey())).append(",")
                .append("\"nomes\":").append(nomesJson(entry.getValue()))
                .append("}");
        }
        json.append("]");
        return json.toString();
    }

    private String aniversariantesJson() {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < aniversariantes.size(); i++) {
            Funcionario f = aniversariantes.get(i);
            if (i > 0) {
                json.append(",");
            }
            json.append("{")
                .append("\"nome\":").append(texto(f.getNome())).append(",")
                .append("\"dataNascimento\":").append(texto(formatoData.format(f.getDataNascimento())))
                .append("}");
        }
        json.append("]");
        return json.toString();
    }

    private String idadeJson() {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < porIdade.size(); i++) {
            Funcionario f = porIdade.get(i);
            if (i > 0) {
                json.append(",");
            }
            json.append("{")
                .append("\"nome\":").append(texto(f.getNome())).append(",")
                .append("\"idade\":").append(idade(f))
                .append("}");
        }
        json.append("]");
        return json.toString();
    }

    private String nomesJson(List<Funcionario> funcionarios) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < funcionarios.size(); i++) {
            if (i > 0) {
                json.append(",");
            }
            json.append(texto(funcionarios.get(i).getNome()));
        }
        json.append("]");
        return json.toString();
    }

    private String equivalenciaJson() {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < alfabetica.size(); i++) {
            Funcionario f = alfabetica.get(i);
            if (i > 0) {
                json.append(",");
            }
            json.append("{")
                .append("\"nome\":").append(texto(f.getNome())).append(",")
                .append("\"salario\":").append(texto(formatoNumero.format(f.getSalario()))).append(",")
                .append("\"equivalencia\":").append(texto(equivalencia(f) + "x"))
                .append("}");
        }
        json.append("]");
        return json.toString();
    }

    private String texto(String valor) {
        return "\"" + valor.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    public static void main(String[] args) throws IOException {
        Main relatorio = Main.calcular();
        relatorio.imprimirConsole();

        int porta = 8080;
        String portaAmbiente = System.getenv("PORT");
        if (portaAmbiente != null && !portaAmbiente.isEmpty()) {
            porta = Integer.parseInt(portaAmbiente);
        }

        Servidor.iniciar(porta, relatorio.gerarJson());
    }
}
