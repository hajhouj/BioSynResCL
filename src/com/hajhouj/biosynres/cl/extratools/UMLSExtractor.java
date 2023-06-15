package com.hajhouj.biosynres.cl.extratools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class UMLSExtractor {
	private static String DB_URL;
	private static String USER;
	private static String PASS;

	static {
		Properties props = new Properties();
		try (InputStream in = Files.newInputStream(Paths.get("database.properties"))) {
			props.load(in);
			DB_URL = props.getProperty("db_url");
			USER = props.getProperty("username");
			PASS = props.getProperty("password");
		} catch (IOException e) {
			// If the properties file doesn't exist, create it and exit the program
			try (OutputStream out = Files.newOutputStream(Paths.get("database.properties"))) {
				props.setProperty("db_url", "jdbc:mysql://localhost/your_database_name");
				props.setProperty("username", "your_username");
				props.setProperty("password", "your_password");
				props.store(out, "Database properties");
				System.out.println("Created new 'database.properties' file. Please fill in your database details.");
				System.exit(1);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			DB_URL = USER = PASS = null;
		}
	}

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			printUsage();
			System.exit(1);
		}

		String feature = args[0];

		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
			switch (feature) {
			case "extract":
				String sab = args.length > 1 ? args[1] : "ALL";
				String lat = args.length > 2 ? args[2] : "ALL";
				String tty = args.length > 3 ? args[3] : "ALL";
				extractConcepts(conn, sab, lat, tty);
				break;
			case "list-vocab-sources":
				listSAB(conn);
				break;
			case "list-lang":
				listLAT(conn);
				break;
			case "list-types":
				listTTY(conn);
				break;
			default:
				System.out.println("Invalid command");
				printUsage();
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out
				.println("extract <Vocabulary source | ALL> <Language | ALL> <Term type | ALL>: Extracts concepts and writes them to 'terms.txt' and 'terms_meta.txt'.");
		System.out.println("list-vocab-sources: Lists available source vocabularies.");
		System.out.println("list-lang: Lists available languages.");
		System.out.println("list-types: Lists available term types.");
	}

	private static void extractConcepts(Connection conn, String sab, String lat, String tty)
			throws SQLException, IOException {
		String query = "SELECT * FROM MRCONSO";
		String where = " WHERE (SAB = ? OR ? = 'ALL') AND (LAT = ? OR ? = 'ALL') AND (TTY = ? OR ? = 'ALL')";

		try (PreparedStatement stmt = conn.prepareStatement(query + where);
				BufferedWriter termsWriter = Files.newBufferedWriter(Paths.get("terms.txt"));
				BufferedWriter metaWriter = Files.newBufferedWriter(Paths.get("terms_meta.txt"))) {

			stmt.setString(1, sab);
			stmt.setString(2, sab);
			stmt.setString(3, lat);
			stmt.setString(4, lat);
			stmt.setString(5, tty);
			stmt.setString(6, tty);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String term = rs.getString("STR");
					termsWriter.write(term + "\n");

					String meta = rs.getString("CUI") + "," + rs.getString("LAT") + "," + rs.getString("SAB") + ","
							+ rs.getString("TTY") + "," + rs.getString("CODE");
					metaWriter.write(meta + "\n");
				}
			}
		}
	}

	private static void listSAB(Connection conn) throws SQLException {
		String query = "SELECT DISTINCT SAB FROM MRCONSO";

		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				System.out.println(rs.getString("SAB"));
			}
		}
	}

	private static void listLAT(Connection conn) throws SQLException {
		String query = "SELECT DISTINCT LAT FROM MRCONSO";

		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				System.out.println(rs.getString("LAT"));
			}
		}
	}

	private static void listTTY(Connection conn) {
		String query = "SELECT DISTINCT TTY FROM MRCONSO";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					System.out.println(rs.getString("TTY"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
