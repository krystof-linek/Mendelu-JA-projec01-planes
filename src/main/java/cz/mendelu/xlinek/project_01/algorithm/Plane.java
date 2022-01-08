package cz.mendelu.xlinek.project_01.algorithm;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Plane {
    private String plate;
    private String parking;

    public Plane(String plate, String parking) {
        this.plate = plate;
        this.parking = parking;
    }
}
