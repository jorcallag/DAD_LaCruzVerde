package types;

public class planta {
	
	private int id_planta;
	private String nombre_planta; 
	private float temp_amb_planta;
	private float humed_tierra_planta;
	private float humed_amb_planta;
	
	public planta(int id_planta, String nombre_planta, float temp_amb_planta, float humed_tierra_planta, float humed_amb_planta) {
		super();
		this.id_planta = id_planta;
		this.nombre_planta = nombre_planta;
		this.temp_amb_planta = temp_amb_planta;
		this.humed_tierra_planta = humed_tierra_planta;
		this.humed_amb_planta = humed_amb_planta;
	}

	public int getId_planta() {
		return id_planta;
	}

	public void setId_planta(int id_planta) {
		this.id_planta = id_planta;
	}

	public String getNombre_planta() {
		return nombre_planta;
	}

	public void setNombre_planta(String nombre_planta) {
		this.nombre_planta = nombre_planta;
	}

	public float getTemp_amb_planta() {
		return temp_amb_planta;
	}

	public void setTemp_amb_planta(float temp_amb_planta) {
		this.temp_amb_planta = temp_amb_planta;
	}

	public float getHumed_tierra_planta() {
		return humed_tierra_planta;
	}

	public void setHumed_tierra_planta(float humed_tierra_planta) {
		this.humed_tierra_planta = humed_tierra_planta;
	}

	public float getHumed_amb_planta() {
		return humed_amb_planta;
	}

	public void setHumed_amb_planta(float humed_amb_planta) {
		this.humed_amb_planta = humed_amb_planta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(humed_amb_planta);
		result = prime * result + Float.floatToIntBits(humed_tierra_planta);
		result = prime * result + id_planta;
		result = prime * result + ((nombre_planta == null) ? 0 : nombre_planta.hashCode());
		result = prime * result + Float.floatToIntBits(temp_amb_planta);
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
		planta other = (planta) obj;
		if (Float.floatToIntBits(humed_amb_planta) != Float.floatToIntBits(other.humed_amb_planta))
			return false;
		if (Float.floatToIntBits(humed_tierra_planta) != Float.floatToIntBits(other.humed_tierra_planta))
			return false;
		if (id_planta != other.id_planta)
			return false;
		if (nombre_planta == null) {
			if (other.nombre_planta != null)
				return false;
		} else if (!nombre_planta.equals(other.nombre_planta))
			return false;
		if (Float.floatToIntBits(temp_amb_planta) != Float.floatToIntBits(other.temp_amb_planta))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "planta [id_planta=" + id_planta + ", nombre_planta=" + nombre_planta + ", temp_amb_planta="
				+ temp_amb_planta + ", humed_tierra_planta=" + humed_tierra_planta + ", humed_amb_planta="
				+ humed_amb_planta + "]";
	}

}
