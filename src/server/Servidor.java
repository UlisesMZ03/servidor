package server;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.stream.XMLStreamException;

public class Servidor {

    private static final int PUERTO = 8080;

    public static void main(String[] args) throws XMLStreamException, FileNotFoundException {
        // Crear un árbol binario de búsqueda para usuarios
        BinarySearchTree usuarios = new BinarySearchTree();

        // Agregar usuarios válidos
        
        usuarios.insert(new Usuario("usuario8", "password8"));
       // usuarios.guardarUsuariosEnXML("usuas.xml");
        //usuarios.guardarUsuariosEnXML("file:C:/Users/ulise/Desktop/TEC/Algoritmos y estructura de datos I/Proyecto2/JavaFXApplication8/src/usuarios.xml");
        usuarios.toXml();
        usuarios.printInOrder();
        
        try {
            // Crear un servidor socket
            ServerSocket servidorSocket = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado");

            while (true) {
                // Esperar a que un cliente se conecte
                Socket clienteSocket = servidorSocket.accept();
                System.out.println("Cliente conectado: " + clienteSocket.getInetAddress());

                // Crear flujos de entrada/salida para comunicación con el cliente
                ObjectOutputStream salida = new ObjectOutputStream(clienteSocket.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(clienteSocket.getInputStream());

                // Leer credenciales del cliente
                Credenciales credenciales = (Credenciales) entrada.readObject();
                System.out.println("Credenciales recibidas: " + credenciales);

                // Verificar credenciales con el árbol de usuarios válidos
                boolean credencialesCorrectas = usuarios.contains(credenciales.getUsuario())
                        && usuarios.get(credenciales.getUsuario()).getPassword().equals(credenciales.getPassword());

                // Enviar respuesta al cliente
                if (credencialesCorrectas) {
                    salida.writeObject("OK");
                } else {
                    salida.writeObject("ERROR");
                }

                // Cerrar flujos de entrada/salida y conexión con el cliente
                entrada.close();
                salida.close();
                clienteSocket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
