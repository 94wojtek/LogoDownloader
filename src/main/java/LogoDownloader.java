import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Formatter;
import java.util.List;

public class LogoDownloader {

    private final FileLinesReader fileLinesReader = new DefaultFileLinesReader();
    private final String PATH_TEMPLATE = "PATH TEMPLATE FOR FORMATTING";
    private final String URL_TEMPLATE = "URL TEMPLATE FOR FORMATTING";
    private final List<String> companies = fileLinesReader.readAllLines("PATH TO TEXT FILE WITH COMPANIES LIST");
    private final String DEFAULT_LOGO = "src/main/resources/nordea-default.jpg";

    private Formatter formatter;


    public void loopReadWritePicture() {
         for(String company : companies) {
            writePicture(company);
        }
    }

    public void writePicture(String company) {
        formatter = new Formatter();
        try {
            File outputfile = new File(formatter.format(PATH_TEMPLATE, company).toString());
            ImageIO.write(readPicture(company), "png", outputfile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println("Logo for company " + company + " can't be saved as it was not found.");
        }
    }

    public BufferedImage readPicture(String company) {
        BufferedImage bufferedImage = null;
        formatter = new Formatter();
        try {
            URL url = new URL(formatter.format(URL_TEMPLATE, company).toString());
            bufferedImage = ImageIO.read(url);
        } catch (IOException e) {
            System.err.println(e.getMessage() + " Company logo for " + company + " not found. Default logo assigned.");
            try {
                formatter = new Formatter();
                Files.copy(Path.of(DEFAULT_LOGO), Path.of(formatter.format(PATH_TEMPLATE,
                        company).toString()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return bufferedImage;
    }

    public static void main(String[] args) {
       LogoDownloader ld = new LogoDownloader();
       ld.loopReadWritePicture();
    }
}
