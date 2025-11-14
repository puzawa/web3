package web3;

import com.google.gson.Gson;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web3.point.GraphResponse;
import web3.view.CheckboxView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

@WebServlet("/getRs")
public class GetRsServlet extends HttpServlet {

    @Inject
    private ControllerBean controllerBean;

    @Inject
    private Provider<CheckboxView> checkboxView;

    private void writeJsonResponse(HttpServletResponse response,
                                   BigDecimal maxR,
                                   ArrayList<BigDecimal> enabledR) throws IOException {

        Gson gson = new Gson();

        GraphResponse graphResponse = new GraphResponse(
                null,
                maxR,
                enabledR
        );
        String jsonResponse = gson.toJson(graphResponse);
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ArrayList<BigDecimal> enabledR = checkboxView.get().getEnabledR();
        if (enabledR.isEmpty()) {
            writeJsonResponse(response, BigDecimal.ZERO, new ArrayList<>());
            return;
        }

        BigDecimal maxR = enabledR.get(0);
        writeJsonResponse(response, maxR, enabledR);
    }
}

