package types;

public class dispositivo {
	
	private int id_dispositivo;
	private String ip;
	private String nombre;
	private int id_planta;
	private long tiempoInicial;
	
	public dispositivo() {
		super();
	}
	
	public dispositivo(int id_dispositivo, String ip, String nombre, int id_planta, long tiempoInicial) {
		super();
		this.id_dispositivo = id_dispositivo;
		this.ip = ip;
		this.nombre = nombre;
		this.id_planta = id_planta;
		this.tiempoInicial = tiempoInicial;
	}

	public int getId_dispositivo() {
		return id_dispositivo;
	}

	public void setId_dispositivo(int id_dispositivo) {
		this.id_dispositivo = id_dispositivo;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getId_planta() {
		return id_planta;
	}

	public void setId_planta(int id_planta) {
		this.id_planta = id_planta;
	}

	public long getTiempoInicial() {
		return tiempoInicial;
	}

	public void setTiempoInicial(long tiempoInicial) {
		this.tiempoInicial = tiempoInicial;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id_dispositivo;
		result = prime * result + id_planta;
		result = prime * result + (int) (tiempoInicial ^ (tiempoInicial >>> 32));
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		dispositivo other = (dispositivo) obj;
		if (id_dispositivo != other.id_dispositivo)
			return false;
		if (id_planta != other.id_planta)
			return false;
		if (tiempoInicial != other.tiempoInicial)
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "dispositivo [id_dispositivo=" + id_dispositivo + ", ip=" + ip + ", nombre=" + nombre + ", id_planta="
				+ id_planta + ", tiempoInicial=" + tiempoInicial + "]";
	}
	
}
