package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Dispositivo {
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("tipo")
	private TipoDispositivo tipo;
	
	@JsonProperty("descripcion")
	private String descripcion;
	
	@JsonProperty("proveedor")
	private String proveedor;
	
	@JsonProperty("downstream")
	private String downstream;
	
	@JsonProperty("upstream")
	private String upstream;
	
	@JsonProperty("descripcion")
	private String atenuacion;
	
	@JsonProperty("mac1_1")
	private String mac1_1;
	
	@JsonProperty("mac1_2")
	private String mac1_2;
	
	@JsonProperty("mac2_1")
	private String mac2_1;
	
	@JsonProperty("mac2_2")
	private String mac2_2;
	
	@JsonProperty("mac3_1")
	private String mac3_1;
	
	@JsonProperty("mac3_2")
	private String mac3_2;
	
	@JsonProperty("mac4_1")
	private String mac4_1;
	
	@JsonProperty("mac4_2")
	private String mac4_2;
	
	@JsonProperty("mac5_1")
	private String mac5_1;
	
	@JsonProperty("mac5_2")
	private String mac5_2;
	
	@JsonProperty("mac6_1")
	private String mac6_1;
	
	@JsonProperty("mac6_2")
	private String mac6_2;
	
	@JsonProperty("eocMac1_1")
	private String eocMac1_1;
	
	@JsonProperty("eocMac1_2")
	private String eocMac1_2;
	
	@JsonProperty("eocMac2_1")
	private String eocMac2_1;
	
	@JsonProperty("eocMac2_2")
	private String eocMac2_2;
	
	@JsonProperty("eocMac3_1")
	private String eocMac3_1;
	
	@JsonProperty("eocMac3_2")
	private String eocMac3_2;
	
	@JsonProperty("eocMac4_1")
	private String eocMac4_1;
	
	@JsonProperty("eocMac4_2")
	private String eocMac4_2;
	
	@JsonProperty("eocMac5_1")
	private String eocMac5_1;
	
	@JsonProperty("eocMac5_2")
	private String eocMac5_2;
	
	@JsonProperty("eocMac6_1")
	private String eocMac6_1;
	
	@JsonProperty("eocMac6_2")
	private String eocMac6_2;
	
	
	

	public Dispositivo() {
		super();
	}
	
	public Dispositivo(
			@JsonProperty("id") Long id, 
			@JsonProperty("tipo") TipoDispositivo tipo,
			@JsonProperty("descripcion") String descripcion,
			@JsonProperty("proveedor") String proveedor,
			@JsonProperty("downstream") String downstream,
			@JsonProperty("upstream") String upstream,
			@JsonProperty("atenuacion") String atenuacion,
			@JsonProperty("mac1_1") String mac1_1, 
			@JsonProperty("mac1_2") String mac1_2, 
			@JsonProperty("mac2_1") String mac2_1, 
			@JsonProperty("mac2_2") String mac2_2, 
			@JsonProperty("mac3_1") String mac3_1, 
			@JsonProperty("mac3_2") String mac3_2,
			@JsonProperty("mac4_1") String mac4_1, 
			@JsonProperty("mac4_2") String mac4_2,
			@JsonProperty("mac5_1") String mac5_1, 
			@JsonProperty("mac5_2") String mac5_2,
			@JsonProperty("mac6_1") String mac6_1, 
			@JsonProperty("mac6_2") String mac6_2,
			@JsonProperty("eocMac1_1") String eocMac1_1, 
			@JsonProperty("eocMac1_2") String eocMac1_2, 
			@JsonProperty("eocMac2_1") String eocMac2_1, 
			@JsonProperty("eocMac2_2") String eocMac2_2, 
			@JsonProperty("eocMac3_1") String eocMac3_1, 
			@JsonProperty("eocMac3_2") String eocMac3_2,
			@JsonProperty("eocMac4_1") String eocMac4_1, 
			@JsonProperty("eocMac4_2") String eocMac4_2,
			@JsonProperty("eocMac5_1") String eocMac5_1, 
			@JsonProperty("eocMac5_2") String eocMac5_2,
			@JsonProperty("eocMac6_1") String eocMac6_1, 
			@JsonProperty("eocMac6_2") String eocMac6_2) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.proveedor = proveedor;
		this.downstream = downstream;
		this.upstream = upstream;
		this.atenuacion = atenuacion;
		this.mac1_1 = mac1_1;
		this.mac1_2 = mac1_2;
		this.mac2_1 = mac2_1;
		this.mac2_2 = mac2_2;
		this.mac3_1 = mac3_1;
		this.mac3_2 = mac3_2;
		this.mac4_1 = mac4_1;
		this.mac4_2 = mac4_2;
		this.mac5_1 = mac5_1;
		this.mac5_2 = mac5_2;
		this.mac6_1 = mac5_1;
		this.mac6_2 = mac5_2;
		this.eocMac1_1 = eocMac1_1;
		this.eocMac1_2 = eocMac1_2;
		this.eocMac2_1 = eocMac2_1;
		this.eocMac2_2 = eocMac2_2;
		this.eocMac3_1 = eocMac3_1;
		this.eocMac3_2 = eocMac3_2;
		this.eocMac4_1 = eocMac4_1;
		this.eocMac4_2 = eocMac4_2;
		this.eocMac5_1 = eocMac5_1;
		this.eocMac5_2 = eocMac5_2;
		this.eocMac6_1 = eocMac5_1;
		this.eocMac6_2 = eocMac5_2;
	}


	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getDownstream() {
		return downstream;
	}

	public void setDownstream(String downstream) {
		this.downstream = downstream;
	}

	public String getUpstream() {
		return upstream;
	}

	public void setUpstream(String upstream) {
		this.upstream = upstream;
	}

	public String getAtenuacion() {
		return atenuacion;
	}

	public void setAtenuacion(String atenuacion) {
		this.atenuacion = atenuacion;
	}

	public String getMac5_1() {
		return mac5_1;
	}

	public void setMac5_1(String mac5_1) {
		this.mac5_1 = mac5_1;
	}

	public String getMac5_2() {
		return mac5_2;
	}

	public void setMac5_2(String mac5_2) {
		this.mac5_2 = mac5_2;
	}

	public String getMac6_1() {
		return mac6_1;
	}

	public void setMac6_1(String mac6_1) {
		this.mac6_1 = mac6_1;
	}

	public String getMac6_2() {
		return mac6_2;
	}

	public void setMac6_2(String mac6_2) {
		this.mac6_2 = mac6_2;
	}

	public String getEocMac1_1() {
		return eocMac1_1;
	}

	public void setEocMac1_1(String eocMac1_1) {
		this.eocMac1_1 = eocMac1_1;
	}

	public String getEocMac1_2() {
		return eocMac1_2;
	}

	public void setEocMac1_2(String eocMac1_2) {
		this.eocMac1_2 = eocMac1_2;
	}

	public String getEocMac2_1() {
		return eocMac2_1;
	}

	public void setEocMac2_1(String eocMac2_1) {
		this.eocMac2_1 = eocMac2_1;
	}

	public String getEocMac2_2() {
		return eocMac2_2;
	}

	public void setEocMac2_2(String eocMac2_2) {
		this.eocMac2_2 = eocMac2_2;
	}

	public String getEocMac3_1() {
		return eocMac3_1;
	}

	public void setEocMac3_1(String eocMac3_1) {
		this.eocMac3_1 = eocMac3_1;
	}

	public String getEocMac3_2() {
		return eocMac3_2;
	}

	public void setEocMac3_2(String eocMac3_2) {
		this.eocMac3_2 = eocMac3_2;
	}

	public String getEocMac4_1() {
		return eocMac4_1;
	}

	public void setEocMac4_1(String eocMac4_1) {
		this.eocMac4_1 = eocMac4_1;
	}

	public String getEocMac4_2() {
		return eocMac4_2;
	}

	public void setEocMac4_2(String eocMac4_2) {
		this.eocMac4_2 = eocMac4_2;
	}

	public String getEocMac5_1() {
		return eocMac5_1;
	}

	public void setEocMac5_1(String eocMac5_1) {
		this.eocMac5_1 = eocMac5_1;
	}

	public String getEocMac5_2() {
		return eocMac5_2;
	}

	public void setEocMac5_2(String eocMac5_2) {
		this.eocMac5_2 = eocMac5_2;
	}

	public String getEocMac6_1() {
		return eocMac6_1;
	}

	public void setEocMac6_1(String eocMac6_1) {
		this.eocMac6_1 = eocMac6_1;
	}

	public String getEocMac6_2() {
		return eocMac6_2;
	}

	public void setEocMac6_2(String eocMac6_2) {
		this.eocMac6_2 = eocMac6_2;
	}

	public TipoDispositivo getTipo() {
		return tipo;
	}

	public void setTipo(TipoDispositivo tipo) {
		this.tipo = tipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMac1_1() {
		return mac1_1;
	}

	public void setMac1_1(String mac1_1) {
		this.mac1_1 = mac1_1;
	}

	public String getMac1_2() {
		return mac1_2;
	}

	public void setMac1_2(String mac1_2) {
		this.mac1_2 = mac1_2;
	}

	public String getMac2_1() {
		return mac2_1;
	}

	public void setMac2_1(String mac2_1) {
		this.mac2_1 = mac2_1;
	}

	public String getMac2_2() {
		return mac2_2;
	}

	public void setMac2_2(String mac2_2) {
		this.mac2_2 = mac2_2;
	}

	public String getMac3_1() {
		return mac3_1;
	}

	public void setMac3_1(String mac3_1) {
		this.mac3_1 = mac3_1;
	}

	public String getMac3_2() {
		return mac3_2;
	}

	public void setMac3_2(String mac3_2) {
		this.mac3_2 = mac3_2;
	}

	public String getMac4_1() {
		return mac4_1;
	}

	public void setMac4_1(String mac4_1) {
		this.mac4_1 = mac4_1;
	}

	public String getMac4_2() {
		return mac4_2;
	}

	public void setMac4_2(String mac4_2) {
		this.mac4_2 = mac4_2;
	}
	
	
	

}
