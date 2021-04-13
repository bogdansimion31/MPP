package eng.ubb.brigadagrea.server.repository;

import eng.ubb.brigadagrea.server.domain.Client;
import eng.ubb.brigadagrea.server.domain.Order;
import eng.ubb.brigadagrea.server.domain.Product;
import eng.ubb.brigadagrea.server.domain.validators.OrderValidator;
import eng.ubb.brigadagrea.server.domain.validators.exceptions.SystemException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class XmlOrderRepository extends InMemoryRepository<Long, Order>{
    String filename;
    private IRepository<Long, Client> clientRepo;
    private IRepository<Long, Product> productRepo;

    public XmlOrderRepository(OrderValidator orderValidator, String filename, IRepository<Long, Client> clientRepo, IRepository<Long, Product> productRepo){
        super(orderValidator);
        this.filename = filename;
        this.clientRepo = clientRepo;
        this.productRepo = productRepo;
    }

    private static String getTextFromTagName(Element parentElement, String tagName) {
        Node node = parentElement.getElementsByTagName(tagName).item(0);
        return node.getTextContent();
    }

    private Order createOrderFromElement(Element orderElement) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        Client client;
        Product product;
        Long id = Long.parseLong(orderElement.getAttribute("Id"));
        client = this.clientRepo.findById(Long.parseLong(getTextFromTagName(orderElement, "clientId"))).get();
        product = this.productRepo.findById(Long.parseLong(getTextFromTagName(orderElement, "productId"))).get();
        int quantity = Integer.parseInt(getTextFromTagName(orderElement, "quantity"));
        Order order = new Order(client, product, quantity);
        order.setId(id);
        return order;
    }

    private void loadData() throws ParserConfigurationException, IOException, SAXException, TransformerException {

        List<Order> result;
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(this.filename);
        Element root = document.getDocumentElement();
        NodeList children = root.getChildNodes();
        result = IntStream
                .range(0, children.getLength())
                .mapToObj(children::item)
                .filter(node -> node instanceof Element)
                .map(node -> {
                    try {
                        return createOrderFromElement((Element) node);
                    } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
                        throw new SystemException("System error.");
                    }
                })
                .collect(Collectors.toList());
        for (Order order: result) {
            super.save(order);
        }
    }

    public void saveOrder(Order order) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(this.filename);
        Element root = document.getDocumentElement();
        Node orderNode = orderToNode(order, document);
        root.appendChild(orderNode);
        Transformer transformer = TransformerFactory
                .newInstance()
                .newTransformer();
        transformer.transform(new DOMSource(document),
                new StreamResult(new File(this.filename)));
    }

    private void deleteFileContent() throws IOException, TransformerException, ParserConfigurationException, SAXException {
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

    public static Node orderToNode(Order order, Document document) {
        Element orderElement = document.createElement("order");
        orderElement.setAttribute("Id", String.valueOf(order.getId()));
        appendChildWithTextToNode(document, orderElement, "clientId", String.valueOf(order.getClient().getId()));
        appendChildWithTextToNode(document, orderElement, "productId", String.valueOf(order.getProduct().getId()));
        appendChildWithTextToNode(document, orderElement, "quantity", String.valueOf(order.getQuantity()));
        return orderElement;
    }

    private static void appendChildWithTextToNode(Document document, Node parentNode, String tagName, String textContent) {
        Element element = document.createElement(tagName);
        element.setTextContent(textContent);
        parentNode.appendChild(element);
    }

    public Optional<Order> findById(Long id) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        loadData();
        return super.findById(id);
    }

    @Override
    public Iterable<Order> findAll() throws IOException, SAXException, ParserConfigurationException, TransformerException {
        loadData();
        return super.findAll();
    }

    @Override
    public Optional<Order> save(Order entity) throws ValidatorException, IOException, SAXException, ParserConfigurationException, TransformerException {
        loadData();
        Optional<Order> returnValue = super.save(entity);
        saveOrder(entity);
        return returnValue;
    }

    @Override
    public Optional<Order> delete(Long id) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        loadData();
        Optional<Order> returnValue = super.delete(id);
        this.deleteFileContent();
        for (Order order: super.findAll()) {
            saveOrder(order);
        }
        return returnValue;
    }

    @Override
    public Optional<Order> update(Order entity) throws ValidatorException, IOException, SAXException, ParserConfigurationException, TransformerException {
        loadData();
        Optional<Order> returnValue = super.update(entity);
        this.deleteFileContent();
        for (Order order: super.findAll()) {
            saveOrder(order);
        }
        return returnValue;
    }
}