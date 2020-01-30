package controller;

import model.hibernate.entity.InferenceResult;
import model.hibernate.entity.NormalizedWeatherCondition;
import model.hibernate.entity.WeatherCondition;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
                if (normalizedWeatherCondition.getWeatherConditionsID() == weatherCondition.getWeatherConditionsID()) {
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

    private static EntityManager createEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("weatherStationMavenWebapp");
        return factory.createEntityManager();
    }
}
