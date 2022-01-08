package cz.mendelu.xlinek.project_01.country;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor // <--- lombook
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CountrySeq")
    private Long id;
    @Column(name = "COUNTRY_NAME")
    private String countryName;
    @Column(name = "CITY")
    private String city;
    @Column(name = "COUNTRY_CODE")
    private String countryCode;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "COUNTRY_FID", referencedColumnName = "id")
    private List<Borders> borders = new ArrayList<>();

    public Country(String countryName, String city, String countryCode) {
        this.countryName = countryName;
        this.city = city;
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", countryName='" + countryName + '\'' +
                ", city='" + city + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", borders=" + borders +
                '}';
    }
}
