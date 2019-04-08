package helpers;

import java.io.*;

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

    /**
     * Deletes and re-creates the specified File (if the file exists).
     * Otherwise, it tries to create it.
     * @param file
     * @return
     * @throws IOException
     */
    public static boolean recreateFile(File file) throws IOException {

        if(!file.exists()) {

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.close();

        } else {

            if(file.delete()) {

                System.out.println("File " + file.getName() + " was deleted!");

                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.close();

                System.out.println("File " + file.getName() + " was created again!");

            } else {
                throw new IOException("Cannot delete file!!");
            }

        }

        return true;
    }

    /**
     *
     * @return InputStream of the Nodes CSV
     */
    public static InputStream getNodesCSV() {
        InputStream in = FileHelpers.class.getResourceAsStream(Constants.CSV_NODES);

        return in;
    }

    /**
     *
     * @return InputStream of the Edges CSV
     */
    public static InputStream getEdgesCSV() {
        InputStream in = FileHelpers.class.getResourceAsStream(Constants.CSV_EDGES);

        return in;
    }

    /**
     *
     * @return InputStream of the Workspaces CSV
     */
    public static InputStream getWorkspacesCSV() {
        InputStream in = FileHelpers.class.getResourceAsStream(Constants.CSV_WORKSPACES);

        return in;
    }

}
