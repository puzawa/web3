package web3;

import com.google.gson.Gson;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashCode;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
public class GetPointsServlet extends HttpServlet {

    @Inject
    private ControllerBean controllerBean;

    @Inject
    private Provider<CheckboxView> checkboxView;

    private String getJsonResponse(HttpServletResponse response,
                                   ArrayList<PointDTO> pointDTOS,
                                   BigDecimal maxR,
                                   ArrayList<BigDecimal> enabledR) throws IOException {

        Gson gson = new Gson();

        GraphResponse graphResponse = new GraphResponse(
                pointDTOS,
                maxR,
                enabledR
        );
      return gson.toJson(graphResponse);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ArrayList<BigDecimal> enabledR = checkboxView.get().getEnabledR();
        if (enabledR.isEmpty()) {
            String jsonResponse = getJsonResponse(response, new ArrayList<>(), BigDecimal.ZERO, new ArrayList<>());
            response.getWriter().write(jsonResponse);

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
        String jsonResponse = getJsonResponse(response, pointDTOS, maxR, enabledR);
        response.getWriter().write(jsonResponse);
//        HashCode jsonHash = Hashing.murmur3_128().hashUnencodedChars(jsonResponse);
//        HttpSession session = request.getSession(true);
//        Long previousHash = (Long) session.getAttribute("jsonHash");
//        if (previousHash != null && previousHash.equals(jsonHash.asLong())) {
//           // response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
//        } else {
//            session.setAttribute("jsonHash", jsonHash.asLong());
//
//        }
    }
}

