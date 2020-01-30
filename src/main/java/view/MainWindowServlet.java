package view;
import controller.DataController;
import model.hibernate.entity.InferenceResult;
import model.hibernate.entity.NormalizedWeatherCondition;
import model.hibernate.entity.WeatherCondition;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/mainWindowServlet")
public class MainWindowServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Set<NormalizedWeatherCondition> normalizedWeatherConditionSet = DataController.init();
        for (NormalizedWeatherCondition normalizedWeatherCondition : normalizedWeatherConditionSet) {
            response.getWriter().write(String.valueOf(normalizedWeatherCondition));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            WeatherCondition weatherCondition = fillNewWeatherCondition(request);
            HttpSession session = request.getSession(true);
            session.setAttribute("weatherCondition", weatherCondition);
            response.sendRedirect(".");
        } catch (Exception e) {
            request.setAttribute("message", e.getMessage());
            getServletContext().getRequestDispatcher("/").forward(request, response);
        }
    }

    private WeatherCondition fillNewWeatherCondition(HttpServletRequest request) {
    return new WeatherCondition(Timestamp.valueOf(request.getParameter("measurementTime").toString()),
                Double.parseDouble(request.getParameter("temperature").toString()),
                Double.parseDouble(request.getParameter("airHumidity").toString()),
                Double.parseDouble(request.getParameter("airPressure").toString()));
    }
}
