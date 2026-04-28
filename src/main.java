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
 * Clase main
 * Ejecuta las tareas de creacion de reportes (puntos 3 y 4 del PDF).
 */
public class main {

    private static final String DELIMITER = ";";
    private static final String CHARSET = "UTF-8";

    public static void main(String[] args) {
        try {
            // Paso 1: Leer datos maestros
            Map<Integer, Producto> productos = leerProductos("productos.csv");
            Map<Long, Vendedor> vendedores = leerVendedores("vendedores.csv");

            // Paso 2: Procesar todos los archivos de vendedores
            procesarVentas(vendedores, productos);

            // Paso 3: Generar los dos reportes CSV solicitados
            generarReporteVendedores(vendedores);
            generarReporteProductos(productos);

            System.out.println("Reportes generados exitosamente!");
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    private static Map<Integer, Producto> leerProductos(String archivo) throws Exception {
        Map<Integer, Producto> productos = new HashMap<>();
        try (Scanner sc = new Scanner(new File(archivo), CHARSET)) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split(DELIMITER);
                if (partes.length >= 3) {
                    int id = Integer.parseInt(partes[0].trim());
                    String nombre = partes[1].trim();
                    double precio = Double.parseDouble(partes[2].trim());
                    productos.put(id, new Producto(id, nombre, precio));
                }
            }
        }
        return productos;
    }

    private static Map<Long, Vendedor> leerVendedores(String archivo) throws Exception {
        Map<Long, Vendedor> vendedores = new HashMap<>();
        try (Scanner sc = new Scanner(new File(archivo), CHARSET)) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split(DELIMITER);
                if (partes.length >= 4) {
                    long id = Long.parseLong(partes[1].trim());
                    String nombre = partes[2].trim();
                    String apellido = partes[3].trim();
                    vendedores.put(id, new Vendedor(id, nombre, apellido));
                }
            }
        }
        return vendedores;
    }

    private static void procesarVentas(Map<Long, Vendedor> vendedores, Map<Integer, Producto> productos) {
        for (Vendedor v : vendedores.values()) {
            File f = new File("vendedor_" + v.id + ".csv");
            if (!f.exists()) continue;

            try (Scanner sc = new Scanner(f, CHARSET)) {
                if (sc.hasNextLine()) sc.nextLine(); // Saltar linea de encabezado del vendedor
                while (sc.hasNextLine()) {
                    String linea = sc.nextLine();
                    String[] partes = linea.split(DELIMITER);
                    if (partes.length >= 2) {
                        int pId = Integer.parseInt(partes[0].trim());
                        int cant = Integer.parseInt(partes[1].trim());
                        Producto p = productos.get(pId);
                        if (p != null) {
                            v.totalRecaudado += (p.precio * cant);
                            p.cantidadVendida += cant;
                        }
                    }
                }
            } catch (Exception e) {
                // Se ignora el archivo si tiene formato erroneo pero se continua con los demas
            }
        }
    }

    private static void generarReporteVendedores(Map<Long, Vendedor> vendedores) throws IOException {
        List<Vendedor> lista = new ArrayList<>(vendedores.values());
        // Ordenar de mayor a menor dinero recaudado
        lista.sort((a, b) -> Double.compare(b.totalRecaudado, a.totalRecaudado));

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("reporte_vendedores.csv"), CHARSET)))) {
            for (Vendedor v : lista) {
                writer.println(v.nombre + " " + v.apellido + DELIMITER + v.totalRecaudado);
            }
        }
    }

    private static void generarReporteProductos(Map<Integer, Producto> productos) throws IOException {
        List<Producto> lista = new ArrayList<>(productos.values());
        // Ordenar de mayor a menor cantidad vendida
        lista.sort((a, b) -> Integer.compare(b.cantidadVendida, a.cantidadVendida));

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("reporte_productos.csv"), CHARSET)))) {
            for (Producto p : lista) {
                writer.println(p.nombre + DELIMITER + p.precio + DELIMITER + p.cantidadVendida);
            }
        }
    }
}

// Clases auxiliares para manejo de datos
class Producto {
    int id; String nombre; double precio; int cantidadVendida = 0;
    Producto(int i, String n, double p) { id = i; nombre = n; precio = p; }
}

class Vendedor {
    long id; String nombre; String apellido; double totalRecaudado = 0;
    Vendedor(long i, String n, String a) { id = i; nombre = n; apellido = a; }
}