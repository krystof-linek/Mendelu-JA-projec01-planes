package cz.mendelu.xlinek.project_01.country;

import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country, Long> {
    Country findCountryByCountryCodeEquals(String str);
    Country findCountryByCityEquals(String str);
}
