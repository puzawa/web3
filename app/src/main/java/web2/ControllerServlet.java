package web2;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String ERROR_MESSAGE = "Controller error: %s";

        String contentType = request.getContentType();
        if (contentType == null || !contentType.contains("application/json")) {
            request.setAttribute("error", String.format(ERROR_MESSAGE, "Unexpected content type"));
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        int contentLength = request.getContentLength();
        if (contentLength <= 0) {
            request.setAttribute("error", String.format(ERROR_MESSAGE, "Request body is empty"));
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        System.out.println("to checkArea");
        request.getRequestDispatcher("/checkArea").forward(request, response);

    }
}
