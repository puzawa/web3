package web3;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import web3.util.MathFunctions;
import web3.view.CheckboxView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/getPoints")
public class GetPointsServerlet extends HttpServlet {

    @Inject
    private ControllerBean controllerBean;

    @Inject
    private CheckboxView checkboxView;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<BigDecimal> enabledR = checkboxView.getEnabledR();
        if(enabledR.isEmpty())
            return;

        BigDecimal maxR =enabledR.get(0);

        ArrayList<PointDTO> pointDTOS = new ArrayList<>();
        for(Point p : controllerBean.getPoints()){

            BigDecimal x = p.getX();
            BigDecimal y = p.getY();
            BigDecimal realR = p.getR();
            if(!enabledR.contains(realR.stripTrailingZeros()))
                continue;

            boolean isHit = MathFunctions.hitCheck(x, y, maxR);
            pointDTOS.add(new PointDTO(x,y, realR, isHit));

        }
        Gson gson = new Gson();

        GraphResponse graphResponse = new GraphResponse(pointDTOS,maxR);

        String jsonResponse = gson.toJson(graphResponse);
        response.getWriter().write(jsonResponse);
    }
}

