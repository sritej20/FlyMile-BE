package ca.flymile.CreditCards;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Data
@Accessors(chain = true)
public class CreditCard {
    private int cardID;
    private String countryCode;
    private boolean cardTypeBusiness;
    private String cardIssuer;
    private String applyURL;
    private String cardName;
    private String cardImagePath;

    private static List<CreditCard> readCreditCardsFromFile(String filename) {
        List<CreditCard> creditCards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null && i < 36) {
                i++;
                String[] parts = line.split("#");
                int cardID = Integer.parseInt(parts[0]);
                String countryCode = parts[1];
                boolean cardTypeBusiness = Boolean.parseBoolean(parts[2]);
                String cardIssuer = parts[3];
                String applyURL = parts[4];
                String cardName = parts[5];
                String cardImagePath = parts[6];

                CreditCard card = new CreditCard(cardID, countryCode, cardTypeBusiness, cardIssuer, applyURL, cardName, cardImagePath);
                creditCards.add(card);
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return creditCards;
    }

    public static void main(String[] args) {
        String filePath = "src/main/java/ca/flymile/CreditCards/cardDetails.txt";
        List<CreditCard> creditCards = readCreditCardsFromFile(filePath);
        String filePathToWrite = "src/main/java/ca/flymile/CreditCards/creditCards.json";
        writeCreditCardsToJsonFile(creditCards, filePathToWrite);
    }
    public static void writeCreditCardsToJsonFile(List<CreditCard> creditCards, String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(creditCards);

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(json);
        } catch (IOException e) {
            System.err.println("An error occurred while writing JSON to file: " + e.getMessage());
        }
    }
}
