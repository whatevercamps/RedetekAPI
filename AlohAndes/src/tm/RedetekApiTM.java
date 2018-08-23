/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: RotondAndes 
 * Autor: David Bauista - dj.bautista10@uniandes.edu.co
 * -------------------------------------------------------------------
 */
package tm;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.List;
import java.util.Properties;



import dao.DAOTablaClientes;
import dao.DAOTablaNodos;
import dao.DAOTablaPlanes;
import dao.DAOTablaDispositivos;
import vos.Cliente;
import vos.Nodo;
import vos.Plan;
import vos.Dispositivo;

import java.text.SimpleDateFormat;


/**
 * Transaction Manager de la aplicacion (TM)
 * Fachada en patron singleton de la aplicacion
 * @author David Bautista
 */
public class RedetekApiTM {


	/**
	 * Atributo estatico que contiene el path relativo del archivo que tiene los datos de la conexion
	 */
	private static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";

	/**
	 * Atributo estatico que contiene el path absoluto del archivo que tiene los datos de la conexion
	 */
	private  String connectionDataPath;

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base de datos.
	 */
	private String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base de datos.
	 */
	private String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de datos.
	 */
	private String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base de datos.
	 */
	private String driver;

	/**
	 * conexion a la base de datos
	 */
	private Connection conn;

	/**
	 * Atributo que dará el último estado correcto de la base de datos.
	 */
	private Savepoint savepoint;

	/**
	 * Metodo constructor de la clase RotondAndesMaster, esta clase modela y contiene cada una de las 
	 * transacciones y la logica de negocios que estas conllevan.
	 * <b>post: </b> Se crea el objeto RotondAndesMaster, se inicializa el path absoluto del archivo de conexion y se
	 * inicializa los atributos que se usan par la conexion a la base de datos.
	 * @param contextPathP - path absoluto en el servidor del contexto del deploy actual
	 */
	public RedetekApiTM(String contextPathP) {
		connectionDataPath = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
		initConnectionData();
	}

	/**
	 * Metodo que  inicializa los atributos que se usan para la conexion a la base de datos.
	 * <b>post: </b> Se han inicializado los atributos que se usan par la conexion a la base de datos.
	 */
	private void initConnectionData() {
		try {
			File arch = new File(this.connectionDataPath);
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(arch);
			prop.load(in);
			in.close();
			this.url = prop.getProperty("url");
			this.user = prop.getProperty("usuario");
			this.password = prop.getProperty("clave");
			this.driver = prop.getProperty("driver");
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que  retorna la conexion a la base de datos
	 * @return Connection - la conexion a la base de datos
	 * @throws SQLException - Cualquier error que se genere durante la conexion a la base de datos
	 */
	private Connection darConexion() throws SQLException {
		System.out.println("Connecting to: " + url + " With user: " + user);
		return DriverManager.getConnection(url, user, password);
	}



	////////////////////////////////////////
	///////Transacciones////////////////////
	////////////////////////////////////////



	public Nodo crearNodo(Nodo nuevo) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaNodos dao = new DAOTablaNodos();
		List<Nodo> ret = null;
		try {

			if(this.conn == null || this.conn.isClosed()){
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}


			//verificar reglas de negocio
			if (!darNodosPor(DAOTablaNodos.BUSQUEDA_POR_ID, nuevo.getId().toString()).isEmpty()) {
				throw new Exception("Ya hay un nodo con el id " + nuevo.getId());
			}
			String octetos = nuevo.getOcteto1() + ":" + nuevo.getOcteto2() + ":" + nuevo.getOcteto3();

			if (!darNodosPor(DAOTablaNodos.BUSQUEDA_POR_OCTETOS, octetos).isEmpty()) {
				throw new Exception("Ya hay un nodo con el los octetos iniciales " + octetos);
			}

			nuevo.setClientes(new ArrayList<Cliente>());

			dao.setConn(conn);
			dao.crearNodo(nuevo);

			this.savepoint = this.conn.setSavepoint();
			System.out.println("lo creo");
			//verificar 

			ret = darNodosPor(DAOTablaNodos.BUSQUEDA_POR_OCTETOS, octetos);

			if(ret.isEmpty()) {
				throw new Exception("No se guardo correctamente el nodo, revisar xd...");
			}

			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret.get(0);
	}



	public Cliente crearCliente(Cliente nuevo, Long idNodo) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaClientes dao = new DAOTablaClientes();
		List<Cliente> ret = null;
		try {

			if(this.conn == null || this.conn.isClosed()){
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}


			//verificar reglas de negocio
			if (!darClientesPor(DAOTablaClientes.BUSQUEDA_POR_CEDULA, nuevo.getCedula().toString()).isEmpty()) {
				throw new Exception("Ya hay un cliente con la cedula " + nuevo.getCedula());
			}

			Integer octeto4disp = buscarOcteto4Disponible(idNodo);

			if(octeto4disp == null)
				throw new Exception("El nodo no tiene una ip disponible");


			nuevo.setOcteto4(octeto4disp);


			List<Plan> plan = darPlanesPor(DAOTablaPlanes.BUSQUEDA_POR_ID, nuevo.getPlan().getId().toString());

			if(plan.isEmpty())
				throw new Exception("El plan no existe");

			nuevo.setPlan(plan.get(0));
			dao.setConn(conn);
			dao.crearCliente(nuevo, idNodo);

			this.savepoint = this.conn.setSavepoint();
			System.out.println("lo creo");
			//verificar 

			ret = darClientesPor(DAOTablaClientes.BUSQUEDA_POR_CEDULA,nuevo.getCedula().toString());

			if(ret.isEmpty()) {
				throw new Exception("No se guardo correctamente el cliente, revisar xd...");
			}

			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret.get(0);
	}


	public Dispositivo crearRouter(Dispositivo router) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaDispositivos dao = new DAOTablaDispositivos();
		List<Dispositivo> ret = null;
		try {

			if(this.conn == null || this.conn.isClosed()){
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}


			//verificar reglas de negocio
			String macs = router.getMac1_1() + ":";
			macs += router.getMac1_2() + ":";
			macs += router.getMac2_1() + ":";
			macs += router.getMac2_2() + ":";
			macs += router.getMac3_1() + ":";
			macs += router.getMac3_2() + ":";
			macs += router.getMac4_1() + ":";
			macs += router.getMac4_2();

			if (!darDispositivosPor(DAOTablaDispositivos.BUSQUEDA_POR_MACS, macs).isEmpty()) {
				throw new Exception("Ya hay un router con dicho mac ");
			}

			dao.setConn(conn);
			dao.crearRouter(router);


			System.out.println("lo creo");
			//verificar 

			ret = darDispositivosPor(DAOTablaDispositivos.BUSQUEDA_POR_MACS, macs);

			if(ret.isEmpty()) {
				throw new Exception("No se guardo correctamente el cliente, revisar xd...");
			}

			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret.get(0);
	}


	public Integer buscarOcteto4Disponible(Long idNodo) throws SQLException, Exception{
		Integer octeto4disp = null;
		boolean conexionPropia = false; 
		DAOTablaNodos dao = new DAOTablaNodos();
		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);

			if(darNodosPor(DAOTablaNodos.BUSQUEDA_POR_ID, idNodo.toString()).isEmpty())
				throw new Exception("No existe el nodo");

			octeto4disp = dao.buscarOcteto4Disponible(idNodo);


		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return octeto4disp;
	}



	public List<Cliente> darClientesPor(int filtro, String parametro) throws SQLException, Exception{
		boolean conexionPropia = false; 
		List<Cliente> clientes = new ArrayList<>(); 
		DAOTablaClientes dao = new DAOTablaClientes();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);
			clientes = dao.darClientesPor(filtro, parametro);

			for(Cliente c : clientes) {
				c.setPlan(darPlanesPor(DAOTablaPlanes.BUSQUEDA_POR_ID, c.getPlan().getId().toString()).get(0));
				c.setDispositivos(darDispositivosPor(DAOTablaDispositivos.BUSQUEDA_POR_CLIENTE, c.getCedula().toString()));
			}

		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return clientes; 
	}


	public List<Dispositivo> darDispositivosPor(int filtro, String parametro) throws SQLException, Exception{
		boolean conexionPropia = false; 
		List<Dispositivo> dispositivos = new ArrayList<>(); 
		DAOTablaDispositivos dao = new DAOTablaDispositivos();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);
			dispositivos = dao.darDispositivosPor(filtro, parametro);


		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return dispositivos; 
	}

	public List<Plan> darPlanesPor(int filtro, String parametro) throws SQLException, Exception{
		boolean conexionPropia = false; 
		List<Plan> planes = new ArrayList<Plan>(); 
		DAOTablaPlanes dao = new DAOTablaPlanes();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);
			planes = dao.darPlanesPor(filtro, parametro);


		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return planes; 
	}



	public List<Nodo> darNodosPor(int filtro, String parametro) throws SQLException, Exception{
		boolean conexionPropia = false; 
		List<Nodo> nodos = new ArrayList<>(); 
		DAOTablaNodos dao = new DAOTablaNodos();

		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.savepoint = this.conn.setSavepoint();
			}
			dao.setConn(conn);
			nodos = dao.darNodosPor(filtro, parametro);

			for(Nodo n : nodos) {
				n.setClientes(darClientesPor(DAOTablaClientes.BUSQUEDA_POR_ID_NODO, n.getId().toString()));
			}

		}catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {

				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return nodos; 
	}

	public Cliente modificarCliente(Long idCliente, Cliente nuevo, Long idNodo) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaClientes dao = new DAOTablaClientes();
		Cliente ret = null;
		try {

			if(this.conn == null || this.conn.isClosed()){
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}


			//verificar reglas de negocio

			List<Cliente> cli = darClientesPor(DAOTablaClientes.BUSQUEDA_POR_CEDULA, idCliente.toString());
			if (cli.isEmpty()) {
				throw new Exception("No hay un cliente con la cedula " + idCliente);
			}
			
			
			
			Nodo nd = darNodosPor(DAOTablaNodos.BUSQUEDA_POR_ID, idNodo.toString()).get(0);
			Cliente cl = cli.get(0);
			
			if(nd.getOcteto1() != cl.getOcteto1() || nd.getOcteto2() != cl.getOcteto2() || nd.getOcteto3() != cl.getOcteto3()) {
				Integer octeto4disp = buscarOcteto4Disponible(idNodo);

				if(octeto4disp == null)
					throw new Exception("El nodo no tiene una ip disponible");
				
				nuevo.setOcteto4(octeto4disp);
				
			}
			

			if(!idCliente.toString().equalsIgnoreCase(nuevo.getCedula().toString())) {
				System.out.println("cedula anterior:" + idCliente.toString());
				System.out.println("cedula nueva:" + nuevo.getCedula().toString());
				ret = crearCliente(nuevo, idNodo);
				trasladarDispositivos(idCliente, ret.getCedula());
				if(conexionPropia)
					this.savepoint = this.conn.setSavepoint();
				
				borrarCliente(idCliente);
			} else {
				dao.setConn(this.conn);
				dao.modificarCliente(idCliente, nuevo, idNodo);
				ret = nuevo;
			}

		

			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret;
	}

	public Boolean trasladarDispositivos(Long anteriorClienteId, Long nuevoClienteId) throws SQLException, Exception{
		boolean conexionPropia = false; 
		DAOTablaDispositivos dao = new DAOTablaDispositivos();
		try {

			if(this.conn == null || this.conn.isClosed()){
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}


			//verificar reglas de negocio


			dao.setConn(this.conn);

			dao.trasladarDispositivo(anteriorClienteId, nuevoClienteId);




			if(!darDispositivosPor(DAOTablaDispositivos.BUSQUEDA_POR_CLIENTE, anteriorClienteId.toString()).isEmpty())
				throw new Exception("No se logró independizar completamente los dispositivos de su antiguo dueño");


			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;

		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;

		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return true;
	}


	public Boolean borrarCliente(Long idCliente) throws SQLException, Exception{
		Boolean ret = false;
		boolean conexionPropia = false; 
		DAOTablaClientes dao = new DAOTablaClientes();
		try {

			if(this.conn == null || this.conn.isClosed()){
				this.conn = darConexion(); 
				conexionPropia = true; 
				this.conn.setAutoCommit(false);
				this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				this.savepoint = this.conn.setSavepoint();
			}


			//verificar reglas de negocio
			if(conexionPropia) {
				System.out.println("idCliente a borrar: " + idCliente);
				if (darClientesPor(DAOTablaClientes.BUSQUEDA_POR_CEDULA, idCliente.toString()).isEmpty()){
					throw new Exception("No hay un cliente con la cedula " + idCliente);
				}

				if(!darDispositivosPor(DAOTablaDispositivos.BUSQUEDA_POR_CLIENTE, idCliente.toString()).isEmpty())
					throw new Exception("No se puede eliminar el cliente porque aún tiene dispositivos asociados con su cédula.");

			}


	
			dao.setConn(conn);
			ret = dao.borrarCliente(idCliente);

			if(conexionPropia)
				this.conn.commit();
			System.out.println("lo creo");
			//verificar 

			

			if(conexionPropia)
				this.conn.commit();

		}  catch (SQLException e) {
			this.conn.rollback(this.savepoint);
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			this.conn.rollback(this.savepoint);
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null && conexionPropia)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ret;
	}






}
