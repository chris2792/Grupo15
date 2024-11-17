package programacion.grupo15_sc._mv_avanceno2;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Movimientos extends Usuario { // Hereda de la clase Usuario

    private String transaccion;
    private int monto;
    private int saldo;
    private static final String FOLDER_PATH = "movimientos/";  // Carpeta dentro del directorio del proyecto
    private String cuenta;

    // Constructor por defecto
    public Movimientos(String cuenta, int saldo) {
        super();  // Llamada al constructor de la clase Usuario
        this.cuenta = cuenta;
        this.saldo = saldo;
    }

    // Getter - Setter
    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    // Método para obtener el nombre del archivo de transacciones de un usuario específico
    private String obtenerArchivoTransacciones() {
        // Asegurarse de que la carpeta existe antes de guardar el archivo
        File folder = new File(FOLDER_PATH);
        if (!folder.exists()) {
            folder.mkdirs();  // Crea la carpeta si no existe
        }
        return FOLDER_PATH + getCuenta() + "_movimientos.extension";
    }

    // Método para leer el saldo desde el archivo correspondiente al usuario
    public int leerSaldoDesdeArchivo() throws IOException {
        int saldo = 0;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(obtenerArchivoTransacciones()))) {
            List<String> transacciones = (List<String>) ois.readObject();
            if (!transacciones.isEmpty()) {
                String ultimaTransaccion = transacciones.get(transacciones.size() - 1);
                String[] partes = ultimaTransaccion.split(", ");
                for (String parte : partes) {
                    if (parte.contains("Saldo:")) {
                        saldo = Integer.parseInt(parte.split(": ")[1]);
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            saldo = 0; // Si el archivo no existe, el saldo es 0 por defecto
        } catch (IOException | ClassNotFoundException e) {
            throw new IOException("Error al leer el saldo desde el archivo.", e);
        }
        return saldo;
    }

    // Método para actualizar el saldo en el archivo correspondiente al usuario
    private void actualizarSaldoEnArchivo(int nuevoSaldo) throws IOException {
        List<String> transacciones = leerTransacciones(); // Leer las transacciones existentes

        if (transacciones.isEmpty()) {
            // Si es la primera transacción, agregar un ingreso inicial con saldo 0
            transacciones.add("Cuenta: " + getCuenta() + ", Tipo: Ingreso inicial, Monto: 0, Saldo: " + nuevoSaldo);
        } else {
            // Si ya hay transacciones, actualizar el saldo de la última transacción
            String ultimaTransaccion = transacciones.get(transacciones.size() - 1);
            transacciones.set(transacciones.size() - 1, ultimaTransaccion.replaceAll("Saldo: \\d+", "Saldo: " + nuevoSaldo));
        }

        // Escribir las transacciones actualizadas
        escribirTransacciones(transacciones);
    }

    // Método para leer las transacciones del archivo del usuario
    public List<String> leerTransacciones() {
        List<String> transacciones = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(obtenerArchivoTransacciones()))) {
            transacciones = (List<String>) ois.readObject();
        } catch (FileNotFoundException e) {
            // El archivo no existe, lo que es esperado si es la primera vez que el usuario realiza una transacción
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error al leer las transacciones: " + e.getMessage());
        }
        return transacciones;
    }

    // Método para registrar las transacciones
    private void registro(String tipo, int monto) {
        List<String> transacciones = leerTransacciones();  // Leer las transacciones existentes

        // Obtener el saldo actualizado del objeto
        int saldo = getSaldo();

        // Añadir la nueva transacción
        String transaccion = "Cuenta: " + getCuenta() + ", Tipo: " + tipo + ", Monto: " + monto + ", Saldo: " + saldo;
        transacciones.add(transaccion);

        // Escribir la lista de transacciones actualizada
        escribirTransacciones(transacciones);
    }

    // Método para escribir una lista de transacciones en el archivo correspondiente al usuario
    private void escribirTransacciones(List<String> transacciones) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(obtenerArchivoTransacciones()))) {
            oos.writeObject(transacciones);  // Escribe la lista de transacciones en el archivo
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al escribir las transacciones: " + e.getMessage());
        }
    }

    // Método para manejar depósitos
    public void deposito(int monto) {
        try {
            int saldo = leerSaldoDesdeArchivo();
            if (monto <= 0) {
                JOptionPane.showMessageDialog(null, "El monto a depositar debe ser mayor que 0.");
                return;
            }

            saldo += monto;
            setSaldo(saldo); // Actualizamos el saldo en el objeto Movimientos
            actualizarSaldoEnArchivo(saldo); // Actualizamos el saldo en el archivo
            registro("Depósito", monto);
            JOptionPane.showMessageDialog(null, "Depósito exitoso. Su nuevo saldo es: " + saldo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al acceder al archivo: " + e.getMessage());
        }
    }

    // Método para manejar retiros
    public void retiro(int monto) {
        try {
            int saldo = leerSaldoDesdeArchivo();
            if (monto > saldo) {
                JOptionPane.showMessageDialog(null, "Saldo insuficiente. Su saldo actual es: " + saldo);
            } else {
                saldo -= monto;
                setSaldo(saldo);
                actualizarSaldoEnArchivo(saldo);
                registro("Retiro", monto);
                JOptionPane.showMessageDialog(null, "Retiro exitoso. Su nuevo saldo es: " + saldo);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al acceder al archivo: " + e.getMessage());
        }
    }
}
