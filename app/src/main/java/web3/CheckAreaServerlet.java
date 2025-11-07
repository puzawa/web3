package web3;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import web3.util.MathFunctions;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/checkArea")
public class CheckAreaServerlet extends HttpServlet {

    private static class HitRequest {
        BigDecimal x;
        BigDecimal y;
        BigDecimal r;
    }

    private static class HitResponse {
        boolean hit;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        Gson gson = new Gson();
        HitRequest hitRequest = gson.fromJson(sb.toString(), HitRequest.class);

        boolean isHit = MathFunctions.hitCheck(hitRequest.x, hitRequest.y, hitRequest.r);

        HitResponse hitResponse = new HitResponse();
        hitResponse.hit = isHit;

        String jsonResponse = gson.toJson(hitResponse);
        response.getWriter().write(jsonResponse);
    }
}
