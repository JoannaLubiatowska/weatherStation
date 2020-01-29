package controller;

import model.entity.WeatherCondition;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<WeatherCondition> weatherCondition = loadWeatherConditionData();
//        ClassificationProblem problem = DataNormalizationProcesor.getClassification(weatherCondition);
//
//        BayesClassifier classifier = new BayesClassifier();
//        classifier.configure(problem.getTrainingSet());
//        System.out.println(classifier);
//
//        problem.getTestSet().forEach(classifier::classify);
//        System.out.println(problem.getTestSet());
    }

    private static List<WeatherCondition> loadWeatherConditionData() {
        List<WeatherCondition> weatherCondition;
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("weatherStationMavenWebapp");
        EntityManager em = factory.createEntityManager();
        weatherCondition = em.createQuery("SELECT d FROM WeatherCondition d", WeatherCondition.class).getResultList();
        weatherCondition.forEach(System.out::println);
        em.close();
        return weatherCondition;
    }
}
