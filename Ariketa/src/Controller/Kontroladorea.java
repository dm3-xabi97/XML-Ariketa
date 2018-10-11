/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Ibilgailuak;
import java.io.File;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class Kontroladorea {

    public static ObservableList<Ibilgailuak> datuakKargatuxml(File aukeratutakoa) throws SAXException, IOException, ParserConfigurationException {
        ObservableList<Ibilgailuak> ibil = FXCollections.observableArrayList();
        String modeloa = null, marka = null, matrikula = null;
        int id = 0;
        try {

            /* DOM zuhaitza sortu, xml fitxategi batetik abiatuta */
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document domDok = builder.parse(aukeratutakoa); // fitxategitik DOM tree-ra pasatu
            NodeList ibilgailuakNodoak = domDok.getElementsByTagName("ibilgailua");

            for (int i = 0; i < ibilgailuakNodoak.getLength(); i++) {
                Element ibilElem = (Element) ibilgailuakNodoak.item(i);
                NodeList ibilgailuNodoSemeak = ibilElem.getChildNodes();
                for (int j = 0; j < ibilgailuNodoSemeak.getLength(); j++) {
                    Node semea = ibilgailuNodoSemeak.item(j);
                    if (semea.getNodeName() == "id") {
                        id = Integer.parseInt(((Element) semea.getChildNodes()).getTextContent());
                    } else if (semea.getNodeName() == "modeloa") {
                        modeloa = ((Element) semea.getChildNodes()).getTextContent();
                    } else if (semea.getNodeName() == "marka") {
                        marka = ((Element) semea.getChildNodes()).getTextContent();
                    } else if (semea.getNodeName() == "matrikula") {
                        matrikula = ((Element) semea.getChildNodes()).getTextContent();
                    }
                }
                Ibilgailuak ibilg = new Ibilgailuak(id, modeloa, marka, matrikula);// Ibilgailua objektua sortu, xml-ko datuekin
                ibil.add(ibilg); // ObservableList-ean gehitu
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ibil;
    }

    public static void lista_gordexml(ObservableList<Ibilgailuak> ibil, File aukeratutakoa) {
        try {
            //dokumentua eratzeko eta elementuak eratzeko
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            //elementu nagusia
            Element eRaiz = doc.createElement("ibilgailuak");
            doc.appendChild(eRaiz);
            for (Ibilgailuak ibilg : ibil) {  //objetu baten bitartez observablelist-eko elementuak hartzeko
                Element ibilgailua = doc.createElement("ibilgailua"); //elemetnu semea
                eRaiz.appendChild(ibilgailua);  //azkenengoari gehitzen dio
                //balioak
                Element eId = doc.createElement("id");
                eId.appendChild(doc.createTextNode(String.valueOf(ibilg.getId())));
                ibilgailua.appendChild(eId);

                Element eModeloa = doc.createElement("modeloa");
                eModeloa.appendChild(doc.createTextNode(ibilg.getModeloa()));
                ibilgailua.appendChild(eModeloa);

                Element eMarka = doc.createElement("marka");
                eMarka.appendChild(doc.createTextNode(ibilg.getMarka()));
                ibilgailua.appendChild(eMarka);

                Element eMatrikula = doc.createElement("matrikula");
                eMatrikula.appendChild(doc.createTextNode(ibilg.getMatrikula()));
                ibilgailua.appendChild(eMatrikula);
            }
            //fitxategiaren sorrera amaitzeko
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(aukeratutakoa);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
