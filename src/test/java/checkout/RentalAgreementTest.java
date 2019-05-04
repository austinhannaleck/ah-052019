package checkout;

import exception.InvalidInputException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import tool.Tool;
import tool.ToolManager;
import tool.ToolManagerCsv;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RentalAgreementTest {
    private String checkoutDate;
    private File toolFile;
    private File pricingTableFile;
    private ToolManager toolManager;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");

    @Before
    public void setup() throws IOException {
        toolFile = new File(getClass().getResource("../testTools.csv").getFile());
        pricingTableFile = new File("src/test/java/config/pricingTables.csv");
        toolManager = new ToolManagerCsv(toolFile, pricingTableFile);
    }

    @Test(expected = InvalidInputException.class)
    public void chainsawWithInvalidDiscountCode() throws InvalidInputException {
        checkoutDate = "09/03/15";
        Tool tool = toolManager.getTool("CHNS");
        RentalAgreement rentalAgreement = new RentalAgreement(tool,
                toolManager.getPricingDirectory(tool.getType()),
                5,
                101,
                LocalDate.parse(checkoutDate, formatter));
    }

    @Test
    public void ladderDuringJuly4th() throws InvalidInputException {
        checkoutDate = "07/02/20";
        Tool tool = toolManager.getTool("LADW");
        RentalAgreement rentalAgreement = new RentalAgreement(tool, toolManager.getPricingDirectory(tool.getType()), 3, 10,
                LocalDate.parse(checkoutDate, formatter));

        verifyRentalAgreementValues(rentalAgreement,
                "07/05/20",
                "$1.99",
                2,
                "$3.98",
                "10%",
                "$0.40",
                "$3.58");

    }

    @Test
    public void chainsawDuringJuly4th() throws InvalidInputException {
        checkoutDate = "07/02/15";
        Tool tool = toolManager.getTool("CHNS");
        RentalAgreement rentalAgreement = new RentalAgreement(tool, toolManager.getPricingDirectory(tool.getType()), 5, 25,
                LocalDate.parse(checkoutDate, formatter));

        verifyRentalAgreementValues(rentalAgreement,
                "07/07/15",
                "$1.49",
                3,
                "$4.47",
                "25%",
                "$1.12",
                "$3.35");

    }

    @Test
    public void jackhammerDuringLaborDay() throws InvalidInputException {
        checkoutDate = "09/03/15";
        Tool tool = toolManager.getTool("JAKD");
        RentalAgreement rentalAgreement = new RentalAgreement(tool, toolManager.getPricingDirectory(tool.getType()), 6, 0,
                LocalDate.parse(checkoutDate, formatter));

        verifyRentalAgreementValues(rentalAgreement,
                "09/09/15",
                "$2.99",
                3,
                "$8.97",
                "0%",
                "$0.00",
                "$8.97");
    }

    @Test
    public void jackHammerDuringFourthOfJulyNoDiscount() throws InvalidInputException {
        checkoutDate = "07/02/15";
        Tool tool = toolManager.getTool("JAKR");
        RentalAgreement rentalAgreement = new RentalAgreement(tool, toolManager.getPricingDirectory(tool.getType()), 9, 0,
                LocalDate.parse(checkoutDate, formatter));

        verifyRentalAgreementValues(rentalAgreement,
                "07/11/15",
                "$2.99",
                5,
                "$14.95",
                "0%",
                "$0.00",
                "$14.95");
    }

    @Test
    public void jackhammerDuringFourthOfJulyWithDiscount() throws InvalidInputException {
        checkoutDate = "07/02/20";
        Tool tool = toolManager.getTool("JAKR");
        RentalAgreement rentalAgreement = new RentalAgreement(tool, toolManager.getPricingDirectory(tool.getType()), 4, 50,
                LocalDate.parse(checkoutDate, formatter));

        verifyRentalAgreementValues(rentalAgreement,
                "07/06/20",
                "$2.99",
                1,
                "$2.99",
                "50%",
                "$1.50",
                "$1.49");
    }

    private void verifyRentalAgreementValues(RentalAgreement rentalAgreement,
                                             String expectedDueDate,
                                             String expectedDailyCharge,
                                             int expectedChargeDays,
                                             String expectedFormattedPreDiscountCharge,
                                             String expectedFormattedDiscount,
                                             String expectedFormattedDiscountAmount,
                                             String expectedFormattedFinalCharge) {
        assertEquals(expectedDueDate, rentalAgreement.getDueDate());
        assertEquals(expectedDailyCharge, rentalAgreement.getFormattedDailyCharge());
        assertEquals(expectedChargeDays, rentalAgreement.getChargeDays());
        assertEquals(expectedFormattedPreDiscountCharge, rentalAgreement.getFormattedPreDiscountCharge());
        assertEquals(expectedFormattedDiscount, rentalAgreement.getFormattedDiscount());
        assertEquals(expectedFormattedDiscountAmount, rentalAgreement.getFormattedDiscountAmount());
        assertEquals(expectedFormattedFinalCharge, rentalAgreement.getFormattedFinalCharge());
    }

}
