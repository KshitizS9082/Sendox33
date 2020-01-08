
import java.util.*;
import java.sql.*;
import java.math.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.List;
import java.io.*;

public class connection {
	
private	String DATABASE_URI;
private String username;
private String password;
//private String table;
private static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
private Connection db_connection =null;
 
private static String SHA256(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] messageDigest = md.digest(input.getBytes("UTF-8"));
    BigInteger no = new BigInteger(1, messageDigest);
    String hashtext = no.toString(16);
    while (hashtext.length() < 32) {
        hashtext = "0" + hashtext;
    }
    return hashtext;
}

 public connection(String databasename, String username,String password)
 { this.DATABASE_URI = "jdbc:mysql://localhost/"+databasename+"?useSSL=false";
   this.username=username;
   this.password=password;
   
    try {
    	System.out.println(Class.forName(JDBC_DRIVER));
    	db_connection =DriverManager.getConnection(DATABASE_URI,username,password);
    	
    }catch(Exception ex) {ex.printStackTrace();}
 
 }
	
 /*public  boolean adduser(String username,String password,String table)throws Exception //add into the login table //write adduser for signup also
 {  try { Statement stmt = db_connection.createStatement();
          String hashedpasswd = SHA256(password);
          if(checkLogin(username,password,table)==false)
          {
        	  
        
          
          String query = "INSERT INTO "+ table +" VALUES('"+username+"','"+hashedpasswd+"');";
          
          stmt.executeUpdate(query);
          
          return true;
 }
        }catch(SQLException sq) {sq.printStackTrace(); return false;}
         catch(Exception e) {e.printStackTrace(); return false;}
   return false;//if user already exists
 
 }
 */
 
 //change shitz in add user to signup
 
public  String adduser(String username,String password,String table,String firstname,String lastname,String email)throws Exception//add into the login table //write adduser for signup also
 {  try { Statement stmt = db_connection.createStatement();
          String query = "INSERT INTO "+ table +" VALUES( default , '"+username+"','"+password+"','"+firstname+"','"+lastname+"','"+email+"');";
          if(check_user(username,table).equals("no"))
          { stmt.executeUpdate(query);
          add_about(username,"about_users","");
          return "success?";}
          else if(check_user(username,table).equals("yes")){
        	  return "fail?type=user name already exists";
          }else {
        	  return "fail?entered case where failure but not specified type!";
          }
        	  
           
          
        }catch(SQLException sq) {sq.printStackTrace(); return "fail?entered case where failure but not specified type!";}
          catch(Exception e) {e.printStackTrace(); return "fail?entered case where failure but not specified type!";}
 
 }
 
 /*
 public   String validateuserdata(String username,String password,String table,String firstname,String lastname,String email)
 {//add email verification  "incorrect email"
	 try { Statement stmt = db_connection.createStatement();
	       String query = "SELECT uname FROM " + table + " WHERE uname = '" + username + "';";
	       ResultSet rs =stmt.executeQuery(query);
		   int flag=1;
		    while(rs.next()) 
		    {
		    	if(rs.getString(1).equals(username))
	    	       { flag=0;
	    		    break;
	    		   
	    	       }
		    }
		    if(flag==1)
		    	return "success";
		    else
		    	  return "username already exists";
	 }catch(SQLException sq) {sq.printStackTrace(); return "failure";}
 }
 */
 
 /*public  boolean checkLogin(String username,String password,String table)throws Exception
 {  try{Statement stmt = db_connection.createStatement();
          String hashedpasswd = SHA256(password);
   
    String query = "SELECT uname, password FROM " + table + " WHERE uname = '" + username + "';";

    ResultSet rs =stmt.executeQuery(query);
    
    if(rs==null) {
    	return false;
    }
    else {int flag=0;
    	   while(rs.next())
    	   { if(rs.getString(2).equals(hashedpasswd))
    	       { flag=1;
    		   return true;
    		   
    	       }
    	   
    	   }
    	   if(flag==0)
    	   { return false;}
    }
    
 }catch (SQLException sq) {
		sq.printStackTrace();
		return false;
                  }
 catch(Exception e) {e.printStackTrace(); return false;}
 return false;
	
 }*/
 
 /*
	public void close() throws SQLException{
		db_connection.close();
	}
*/
 
 // functions to write 
 //  check_user(String username,String table="login_users")->return yes/no
 //  fetch_public_key(String Username,String table="login_users") -> return public key
 //  signup_manager(data) -> 
 //  sendmessage(String uname,String data)
 //  send_inbox(String uname,int index)->(sender,name_of_file)
 //  delete_account(String username )
 //  edit_status(String uname,String status)
 //  view_contact(String uname)
 //
 
public String check_user(String username,String table)
{     try { Statement stmt = db_connection.createStatement();
String query = "SELECT * FROM " + table + " WHERE uname = '" + username + "';";
System.out.println(query);
ResultSet rs =stmt.executeQuery(query);
  int flag=0;
   while(rs.next()) 
   {
   	if(rs.getString(2).equals(username))
	       { flag=1;
		    break;
		   
	       }
   }
   if(flag==1)
   	{
   	
   	return "yes";}
   else
   	  {return "no";}
}catch(SQLException sq){sq.printStackTrace(); return "failure";}
	
}
 public String Sign_up(String username,String password,String table,String firstname,String lastname,String email)
 {//add email verification  "incorrect email"
	 String s=check_user(username,table);
	    if(s.contentEquals("no"))
	    	{try{adduser(username,password,table,firstname,lastname,email);
	    	      //add the about default
	    	   
	    	
	    	     //generate the public key and private key 
	    	      // store public key in database
	    	
	    	  }catch(Exception e) {e.printStackTrace();
	    	         }
	    	
	    	return "success";}
	    else if(s.contentEquals("yes"))
	    	  return "username already exists";
	return null;
 
 }
 
 public String addpublic_key(String n,String e,String table,String username)
 {  
	 try{
		 
		 BigInteger N = new BigInteger(n);
		 BigInteger E = new BigInteger(e);
		 RSAPublicKey p = new RSAPublicKey(E,N);
	 Object o =null;
      o=p;
      
     
      PreparedStatement stmt=null;
      String query=null;
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(o);
      oos.flush();
      oos.close();
      bos.close();
      byte[] data = bos.toByteArray();
      System.out.println(data);
      query="insert into "+table+" values( default ,'"+username+"', ?);";
      
     System.out.println(query);
      //"SELECT username FROM " + table + " WHERE username = '" + username + "';";
      stmt=db_connection.prepareStatement(query);
      stmt.setObject(1,data);
      stmt.executeUpdate();
      return "success";}
	 catch(Exception err)
	 {   
		 return "fail";
		 
	 }
      
      
 

}
 
 public  String fetch_public_key(String username,String table) throws SQLException
 {  Object rmObj =null;
    PreparedStatement ps=null;
    ResultSet rs=null;
    String query=null;
    query="select * from " + table +" where uname= '" +username + "';";
    System.out.println(query);
    ps=db_connection.prepareStatement(query);
    rs=ps.executeQuery();
    RSAPublicKey mc = null;
    
    if(rs.next())
    {
        ByteArrayInputStream bais;

        ObjectInputStream ins;

        try {

        bais = new ByteArrayInputStream(rs.getBytes("public_key"));

        ins = new ObjectInputStream(bais);

        mc =(RSAPublicKey)ins.readObject();
        
        BigInteger N;
        
        BigInteger e;
        
        N=mc.getmodulus();
        
        e=mc.getexponent();
        
        String s1="";
        
        String s2="";
        
        s1= N.toString();
        
        s2= e.toString();
        
        ins.close();
        
        return s1+"&"+s2;
        
        
        

       // System.out.println("Object in value ::"+mc.getSName());
        

        }
        catch (Exception e) {

        e.printStackTrace();
        }

    }
	return null;

   
	 
	 
	 
 
 }
 
 
 public void add_about(String uname,String table,String about) throws SQLException
 {   Statement stmt = db_connection.createStatement();
     String query = "insert into "+table+" values(default,'" +uname+ "','"+about+"');";
     System.out.println(query);
     if(about.equals(""))//empty string
     {   query="insert into "+table+" values(default,'" +uname+ "',default);";
         System.out.println(query);
     }
     stmt.executeUpdate(query);
     
     
    
 
 }
 
 public String view_contact(String uname,String table) throws SQLException
 {  Statement stmt = db_connection.createStatement();
    String query = "select * from "+table+" where uname='"+uname+"';";
    System.out.println(query);
    ResultSet rs=null;
    rs=stmt.executeQuery(query);
   if(! rs.next())
   {
	   return "fail?";
   }
   
    String s=rs.getString(3);
    if (s.length()==0) {
    	return "fail?";
    }
    else
    return ("success?" + s);
  
  
	 
 }
 
 public String Edit_contact(String uname,String table,String new_status) throws SQLException
 {  try {
	 Statement stmt = db_connection.createStatement();
	    String query = "update "+table+" set about='"+new_status+"' "+"where uname='"+uname+"';";
	    System.out.println(query);
	    stmt.executeUpdate(query);
 	}catch(Exception e) {
 		 e.printStackTrace();
 		 return "fail?";
 	}
	return "success?";
	 
 }
// String query = "INSERT INTO "+ table +" VALUES( ' "+username+"','"+password+"','"+firstname+"','"+lastname+"','"+email+"');";
// stmt.executeUpdate(query);
	 public String Delete_account(String uname,String password) throws SQLException
	 {  Statement stmt = db_connection.createStatement();
	    String table="login_users";
	    String query = "SELECT * FROM " + table + " WHERE uname = '" + uname + "';";
	    System.out.println(query);
	    ResultSet rs =stmt.executeQuery(query);
	    rs.next();
	    if(rs.getString(3).equals(password))
	    { query="delete from login_users where uname='"+uname+"';";
	     stmt.executeUpdate(query);
	    query="delete from about_users where uname='"+uname+"';";
	   	     stmt.executeUpdate(query);
	    query="delete from public_keys where uname='"+uname+"';";
	   	     stmt.executeUpdate(query);
	    return "success?";
	    	
	    }
	    else return "fail?";
	 
	   
		 
	 }
	 
	 public String send_message(String Reciever,String Sender,String table,String content) throws SQLException//change content type to any other object later
	 {   try{//query="insert into "+table+" values( default ,'"+username+"', ?);";
		      if(check_user(Reciever,"login_users").equals("yes"))
		      { byte[] data=content.getBytes();
	      String query = "insert into " + table + " values( default,'"+Reciever+"','"+Sender+"','"+content+"');";
	      PreparedStatement stmt = db_connection.prepareStatement(query);
	      //stmt.setObject(1,data);
	      System.out.println(query);
	      stmt.executeUpdate(query);
	      return "success?";
		      }
		      else {
		    	  return "fail?";}
	 }catch(Exception e){     e.printStackTrace(); return "fail?";
	   
	 }
	 
}
	 
		public String load_messages(String uname,int index,String table) throws SQLException
		{   Statement stmt = db_connection.createStatement();
		    String query = "SELECT * FROM " + table + " WHERE reciever = '" + uname + "'"+" order by message_id desc"+";";
		    System.out.println(query);
		     ResultSet rs =stmt.executeQuery(query);
		   int  count=0;
		   String result = "success?" ;
//		    List<String> messages=new Vector<String>();
		     while(rs.next())
		     { count++;
		       if(count>(index-1)*5&&count<=(index)*5)
		       {
//		    	   messages.add(rs.getString(4));
		    	   String a = (rs.getString(3) );
		    	   String b = (rs.getString(4) );
		    	 result = result + a + "&#@=@#&";
		    	 result = result + b + "&#@!@#&";
		       }     //rs.getString(3) gives the name of sender for that message
		     }
//		 return messages;
		 if(result.length() > 8) {
			 result = result.substring(0, result.length() - 7);
		 }
			return result;
		}

	public static void main(String[] args)
	{ connection C=new connection("sendox","root","KshitizSharma28*");
	  
	
	  
	  try {
		 // System.out.println(C.check_user("mad","login_users"));
//		C.adduser("chutiyaLal","ajeebHash","login_users","madhu","sudhan","pmsreddifeb18@gmail.com");
		//C.addpublic_key(publicKey,"public_keys","mad");
		//RSAPublicKey p= C.fetch_public_key("mad","public_keys");
		  //System.out.println(p.getPubExp());
		 // C.add_about("mad","about_users","");
		  //System.out.println(C.view_contact("mad","about_users")); 
		 // C.Edit_contact("mad","about_users","i dont fuck with ya");
		  //C.Delete_account("mad","007");
//		  String response =C.send_message("mad","maddy","messages","hi ra how are you");
		//String response = C.fetch_public_key("SHITTYUSERNAME", "public_keys");
//		  String messages=null;
//		  messages=C.load_messages("mad",2,"messages");
//		  System.out.println(messages);
		  String response = C.load_messages("SHITTYUSERNAME",1,"messages");
		System.out.println("refmsfpsfsd = " + response);
		 
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
		
	}

}
