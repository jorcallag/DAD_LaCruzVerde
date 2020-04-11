package types;

public class actuador_valor {
	
	private int id_actuador_valor;
	private int id_actuador;
	private boolean on;
	private long tiempo;
	
	public actuador_valor() {
		super();
	}

	public actuador_valor(int id_actuador_valor, int id_actuador, boolean on, long tiempo) {
		super();
		this.id_actuador_valor = id_actuador_valor;
		this.id_actuador = id_actuador;
		this.on = on;
		this.tiempo = tiempo;
	}

	public int getId_actuador_valor() {
		return id_actuador_valor;
	}

	public void setId_actuador_valor(int id_actuador_valor) {
		this.id_actuador_valor = id_actuador_valor;
	}

	public int getId_actuador() {
		return id_actuador;
	}

	public void setId_actuador(int id_actuador) {
		this.id_actuador = id_actuador;
	}

	public boolean getOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}

	public long getTiempo() {
		return tiempo;
	}

	public void setTiempo(long tiempo) {
		this.tiempo = tiempo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id_actuador;
		result = prime * result + id_actuador_valor;
		result = prime * result + (on ? 1231 : 1237);
		result = prime * result + (int) (tiempo ^ (tiempo >>> 32));
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
		actuador_valor other = (actuador_valor) obj;
		if (id_actuador != other.id_actuador)
			return false;
		if (id_actuador_valor != other.id_actuador_valor)
			return false;
		if (on != other.on)
			return false;
		if (tiempo != other.tiempo)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "actuador_valor [id_actuador_valor=" + id_actuador_valor + ", id_actuador=" + id_actuador + ", on=" + on
				+ ", tiempo=" + tiempo + "]";
	}

}
