package types;

public class sensor_valor {

	private int id_sensor_valor;
	private int id_sensor;
	private float valor;
	private float precision_valor;
	private long tiempo;
	
	public sensor_valor() {
		super();
	}

	public sensor_valor(int id_sensor_valor, int id_sensor, float valor, float precision_valor, long tiempo) {
		super();
		this.id_sensor_valor = id_sensor_valor;
		this.id_sensor = id_sensor;
		this.valor = valor;
		this.precision_valor = precision_valor;
		this.tiempo = tiempo;
	}

	public int getId_sensor_valor() {
		return id_sensor_valor;
	}

	public void setId_sensor_valor(int id_sensor_valor) {
		this.id_sensor_valor = id_sensor_valor;
	}

	public int getId_sensor() {
		return id_sensor;
	}

	public void setId_sensor(int id_sensor) {
		this.id_sensor = id_sensor;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public float getPrecision_valor() {
		return precision_valor;
	}

	public void setPrecision_valor(float precision_valor) {
		this.precision_valor = precision_valor;
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
		result = prime * result + Float.floatToIntBits(precision_valor);
		result = prime * result + id_sensor;
		result = prime * result + id_sensor_valor;
		result = prime * result + (int) (tiempo ^ (tiempo >>> 32));
		result = prime * result + Float.floatToIntBits(valor);
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
		sensor_valor other = (sensor_valor) obj;
		if (Float.floatToIntBits(precision_valor) != Float.floatToIntBits(other.precision_valor))
			return false;
		if (id_sensor != other.id_sensor)
			return false;
		if (id_sensor_valor != other.id_sensor_valor)
			return false;
		if (tiempo != other.tiempo)
			return false;
		if (Float.floatToIntBits(valor) != Float.floatToIntBits(other.valor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "sensor_valor [id_sensor_valor=" + id_sensor_valor + ", id_sensor=" + id_sensor + ", valor=" + valor
				+ ", precision_valor=" + precision_valor + ", tiempo=" + tiempo + "]";
	}
	
}
