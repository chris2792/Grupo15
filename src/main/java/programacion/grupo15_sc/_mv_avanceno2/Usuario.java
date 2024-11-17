
package programacion.grupo15_sc._mv_avanceno2;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements Comparable<Usuario>, Serializable  {
    
    private String nombre;
    private String cuenta;
    private String pin;
    private int saldo;  // Nuevo atributo para el saldo del usuario
    private final ArrayList<Usuario> Clientes = new ArrayList<>();

    // Constructor
    public Usuario() {}

    public Usuario(String nombre, String cuenta, String pin, int saldo) {
        this.nombre = nombre;
        this.cuenta = cuenta;
        this.pin = pin;
        this.saldo = saldo; // Inicializamos el saldo
    }

    // Getter - Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    // Métodos para manejar el archivo de usuarios
    public static void EscribirUsuarios(List<Usuario> usuarios) {
        try {
            FileOutputStream miArchivo = new FileOutputStream("Usuarios.extension");
            ObjectOutputStream output = new ObjectOutputStream(miArchivo);
            output.writeObject(usuarios);
        } catch (IOException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    public static List<Usuario> LeerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            FileInputStream miArchivo2 = new FileInputStream("Usuarios.extension");
            ObjectInputStream input = new ObjectInputStream(miArchivo2);
            usuarios = (List<Usuario>) input.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return usuarios;
    }

    // Método para agregar un usuario a la lista y guardar en el archivo
    public static void agregarUsuario(Usuario nuevoUsuario) {
        List<Usuario> usuarios = LeerUsuarios();
        usuarios.add(nuevoUsuario);
        EscribirUsuarios(usuarios);
    }

    // Método para verificar el usuario y la contraseña ingresados
    public static boolean verificarUsuario(String usuarioIngresado, String contraseñaIngresada) {
        List<Usuario> usuarios = LeerUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().equals(usuarioIngresado) && usuario.getPin().equals(contraseñaIngresada)) {
                return true;
            }
        }
        return false;
    }

    // Método para obtener el usuario
    public static Usuario obtenerUsuario(String usuarioIngresado, String contraseñaIngresada) {
        List<Usuario> usuarios = LeerUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().equals(usuarioIngresado) && usuario.getPin().equals(contraseñaIngresada)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public int compareTo(Usuario otro) {
        return this.nombre.compareTo(otro.nombre);
    }

    @Override
    public String toString() {
        return "Usuario{" + "nombre='" + nombre + '\'' + ", cuenta=" + cuenta + '\'' + ", pin=" + pin + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Usuario)) return false;
        Usuario other = (Usuario) obj;
        return this.cuenta.equals(other.cuenta); // Comparar por cuenta
    }

    @Override
    public int hashCode() {
        return cuenta.hashCode(); // Generar el hash basado en la cuenta
    }
}
