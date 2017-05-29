package org.aptus.basicauth;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


import java.util.ArrayList;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.sql.*;

import org.apache.commons.codec.binary.Base64;

public class App {

	public static void main(String[] args) {

		try {
			String webPage = "http://192.168.1.17:8080/api/json?pretty=true";
			String name = "aptus";
			String password = "@ptus2016";

			String authString = name + ":" + password;
			//System.out.println("auth string: " + authString);
			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
			String authStringEnc = new String(authEncBytes);
			//System.out.println("Base64 encoded auth string: " + authStringEnc);

			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			String result = sb.toString();
			System.out.println(result);
			
			insertToDb(result);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void insertToDb(String result){
		
		
		JSONParser parser=new JSONParser();
        try {
            Object obj = parser.parse(result);
            JSONObject jsonObj = (JSONObject) obj;
           
            Object _class=(Object)jsonObj.get("_class");
            
            Object mode=(Object)jsonObj.get("mode");
            
            Object nodeDescription=(Object)jsonObj.get("nodeDescription");
            
            Object nodeName="";
            //nodeName=(Object)jsonObj.get("nodeName");
            
            //System.out.println(nodeName);
            Object numExecutors=(Object)jsonObj.get("numExecutors");
            
            Object description="";
            //description=(Object)jsonObj.get("description");
            
            Object overallLoad="";
            //overallLoad=(Object)jsonObj.get("overallLoad").toString();
            Object quietingDown=(Object)jsonObj.get("quietingDown").toString();
            
            Long slaveAgentPort=(Long)jsonObj.get("slaveAgentPort");
            System.out.println(slaveAgentPort);
            
            Object useCrumbs=(Object)jsonObj.get("useCrumbs");
            
            Object useSecurity=(Object)jsonObj.get("useSecurity");
            
            
            
            JSONArray jobs=(JSONArray) jsonObj.get("jobs");
            JSONArray views=(JSONArray) jsonObj.get("views");
            
            JSONObject primaryViewObj=(JSONObject)jsonObj.get("primaryView");
            JSONObject unlabeledLoadObj=(JSONObject)jsonObj.get("unlabeledLoad");
           
            Object primaryView_class=(Object)primaryViewObj.get("_class");
            Object primaryView_name=(Object)primaryViewObj.get("name");
            Object primaryView_url=(Object)primaryViewObj.get("url");
            
            Object unlabeledLoad=(Object)unlabeledLoadObj.get("_class");
            
            ArrayList <Object> jobs_class=new ArrayList<Object>();
            ArrayList <Object> jobs_name=new ArrayList<Object>();
            ArrayList <Object> jobs_url=new ArrayList<Object>();
            ArrayList <Object> jobs_color=new ArrayList<Object>();
            
            for (Object o:jobs){
            	JSONObject jo=(JSONObject)o;
            	jobs_class.add((Object)jo.get("_class"));
            	jobs_name.add((Object)jo.get("name"));
            	jobs_url.add((Object)jo.get("url"));
            	jobs_color.add((Object)jo.get("color"));
            	
            }
          
            ArrayList <Object> views_class=new ArrayList<Object>();
            ArrayList <Object> views_name=new ArrayList<Object>();
            ArrayList <Object> views_url=new ArrayList<Object>();
            
            for (Object o:views){
            	JSONObject jo=(JSONObject)o;
            	views_class.add((Object)jo.get("_class"));
            	views_name.add((Object)jo.get("name"));
            	views_url.add((Object)jo.get("url"));
            	
            
            }
            
            
            /* jdbc connection */ 
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String URL = "jdbc:mysql://localhost/jsonread";
                String USER = "root";
                String PASS = "";
                Connection conn = DriverManager.getConnection(URL, USER, PASS);
                Statement stmt=null;
                try{
                    stmt=conn.createStatement();
                    String sql="CREATE TABLE IF NOT EXISTS basicauth (_class varchar(100),mode varchar(100),nodeDescription varchar(100),nodeName varchar(100),numExecutors varchar(100),description varchar(100),jobs_class varchar(100),jobs_name varchar(100),jobs_url varchar(100),jobs_color varchar(50),overallLoad varchar(100),primaryView_class varchar(100),primaryView_name varchar(100),primaryView_url varchar(100),quitingDown varchar(100),slaveAgentPort varchar(100),unlabeledNode_class varchar(100),useCrumbs varchar(100),useSecurity varchar(100),views_class varchar(100),views_name varchar(100),views_url varchar(100))";
                    Boolean ret = stmt.execute(sql);
                    System.out.println(ret.toString());
                }catch (SQLException s){
                    System.out.println("error in create sql ");
                }finally {
                    stmt.close();
                
            	}
                /* insert data */
                
                
                for(int i=0;i<jobs_class.size();i++){
                	stmt=null;
                	
           	
                	
                	
                   
                	Object ijobs_class,ijobs_name,ijobs_url,ijobs_color,iviews_class,iviews_name,iviews_url;
                	
                	if(i<views_class.size()){
                		
                		iviews_class=views_class.get(i);
                		iviews_name=views_name.get(i);
                		iviews_url=views_url.get(i);
                		
                		
                	}else{
                		iviews_class="";
                		iviews_name="";
                		iviews_url="";
                		
                		
                	}
                	ijobs_class=jobs_class.get(i);
                	ijobs_name=jobs_name.get(i);
                	ijobs_url=jobs_url.get(i);
                	ijobs_color=jobs_color.get(i);
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	try{
                		stmt=conn.createStatement();
                		String sql="INSERT INTO jsonread.basicauth (_class,mode,nodeDescription,nodeName,numExecutors,description,jobs_class,jobs_name,jobs_url,jobs_color,overallLoad,primaryView_class,primaryView_name,primaryView_url,quitingDown,slaveAgentPort,unlabeledNode_class,useCrumbs,useSecurity,views_class,views_name,views_url) VALUES" +	"('"+_class+"','"+mode+"','"+nodeDescription+"','"+nodeName+"','"+numExecutors+"','"+description+"','"+ijobs_class+"','"+ijobs_name+"','"+ijobs_url+"','"+ijobs_color+"','"+overallLoad+"','"+primaryView_class+"','"+primaryView_name+"','"+primaryView_url+"','"+quietingDown+"','"+slaveAgentPort+"','"+unlabeledLoad+"','"+useCrumbs+"','"+useSecurity+"','"+iviews_class+"','"+iviews_name+"','"+iviews_url+"')";
                		System.out.println("hello");
                		System.out.println(sql);
                		System.out.println("hello");
                		int ret = stmt.executeUpdate(sql);
                			System.out.println(ret);

                	}catch (SQLException s){
                		System.out.println("error in sql ");
                	}finally {
                		stmt.close();
                	}
                
                }
                
                
                
                
                
                
                
                
            }catch (Exception e){
            	System.out.println("error in jdbc ");
            }
            
           
            
           
            
            
            
            
            
            
            
            
            
            
            
            
        }catch (Exception e){
        	System.out.println("error in json read ");
        }

	}
}