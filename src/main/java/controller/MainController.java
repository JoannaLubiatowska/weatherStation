package controller;

import model.hibernate.entity.WeatherCondition;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    private static List<WeatherCondition> weatherCondition;
    public static void main(String[] args) {
        EntityManager entityManager = createEntityManager();
        weatherCondition = loadWeatherConditionData(entityManager);
        ClassificationProblem problem = DataNormalizationProcessor.getClassification(weatherCondition);

        BayesClassifier classifier = new BayesClassifier();
        classifier.configure(problem.getTrainingSet());
        System.out.println(classifier);

        problem.getTestSet().forEach(classifier::classify);
        System.out.println(problem.getTestSet());
    }

    private static void refreshData(EntityManager entityManager) {
        List<WeatherCondition> refreshedWeatherCondition = loadWeatherConditionData(entityManager);
        //entityManager.getTransaction().
        if(refreshedWeatherCondition.size() > weatherCondition.size()) {

        }
    }

    private static List<WeatherCondition> loadWeatherConditionData(EntityManager entityManager) {
        List<WeatherCondition> weatherCondition = new ArrayList<>();
        weatherCondition = entityManager.createQuery("SELECT d FROM WeatherCondition d", WeatherCondition.class).getResultList();
        //weatherCondition.forEach(System.out::println);
        entityManager.close();
        return weatherCondition;
    }
    private static EntityManager createEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("weatherStationMavenWebapp");
        return factory.createEntityManager();
    }
}
