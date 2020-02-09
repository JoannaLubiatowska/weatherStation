package controller;

import lombok.extern.slf4j.Slf4j;
import model.hibernate.entity.InferenceResult;
import model.hibernate.entity.NormalizedWeatherCondition;
import model.hibernate.entity.WeatherCondition;

import javax.persistence.*;
import java.util.*;

@Slf4j
public class DataController {
    private static List<WeatherCondition> weatherConditionList;
    private static EntityManager em;

    public static void init() {
        initEntityManager();
    }

    private static void initEntityManager() {
        if (em == null || !em.isOpen()) {
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("weatherStationMavenWebapp");
            em = factory.createEntityManager();
        } else {
            if (!em.isOpen()) {
                log.warn("Entity manager is closed during application running. It is significant performance issue.");
                EntityManagerFactory factory = Persistence.createEntityManagerFactory("weatherStationMavenWebapp");
                em = factory.createEntityManager();
            }
        }
    }

    public static void destroyEntityManager() {
        em.close();
        em = null;
    }

    public static List<WeatherCondition> classifyData() {
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
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(weatherCondition);
        transaction.commit();
        return weatherCondition;
    }

    public static void insertWeatherCondition(WeatherCondition weatherCondition) {
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(weatherCondition);
        entityManager.getTransaction().commit();
    }

    private static List<WeatherCondition> loadWeatherConditionList() {
        EntityManager entityManager = getEntityManager();
        List<WeatherCondition> weatherCondition;
        weatherCondition = entityManager.createQuery("SELECT d FROM WeatherCondition d", WeatherCondition.class).getResultList();
        return weatherCondition;
    }

    private static InferenceResult getInferenceResultByIndex(int inferenceIndex) {
        EntityManager entityManager = getEntityManager();
        return entityManager.createQuery("FROM InferenceResult ir WHERE ir.inferenceResultsID = :id", InferenceResult.class)
                .setParameter("id", inferenceIndex)
                .getSingleResult();
    }

    public static Optional<WeatherCondition> findLastClassifiedConditionBefore(Date fireTime) {
        TypedQuery<WeatherCondition> query = getEntityManager().createQuery("FROM WeatherCondition wc WHERE wc.measurementTime < :date AND wc.inferenceResult != null ORDER BY wc.measurementTime DESC", WeatherCondition.class);
        query.setParameter("date", fireTime);
        query.setMaxResults(1);
        return query.getResultList().stream().findFirst();
    }

    private static EntityManager getEntityManager() {
        initEntityManager();
        return em;
    }
}
