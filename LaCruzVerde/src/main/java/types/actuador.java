package types;

public class actuador {
	
	private int id_actuador;
	private String tipo;
	private String nombre;
	private int id_dispositivo;
	
	public actuador() {
		super();
	}
	
	public actuador(int id_actuador, String tipo, String nombre, int id_dispositivo) {
		super();
		this.id_actuador = id_actuador;
		this.tipo = tipo;
		this.nombre = nombre;
		this.id_dispositivo = id_dispositivo;
	}

	public int getId_actuador() {
		return id_actuador;
	}

	public void setId_actuador(int id_actuador) {
		this.id_actuador = id_actuador;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getId_dispositivo() {
		return id_dispositivo;
	}

	public void setId_dispositivo(int id_dispositivo) {
		this.id_dispositivo = id_dispositivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id_actuador;
		result = prime * result + id_dispositivo;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		actuador other = (actuador) obj;
		if (id_actuador != other.id_actuador)
			return false;
		if (id_dispositivo != other.id_dispositivo)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "actuador [id_actuador=" + id_actuador + ", tipo=" + tipo + ", nombre=" + nombre + ", id_dispositivo="
				+ id_dispositivo + "]";
	}
	
}
