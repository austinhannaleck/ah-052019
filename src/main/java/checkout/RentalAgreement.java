package checkout;

import exception.InvalidInputException;
import tool.PricingTable;
import tool.Tool;
import util.HolidayUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/*
* Representation of a rental agreement.
*
* Percents are formatted 99%
* Currency is formatted $9,999.99
*
*
 */
public class RentalAgreement {

    private Tool tool;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
    private PricingTable pricingTable;
    private String toolCode;
    private String toolType;
    private String toolBrand;
    private int rentalDays;
    private int discount;
    private int chargeDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private BigDecimal discountAmount;
    private BigDecimal preDiscountCharge;
    private BigDecimal finalCharge;

    public RentalAgreement(Tool tool, PricingTable pricingTable, int rentalDays, int discount, LocalDate checkoutDate) throws InvalidInputException {
        this.tool = tool;
        this.toolCode = tool.getToolCode();
        this.toolType = tool.getType();
        this.toolBrand = tool.getBrand();
        this.pricingTable = pricingTable;
        this.rentalDays = rentalDays;
        this.discount = discount;
        this.checkoutDate = checkoutDate;

        validateInput();

        // Generate rental agreement given input is valid.
        generateRentalAgreement();
    }


    /*
    *
     */
    private void validateInput() throws InvalidInputException{
        if(rentalDays < 1) {
            throw new InvalidInputException("ERROR: Invalid input. Ensure that rental days are greater than 1");
        }

        if(discount > 100 || discount < 0) {
            throw new InvalidInputException("ERROR: Invalid input. Ensure that discount is in the range 0-100");
        }
    }

    private void generateRentalAgreement() {
        // Calculate due date.
        dueDate = checkoutDate.plusDays(rentalDays);

        // Calculate chargeable days.
        chargeDays = calculateChargeableDays();

        // Calculate total before discount, discount amount based on user-inputted percentage, and total after discount.
        preDiscountCharge = pricingTable.getDailyCharge().multiply(new BigDecimal(chargeDays)).setScale(2, RoundingMode.HALF_UP);
        discountAmount = calculateDiscountAmount();
        finalCharge = preDiscountCharge.subtract(discountAmount);
    }

    /*
    * Calculates discount amount based on user provided discount charge.
     */
    private BigDecimal calculateDiscountAmount() {
        return preDiscountCharge.multiply(new BigDecimal(discount)
                .divide(new BigDecimal(100)))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /*
     * Calculates chargeable days, from day after checkout through and including the due date.
     * Excludes "no charge" days, as determined by the tool's price table.
     */
    private int calculateChargeableDays() {
        int chargeableDays = 0;
        boolean isWeekdayCharge = pricingTable.isWeekdayCharge();
        boolean isWeekendCharge = pricingTable.isWeekendCharge();
        boolean isHolidayCharge = pricingTable.isHolidayCharge();

        for(int i = 1; i <= rentalDays; i++) {
            boolean chargeable = false;
            LocalDate rentalDate = checkoutDate.plusDays(i);
            DayOfWeek dayOfWeek = rentalDate.getDayOfWeek();

            // Check weekday charge.
            if(isWeekdayCharge && (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY)) {
                chargeable = true;
            }

            // Check holiday charge. Must check holiday before checking for weekend, in the event of an observed holiday.
            if(HolidayUtil.isHoliday(rentalDate) && !isHolidayCharge) {
                chargeable = false;
            }

            // Check weekend charge.
            if(isWeekendCharge && (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)) {
                chargeable = true;
            }

            if(chargeable) {
                chargeableDays++;
            }
        }

        return chargeableDays;
    }

    public String getFormattedDiscount() {
        return new String(Integer.toString(discount) + "%");
    }

    public String getFormattedDiscountAmount() {
        return NumberFormat.getCurrencyInstance().format(discountAmount);
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public int getChargeDays() {
        return chargeDays;
    }

    public String getFormattedPreDiscountCharge() {
        return NumberFormat.getCurrencyInstance().format(preDiscountCharge);
    }

    public String getFormattedFinalCharge() {
        return NumberFormat.getCurrencyInstance().format(finalCharge);
    }

    public String getCheckoutDate() {
        return checkoutDate.format(formatter);
    }

    public String getDueDate() {
        return dueDate.format(formatter);
    }

    public String getToolCode() {
        return toolCode;
    }

    public String getToolBrand() { return toolBrand; }

    public String getToolType() {
        return toolType;
    }

    public String getFormattedDailyCharge() {
        String formattedDailyCharge =
                NumberFormat.getCurrencyInstance().format(pricingTable.getDailyCharge());
        return formattedDailyCharge;
    }
}
