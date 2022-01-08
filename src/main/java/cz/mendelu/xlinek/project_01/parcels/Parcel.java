package cz.mendelu.xlinek.project_01.parcels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor // <--- lombook
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ParcelSeq")
    @Column(name = "ID")
    private Long id;
    @Column(name = "LOCATION")
    private String location;
    @Column(name = "DESTINATION")
    private String destination;

    public Parcel(String location, String destination) {
        this.location = location;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Parcel{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}

