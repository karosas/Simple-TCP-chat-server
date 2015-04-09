package com.chatroom.servlet;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletContextListener;

import com.chatroom.model.*;

public class StartupServlet implements ServletContextListener {

	// private static Logger logger = Logger.getAnonymousLogger(ApplicationListener.class);

	    @Override
	    public void contextDestroyed(ServletContextEvent servletContextEvent) {
	       // logger.info("class : context destroyed");

	    }

	    @Override
	    public void contextInitialized(ServletContextEvent servletContextEvent) {
	        ServletContext context = servletContextEvent.getServletContext();
	        ///// HERE You launch your class
	        ChatServer server = new ChatServer();
	        server.start();
	       // logger.info("myapp : context Initialized");
	    }

}
