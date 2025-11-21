package web3;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@WebServlet("/sse")
public class SseServlet extends HttpServlet {
    private static final Set<PrintWriter> clients = new CopyOnWriteArraySet<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();
        clients.add(writer);

        try {
            while (!Thread.interrupted()) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
        } finally {
            clients.remove(writer);
        }
    }

    public static void broadcast(String message) {
        for (PrintWriter client : clients) {
            client.println("data: " + message + "\n");
            client.flush();
        }
    }
}

