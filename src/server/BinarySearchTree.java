package server;


import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javafx.scene.control.TreeItem;
import org.w3c.dom.NodeList;

public class BinarySearchTree {

    private Node root;

    public BinarySearchTree() {
        root = null;
        try {
            File file = new File("C:/Users/ulise/Desktop/TEC/Algoritmos y estructura de datos I/pro/servidor/src/server/usuarios.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList nodeList = document.getElementsByTagName("usuario");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String nombreUsuario = element.getAttribute("nombreUsuario");
                String contrasena = element.getAttribute("contrasena");
                Usuario usuario = new Usuario(nombreUsuario, contrasena);
                insert(usuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(Usuario usuario) {
        root = insertRecursive(root, usuario);
        toXml();
    }

    private Node insertRecursive(Node current, Usuario usuario) {
        if (current == null) {
            return new Node(usuario);
        }

        if (usuario.getNombreUsuario().compareTo(current.usuario.getNombreUsuario()) < 0) {
            current.left = insertRecursive(current.left, usuario);
        } else if (usuario.getNombreUsuario().compareTo(current.usuario.getNombreUsuario()) > 0) {
            current.right = insertRecursive(current.right, usuario);
        } else {
            // el usuario ya existe, no se agrega nada y se muestra un mensaje
            System.out.println("El usuario " + usuario.getNombreUsuario() + " ya existe.");
            return current;
        }

        return current;
    }

    public boolean contains(String nombreUsuario) {
        return containsRecursive(root, nombreUsuario);
    }

    private boolean containsRecursive(Node current, String nombreUsuario) {
        if (current == null) {
            return false;
        }

        if (nombreUsuario.equals(current.usuario.getNombreUsuario())) {
            return true;
        }

        if (nombreUsuario.compareTo(current.usuario.getNombreUsuario()) < 0) {
            return containsRecursive(current.left, nombreUsuario);
        } else {
            return containsRecursive(current.right, nombreUsuario);
        }
    }

    public void printInOrder() {
        printInOrderRecursive(root);
    }

    private void printInOrderRecursive(Node current) {
        if (current == null) {
            return;
        }

        printInOrderRecursive(current.left);
        System.out.println(current.usuario);
        printInOrderRecursive(current.right);
    }

    public Usuario get(String nombreUsuario) {
        Node current = root;
        while (current != null) {
            if (nombreUsuario.equals(current.usuario.getNombreUsuario())) {
                return current.usuario;
            } else if (nombreUsuario.compareTo(current.usuario.getNombreUsuario()) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    public void toXml() {
        try {
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(new File("C:/Users/ulise/Desktop/TEC/Algoritmos y estructura de datos I/pro/servidor/src/server/usuarios.xml")));
            writer.writeStartDocument();
            writer.writeStartElement("usuariooos");

            toXmlRecursive(root, writer);

            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void toXmlRecursive(Node current, XMLStreamWriter writer) throws XMLStreamException {
        if (current == null) {
            return;
        }

        writer.writeStartElement("usuario");
        writer.writeAttribute("nombreUsuario", current.usuario.getNombreUsuario());
        writer.writeAttribute("contrasena", current.usuario.getPassword());
        writer.writeEndElement();

        toXmlRecursive(current.left, writer);
        toXmlRecursive(current.right, writer);
    }

    public TreeItem<Usuario> toTreeItem() {
        return toTreeItemRecursive(root);
    }

    private TreeItem<Usuario> toTreeItemRecursive(Node current) {
        if (current == null) {
            return null;
        }

        TreeItem<Usuario> item = new TreeItem<>(current.usuario);
        item.setExpanded(true);
        item.getChildren().add(toTreeItemRecursive(current.left));
        item.getChildren().add(toTreeItemRecursive(current.right));

        return item;
    }

    private class Node {

        private Usuario usuario;
        private Node left;
        private Node right;

        public Node(Usuario usuario) {
            this.usuario = usuario;
            left = null;
            right = null;
        }
    }

}
