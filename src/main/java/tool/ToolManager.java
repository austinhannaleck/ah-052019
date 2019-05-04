package tool;

import java.math.BigDecimal;

public interface ToolManager {

    Tool getTool(String toolCode);

    PricingTable getPricingDirectory(String toolType);

    BigDecimal getDailyRentalCharge(String toolType);
}
