import javax.swing.*;
import java.util.ArrayList;

public class SistemaReservas {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ArrayList<Usuario> usuarios = new ArrayList<>();
            usuarios.add(new Usuario(1, "admin", "123", true)); // Administrador
            usuarios.add(new Usuario(2, "usuario", "123", true)); // Usuario Deportivo
 

            // Cuadro de diálogo de inicio de sesión
            String nombreUsuario = JOptionPane.showInputDialog("Ingrese su nombre de usuario:");
            String contraseña = JOptionPane.showInputDialog("Ingrese su contraseña:");

            Usuario usuarioActual = null;
            for (Usuario usuario : usuarios) {
                if (usuario.getNombre().equals(nombreUsuario) && usuario.validarCredenciales(usuario.getId(), contraseña)) {
                    usuarioActual = usuario;
                    break;
                }
            }

            if (usuarioActual == null) {
                JOptionPane.showMessageDialog(null, "Acceso denegado. Usuario o contraseña incorrectos.");
                return;
            }

            JFrame frame = new JFrame("Sistema de Reservas de Espacios Deportivos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new Interfaz(usuarios, usuarioActual));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
