import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

public class Servidor {
    public static void iniciar(int porta, String jsonResultados) throws IOException {
        HttpServer servidor = HttpServer.create(new InetSocketAddress("0.0.0.0", porta), 0);
        Path pastaWeb = Paths.get("web").toAbsolutePath();

        servidor.createContext("/api/resultados", (HttpExchange troca) -> {
            if (!"GET".equalsIgnoreCase(troca.getRequestMethod())) {
                enviar(troca, 405, "text/plain; charset=utf-8", "Metodo nao permitido");
                return;
            }
            enviar(troca, 200, "application/json; charset=utf-8", jsonResultados);
        });

        servidor.createContext("/", (HttpExchange troca) -> {
            if (!"GET".equalsIgnoreCase(troca.getRequestMethod())) {
                enviar(troca, 405, "text/plain; charset=utf-8", "Metodo nao permitido");
                return;
            }

            String caminhoPedido = troca.getRequestURI().getPath();
            if (caminhoPedido == null || "/".equals(caminhoPedido)) {
                caminhoPedido = "/index.html";
            }

            Path arquivo = pastaWeb.resolve(caminhoPedido.substring(1)).normalize();
            if (!arquivo.startsWith(pastaWeb) || !Files.isRegularFile(arquivo)) {
                enviar(troca, 404, "text/plain; charset=utf-8", "Arquivo nao encontrado");
                return;
            }

            byte[] bytes = Files.readAllBytes(arquivo);
            troca.getResponseHeaders().set("Content-Type", tipoConteudo(arquivo.getFileName().toString()));
            troca.sendResponseHeaders(200, bytes.length);
            try (OutputStream saida = troca.getResponseBody()) {
                saida.write(bytes);
            }
        });

        servidor.setExecutor(Executors.newCachedThreadPool());
        servidor.start();
        System.out.println("Interface em http://localhost:" + porta);
    }

    private static void enviar(HttpExchange troca, int status, String tipo, String corpo) throws IOException {
        byte[] bytes = corpo.getBytes(StandardCharsets.UTF_8);
        troca.getResponseHeaders().set("Content-Type", tipo);
        troca.sendResponseHeaders(status, bytes.length);
        try (OutputStream saida = troca.getResponseBody()) {
            saida.write(bytes);
        }
    }

    private static String tipoConteudo(String nome) {
        if (nome.endsWith(".html")) {
            return "text/html; charset=utf-8";
        }
        if (nome.endsWith(".css")) {
            return "text/css; charset=utf-8";
        }
        if (nome.endsWith(".js")) {
            return "application/javascript; charset=utf-8";
        }
        return "application/octet-stream";
    }
}
