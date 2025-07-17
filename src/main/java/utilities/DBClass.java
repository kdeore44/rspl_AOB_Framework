package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBClass {
		
	Connection con;
	Statement stm;
	ResultSet rs;
	public ResultSet dbconn(String tableName)
	{
		String msdriver ="com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String msurl = "";
		String msuid ="";
		String mspwd="";
		/*
		 * String ordriver ="oracle.jdbc.driver.OracleDriver"; String orurl =
		 * "jdbc:oracle://<DB server name>"; String oruid ="xxxx"; String orpwd="xxxx";
		 */
		
		String query = "select * from "+tableName+";";
		try {
			Class.forName(msdriver);
			con = DriverManager.getConnection(msurl,msuid,mspwd);
			stm = con.createStatement();
			rs = stm.executeQuery(query);
	
			while(rs.next()) { System.out.println(rs.getString(4)+" | "+rs.getString(5));}
			
			con.close();
			return rs;
		}catch(Exception e)
		{
			return rs;
		}
	}
	
	//Unite Test
	
	public static void main(String[] arg)
	{
		DBClass dc = new DBClass();
		dc.dbconn("tbl_Employees");
	}
}
