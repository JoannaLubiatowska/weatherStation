package controller;

import model.hibernate.entity.InferenceResult;
import model.hibernate.entity.NormalizedWeatherCondition;
import model.hibernate.entity.WeatherCondition;

import javax.persistence.*;
import java.util.*;

public class DataController {
    private static List<WeatherCondition> weatherConditionList;

    public static List<WeatherCondition> init() {
        weatherConditionList = loadWeatherConditionList();
        ClassificationProblem problem = DataNormalizationProcessor.getClassification(weatherConditionList);
        BayesClassifier classifier = new BayesClassifier();
        classifier.configure(problem.getTrainingSet());
        problem.getTestSet().forEach(classifier::classify);
        return adjustInterferenceToWeatherCondition(problem.getTestSet());
    }

    private static List<WeatherCondition> adjustInterferenceToWeatherCondition(Set<NormalizedWeatherCondition> normalizedWeatherConditionSet) {
        List<WeatherCondition> weatherConditionListAfterClassification = new ArrayList<>();
        for (NormalizedWeatherCondition normalizedWeatherCondition : normalizedWeatherConditionSet) {
            for (WeatherCondition weatherCondition : weatherConditionList) {
                if (normalizedWeatherCondition.getWeatherConditionsID().equals(weatherCondition.getWeatherConditionsID())) {
                    int inferenceIndex = normalizedWeatherCondition.getInference().ordinal() + 1;
                    weatherConditionListAfterClassification.add(updateWeatherCondition(weatherCondition,
                            getInferenceResultByIndex(inferenceIndex)));
                }
            }
        }
        return weatherConditionListAfterClassification;
    }

    private static WeatherCondition updateWeatherCondition(WeatherCondition weatherCondition, InferenceResult inferenceResult) {
        weatherCondition.setInferenceResult(inferenceResult);
        EntityManager entityManager = createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(weatherCondition);
        transaction.commit();
        entityManager.close();
        return weatherCondition;
    }

    public static void insertWeatherCondition(WeatherCondition weatherCondition) {
        EntityManager entityManager = createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(weatherCondition);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private static List<WeatherCondition> loadWeatherConditionList() {
        EntityManager entityManager = createEntityManager();
        List<WeatherCondition> weatherCondition;
        weatherCondition = entityManager.createQuery("SELECT d FROM WeatherCondition d", WeatherCondition.class).getResultList();
        entityManager.close();
        return weatherCondition;
    }

    private static InferenceResult getInferenceResultByIndex(int inferenceIndex) {
        EntityManager entityManager = createEntityManager();
        return entityManager.createQuery("FROM InferenceResult ir WHERE ir.inferenceResultsID = :id", InferenceResult.class)
                .setParameter("id", inferenceIndex)
                .getSingleResult();
    }

    public static Optional<WeatherCondition> findLastClassifiedConditionBefore(Date fireTime) {
        TypedQuery<WeatherCondition> query = createEntityManager().createQuery("FROM WeatherCondition wc WHERE wc.measurementTime < :date AND wc.inferenceResult != null ORDER BY wc.measurementTime DESC", WeatherCondition.class);
        query.setParameter("date", fireTime);
        query.setMaxResults(1);
        return query.getResultList().stream().findFirst();
    }

    private static EntityManager createEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("weatherStationMavenWebapp");
        return factory.createEntityManager();
    }
}
