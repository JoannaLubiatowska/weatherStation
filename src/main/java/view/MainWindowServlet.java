package view;

import com.google.firebase.messaging.FirebaseMessagingException;
import controller.DataController;
import model.hibernate.entity.WeatherCondition;
import notifier.NotifierService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@WebServlet("/mainWindowServlet")
public class MainWindowServlet extends HttpServlet {

    private NotifierService notifierService = new NotifierService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        runClassification(response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WeatherCondition weatherCondition = insertToDbNewWeatherCondition(request);
        response.getWriter().write("Received: " + weatherCondition);
        runClassification(response);
        try {
            notifierService.notifyLastConditionBefore(new Date());
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    private WeatherCondition insertToDbNewWeatherCondition(HttpServletRequest request) {
        WeatherCondition weatherCondition = new WeatherCondition(Timestamp.valueOf(request.getParameter("measurementTime")),
                Double.parseDouble(request.getParameter("temperature")),
                Double.parseDouble(request.getParameter("airHumidity")),
                Double.parseDouble(request.getParameter("airPressure")));
        DataController.insertWeatherCondition(weatherCondition);
        return weatherCondition;
    }

    private void runClassification(HttpServletResponse response) throws IOException {
        List<WeatherCondition> weatherConditionList = DataController.init();
        if(weatherConditionList.size() > 0) {
            for (WeatherCondition weatherCondition : weatherConditionList) {
                response.getWriter().write("Classified and saved to Database: " + String.valueOf(weatherCondition));
            }
        } else {
            response.getWriter().write("Server is working. No new data to process.");
        }
    }
}
