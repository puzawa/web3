package web3;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import web3.point.GraphResponse;
import web3.point.Point;
import web3.point.PointDTO;
import web3.util.MathFunctions;
import web3.view.CheckboxView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

@WebServlet("/getPoints")
public class GetPointsServerlet extends HttpServlet {

    @Inject
    private ControllerBean controllerBean;

    @Inject
    private Provider<CheckboxView> checkboxView;

    private void writeJsonResponse(HttpServletResponse response,
                                   ArrayList<PointDTO> pointDTOS,
                                   BigDecimal maxR,
                                   ArrayList<BigDecimal> enabledR) throws IOException {

        Gson gson = new Gson();

        GraphResponse graphResponse = new GraphResponse(
                pointDTOS,
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
            writeJsonResponse(response, new ArrayList<>(), BigDecimal.ZERO, new ArrayList<>());
            return;
        }

        BigDecimal maxR = enabledR.get(0);

        ArrayList<PointDTO> pointDTOS = new ArrayList<>();
        for (Point p : controllerBean.getPoints()) {

            BigDecimal x = p.getX();
            BigDecimal y = p.getY();
            BigDecimal realR = p.getR();
            if (!enabledR.contains(realR.stripTrailingZeros()))
                continue;

            boolean isHit = MathFunctions.hitCheck(x, y, maxR);
            pointDTOS.add(new PointDTO(x, y, realR, isHit));

        }

        Collections.reverse(pointDTOS);
        writeJsonResponse(response, pointDTOS, maxR, enabledR);
    }
}

