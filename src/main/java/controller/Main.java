package controller;

import model.entity.WeatherCondition;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Main {

    private static List<WeatherCondition> weatherCondition;

    public static void main(String[] args) {
        loadWeatherConditionData();
        BayesClassifier classifier = new BayesClassifier();
    }

    private static void loadWeatherConditionData() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("weatherStationMavenWebapp");
        EntityManager em = factory.createEntityManager();
        weatherCondition = em.createQuery("SELECT * FROM WeatherCondition", WeatherCondition.class).getResultList();
        weatherCondition.stream().forEach(System.out::println);
        em.close();
    }
}
