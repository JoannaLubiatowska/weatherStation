package controller;

import model.entity.NormalizedWeatherCondition;

import java.util.Set;

public class ClassificationProblem {

    private Set<NormalizedWeatherCondition> trainingSet;
    private Set<NormalizedWeatherCondition> testSet;

    public ClassificationProblem(Set<NormalizedWeatherCondition> trainingSet, Set<NormalizedWeatherCondition> testSet) {
        this.trainingSet = trainingSet;
        this.testSet = testSet;
    }

    public Set<NormalizedWeatherCondition> getTrainingSet() {
        return trainingSet;
    }

    public Set<NormalizedWeatherCondition> getTestSet() {
        return testSet;
    }

    @Override
    public String toString() {
        return "Classification{" +
                "trainingSet=" + trainingSet +
                ",\n testSet=" + testSet +
                '}';
    }
}
