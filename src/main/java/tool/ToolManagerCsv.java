package tool;

import util.CsvReader;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class ToolManagerCsv implements ToolManager{

    private CsvReader csvReader;
    private Map<String, Tool> toolDirectory;
    private Map<String, PricingTable> pricingDirectory;

    public ToolManagerCsv(File toolFile, File pricingFile) throws IOException {
        csvReader = new CsvReader();
        toolDirectory = csvReader.generateToolDirectory(toolFile);
        pricingDirectory = csvReader.generatePricingDirectory(pricingFile);
    }

    @Override
    public Tool getTool(String toolCode) {
        return toolDirectory.get(toolCode);
    }

    @Override
    public PricingTable getPricingDirectory(String toolType) {
        return pricingDirectory.get(toolType);
    }

    @Override
    public BigDecimal getDailyRentalCharge(String toolType) {
        return pricingDirectory.get(toolType).getDailyCharge();
    }

}
