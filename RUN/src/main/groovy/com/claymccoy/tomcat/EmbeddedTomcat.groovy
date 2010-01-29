package com.claymccoy.tomcat

import org.apache.catalina.Context
import org.apache.catalina.Wrapper
import org.apache.catalina.startup.Embedded

public class EmbeddedTomcat {

  final int port
  final String contextPath
  final String tomcatHome

  static EmbeddedTomcat getInstance(String[] args) {
    int port = 8080
    String contextPath = "/HelloWar"
    if (args) {
      port = args[0] as int
      if (args.length > 1) {
        contextPath = args[1]
      }
    }
    new EmbeddedTomcat(port, contextPath)
  }

  EmbeddedTomcat(int port, String contextPath) {
    this.port = port
    tomcatHome = System.getProperty("user.home") + "${contextPath}/tomcat/embedded/port_${this.port}"
    if (!contextPath.isEmpty() && !contextPath.startsWith("/")) {
      contextPath = "/" + contextPath;
    }
    this.contextPath = contextPath

    println "Embedded Tomcat Settings"
    println "Port: ${this.port}"
    println "servlet context path: ${this.contextPath}"
    println "Home: ${tomcatHome}"
  }

  void runWar(String warPath) {
    println "War Path: ${warPath}"
    final ant = new AntBuilder()
    final String appBasePath = "${tomcatHome}${contextPath}"
    ant.unzip(src:warPath, dest:appBasePath)
    run appBasePath
  }

  void run(String appBasePath) {
    println "App Base: ${appBasePath}"

    final embedded = new Embedded()
    final engine = embedded.createEngine()
    final localHost = embedded.createHost("localHost", appBasePath)

    engine.with {
      name = "localEngine"
      addChild localHost
      defaultHost = localHost.name
    }

    final rootContext = embedded.createContext(contextPath, appBasePath)
    rootContext.parentClassLoader = Thread.currentThread().getContextClassLoader()
    localHost.addChild rootContext
    loadDefaultServlets rootContext

    embedded.with {
      catalinaHome = tomcatHome
      addEngine(engine)
      addConnector(createConnector((InetAddress) null, port, false))
      start()
    }

    println "Started Tomcat.  Go to http://localhost:${port}${contextPath}"

    // This is only here because Tomcat will stop when this thread stops.
    while(true) {
      Thread.sleep(1000*60);
    }
  }


  /**
   * Need to map the default and jsp servlets because the default web.xml is not available in embedded model.
   * These are needed to server static content and JSPs respectively
   */
  private void loadDefaultServlets(Context rootContext) {

    final Wrapper defaultServlet = rootContext.createWrapper()
    defaultServlet.with() {
      name = "default"
      servletClass = "org.apache.catalina.servlets.DefaultServlet"
      addInitParameter "debug", "0"
      addInitParameter "listings", "false"
      loadOnStartup = 1
    }

    final Wrapper jspServlet = rootContext.createWrapper();
    jspServlet.with {
      name = "jsp"
      servletClass = "org.apache.jasper.servlet.JspServlet"
      addInitParameter "fork", "false"
      addInitParameter "xpoweredBy", "false"
      loadOnStartup = 2
    }

    rootContext.with {
      addChild defaultServlet
      addServletMapping "/", "default"
      addChild jspServlet
      addServletMapping "*.jsp", "jsp"
      addServletMapping "*.jspx", "jsp"
    }
  }

}