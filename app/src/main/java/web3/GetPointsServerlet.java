package web3;
import jakarta.inject.Inject;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/getPoints")
public class GetPointsServerlet extends HttpServlet {

    @Inject
    private ControllerBean controllerBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        BigDecimal r = null;
        try {
            r = new BigDecimal(request.getParameter("r"));
        }catch (NumberFormatException e){
            return;
        }

        ArrayList<PointDTO> pointDTOS = new ArrayList<>();
        for(Point p : controllerBean.getPoints()){
            BigDecimal x = p.getX();
            BigDecimal y = p.getY();
            BigDecimal realR = p.getR();

            boolean isHit = MathFunctions.hitCheck(x, y, r);
            pointDTOS.add(new PointDTO(x,y, realR, isHit));

        }
        Gson gson = new Gson();

        PointsDTO pointsDTO = new PointsDTO(pointDTOS);

        String jsonResponse = gson.toJson(pointsDTO);
        response.getWriter().write(jsonResponse);
    }
}

