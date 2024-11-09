import java.util.ArrayList;

public class Usuario {
    private int id;
    private String nombre;
    private String contraseña;
    private boolean esAdministrador;
    private ArrayList<Reserva> reservas;
    private ArrayList<Reserva> historialReservas;

    public Usuario(int id, String nombre, String contraseña, boolean esAdministrador) {
        this.id = id;
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.esAdministrador = esAdministrador;
        this.reservas = new ArrayList<>();
        this.historialReservas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }
    
    public int getId() {
        return id;
    }

    public boolean esAdministrador() {
        return esAdministrador;
    }

    public void solicitarReserva(Reserva reserva) {
        reservas.add(reserva);
        historialReservas.add(reserva);
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public boolean eliminarReserva(String reservaSeleccionada) {
        for (Reserva reserva : reservas) {
            if (reserva.toString().equals(reservaSeleccionada)) {
                reservas.remove(reserva);
                return true;
            }
        }
        return false;
    }

    public void eliminarReservas(String reservaStr) {
        reservas.removeIf(reserva -> reserva.toString().equals(reservaStr));
        
        // Buscar la reserva eliminada en el historial
        for (Reserva reserva : historialReservas) {
            if (reserva.toString().equals(reservaStr)) {
                reserva.marcarComoEliminada(); // Marcar como eliminada (metodo que se agrega en la clase Reserva)
                break;
            }
        }
    }
    
    public boolean validarCredenciales(int id, String contraseña) {
        return this.id == id && this.contraseña.equals(contraseña);
    }

    public void login(int id, String contraseña) {
        if (validarCredenciales(id, contraseña)) {
            System.out.println("Bienvenido " + nombre);
        } else {
            System.out.println("Usuario o contraseña incorrectos");
        }
    }
}
