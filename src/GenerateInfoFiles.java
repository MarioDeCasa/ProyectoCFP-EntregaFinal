/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author davi2
 */
import java.io.*;
import java.util.*;

/**
 * Clase GenerateInfoFiles
 * Se encarga de generar archivos de prueba pseudoaleatorios segun los 
 * requisitos del punto 5 del proyecto.
 */
public class GenerateInfoFiles {

    private static final String CHARSET = "UTF-8";
    private static final String DELIMITER = ";";

    public static void main(String[] args) {
        try {
            // 5b. Crear archivo de productos (ejemplo con 10 productos)
            createProductsFile(10);
            
            // 5c. Crear archivo de informacion de vendedores (ejemplo con 5 vendedores)
            createSalesManInfoFile(5);
            
            // Generar archivos de ventas individuales para los vendedores creados
            // Basado en el punto 5a del PDF
            for (int i = 1; i <= 5; i++) {
                createSalesMenFile(8, "Vendedor_" + i, 1000 + i);
            }
            
            System.out.println("Archivos de prueba generados exitosamente!");
        } catch (Exception e) {
            System.err.println("ERROR al generar archivos: " + e.getMessage());
        }
    }

    /**
     * 5b. Crea un archivo con informacion pseudoaleatoria de productos.
     */
    public static void createProductsFile(int productsCount) throws IOException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("productos.csv"), CHARSET)))) {
            for (int i = 1; i <= productsCount; i++) {
                // Formato: ID; Nombre; Precio
                writer.println(i + DELIMITER + "Producto " + i + DELIMITER + (1500 * i));
            }
        }
    }

    /**
     * 5c. Crea un archivo con informacion de salesmanCount vendedores.
     */
    public static void createSalesManInfoFile(int salesmanCount) throws IOException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("vendedores.csv"), CHARSET)))) {
            for (int i = 1; i <= salesmanCount; i++) {
                // Formato PDF: TipoDoc; NumDoc; Nombres; Apellidos
                writer.println("CC" + DELIMITER + (1000 + i) + DELIMITER + "NombreVendedor" + i + DELIMITER + "Apellido" + i);
            }
        }
    }

    /**
     * 5a. Crea un archivo de ventas de un vendedor con nombre e id dados.
     * Segun formato de pagina 2 del PDF.
     */
    public static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        Random rand = new Random();
        String fileName = "vendedor_" + id + ".csv";
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), CHARSET)))) {
            // Primera linea segun PDF: Tipo Documento; Numero Documento
            writer.println("CC" + DELIMITER + id);
            
            // Ventas segun formato PDF: IDProducto; Cantidad; (IMPORTANTE: punto y coma al final)
            for (int i = 0; i < randomSalesCount; i++) {
                int productoId = rand.nextInt(10) + 1;
                int cantidad = rand.nextInt(5) + 1;
                writer.println(productoId + DELIMITER + cantidad + DELIMITER);
            }
        }
    }
}