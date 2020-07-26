
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import com.mysql.jdbc.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ABC
 */
public class MyConnect {
    private final static String className="com.mysql.jdbc.Driver";
    private final static String url="jdbc:mysql://localhost:3306/chat";
    private final static String user="root";
    private final static String pass="";
	//private final static String tableName;
	private static Connection connection;
        
        public void connect() throws SQLException {
		try {
			Class.forName(className);
			connection=DriverManager.getConnection(url, user, pass);
			//System.out.println("Success connect");
		}catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found");
		}catch (SQLException e) {
			// TODO: handle exception
			System.out.println("Error connect");
		}
	}
        
        public ResultSet getData(String table) {
		ResultSet rs=null;
		String sql="select * from "+table;
		Statement st;
		try {
			st=connection.createStatement();
			rs=st.executeQuery(sql);
		}catch (SQLException e) {
			// TODO: handle exception
			System.out.println("Select Error \n"+e.toString());
		}
		return rs;
	}
        
        public void insertAccount(String user1,String pass1) throws ClassNotFoundException{
            Connection connection=null;
            PreparedStatement statement=null;
            
            try{
                Class.forName(className);
		connection=DriverManager.getConnection(url, user, pass);
			
		String sql = "insert into login_client"+"(usename,pass) values(?,?)";
		statement= (PreparedStatement) connection.prepareCall(sql);
		statement.setString(1,user1);
		statement.setString(2,pass1);
                statement.execute();
            }catch(SQLException ex){
                System.out.println("ERROR Insert \n");
            }
        }
}
