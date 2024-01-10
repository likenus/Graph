package target;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

public class PNGLoader {
    
    private final Path folderPath;

    public PNGLoader(String folderpath) throws IOException {
        this.folderPath = Path.of(folderpath).normalize().toAbsolutePath();
        final File folder = this.folderPath.toFile();

        if (!folder.exists()) {
            throw new IOException(String.format("folder %s does not exist.", this.folderPath.toString()));
        }
        if (!folder.isDirectory()) {
            throw new IOException(String.format("%s is not a directory.", this.folderPath.toString()));
        }
    }

    private File loadFile(String fileName) throws IOException {
        final Path filePath = this.folderPath.resolve(Path.of(fileName));
        final File file = filePath.toFile();

        if (!file.exists()) {
            throw new IOException(String.format("file %s does not exist.", filePath.toString()));
        }
        if (!file.isFile()) {
            throw new IOException(String.format("file %s is not a normal file.", filePath.toString()));
        }

        return file;
    }

    public int[][] loadPng(String fileName) throws IOException {
        File file = loadFile(fileName);

        BufferedImage image = ImageIO.read(file);

        int height = image.getHeight();
        int width = image.getWidth();

        int[][] arr = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                arr[i][j] = image.getRGB(j, i);
            }
        }

        return arr;
    }
}
