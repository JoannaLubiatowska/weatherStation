package controller;

import model.entity.InferenceResult;
import model.entity.NormalizedWeatherCondition;
import model.entity.WeatherCondition;
import model.enums.AirHumidity;
import model.enums.AirPressure;
import model.enums.Inference;
import model.enums.Temperature;

import java.util.*;
import java.util.stream.Collectors;

public class DataNormalizationProcesor {

    public static ClassificationProblem getClassification(List<WeatherCondition> weatherCondition) {
        Map<Boolean, List<NormalizedWeatherCondition>> recordsMap = normalizeData(weatherCondition)
                .stream()
                .collect(Collectors.groupingBy(normalizedWeatherCondition -> Objects.isNull(normalizedWeatherCondition.getInference())));
        return new ClassificationProblem(new HashSet<>(recordsMap.get(false)), new HashSet<>(recordsMap.get(true)));
    }

    private static List<NormalizedWeatherCondition> normalizeData(List<WeatherCondition> weatherConditions) {
        List<NormalizedWeatherCondition> normalizedWeatherConditions = new ArrayList<>();
        for(WeatherCondition wc : weatherConditions) {
            if(Objects.nonNull(wc.getInferenceResult())) {
                normalizedWeatherConditions.add(new NormalizedWeatherCondition(normalizeTemperature(wc.getTemperature()),
                        normalizeAirHumidity(wc.getAirHumidity()), normalizeAirPressure(wc.getAirPressure()),
                        normalizeInferenceResult(wc.getInferenceResult())));
            } else {
                normalizedWeatherConditions.add(new NormalizedWeatherCondition(normalizeTemperature(wc.getTemperature()),
                        normalizeAirHumidity(wc.getAirHumidity()), normalizeAirPressure(wc.getAirPressure())));
            }
        }
        return new ArrayList<>();
    }

    private static Temperature normalizeTemperature(double value) {
        Temperature temp;
        if(value <= 10) {
            temp = Temperature.LOW;
        } else if (value > 10 && value < 20) {
            temp = Temperature.MEDIUM;
        } else {
            temp = Temperature.HIGH;
        }
        return temp;
    }

    private static AirPressure normalizeAirPressure(double value) {
        AirPressure airPressure;
        if(value <= 1000) {
            airPressure = AirPressure.LOW;
        } else if (value > 1000 && value < 1015) {
            airPressure = AirPressure.MEDIUM;
        } else {
            airPressure = AirPressure.HIGH;
        }
        return airPressure;
    }

    private static AirHumidity normalizeAirHumidity(double value) {
        AirHumidity airHumidity;
        if(value <= 40) {
            airHumidity = AirHumidity.LOW;
        } else if (value > 40 && value < 60) {
            airHumidity = AirHumidity.MEDIUM;
        } else {
            airHumidity = AirHumidity.HIGH;
        }
        return airHumidity;
    }

    private static Inference normalizeInferenceResult(InferenceResult inferenceResult) {
        Inference inference = null;
        if(inferenceResult.getInferenceResultsName().equals("bad")) {
            inference = Inference.BAD_WEATHER;
        } else if (inferenceResult.getInferenceResultsName().equals("middle")) {
            inference = Inference.MEDIUM_WEATHER;
        } else if (inferenceResult.getInferenceResultsName().equals("good")) {
            inference = Inference.GOOD_WEATHER;
        }
        return inference;
    }
}
