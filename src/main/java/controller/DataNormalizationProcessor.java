package controller;

import model.hibernate.entity.InferenceResult;
import model.hibernate.entity.NormalizedWeatherCondition;
import model.hibernate.entity.WeatherCondition;
import model.enums.AirHumidity;
import model.enums.AirPressure;
import model.enums.Inference;
import model.enums.Temperature;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

class DataNormalizationProcessor {

    static ClassificationProblem getClassification(List<WeatherCondition> weatherCondition) {
        Map<Boolean, List<NormalizedWeatherCondition>> recordsMap = normalizeData(weatherCondition)
                .stream()
                .collect(Collectors.groupingBy(normalizedWeatherCondition -> Objects
                        .isNull(normalizedWeatherCondition.getInference())));
        return new ClassificationProblem(new HashSet<>(Optional
                .ofNullable(recordsMap.get(false)).orElse(emptyList())),
                new HashSet<>(Optional.ofNullable(recordsMap.get(true)).orElse(emptyList())));
    }

    private static List<NormalizedWeatherCondition> normalizeData(List<WeatherCondition> weatherConditions) {
        List<NormalizedWeatherCondition> normalizedWeatherConditions = new ArrayList<>();
        for(WeatherCondition wc : weatherConditions) {
            if(Objects.nonNull(wc.getInferenceResult())) {
                normalizedWeatherConditions.add(new NormalizedWeatherCondition(
                        wc.getWeatherConditionsID(),
                        normalizeTemperature(wc.getTemperature()),
                        normalizeAirHumidity(wc.getAirHumidity()),
                        normalizeAirPressure(wc.getAirPressure()),
                        normalizeInferenceResult(wc.getInferenceResult())));
            } else {
                normalizedWeatherConditions.add(new NormalizedWeatherCondition(
                        wc.getWeatherConditionsID(),
                        normalizeTemperature(wc.getTemperature()),
                        normalizeAirHumidity(wc.getAirHumidity()),
                        normalizeAirPressure(wc.getAirPressure())));
            }
        }
        return normalizedWeatherConditions;
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
        switch (inferenceResult.getInferenceResultsName()) {
            case "bad":
                inference = Inference.BAD_WEATHER;
                break;
            case "middle":
                inference = Inference.MEDIUM_WEATHER;
                break;
            case "good":
                inference = Inference.GOOD_WEATHER;
                break;
        }
        return inference;
    }
}
