package lab6;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.tuple.Pair;


public class Lab6Main {
    public static void main(String[] args) {
        List<Path> files;
        Path source = Path.of("./src/main/java/lab6/img/src");
        try (Stream<Path> stream = Files.list(source)) {
            files = stream.toList();
            ForkJoinPool pool = new ForkJoinPool(4);
            //files.forEach(System.out::println);

            long time = System.currentTimeMillis();
            pool.submit(
                    () -> files.parallelStream()
                        .filter(path -> path.toString().toLowerCase().endsWith(".jpg"))
                        .map(path -> {
                            try {
                                BufferedImage image = ImageIO.read(path.toFile());
                                System.out.println("File " + path.getFileName() + " read");
                                return Pair.of(path.getFileName().toString(), image);
                            } catch (IOException e) {
                                System.out.println("Error reading file");
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .map(originalPair -> {
                            BufferedImage original = originalPair.getRight();
                            BufferedImage newImage = new BufferedImage(original.getWidth(),
                                                                        original.getHeight(),
                                                                        original.getType());
                            for (int i = 0; i < original.getWidth(); i++) {
                                for (int j = 0; j < original.getHeight(); j++) {
                                    int rgb = original.getRGB(i, j);
                                    Color color = new Color(rgb);
                                    int red = color.getRed();
                                    int blue = color.getBlue();
                                    int green = color.getGreen();
                                    Color outColor = new Color(red, blue, green);
                                    newImage.setRGB(i, j, outColor.getRGB());
                                }
                            }
                            System.out.println("File " + originalPair.getLeft() + " modified");
                            return Pair.of(originalPair.getLeft(), newImage);
                        })
                        .forEach(pair -> {
                            try {
                                ImageIO.write(pair.getRight(),
                                        "jpeg",
                                        Path.of("./src/main/java/lab6/img/mod/" + pair.getLeft()).toFile());
                                System.out.println("File " + pair.getLeft() + " written");
                            } catch (IOException e) {
                                System.out.println("Error writing file " + pair.getLeft());
                            }
                        })
            ).join();
            System.out.println("Time: " + (System.currentTimeMillis() - time) + "ms");


        } catch (IOException e) {
            System.out.println("Error reading files");
        }
    }
}
