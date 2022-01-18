import java.util.List;

@FunctionalInterface
public interface FileLinesReader {
    List<String> readAllLines(String fileName);
}