package cz.mendelu.xlinek.project_01.country;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor // <--- lombook
@Table(name = "borders")
public class Borders {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BordersSeq")
    @Column(name = "ID")
    private Long id;
    @Column(name = "COUNTRY_NAME")
    private String countryName;

    public Borders(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String toString() {
        return countryName;
    }
}
