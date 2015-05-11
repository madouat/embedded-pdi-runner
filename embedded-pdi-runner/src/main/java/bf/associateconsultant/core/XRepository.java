package bf.associateconsultant.core;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;

public class XRepository {
	private static final String CONNECTION_TYPE = "JDBC";
	private String dbUsername;
	private String dbPassword;
	private String dbName;
	private String dbHost = "localhost";
	private String dbType = "postgresql";
	private String dbPort = "5432";

	private String repositoryuser;
	private String repositoryPassword;
	private KettleDatabaseRepository kettleDatabaseRepository;

	public KettleDatabaseRepository getKettleDatabaseRepository() {
		return kettleDatabaseRepository;
	}

	public void setKettleDatabaseRepository(
			KettleDatabaseRepository kettleDatabaseRepository) {
		this.kettleDatabaseRepository = kettleDatabaseRepository;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	public static String getConnectionType() {
		return CONNECTION_TYPE;
	}

	public void build() throws KettleException {
		KettleEnvironment.init();
		DatabaseMeta databaseMeta = new DatabaseMeta("H2Repo", dbType,
				CONNECTION_TYPE, dbHost, dbName, dbPort, dbUsername, dbPassword);
		KettleDatabaseRepositoryMeta repositoryMeta = new KettleDatabaseRepositoryMeta(
				"KettleDatabaseRepository", "H2Repo", "H2 Repository",
				databaseMeta);
		kettleDatabaseRepository = new KettleDatabaseRepository();
		kettleDatabaseRepository.init(repositoryMeta);
		// return kettleDatabaseRepository;
	}

	public XRepository withDbUsername(String aValue) {
		this.setDbUsername(aValue);

		return (XRepository) this;
	}

	public XRepository withDbPassword(String aValue) {
		this.setDbPassword(aValue);

		return (XRepository) this;
	}

	public XRepository withDbName(String aValue) {
		this.setDbName(aValue);

		return (XRepository) this;
	}

	public XRepository withDbHost(String aValue) {
		this.setDbHost(aValue);

		return (XRepository) this;
	}

	public XRepository withDbType(String aValue) {
		this.setDbType(aValue);

		return (XRepository) this;
	}

	public XRepository withDbPort(String aValue) {
		this.setDbPort(aValue);

		return this;
	}

	public String getRepositoryuser() {
		return repositoryuser;
	}

	public void setRepositoryuser(String repositoryuser) {
		this.repositoryuser = repositoryuser;
	}

	public String getRepositoryPassword() {
		return repositoryPassword;
	}

	public void setRepositoryPassword(String repositoryPassword) {
		this.repositoryPassword = repositoryPassword;
	}

	public XRepository withRepositoryuser(String repositoryuser) {
		setRepositoryuser(repositoryuser);
		return this;
	}

	public XRepository withRepositoryPassword(String repositoryPassword) {
		setRepositoryPassword(repositoryPassword);
		return this;
	}
}
