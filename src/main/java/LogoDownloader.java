import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Formatter;
import java.util.List;

/**
 * Downloader for Customer Brain customer logo. Logo is downloaded from https://logo.clearbit.com/ website.
 * Company name to find logo is retrieved from resource companies.txt file.
 * After download, logo is saved in directory C:\Logos\ created by default constructor of this class.
 * Log is saved as .png file with name of company to which it belongs.
 * If no logo was found, Nordea logo is copied from this content resource root to destination directory.
 *
 * @author Wojciech Wachta
 *
 * @version 1.0
 *
 * 18/01/2021
 *
 */
public class LogoDownloader {

    private final FileLinesReader fileLinesReader = new DefaultFileLinesReader();
    private final String PATH_TEMPLATE = "C:\\Logos\\%s.png";
    private final String URL_TEMPLATE = "https://logo.clearbit.com/%s.com?size=700";
    private final List<String> companies = fileLinesReader.readAllLines("src/main/resources/companies.txt");
    private final String DEFAULT_LOGO = "src/main/resources/nordea-default.jpg";

    private Formatter formatter;

    public LogoDownloader() {
        try {
            Files.createDirectories(Paths.get("C:\\Logos\\"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void loopReadWritePicture() {
         for(String company : companies) {
            writePicture(company);
        }
    }

    //writes downloaded logo to file location
    //logo is named as value of current company param value
    //current company param value is passed to PATH_TEMPLATE variable by Formatter.format()
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

    public void splitNameAndId(String company) {
        String[] nameAndId = company.split(" ");
        System.out.println(nameAndId[0]);
        System.out.println(nameAndId[1]);
    }

    //logo is downloaded from https://logo.clearbit.com/ and stored in BufferedImage object
    //if logo can't be found via vendor website, default Nordea logo is copied to given location
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
       //ld.splitNameAndId("company 123456");
    }
}
