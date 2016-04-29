/**
 * @author Bhumit
 *
 */

package com.bits.sshweb;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@SuppressWarnings("serial")
public class HelloServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter out = resp.getWriter();
    //OutputStream oo = resp.getOutputStream();
    //oo.write(12345);
    out.println("Hello bhumit");
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    GregorianCalendar gcalendar = new GregorianCalendar();
	String failureImageFileName=dateFormat.format(new java.util.Date())+File.separator +gcalendar.get(Calendar.HOUR)+gcalendar.get(Calendar.MINUTE)+"_"+gcalendar.getTimeInMillis()+"\n";
    try{
    	doSSH();
    }
    catch(Exception e){
    	out.println("1. "+failureImageFileName);
    	out.println("2. "+gcalendar.get(Calendar.HOUR)+gcalendar.get(Calendar.MINUTE)+"_"+gcalendar.getTimeInMillis()+"\n");
    	out.println("3. "+e);
    	out.println("4. "+e.getStackTrace().toString());
    	out.println("5. "+e.getMessage());
    	out.println("6. "+e.getLocalizedMessage());
    	out.println();
    }
    
    out.println("After SSH .... "+failureImageFileName);
    
  }
  
  public static void doSSH() throws JSchException, IOException, InterruptedException{
  	String command = "vt100";
	String command1 = "ls";
	

	JSch jsch = new JSch();
	Session session = jsch.getSession("adminb", "ec2-54-172-8-78.compute-1.amazonaws.com", 443);
	session.setPassword("admin");
	java.util.Properties config = new java.util.Properties();
	config.put("StrictHostKeyChecking", "no");
	session.setConfig(config);
	session.connect();

	Channel channel = session.openChannel("shell");
	OutputStream inputstream_for_the_channel = channel.getOutputStream();
	PrintStream commander = new PrintStream(inputstream_for_the_channel, true);

	channel.setOutputStream(System.out, true);

	channel.connect();
	//commander.println(command);    
	commander.println(command1);

	commander.println("exit");
	commander.close();

	do {
		Thread.sleep(1000);
	} 
	while(!channel.isEOF());
	session.disconnect();
  }
}

