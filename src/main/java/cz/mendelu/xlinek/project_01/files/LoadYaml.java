package cz.mendelu.xlinek.project_01.files;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import cz.mendelu.xlinek.project_01.PlaneApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;

/**
 * Trida slouzi k nacteni vychozich dat z yaml souboru.
 * Nacitame data ohledne dostupnych letadel.
 * Umisteni souboru je: ./src/main/resources/planes.yaml
 */

public class LoadYaml {
    private static final Logger log = LoggerFactory.getLogger(PlaneApplication.class);

    private final String path;

    //mapa pro ulozeni letadel
    private HashMap<String, String> planes;
    //konstruktor nacte letadla
    public LoadYaml(String path) {
        this.path = path;
        loadPlanes();
    }
    /**
     * Funkce nacte dostupne letadla ze souboru.
     * Pripadne upozorni, ze soubor neexistuje.
     */
    private void loadPlanes(){
        try {
            File file = new File(path);

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            Planes planes = mapper.readValue(file, Planes.class);

            this.planes = planes.getPlanes();

        } catch (Exception e){

            log.error(e.getLocalizedMessage());

            System.exit(1);
        }
    }

    public HashMap<String, String> getPlanes(){
        return planes;
    }






}
