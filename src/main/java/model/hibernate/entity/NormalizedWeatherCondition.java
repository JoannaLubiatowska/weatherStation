package model.hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import model.enums.AirHumidity;
import model.enums.AirPressure;
import model.enums.Inference;
import model.enums.Temperature;


@Data
@Builder
@AllArgsConstructor
@ToString
public class NormalizedWeatherCondition {
    private Temperature temperature;
    private AirHumidity airHumidity;
    private AirPressure airPressure;
    private Inference inference;

    public NormalizedWeatherCondition(Temperature temperature, AirHumidity airHumidity, AirPressure airPressure) {
        this.temperature = temperature;
        this.airHumidity = airHumidity;
        this.airPressure = airPressure;
    }
}
