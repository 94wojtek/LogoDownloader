import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class DefaultFileLinesReader implements FileLinesReader {
    @Override
    public List<String> readAllLines(String fileName) {
        List<String> tmpList = new LinkedList<>();
        try {
            tmpList = Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return tmpList;
    }
}
