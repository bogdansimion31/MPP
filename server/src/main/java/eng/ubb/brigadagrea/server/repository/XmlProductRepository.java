package eng.ubb.brigadagrea.server.repository;

import eng.ubb.brigadagrea.server.domain.Product;
import eng.ubb.brigadagrea.server.domain.validators.ProductValidator;
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

public class XmlProductRepository extends InMemoryRepository<Long, Product>{
    String filename;
    public XmlProductRepository(ProductValidator productValidator, String filename){
        super(productValidator);
        this.filename = filename;
    }

    private static String getTextFromTagName(Element parentElement, String tagName) {
        Node node = parentElement.getElementsByTagName(tagName).item(0);
        return node.getTextContent();
    }

    private static Product createProductFromElement(Element productElement) {
        Product product = new Product();
        Long id = Long.parseLong(productElement.getAttribute("productId"));
        product.setId(id);
        product.setName(getTextFromTagName(productElement, "name"));
        product.setCategory(getTextFromTagName(productElement, "category"));
        product.setStock(Integer.parseInt(getTextFromTagName(productElement, "stock")));
        return product;
    }

    private void loadData() throws ParserConfigurationException, IOException, SAXException, TransformerException {

        List<Product> result;
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
                .map(node -> createProductFromElement((Element) node))
                .collect(Collectors.toList());
        for (Product product: result) {
            super.save(product);
        }
    }

    public void saveProduct(Product product) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(this.filename);
        Element root = document.getDocumentElement();
        Node productNode = productToNode(product, document);
        root.appendChild(productNode);
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

    public static Node productToNode(Product product, Document document) {
        Element productElement = document.createElement("product");
        productElement.setAttribute("productId", product.getId().toString());
        appendChildWithTextToNode(document, productElement,"name", product.getName());
        appendChildWithTextToNode(document, productElement, "category", product.getCategory());
        appendChildWithTextToNode(document, productElement, "stock", String.valueOf(product.getStock()));
        return productElement;
    }

    private static void appendChildWithTextToNode(Document document, Node parentNode, String tagName, String textContent) {
        Element element = document.createElement(tagName);
        element.setTextContent(textContent);
        parentNode.appendChild(element);
    }

    public Optional<Product> findById(Long id) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        loadData();
        return super.findById(id);
    }

    @Override
    public Iterable<Product> findAll() throws IOException, SAXException, ParserConfigurationException, TransformerException {
        loadData();
        return super.findAll();
    }

    @Override
    public Optional<Product> save(Product entity) throws ValidatorException, IOException, SAXException, ParserConfigurationException, TransformerException {
        loadData();
        Optional<Product> returnValue = super.save(entity);
        saveProduct(entity);
        return returnValue;
    }

    @Override
    public Optional<Product> delete(Long id) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        loadData();
        Optional<Product> returnValue = super.delete(id);
        this.deleteFileContent();
        for (Product product: super.findAll()) {
            saveProduct(product);
        }
        return returnValue;
    }

    @Override
    public Optional<Product> update(Product entity) throws ValidatorException, IOException, SAXException, ParserConfigurationException, TransformerException {
        loadData();
        Optional<Product> returnValue = super.update(entity);
        this.deleteFileContent();
        for (Product product: super.findAll()) {
            saveProduct(product);
        }
        return returnValue;
    }
}
