package model.hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WeatherConditions", uniqueConstraints = { @UniqueConstraint(columnNames = { "weatherConditionsID" }) })
public class WeatherCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer weatherConditionsID;
    private Timestamp measurementTime;
    private double temperature;
    private double airHumidity;
    private double airPressure;
    @ManyToOne
    @JoinColumn(name = "inferenceResultsID"/*, referencedColumnName = "inferenceResultsID"*/)
    private InferenceResult inferenceResult;

    public WeatherCondition(Timestamp measurementTime, double temperature, double airHumidity, double airPressure) {
        this.measurementTime = measurementTime;
        this.temperature = temperature;
        this.airHumidity = airHumidity;
        this.airPressure = airPressure;
    }
}



