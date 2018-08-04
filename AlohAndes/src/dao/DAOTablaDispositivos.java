package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Dispositivo;


public class DAOTablaDispositivos {


	public static final int BUSQUEDA_POR_ID = 1;
	public static final int BUSQUEDA_POR_MACS = 2;
	public static final int BUSQUEDA_POR_CLIENTE = 3;
	private ArrayList<Object> recursos;

	private Connection conn;

	public DAOTablaDispositivos() {
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



	public List<Dispositivo> darDispositivosPor(int filtro, String parametro) throws SQLException, Exception {
		List<Dispositivo> routers = new ArrayList<Dispositivo>();
		String sql = "SELECT * FROM DISPOSITIVOS, TIPOS_DISPOSITIVOS TIP WHERE TIPO = TIP.ID" ;

		switch(filtro) {

		case BUSQUEDA_POR_ID:
			sql += " AND ID = " + parametro + " AND ROWNUM <= 1";
			break;

		case BUSQUEDA_POR_MACS:
			String[] paraSp = parametro.split(":");
			sql+= " AND MAC1_1 = '" + paraSp[0] +
					"' AND MAC1_2 = '" + paraSp[1] +
					"' AND MAC2_1 = '" + paraSp[2] +
					"' AND MAC2_2 = '" + paraSp[3] +
					"' AND MAC3_1 = '" + paraSp[4] +
					"' AND MAC3_2 = '" + paraSp[5] +
					"' AND MAC4_1 = '" + paraSp[6] +
					"' AND MAC4_2 = '" + paraSp[7] + "'";
			break;
		case BUSQUEDA_POR_CLIENTE:
			sql +=" AND CLIENTE = " + parametro;
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
			Dispositivo act = new Dispositivo();
			act.setId(rs.getLong("ID"));
			act.setDescripcion(rs.getString("DESCRIPCION"));
			act.setTipo(rs.getString("NOMBRE"));
			act.setMac1_1(rs.getString("MAC1_1"));
			act.setMac1_2(rs.getString("MAC1_2"));
			act.setMac2_1(rs.getString("MAC2_1"));
			act.setMac2_2(rs.getString("MAC2_2"));
			act.setMac3_1(rs.getString("MAC3_1"));
			act.setMac3_2(rs.getString("MAC3_2"));
			act.setMac4_1(rs.getString("MAC4_1"));
			act.setMac4_2(rs.getString("MAC4_2"));


			routers.add(act);
		}
		return routers;
	}


	public void crearRouter(Dispositivo router) throws SQLException, Exception{
		String sql = String.format("INSERT INTO ROUTERS(ID, MAC1_1, MAC1_2, MAC2_1, MAC2_2, MAC3_1, MAC3_2, MAC4_1, MAC4_2) VALUES (ROU_SEQUENCE.NEXTVAL, '%1$s', '%2$s', '%3$s', '%4$s', '%5$s','%6$s', '%7$s', '%8$s')",
				router.getMac1_1(),
				router.getMac1_2(),
				router.getMac2_1(),
				router.getMac2_2(),
				router.getMac3_1(),
				router.getMac3_2(),
				router.getMac4_1(),
				router.getMac4_2());
		System.out.println(sql);
		System.out.println("paso 1");
		PreparedStatement st = conn.prepareStatement(sql);
		System.out.println("paso 2");
		recursos.add(st);
		System.out.println("paso 3");
		st.executeQuery();
		System.out.println("paso 4");

	}


	public void trasladarDispositivo(Long anteriorClienteId, Long nuevoClienteId) throws SQLException, Exception{
		String sql = "UPDATE DISPOSITIVOS SET CLIENTE = " + nuevoClienteId +" WHERE CLIENTE = " + anteriorClienteId;
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
