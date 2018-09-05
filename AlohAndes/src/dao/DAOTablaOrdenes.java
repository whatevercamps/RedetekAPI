package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Plan;
import vos.Dispositivo;
import vos.Orden;


public class DAOTablaOrdenes {


	public static final int BUSQUEDA_POR_ID = 1;
	public static final int BUSQUEDA_POR_CLIENTE = 2;
	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaOrdenes() {
		recursos = new ArrayList<Object>();
	}


	public void cerrarRecursos() {
		for(Object ob : recursos){
			if(ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}

	public void setConn(Connection conn) throws SQLException {
		this.conn = conn;
	}



	public List<Orden> darOrdenesPor(int filtro, String parametro) throws SQLException, Exception {
		List<Orden> ordenes = new ArrayList<Orden>();
		String sql = "SELECT * FROM ORDENES ORD, PLANES PLA "
				+ "WHERE ID_PLAN = PLA.ID";

		switch(filtro) {

		case BUSQUEDA_POR_ID:
			sql += " AND ORD.ID = " + parametro + " AND ROWNUM <= 1";
			break;
		
		case BUSQUEDA_POR_CLIENTE:
			sql += " AND ID_CLIENTE=" + parametro;
			break;
		
		default:
			break;
		}


		PreparedStatement st = conn.prepareStatement(sql);
		recursos.add(st);
		System.out.println("Filtro: " + filtro + ", paramatro: " + parametro);
		System.out.println(sql);
		ResultSet rs = st.executeQuery();

		while(rs.next()) {
			Orden act = new Orden();
			act.setId(rs.getLong("ID"));
			act.setFecha(rs.getDate("FECHA"));
			act.setTipo(rs.getInt("TIPO"));
			Plan pl = new Plan();
			pl.setId(rs.getLong("ID_PLAN"));
			pl.setDescripcion(rs.getString("DESCRIPCION"));
			pl.setCantidadCanales(rs.getInt("CANT_CANALES"));
			pl.setVelocidadInternet(rs.getDouble("VEL_INTERNET"));
			pl.setNombre(rs.getString("NOMBRE"));
			act.setPlan(pl);
			ordenes.add(act);
		}
		return ordenes;
	}


	public void crearPlan(Plan plan) throws SQLException, Exception{
		String sql = String.format("INSERT INTO PLANES(ID, NOMBRE, DESCRIPCION, VEL_INTERNET, CANT_CANALES) VALUES (PLA_SEQUENCE.NEXTVAL, '%1$s', '%2$s', %3$s, %4$s)",
				plan.getNombre(),
				plan.getDescripcion(),
				plan.getVelocidadInternet(),
				plan.getCantidadCanales());
		System.out.println(sql);
		System.out.println("paso 1");
		PreparedStatement st = conn.prepareStatement(sql);
		System.out.println("paso 2");
		recursos.add(st);
		System.out.println("paso 3");
		st.executeQuery();
		System.out.println("paso 4");

	}



}
