package helpers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileHelpers {

    static File jarFile = new File(FileHelpers.class.getProtectionDomain().getCodeSource().getLocation().getPath());

//    public static convertPath(String path) throws IOException {
//
//        if(jarFile.isFile()) {  // Run with JAR file
//
//            final JarFile jar = new JarFile(jarFile);
//            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
//            while(entries.hasMoreElements()) {
//                final String name = entries.nextElement().getName();
//                if (name.startsWith(path + "/")) { //filter according to the path
//                    System.out.println(name);
//                }
//            }
//
//            jar.close();
//
//        } else { // Run with IDE
//
//            final URL url = Launcher.class.getResource("/" + path);
//            if (url != null) {
//                try {
//                    final File apps = new File(url.toURI());
//                    for (File app : apps.listFiles()) {
//                        System.out.println(app);
//                    }
//                } catch (URISyntaxException ex) {
//                    // never happens
//                }
//            }
//
//        }
//
//
//    }

    public static boolean checkJar() {
        return jarFile.isFile();
    }

//    public static File getNodesCSV() {
//        if(checkJar()) {
//
//        } else {
//
//        }
//    }

    public static InputStream getNodesCSV() {
        InputStream in = FileHelpers.class.getResourceAsStream(Constants.CSV_NODES);

        return in;
    }

    public static InputStream getEdgesCSV() {
        InputStream in = FileHelpers.class.getResourceAsStream(Constants.CSV_EDGES);

        return in;
    }

}
