package util;

import tool.PricingTable;
import tool.Tool;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Assumes that any line that begins with # will be ignored.
public class CsvReader {

    // Generates tool directory based on .csv file
    public Map<String, Tool> generateToolDirectory(File file) throws IOException {
        Map<String, Tool> toolDirectory = new HashMap<>();
        List<String[]> toolInfoList = readCsvFile(file);

        for(String[] toolInfo : toolInfoList) {
            try {
                Tool tool = new Tool(toolInfo[CsvConstants.TOOL_DIRECTORY_TOOL_TYPE_INDEX],
                        toolInfo[CsvConstants.TOOL_DIRECTORY_TOOL_BRAND_INDEX],
                        toolInfo[CsvConstants.TOOL_DIRECTORY_TOOL_CODE_INDEX]);
                toolDirectory.put(toolInfo[CsvConstants.TOOL_DIRECTORY_TOOL_CODE_INDEX], tool);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("ArrayIndexOutOfBoundsException: Invalid tool file formatting.");
            }

        }

        return toolDirectory;
    }


    // Generates pricing directory based on .csv file
    public Map<String, PricingTable> generatePricingDirectory(File file) throws IOException {
        Map<String, PricingTable> pricingDirectory = new HashMap<>();
        List<String[]> pricingInfoList = readCsvFile(file);

        for(String[] pricingInfo : pricingInfoList) {
            try {
                // Maybe throw in custom exception if it's not yes or no
                boolean weekdayCharge = determineChargeBoolean(pricingInfo[CsvConstants.PRICING_TABLE_TOOL_WEEKDAY_CHARGE_INDEX]);
                boolean weekendCharge = determineChargeBoolean(pricingInfo[CsvConstants.PRICING_TABLE_TOOL_WEEKEND_CHARGE_INDEX]);
                boolean holidayCharge = determineChargeBoolean(pricingInfo[CsvConstants.PRICING_TABLE_TOOL_HOLIDAY_CHARGE_INDEX]);

                PricingTable pricingTable = new PricingTable(new BigDecimal(pricingInfo[CsvConstants.PRICING_TABLE_TOOL_DAILY_CHARGE_INDEX]),
                        weekdayCharge,
                        weekendCharge,
                        holidayCharge);
                pricingDirectory.put(pricingInfo[CsvConstants.PRICING_TABLE_TOOL_TYPE_INDEX], pricingTable);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("ArrayIndexOutOfBoundsException: Invalid price table formatting");
            }

        }

        return pricingDirectory;
    }

    // Returns a string by string list of output from csv file.
    private List<String[]> readCsvFile(File file) throws IOException{
        List<String[]> outputList = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;
        while((line = bufferedReader.readLine()) != null) {
            if(!ignoreLine(line)) {
                String[] lineInfo = line.split(",");
                outputList.add(lineInfo);
            }
        }

        return outputList;
    }

    private boolean ignoreLine(String line) {
        boolean ignore = false;
        if(line.isEmpty() || line.startsWith("#")) {
            ignore = true;
        }
        return ignore;
    }

    private boolean determineChargeBoolean(String valueFromFile) {
        return valueFromFile.equalsIgnoreCase("yes") ? true : false;
    }

}
