public class Reserva {
    private String fecha;
    private int hora; // Puede ser en formato de 24 horas.
    private String tipoEspacio;
    private String lugar;
    private Usuario usuario;
    private boolean eliminada;

    public Reserva(String fecha, int hora, String tipoEspacio, String lugar, Usuario usuario) {
        this.fecha = fecha;
        this.hora = hora;
        this.tipoEspacio = tipoEspacio;
        this.lugar = lugar;
        this.usuario = usuario;
        this.eliminada = false;
    }

      public void marcarComoEliminada() {
        this.eliminada = true;
    }
    
    public boolean estaEliminada() {
        return eliminada;
    }
    
    public String getFecha() {
        return fecha;
    }

    public int getHora() {
        return hora;
    }

    public String getTipoEspacio() {
        return tipoEspacio;
    }

    public String getLugar() {
        return lugar;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "fecha='" + fecha + '\'' +
                ", hora=" + hora +
                ", tipoEspacio='" + tipoEspacio + '\'' +
                ", lugar='" + lugar + '\'' +
                '}';
    }
}
