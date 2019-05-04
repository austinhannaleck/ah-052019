import checkout.Checkout;
import checkout.RentalAgreement;
import exception.InvalidInputException;
import tool.ToolManager;
import tool.ToolManagerCsv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.Properties;
import java.util.Scanner;

public class pos {
    private Checkout checkout;
    private ToolManager toolManager;
    private String toolCode;
    private String checkoutDate;
    private int rentalDays;
    private int discount;

    public static void main(String[] args) {
        new pos().start();
    }

    public void start() {
        String run = "y";
        String propertiesFile = System.getProperty("propertiesFile");
        Properties props = new Properties();

        try {
            props.load(new FileInputStream(propertiesFile));
        } catch (IOException e) {
            System.out.println("ERROR: Unable to load properties file, shutting down.");
            System.exit(1);
        }

        String configPath = props.getProperty("configurationFilePath");
        File toolsFile = new File(configPath + "/tools.csv");
        File pricingTableFile = new File(configPath + "/pricingTables.csv");
        Scanner input = new Scanner(System.in);

        try {
            loadToolConfigurations(toolsFile, pricingTableFile);
        } catch (IOException e) {
            System.out.println("ERROR: Unable to load configuration files from " + configPath + ". Shutting down.");
            System.exit(1);
        }

        checkout = new Checkout(toolManager);

        while(run.equals("y")) {
            boolean validRentalAgreement = false;
            RentalAgreement rentalAgreement = null;

            collectRentalInfo();

            try {
                rentalAgreement = checkout.generateRentalAgreement(toolCode, rentalDays, discount, checkoutDate);
                validRentalAgreement = true;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            } catch (DateTimeParseException e) {
                System.out.println("ERROR: Invalid date format - please format dates mm/dd/yy");
            }

            if(validRentalAgreement) {
                System.out.println("Generating rental agreement");
                System.out.println("***");
                System.out.println("Tool code: " + rentalAgreement.getToolCode());
                System.out.println("Tool type: " + rentalAgreement.getToolType());
                System.out.println("Tool brand: " + rentalAgreement.getToolBrand());
                System.out.println("Daily charge: " + rentalAgreement.getFormattedDailyCharge());
                System.out.println("Charge days: " + rentalAgreement.getChargeDays());
                System.out.println("Pre-discount charge: " + rentalAgreement.getFormattedPreDiscountCharge());
                System.out.println("Discount percentage: " + rentalAgreement.getFormattedDiscount());
                System.out.println("Discount amount: " + rentalAgreement.getFormattedDiscountAmount());
                System.out.println("Final charge: " + rentalAgreement.getFormattedFinalCharge());

                // Query for another rental agreement
                System.out.println("\nWould you like to submit another rental agreement? Enter 'y' or 'n'.");
                run = input.nextLine();
            }
        }
        System.out.println("Exiting POS system.");
    }

    private void loadToolConfigurations(File toolsFile, File pricingTableFile) throws IOException {
        toolManager = new ToolManagerCsv(toolsFile, pricingTableFile);
    }

    /*
    * Prints view and collects rental info for checkout.
     */
    private void collectRentalInfo() {
        Scanner input = new Scanner(System.in);
        System.out.println("\nPlease input the tool code for the tool being rented:");
        toolCode = input.nextLine();
        System.out.println("Please input the rental days:");
        rentalDays = Integer.parseInt(input.nextLine());
        System.out.println("Please input the checkout date (mm/dd/yy):");
        checkoutDate = input.nextLine();
        System.out.println("Add a percentage discount if applicable:");
        discount = Integer.parseInt(input.nextLine());
    }
}
