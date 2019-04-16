package helpers;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.*;

public class FileHelpers {

    static File jarFile = new File(FileHelpers.class.getProtectionDomain().getCodeSource().getLocation().getPath());

    /**
     * Checks if the program currently runs in JAR
     * @return true if we're in JAR, false otherwise
     */
    public static boolean checkJar() {
        return jarFile.isFile();
    }

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

    /**
     * Copies a zip file to the specified output path
     * @param inputPath
     * @param outputPath
     * @return
     * @throws IOException
     */
    public static String extractFolder(String inputPath, String outputPath) throws IOException {

        if(checkJar()) {

            // Extract folder
            InputStream inputStream = null;

            OutputStream outputStream = null;

            try {

                inputStream =  FileHelpers.class.getResourceAsStream(inputPath);
                outputStream = new FileOutputStream("./" + outputPath);

                byte[] buf = new byte[1024];

                int bytesRead;

                while ((bytesRead = inputStream.read(buf)) > 0) {

                    outputStream.write(buf, 0, bytesRead);

                }

            }
            finally {


                inputStream.close();

                outputStream.close();

            }

            return "./" + outputPath;

        } else {

            // Return path
            return inputPath;

        }

    }

    /**
     * Unzips the specified ZIP file to the specified destination (./destination/destination)
     * @param source
     * @param destination
     * @return
     */
    public static String unzipFile(String source, String destination) {

        try {
            ZipFile zipFile = new ZipFile(source);

            if(!zipFile.isEncrypted()) {

                zipFile.extractAll("./" + destination);

                return "./" + destination + "/" + destination;

            } else {
                return null;
            }
        } catch (ZipException e) {
            e.printStackTrace();

            return null;
        }

    }

}
