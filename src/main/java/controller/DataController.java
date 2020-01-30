package controller;

import model.hibernate.entity.NormalizedWeatherCondition;
import model.hibernate.entity.WeatherCondition;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataController {

//    public static void main(String[] args) {
//        EntityManager entityManager = createEntityManager();
//        weatherCondition = loadWeatherConditionData(entityManager);
//        ClassificationProblem problem = DataNormalizationProcessor.getClassification(weatherCondition);
//
//        BayesClassifier classifier = new BayesClassifier();
//        classifier.configure(problem.getTrainingSet());
//        System.out.println(classifier);
//
//        problem.getTestSet().forEach(classifier::classify);
//        System.out.println(problem.getTestSet());
//    }

    public static Set<NormalizedWeatherCondition> init() {
        EntityManager entityManager = createEntityManager();
        List<WeatherCondition> weatherCondition = loadWeatherConditionData(entityManager);
        ClassificationProblem problem = DataNormalizationProcessor.getClassification(weatherCondition);
        BayesClassifier classifier = new BayesClassifier();
        classifier.configure(problem.getTrainingSet());
        problem.getTestSet().forEach(classifier::classify);
        return problem.getTestSet();
    }

    private static void insertWeatherCondition(EntityManager entityManager, WeatherCondition weatherCondition) {
        entityManager.getTransaction().begin();
        entityManager.persist(weatherCondition);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private static List<WeatherCondition> loadWeatherConditionData(EntityManager entityManager) {
        List<WeatherCondition> weatherCondition = new ArrayList<>();
        weatherCondition = entityManager.createQuery("SELECT d FROM WeatherCondition d", WeatherCondition.class).getResultList();
        entityManager.close();
        return weatherCondition;
    }
    private static EntityManager createEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("weatherStationMavenWebapp");
        return factory.createEntityManager();
    }
}
