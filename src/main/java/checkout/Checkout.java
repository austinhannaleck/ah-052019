package checkout;

import exception.InvalidInputException;
import tool.PricingTable;
import tool.Tool;
import tool.ToolManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Checkout {
    private ToolManager toolManager;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");

    public Checkout(ToolManager toolManager) {
        this.toolManager = toolManager;
    }

    public RentalAgreement generateRentalAgreement(String toolCode,
                                                   int rentalDays,
                                                   int discount,
                                                   String date) throws InvalidInputException {
        Tool tool = toolManager.getTool(toolCode);
        PricingTable pricingTable;
        if(tool != null) {
            pricingTable = toolManager.getPricingDirectory(tool.getType());
        } else {
            throw new InvalidInputException("ERROR: Tool code doesn't exist. Please input a valid tool code.");
        }

        return new RentalAgreement(tool, pricingTable, rentalDays, discount, LocalDate.parse(date, formatter));
    }
}
