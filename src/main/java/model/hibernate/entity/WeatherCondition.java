package model.hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import javax.persistence.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WeatherConditions", uniqueConstraints = { @UniqueConstraint(columnNames = { "weatherConditionsID" }) })
public class WeatherCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int weatherConditionsID;
    private Timestamp measurementTime;
    private double temperature;
    private double airHumidity;
    private double airPressure;
    @ManyToOne
    @JoinColumn(name = "inferenceResultsID")
    private InferenceResult inferenceResult;
}
