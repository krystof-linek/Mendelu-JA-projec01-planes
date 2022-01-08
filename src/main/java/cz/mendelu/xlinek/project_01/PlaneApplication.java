package cz.mendelu.xlinek.project_01;

import cz.mendelu.xlinek.project_01.algorithm.Track;
import cz.mendelu.xlinek.project_01.country.Borders;
import cz.mendelu.xlinek.project_01.country.Country;
import cz.mendelu.xlinek.project_01.country.CountryRepository;

import cz.mendelu.xlinek.project_01.parcels.Parcel;
import cz.mendelu.xlinek.project_01.parcels.ParcelRepository;
import cz.mendelu.xlinek.project_01.requests.GetRequestToJson;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 *
 * Semestralni projekt - preprava balicku
 *
 * V ramci predmetu: Java Aplikace
 *
 * @author Kryštof Linek
 *
 */
@SpringBootApplication
public class PlaneApplication {

    private static boolean validDatabase = true;

    private static final Logger log = LoggerFactory.getLogger(PlaneApplication.class);

    private final GetRequestToJson request = new GetRequestToJson("https://restcountries.com/v3.1/all");

    private final List<JSONObject> countries = request.getData();

    public static void main(String[] args) {
        if ((args.length > 0) && (args[0].equals("true"))) {
            validDatabase = false;
            log.warn("Byl zadan pozadavek na aktualizaci databaze!");
        }

        SpringApplication.run(PlaneApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(CountryRepository couRepo, ParcelRepository parRepo) {
        return (args -> {
            renewDatabse(couRepo, parRepo);

            Track track = new Track(couRepo, parRepo);

            track.deliverPackages();
        });
    }


    /**
     * Funkce vytvori nekolik testacnich balicku, ktere maji byt doruceny.
     * @param repository
     */

    private void exmapleParcels(ParcelRepository repository) {
        repository.save(new Parcel("AUT", "DEU"));
        repository.save(new Parcel("CZE", "RUS"));
        repository.save(new Parcel("CZE", "ITA"));
        repository.save(new Parcel("ROU", "DEU"));
        repository.save(new Parcel("HRV", "HUN"));
        repository.save(new Parcel("SVK", "ALB"));
        //repository.save(new Parcel("GBR", "CZE")); ==> osamoceny ostrov nefunguje
    }
    /**
     * Funkce ulozi do databaze potrebne udaje ohledne jednotlivych statu,
     * vychazime jiz z predem pripraveneho JSON objektu countries.
     * Do databaze ukladame udaje:
     *  1) Nazev statu
     *  2) Hlavni mesto
     *  3) Zkratku statu (CCA3)
     *  4) Prime hranice (pokud existuji jinak prazdne pole)
     *  Na zaver nastavime databazi jako validni.
     * @param repository databaze kam ukladame data
     */
    private void storeCountries(CountryRepository repository) {

        for (int i = 0; i < countries.size(); i++){

            Country country = new Country(countries.get(i).getString("countryName"), countries.get(i).getString("capital"), countries.get(i).getString("countryCode"));

            for (int j = 0; j < countries.get(i).getJSONArray("borders").toList().size(); j++){
                country.getBorders().add(new Borders(countries.get(i).getJSONArray("borders").toList().get(j).toString()));
            }

            repository.save(country);
        }

        validDatabase = true;
    }
    /**
     * Funkce slouzi k aktualizaci zaznamu ohledne jednotlivych zemi v databazi.
     * Pokud je validita databaze nastavena na "false", tak se veskery obsah databaze smaze.
     * Nasledne se zavola funkce, ktera opet naplni databazi prislusnymi daty.
     *
     * Pokud je validita nastavena na "true", ale databaze neobsahuje data, tak dojde k jejimu naplneni.
     *
     * Pokud je validita nastavena na "true" a databaze neni prazdna, tak obsah databaze bude nezmenen.
     *
     * @param crepo tabulka ohledne zemi
     * @param prepo tabulka s balicky
     */
    private void renewDatabse(CountryRepository crepo, ParcelRepository prepo){
        if (validDatabase == false) {
            log.warn("Dojde k smazani obsahu databaze!");
            crepo.deleteAll();
            prepo.deleteAll();
            exmapleParcels(prepo);
            storeCountries(crepo);
            log.info("Aktualizace databaze probehla uspesne.");
        } else {
            if (crepo.count() == 0l)
                storeCountries(crepo);
            else
                log.info("Databaze s informacemi ohledne zemi je nahrana.");

            if (prepo.count() == 0l)
                exmapleParcels(prepo);
            else
                log.info("Seznam balicku k preprave je nahran.");
        }

    }

}