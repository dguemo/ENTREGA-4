import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Interfaz extends JPanel {
    private JTabbedPane tabbedPane;
    private ArrayList<Usuario> usuarios;
    private Usuario usuarioActual;
    private ArrayList<Reserva> reservasGlobales;
    private HashMap<String, ArrayList<Reserva>> reservasPorEspacio;
    private JTable tablaCalendarioGeneral;
    private JPanel panelEliminarReserva;
    private DefaultListModel<Reserva> modeloListaEliminarReserva;

    public Interfaz(ArrayList<Usuario> usuarios, Usuario usuarioActual) {
        this.usuarios = usuarios;
        this.usuarioActual = usuarioActual;
        this.reservasGlobales = new ArrayList<>();
        this.reservasPorEspacio = new HashMap<>();

        String[] espacios = {"Cancha de tenis de mesa", "Cancha de voleibol", "Cancha de baloncesto", "Cancha de fútbol sintética", "Cancha de fútbol de pasto"};
        for (String espacio : espacios) {
            reservasPorEspacio.put(espacio, new ArrayList<>());
        }

        tabbedPane = new JTabbedPane();
        if (usuarioActual.esAdministrador()) {
            agregarPanelesAdministrador();
        } else {
            agregarPanelesUsuarioDeportivo();
        }

        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
    }

    
    private void agregarPanelesAdministrador() {
        tabbedPane.addTab("Calendario General", crearPanelCalendarioGeneral());
        tabbedPane.addTab("Gestión de Espacios", crearPanelGestionEspacios());
        tabbedPane.addTab("Agregar Reserva", crearPanelAgregarReserva());
        tabbedPane.addTab("Eliminar Reserva", crearPanelEliminarReserva());
    }

    private void agregarPanelesUsuarioDeportivo() {
    tabbedPane.addTab("Solicitar Reserva", crearPanelSolicitarReserva());
    tabbedPane.addTab("Observar Disponibilidad", crearPanelObservarDisponibilidad());
    tabbedPane.addTab("Consultar Historial", crearPanelConsultarHistorial());
    tabbedPane.addTab("Cancelar Reserva", crearPanelEliminarReserva());
}

    private JPanel crearPanelObservarDisponibilidad() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Observar disponibilidad de espacios deportivos:"));
        
        String[] espacios = {"Cancha de tenis de mesa", "Cancha de voleibol", "Cancha de baloncesto", "Cancha de fútbol sintética", "Cancha de fútbol de pasto"};
        for (String espacio : espacios) {
            JButton botonEspacio = new JButton(espacio);
            botonEspacio.addActionListener(e -> mostrarCalendarioEspacio(espacio));
            panel.add(botonEspacio);
        }
        
        return panel;
    }

    private JPanel crearPanelCalendarioGeneral() {
        JPanel panel = new JPanel(new BorderLayout());
        tablaCalendarioGeneral = crearTablaHorario();
        panel.add(new JLabel("Calendario general de todas las reservas."), BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaCalendarioGeneral), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelGestionEspacios() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Gestión de espacios deportivos:"));
        
        String[] espacios = {"Cancha de tenis de mesa", "Cancha de voleibol", "Cancha de baloncesto", "Cancha de fútbol sintética", "Cancha de fútbol de pasto"};
        for (String espacio : espacios) {
            JButton botonEspacio = new JButton(espacio);
            botonEspacio.addActionListener(e -> mostrarCalendarioEspacio(espacio));
            panel.add(botonEspacio);
        }
        
        return panel;
    }

    private void mostrarCalendarioEspacio(String espacio) {
        ArrayList<Reserva> reservasEspacio = reservasPorEspacio.get(espacio);
        JTable tablaEspacio = crearTablaHorario();
        mostrarReservasEnTabla(tablaEspacio, reservasEspacio);

        JOptionPane.showMessageDialog(this, new JScrollPane(tablaEspacio), "Calendario de " + espacio, JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel crearPanelAgregarReserva() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton btnAgregarReserva = new JButton("Agregar Reserva");

        btnAgregarReserva.addActionListener(e -> mostrarDialogoAgregarReserva());

        panel.add(new JLabel("Agregar una nueva reserva."), BorderLayout.NORTH);
        panel.add(btnAgregarReserva, BorderLayout.CENTER);
        return panel;
    }

    private void mostrarDialogoAgregarReserva() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Seleccionar Día y Hora");
        dialog.setLayout(new GridLayout(4, 2));

        JLabel labelDia = new JLabel("Día:");
        JLabel labelHora = new JLabel("Hora:");
        JLabel labelEspacio = new JLabel("Espacio:");

        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        JComboBox<String> comboDia = new JComboBox<>(dias);

        String[] horas = {"6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17"};
        JComboBox<String> comboHora = new JComboBox<>(horas);

        String[] espacios = {"Cancha de tenis de mesa", "Cancha de voleibol", "Cancha de baloncesto", "Cancha de fútbol sintética", "Cancha de fútbol de pasto"};
        JComboBox<String> comboEspacio = new JComboBox<>(espacios);

        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.addActionListener(e -> {
            String diaSeleccionado = (String) comboDia.getSelectedItem();
            int horaSeleccionada = Integer.parseInt((String) comboHora.getSelectedItem());
            String espacioSeleccionado = (String) comboEspacio.getSelectedItem();

            if (esReservaDisponible(diaSeleccionado, horaSeleccionada, espacioSeleccionado)) {
                Reserva nuevaReserva = new Reserva(diaSeleccionado, horaSeleccionada, espacioSeleccionado, "Lugar de " + espacioSeleccionado, usuarioActual);
                usuarioActual.solicitarReserva(nuevaReserva);
                reservasGlobales.add(nuevaReserva);
                reservasPorEspacio.get(espacioSeleccionado).add(nuevaReserva);

                actualizarCalendarios();
                actualizarListaEliminarReserva();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Espacio no disponible", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        dialog.add(labelDia);
        dialog.add(comboDia);
        dialog.add(labelHora);
        dialog.add(comboHora);
        dialog.add(labelEspacio);
        dialog.add(comboEspacio);
        dialog.add(btnConfirmar);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private boolean esReservaDisponible(String dia, int hora, String espacio) {
        for (Reserva reserva : reservasPorEspacio.get(espacio)) {
            if (reserva.getFecha().equals(dia) && reserva.getHora() == hora) {
                return false;
            }
        }
        return true;
    }

    private void actualizarCalendarios() {
        mostrarReservasEnTabla(tablaCalendarioGeneral, reservasGlobales);
    }

    private JPanel crearPanelEliminarReserva() {
        panelEliminarReserva = new JPanel(new BorderLayout());
        panelEliminarReserva.add(new JLabel("Eliminar una reserva"), BorderLayout.NORTH);

        modeloListaEliminarReserva = new DefaultListModel<>();
        for (Reserva reserva : usuarioActual.getReservas()) {
            modeloListaEliminarReserva.addElement(reserva);
        }

        JList<Reserva> listaReservas = new JList<>(modeloListaEliminarReserva);
        JButton btnEliminar = new JButton("Eliminar Reserva");

        btnEliminar.addActionListener(e -> {
            Reserva reservaSeleccionada = listaReservas.getSelectedValue();
            if (reservaSeleccionada != null) {
                usuarioActual.eliminarReserva(reservaSeleccionada.toString());
                modeloListaEliminarReserva.removeElement(reservaSeleccionada);
                reservasGlobales.remove(reservaSeleccionada);
                reservasPorEspacio.get(reservaSeleccionada.getTipoEspacio()).remove(reservaSeleccionada);

                actualizarCalendarios();
                actualizarListaEliminarReserva();
            }
        });

        panelEliminarReserva.add(new JScrollPane(listaReservas), BorderLayout.CENTER);
        panelEliminarReserva.add(btnEliminar, BorderLayout.SOUTH);
        return panelEliminarReserva;
    }

    private void actualizarListaEliminarReserva() {
        modeloListaEliminarReserva.clear();
        for (Reserva reserva : usuarioActual.getReservas()) {
            modeloListaEliminarReserva.addElement(reserva);
        }
    }

    private JTable crearTablaHorario() {
        String[] dias = {"Horas", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        String[][] datos = new String[12][8];

        for (int i = 0; i < 12; i++) {
            datos[i][0] = (6 + i) + ":00";
        }

        return new JTable(datos, dias);
    }

    private void mostrarReservasEnTabla(JTable tabla, ArrayList<Reserva> reservas) {
        for (int i = 1; i < tabla.getRowCount(); i++) {
            for (int j = 1; j < tabla.getColumnCount(); j++) {
                tabla.setValueAt("", i, j);
            }
        }

        for (Reserva reserva : reservas) {
            int diaIndex = switch (reserva.getFecha()) {
                case "Lunes" -> 1;
                case "Martes" -> 2;
                case "Miércoles" -> 3;
                case "Jueves" -> 4;
                case "Viernes" -> 5;
                case "Sábado" -> 6;
                case "Domingo" -> 7;
                default -> -1;
            };

            int horaIndex = reserva.getHora() - 6;
            if (horaIndex >= 0 && horaIndex < 12 && diaIndex >= 0) {
                tabla.setValueAt("Reservado", horaIndex, diaIndex);
            }
        }
    }

    private JPanel crearPanelSolicitarReserva() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton btnSolicitarReserva = new JButton("Solicitar Reserva");

        btnSolicitarReserva.addActionListener(e -> mostrarDialogoAgregarReserva());

        panel.add(new JLabel("Solicitar una nueva reserva."), BorderLayout.NORTH);
        panel.add(btnSolicitarReserva, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelConsultarHistorial() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Historial de reservas realizadas."), BorderLayout.NORTH);

        DefaultListModel<Reserva> modeloListaHistorial = new DefaultListModel<>();
        for (Reserva reserva : usuarioActual.getReservas()) {
            modeloListaHistorial.addElement(reserva);
        }

        JList<Reserva> listaHistorial = new JList<>(modeloListaHistorial);
        panel.add(new JScrollPane(listaHistorial), BorderLayout.CENTER);

        return panel;
    }
}
