package com.claymccoy;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;

public class HelloWarServlet
    extends HttpServlet {
	
  @Override
  public void init()
      throws ServletException {
    System.out.println("Initializing Servlet...");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    process(request, response);
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    process(request, response);
  }

  private void process(HttpServletRequest request, HttpServletResponse response) {
    PrintWriter out = null;
    try {
      out = response.getWriter();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    out.println("Hello, I am a Servlet.");
  }

}

