package highloadcup.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import highloadcup.models.init.Locations;
import highloadcup.models.init.Users;
import highloadcup.models.init.Visits;
import highloadcup.repository.LocationsRepository;
import highloadcup.repository.UsersRepository;
import highloadcup.repository.VisitsRepository;
import lombok.Getter;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.NumberFormat;

/**
 * Created by Alikin E.A. on 25.08.17.
 */
@Service
public class InitService {

    @Autowired
    LocationsRepository locationsRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    VisitsRepository visitsRepository;

    @Getter
    private volatile long currentTimeStamp = 0l;

    private static final String dataPath = /*"/mnt/highloaddata/"*/"/tmp/data/";


    @PostConstruct
    private void init() {
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataPath + "options.txt")))){
                String timestamp = reader.lines().findFirst().get();
                currentTimeStamp = new Long(timestamp + "000");
                System.out.println("external timestamp = " + currentTimeStamp);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Version 14.0(4/1000) -Xmx4g -Xms4g -server -XX:+AggressiveOpts ");
            Runtime runtime = Runtime.getRuntime();
            final NumberFormat format = NumberFormat.getInstance();
            final long maxMemory = runtime.maxMemory();
            final long allocatedMemory = runtime.totalMemory();
            final long freeMemory = runtime.freeMemory();
            final long mb = 1024 * 1024;
            final String mega = "MB";
            System.out.println("========================== Memory Info ==========================");
            System.out.println("Free memory: " + format.format(freeMemory / mb) + mega);
            System.out.println("Allocated memory: " + format.format(allocatedMemory / mb) + mega);
            System.out.println("Max memory: " + format.format(maxMemory / mb) + mega);
            System.out.println("Total free memory: " + format.format(freeMemory+(maxMemory - allocatedMemory)/mb)  + mega);
            System.out.println("=================================================================\n");

            ZipFile zipFile = new ZipFile(dataPath + "data.zip");
            zipFile.getFileHeaders().stream().forEach(item -> {
                if (item != null) {
                    try {
                        FileHeader fileHeader = (FileHeader)item;
                        if (fileHeader.getFileName().toString().contains("options")) {
                            try (BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(fileHeader)))){
                                String timestamp = reader.lines().findFirst().get();
                                currentTimeStamp = new Long(timestamp + "000");
                                System.out.println("timestamp = " + currentTimeStamp);
                            }
                        }
                        if (fileHeader.getFileName().toString().contains("users")) {
                            new ObjectMapper()
                                    .readValue(zipFile.getInputStream(fileHeader), Users.class)
                                    .getUsers().stream()
                                    .forEach(element -> usersRepository.put(element));
                        }
                        if (fileHeader.getFileName().toString().contains("visits")) {
                            new ObjectMapper()
                                    .readValue(zipFile.getInputStream(fileHeader), Visits.class)
                                    .getVisits().stream()
                                    .forEach(element -> visitsRepository.put(element));
                        }
                        if (fileHeader.getFileName().toString().contains("locations")) {
                            new ObjectMapper()
                                    .readValue(zipFile.getInputStream(fileHeader), Locations.class)
                                    .getLocations().stream()
                                    .forEach(element -> locationsRepository.put(element));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            System.out.println("End unzip");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
