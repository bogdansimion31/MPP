package eng.ubb.brigadagrea.server.repository;

import eng.ubb.brigadagrea.server.domain.Client;
import eng.ubb.brigadagrea.server.domain.validators.ClientValidator;
import eng.ubb.brigadagrea.server.domain.validators.exceptions.ValidatorException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class XmlClientRepository extends InMemoryRepository<Long, Client>{

    String filename;
    public XmlClientRepository(ClientValidator clientValidator, String filename){
        super(clientValidator);
        this.filename = filename;
    }

    private static String getTextFromTagName(Element parentElement, String tagName) {
        Node node = parentElement.getElementsByTagName(tagName).item(0);
        return node.getTextContent();
    }

    private static Client createClientFromElement(Element clientElement) {
        Client client = new Client();
        Long id = Long.parseLong(clientElement.getAttribute("clientId"));
        client.setId(id);
        client.setName(getTextFromTagName(clientElement, "name"));
        client.setSurname(getTextFromTagName(clientElement, "surname"));
        client.setFidelityScore(Integer.parseInt(getTextFromTagName(clientElement, "fidelityScore")));
        return client;
    }

    private void loadData() throws ParserConfigurationException, IOException, SAXException, TransformerException {

        List<Client> result;
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(this.filename);
        Element root = document.getDocumentElement();
        NodeList children = root.getChildNodes();
        result =IntStream
                .range(0, children.getLength())
                .mapToObj(children::item)
                .filter(node -> node instanceof Element)
                .map(node -> createClientFromElement((Element) node))
                .collect(Collectors.toList());
        for (Client client: result) {
            super.save(client);
        }
    }

    public void saveClient(Client client) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(this.filename);
        Element root = document.getDocumentElement();
        Node clientNode = clientToNode(client, document);
        root.appendChild(clientNode);
        Transformer transformer = TransformerFactory
                .newInstance()
                .newTransformer();
        transformer.transform(new DOMSource(document),
                new StreamResult(new File(this.filename)));
    }

    private void deleteFileContent() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(this.filename);
        Element root = document.getDocumentElement();
        NodeList children = root.getChildNodes();
        while (children.getLength() > 0) {
            Node node = children.item(0);
            node.getParentNode().removeChild(node);
        }
        Transformer transformer = TransformerFactory
                .newInstance()
                .newTransformer();
        transformer.transform(new DOMSource(document),
                new StreamResult(new File(this.filename)));
    }

    public static Node clientToNode(Client client, Document document) {
        Element clientElement = document.createElement("client");

        clientElement.setAttribute("clientId", client.getId().toString());
        appendChildWithTextToNode(document, clientElement,"name", client.getName());
        appendChildWithTextToNode(document, clientElement, "surname", client.getSurname());
        appendChildWithTextToNode(document, clientElement, "fidelityScore", String.valueOf(client.getFidelityScore()));
        return clientElement;
    }

    private static void appendChildWithTextToNode(Document document, Node parentNode, String tagName, String textContent) {
        Element element = document.createElement(tagName);
        element.setTextContent(textContent);
        parentNode.appendChild(element);
    }

    public Optional<Client> findById(Long id) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        loadData();
        return super.findById(id);
    }

    @Override
    public Iterable<Client> findAll() throws IOException, SAXException, ParserConfigurationException, TransformerException {
        loadData();
        return super.findAll();
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException, IOException, SAXException, ParserConfigurationException, TransformerException {
        loadData();
        Optional<Client> returnValue = super.save(entity);
        saveClient(entity);
        return returnValue;
    }

    @Override
    public Optional<Client> delete(Long id) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        loadData();
        Optional<Client> returnValue = super.delete(id);
        this.deleteFileContent();
        for (Client client: super.findAll()) {
            saveClient(client);
        }
        return returnValue;
    }

    @Override
    public Optional<Client> update(Client entity) throws ValidatorException, IOException, SAXException, ParserConfigurationException, TransformerException {
        loadData();
        Optional<Client> returnValue = super.update(entity);
        this.deleteFileContent();
        for (Client client: super.findAll()) {
            saveClient(client);
        }
        return returnValue;
    }
}
