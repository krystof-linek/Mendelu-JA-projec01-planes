package cz.mendelu.xlinek.project_01.requests;

import cz.mendelu.xlinek.project_01.PlaneApplication;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Trida slouzi k zpracovani rest api ohledne zemich.
 * Ze zpracovanych dat se vytvori JSON objekt.
 */
public class GetRequestToJson {

    private static final Logger log = LoggerFactory.getLogger(PlaneApplication.class);

    private final String uri;

    public GetRequestToJson(String uri) {
        this.uri = uri;
    }

    /**
     * Funkce slouzi k vytvoreni potrebneho JSON objektu.
     * Zpracuje rest api dostupne:
     * @see <a href="https://restcountries.com/v3.1/all">https://restcountries.com/v3.1/all</a>
     * @return vraci JSON objekt s filtrovanymi daty.
     */
    public List<JSONObject> getData() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return parse(response.body());

        } catch (Exception e){
            log.error(e.getLocalizedMessage());

            System.exit(3);
            return null;
        }
    }

    /**
     * Funkce parsuje potrebna data.
     * @param responseBody response k parsovani.
     * @return vraci parsovany JSON objekt
     */
    private List<JSONObject> parse(String responseBody){

        JSONArray countries = new JSONArray(responseBody);

        List<JSONObject> parsedCountries = new ArrayList<>();

        for (int i = 0; i < countries.length(); i++) {

            JSONObject country = countries.getJSONObject(i);

            String cca3 = country.getString("cca3");
            String name = country.getJSONObject("name").getString("common");
            String capital = "none";

            JSONObject parsedCountry = new JSONObject();

            JSONArray borders = new JSONArray();

            if (country.has("capital"))
                capital = country.getJSONArray("capital").get(0).toString();

            if (country.has("borders"))
                borders = country.getJSONArray("borders");

            parsedCountry.put("countryCode", cca3);
            parsedCountry.put("countryName", name);
            parsedCountry.put("capital", capital);
            parsedCountry.put("borders", borders);

            parsedCountries.add(parsedCountry);
        }

        return parsedCountries;
    }

}
