package controller;

import model.hibernate.HibernateUtil;
import model.hibernate.entity.InferenceResult;
import model.hibernate.entity.NormalizedWeatherCondition;
import model.hibernate.entity.WeatherCondition;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

public class DataController {
    private static List<WeatherCondition> weatherConditionList;
    private  static List<InferenceResult> inferenceResultList;
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
        weatherConditionList = loadWeatherConditionList();
        inferenceResultList = loadInferenceResultList();
        ClassificationProblem problem = DataNormalizationProcessor.getClassification(weatherConditionList);
        BayesClassifier classifier = new BayesClassifier();
        classifier.configure(problem.getTrainingSet());
        problem.getTestSet().forEach(classifier::classify);
        Set<NormalizedWeatherCondition> normalizedWeatherConditionSet = problem.getTestSet();
        adjustInterferenceToWeatherCondition(normalizedWeatherConditionSet);
        return normalizedWeatherConditionSet;
    }

    private static void adjustInterferenceToWeatherCondition(Set<NormalizedWeatherCondition> normalizedWeatherConditionSet) {
        for(NormalizedWeatherCondition normalizedWeatherCondition : normalizedWeatherConditionSet) {
            for(WeatherCondition weatherCondition : weatherConditionList) {
                if(normalizedWeatherCondition.getWeatherConditionsID() == weatherCondition.getWeatherConditionsID()) {
                    int inferenceIndex = normalizedWeatherCondition.getInference().ordinal() + 1;
                    updateWeatherCondition(weatherCondition, getInferenceResultByIndex(inferenceIndex));
                }
            }
        }
    }

    private static void updateWeatherCondition(WeatherCondition weatherCondition, InferenceResult inferenceResult) {
        weatherCondition.setInferenceResult(inferenceResult);
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(weatherCondition);
        transaction.commit();
        sessionFactory.close();
//        EntityManager entityManager = createEntityManager();
//        entityManager.getTransaction().begin();
//        entityManager.persist(weatherCondition);
//        entityManager.getTransaction().commit();
//        entityManager.close();
    }

    public static void insertWeatherCondition(WeatherCondition weatherCondition) {
        EntityManager entityManager = createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(weatherCondition);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private static List<InferenceResult> loadInferenceResultList() {
        EntityManager entityManager = createEntityManager();
        List<InferenceResult> inferenceResult;
        inferenceResult = entityManager.createQuery("SELECT d FROM InferenceResult d", InferenceResult.class).getResultList();
        entityManager.close();
        return inferenceResult;
    }

    private static List<WeatherCondition> loadWeatherConditionList() {
        EntityManager entityManager = createEntityManager();
        List<WeatherCondition> weatherCondition;
        weatherCondition = entityManager.createQuery("SELECT d FROM WeatherCondition d", WeatherCondition.class).getResultList();
        entityManager.close();
        return weatherCondition;
    }

    private static InferenceResult getInferenceResultByIndex(int inferenceIndex) {
        InferenceResult result = null;
        for(InferenceResult inferenceResult : inferenceResultList) {
           if(inferenceResult.getInferenceResultsID() == inferenceIndex) {
               result = inferenceResult;
           }
        }
        return result;
    }

    private static EntityManager createEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("weatherStationMavenWebapp");
        return factory.createEntityManager();
    }
}
