package web2;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static web2.MathFunctions.*;
import static web2.Validator.*;

@WebServlet("/checkArea")
public class AreaCheckServlet extends HttpServlet {


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String ERROR_MESSAGE = "Cause: %s";
        BigDecimal x;
        BigDecimal y;
        BigDecimal r;

        try {

            HashMap<String, String> valuesMap = RequestParser.parseRequest(request);
            var rawX = valuesMap.get("x");
            var rawY = valuesMap.get("y");
            var rawR = valuesMap.get("r");

            if (rawX == null || rawY == null || rawR == null) {
                request.setAttribute("error", String.format(ERROR_MESSAGE, "Values are required"));
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            x = new BigDecimal(rawX);
            y = new BigDecimal(rawY);
            r = new BigDecimal(rawR);

        }
        catch (IllegalArgumentException e) {
            request.setAttribute("error", String.format(ERROR_MESSAGE, "Unexpected inputs"));
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }


        StringBuilder builder = new StringBuilder();
        var xCheck = checkX(x);
        var yCheck = checkY(y);
        var rCheck = checkR(r);

        if (!xCheck)
            builder.append(String.format(ERROR_MESSAGE, "Unexpected X value\n"));
        if (!yCheck)
            builder.append(String.format(ERROR_MESSAGE, "Unexpected Y value\n"));
        if (!rCheck)
            builder.append(String.format(ERROR_MESSAGE, "Unexpected R value\n"));

        if (!xCheck || !yCheck || !rCheck) {
            request.setAttribute("error", builder.toString());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        var hitResult = hitCheck(x, y, r);

        var newRow = new TableRow(x, y, r, hitResult);
        PointsBean pointsBean = (PointsBean) request.getSession().getAttribute("pointsBean");
        if (pointsBean == null) {
            pointsBean = new PointsBean();
            request.getSession().setAttribute("pointsBean", pointsBean);
        }
        pointsBean.add(newRow);

        request.setAttribute("newRow", newRow);

        request.getRequestDispatcher("/results.jsp").forward(request, response);
    }
}
//TODO
/*
* Сохранять последний R
* Отображать все точки, попал зеленый / мимо красный
* пользователь вводит не правильно, нужно чтоб включалась запись вебкамеры и отправлялась на гелиус видос пока не будет правильно, пока не попадет точнее, если нажал куда то на экран тоже мимо
*
*
* */