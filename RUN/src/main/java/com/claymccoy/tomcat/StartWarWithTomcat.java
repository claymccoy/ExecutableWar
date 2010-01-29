package com.claymccoy.tomcat;

import java.security.ProtectionDomain;
import java.net.URL;
import java.io.File;

public class StartWarWithTomcat {

  public static void main(String args[]) {
	
    final ProtectionDomain protectionDomain = StartWarWithTomcat.class.getProtectionDomain();
    final URL location = protectionDomain.getCodeSource().getLocation();
    String warPath = location.toExternalForm();
    EmbeddedTomcat.getInstance(args).runWar(warPath.replaceFirst("file:", ""));

  }

}
