package target;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class FileLoader {

    private static final String GRAPH_FILE_NAME = "Graph2.dat";

    private final Path folderPath;
    
    public FileLoader(String folderpath) throws IOException {
        this.folderPath = Path.of(folderpath).normalize().toAbsolutePath();
        final File folder = this.folderPath.toFile();

        if (!folder.exists()) {
            throw new IOException(String.format("folder %s does not exist.", this.folderPath.toString()));
        }
        if (!folder.isDirectory()) {
            throw new IOException(String.format("%s is not a directory.", this.folderPath.toString()));
        }
    }

    public List<String> loadGraph() throws IOException {
        return loadSimulationFile(GRAPH_FILE_NAME);
    }

    public List<String> loadGraph(String fileName) throws IOException {
        return loadSimulationFile(fileName);
    }

    public List<String> loadSimulationFile(String fileName) throws IOException {
        final Path filePath = this.folderPath.resolve(Path.of(fileName));
        final File file = filePath.toFile();

        if (!file.exists()) {
            throw new IOException(String.format("file %s does not exist.", filePath.toString()));
        }
        if (!file.isFile()) {
            throw new IOException(String.format("file %s is not a normal file.", filePath.toString()));
        }

        return Files.readAllLines(filePath);
    }
}
