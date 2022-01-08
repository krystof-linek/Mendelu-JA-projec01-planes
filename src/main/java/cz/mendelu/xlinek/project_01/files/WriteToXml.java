package cz.mendelu.xlinek.project_01.files;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import cz.mendelu.xlinek.project_01.PlaneApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Trida slouzi vypisu vystupu do xml souboru.
 */
public class WriteToXml {

    private List<Result> results;

    private static final Logger log = LoggerFactory.getLogger(PlaneApplication.class);

    public WriteToXml(List<Result> results){
        this.results = results;
    }

    /**
     * Funkce vytvari strukturu vystupniho souboru.
     */
    public void createDoc(){
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            //prida hlavni element
            Element rootElement = doc.createElement("Preprava");
            //bude pripojovat k root elementu
            doc.appendChild(rootElement);
            //prida potomky k root elementu
            for (int i = 0; i < results.size(); i++){
                rootElement.appendChild(getParcel(doc, results.get(i).getId(), results.get(i).getFrom(), results.get(i).getTrack().toString(), results.get(i).getTo(), Integer.toString(results.get(i).getTime())));
            }
            //fpro vystup
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            //slouzi k prehlednemu vypisu
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            //inicializace vystupu do konzole nebo souboru
            //StreamResult console = new StreamResult(System.out); //==> vypis do konzole
            StreamResult file = new StreamResult(new File("./src/main/resources/vystup.xml"));
            //vypis do konzole a do souboru
            //transformer.transform(source, console);
            transformer.transform(source, file);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());

            System.exit(2);
        }
    }

    /**
     * Funkce vytvari uzel k jednotlivemu zaznamu.
     * @param doc dokument builder
     * @param plane oznaceni letadla
     * @param odkud lokalizace balicku
     * @param trasa trasa cesty
     * @param kam cilova destinace
     * @param kdy pocet skoku mezi zememi
     * @return vraci uzel
     */
    private Node getParcel(Document doc, String plane, String odkud, String trasa, String kam, String kdy) {
        Element parcel = doc.createElement("Balicek");

        //oznaceni letadla
        parcel.setAttribute("Letadlo", plane);
        //vytvori element odkud
        parcel.appendChild(getParcelElements(doc, parcel, "Odkud", odkud));
        //vytvori element trasa
        parcel.appendChild(getParcelElements(doc, parcel, "Trasa", trasa));
        //vytvori element kam
        parcel.appendChild(getParcelElements(doc, parcel, "Kam", kam));
        //vytvori element kdy
        parcel.appendChild(getParcelElements(doc, parcel, "Kdy", kdy));

        return parcel;
    }

    //vytvoreni uzlu
    private Node getParcelElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

}
