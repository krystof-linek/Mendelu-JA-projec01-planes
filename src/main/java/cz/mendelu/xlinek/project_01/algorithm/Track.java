package cz.mendelu.xlinek.project_01.algorithm;

import cz.mendelu.xlinek.project_01.PlaneApplication;
import cz.mendelu.xlinek.project_01.country.Borders;
import cz.mendelu.xlinek.project_01.country.CountryRepository;
import cz.mendelu.xlinek.project_01.files.LoadYaml;
import cz.mendelu.xlinek.project_01.files.Result;
import cz.mendelu.xlinek.project_01.files.WriteToXml;
import cz.mendelu.xlinek.project_01.parcels.Parcel;
import cz.mendelu.xlinek.project_01.parcels.ParcelRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Track {

    private static final Logger log = LoggerFactory.getLogger(PlaneApplication.class);

    private CountryRepository crepo;
    private ParcelRepository prepo;
    private List<Plane> planes = new ArrayList<>();
    private List<Result> results = new ArrayList<>();

    public Track(CountryRepository crepo, ParcelRepository prepo) {
        this.crepo = crepo;
        this.prepo = prepo;

        loadPlanes();
    }

    /**
     * Funkce transformuje list s objekty Borders na list se stringy.
     * List obsahuje zkratky sousednich statu.
     * @param borders vstupni list s objekty Borders
     * @return vraci list, ktery obsahuje data typu string
     */
    private List<String> bordersToString(List<Borders> borders){

        List<String> bordersStr = new ArrayList<>();

        for (Borders b : borders) {
            bordersStr.add(b.getCountryName());
        }

        return bordersStr;
    }
    /**
     * Funkce kontroluje, jestli nedoslo k zaplneni cesty. Mohlo by dojit k zacykleni.
     * Kontroluje se, jestli cesta jiz neobsahuje vsechny moznosti, ktere momentalni hranice nabizi,
     * Zajima nas tedy, jestli existuje, alespon jeden stat z borders, ktery jeste nebyl navstiven v track
     * @param track seznam vsechn navstivenych zemi
     * @param borders seznam hranic prislusneho statu
     * @return vraci informaci, jestli uz cesta obsahuje vsechny moznosti nebo ne
     */
    private boolean checkFullTrack(List<String> track, List<String> borders){
        boolean containAll = true;

        for (String border : borders) {
            if(!track.contains(border)){
                containAll = false;
            }
        }

        if (containAll == true)
            return true;

        return false;
    }
    /**
     * Funkce zpracuje mapu z yaml souboru a vytvori list s dostupnymi letadly.
     */
    private void loadPlanes(){
        LoadYaml file = new LoadYaml("./src/main/resources/planes.yaml");

        HashMap<String, String> planesMap = file.getPlanes();

        for (HashMap.Entry<String, String> plane : planesMap.entrySet()) {

            Plane p = new Plane(plane.getKey(), plane.getValue());
            planes.add(p);
        }
    }
    /**
     * Funkce nastavi nove parkoviste prislusnemu letadlu.
     * @param index
     * @param country
     */
    private void setPlaneParking(int index, String country){
        planes.get(index).setParking(crepo.findCountryByCountryCodeEquals(country).getCity());

        log.info("Letadlo " + planes.get(index).getPlate() + " parkuje v: " + planes.get(index).getParking());
        log.info("--------------------------------------------------");
        log.info("");
    }
    /**
     * Funkce slouzi k vypisu letu
     * @param trace list pro vypis
     */
    private void trace(List<String> trace){
        String from;
        String to;

        if (trace.size() != 0){
            for (int i = 0; i < trace.size()-1; i++){
                from = trace.get(i);
                to = trace.get(i+1);

                log.info("Letadlo leti z: " + from + " do " + to +".");

            }
        }

        log.info("--------------------------------------------------");
        log.info("Balik byl dorucen!");
        log.info("--------------------------------------------------");
    }
    /**
     * Funkce pouze vytiskne vystup do souboru xml;
     */
    private void printResults(){
        WriteToXml file = new WriteToXml(results);

        file.createDoc();

        file.createDoc();
    }
    /**
     * Funkce pouze ulozi novy zaznam o vysledku transportu balicku.
     * Vysledky se prubezne ukladaji do seznamu results.
     * @param plate oznaceni letadla
     * @param from odkud leti
     * @param track trasa (list)
     * @param to kam leti
     * @param time cas letu (pocet preletu s balickem)
     */
    private void storeResult(String plate, String from, List<String> track,  String to, int time){
        results.add(new Result(plate, from, track, to, time));
    }

    /**
     * Funkce vypise do log souboru zakladni udaje o letu a balicku
     * @param index index letadla, ktere balicek poveze
     * @param location lokace, kde ma balicek nalozit
     * @param destination cilova destinace
     * @param flight misto, kde se nachazi letadlo
     */
    private void startInfo(int index, String location, String destination, String flight){
        log.info("");
        log.info("--------------------------------------------------");
        log.info("Balik nutne dorucit z " + location + " do " + destination + "!");
        log.info("--------------------------------------------------");

        if (flight.equals(location)) {
            log.info("Naklada se balik do letadla " + planes.get(index).getPlate() +".");
            log.info("--------------------------------------------------");
        }
        else {
            log.info("Letadlo " + planes.get(index).getPlate() + " leti z: " + flight + " do " + location +".");
            log.info("Naklada se balik do letadla " + planes.get(index).getPlate() +".");
            log.info("--------------------------------------------------");
        }
    }
    /**
     * Hlavni funkce, ktera slouzi k doruceni vsech balicku.
     */
    public void deliverPackages(){
        //ohledne letadel
        final int numOfPlanes = planes.size();
        int index = 0;
        //ohledne doruceni
        String flight;
        String loc;
        String dest;
        String next;
        //seznam balicku co maji byt doruceny
        List<Parcel> parcels = prepo.findAll();
        //seznam sousednich statu
        List<String> borders;
        //cyklus pro kazdy balicek
        for (Parcel p : parcels) {
            //seznam, zde budeme ukladat cestu letadla
            List<String> track = new ArrayList<>();
            //zjistime kde parkuje letadlo dle indexu
            flight = crepo.findCountryByCityEquals(planes.get(index).getParking()).getCountryCode();
            //nastavime vychozi a cilovou lokaci balicku
            loc = p.getLocation();
            dest = p.getDestination();
            //vypise vychozi udaje
            startInfo(index, loc, dest, flight);
            //letadlo je v zemi, kde se nachazi balicek
            flight = loc;
            //vlozime prvni zemi do cesty
            track.add(flight);
            //cyklus bude probihat dokud cesta nebude obsahovat cilovou destinaci ==> dokud nebude balicek dorucen
            while (!track.contains(dest)) {
                //nalezneme sousedy prislusneho statu
                borders = bordersToString(crepo.findCountryByCountryCodeEquals(flight).getBorders());
                //pokud stat nema prime hranice letadlo leti primo do cilove destinace
                //pokud je sousedni stat uz cilovy, tak letadlo leti do cile

                /* vyresit ostrovy ==> dochazi k zacykleni */
                if ((borders.size() == 0) || (borders.contains(dest))){
                    track.add(dest);
                } else {
                    //kontroluje se aby nedoslo k zacykleni
                    //pokud letadlo obsahuje veskere mozne zemene a nemuze vybrat dalsi zeme ==> zacne se odznova
                    if (checkFullTrack(track, borders)) {
                        track.clear();
                        track.add(loc);
                        borders = bordersToString(crepo.findCountryByCountryCodeEquals(loc).getBorders());
                    }
                    //nahodne vybereme jednu zem z aktualnich sousedu
                    next = borders.get((int) (Math.random() * borders.size()));
                    //nesmi byt vybrana zem, ktera uz byla navstivena
                    while (track.contains(next)) {
                        next = borders.get((int) (Math.random() * borders.size()));
                    }
                    //pridame zem do cesty
                    track.add(next);
                    //nastavime aktualni zem, kde se nachazi letadlo
                    flight = next;
                }
            }
            //vypise trasu
            trace(track);
            //ulozi vysledek
            storeResult(planes.get(index).getPlate(), loc, track, dest, track.size());
            //zaparkuje letadlo
            setPlaneParking(index, dest);
            //navysime index
            index++;
            //pokud je vic balicku nez letadel, tak znovu poleti prvni letadlo
            if (index >= numOfPlanes)
                index = 0;
        }
        //vypiseme vysledek do souboru
        printResults();
    }
}
