package com.claymccoy.tomcat

final ClassLoader classLoader = Thread.currentThread().getContextClassLoader()
final URL resource = classLoader.getResource("")
final File rootPath = new File(resource.getFile().replace("%20", " "))

final File appBasePath = new File(rootPath.absolutePath + "/../../../WEB/src/main/webapp")
assert appBasePath.exists()

EmbeddedTomcat.getInstance(args).run(appBasePath.absolutePath)
