package util;

import org.junit.Before;
import org.junit.Test;
import tool.PricingTable;
import tool.Tool;

import org.hamcrest.collection.IsMapContaining;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class CsvReaderTest {

    private CsvReader csvReader = new CsvReader();
    private Map<String, Tool> toolDirectory = new HashMap<>();
    private Map<String, PricingTable> pricingDirectory = new HashMap<>();
    private File validToolFile;
    private File invalidToolFile;
    private File validPricingTableFile;
    private File invalidPricingTableFile;

    @Before
    public void setup() {
        validToolFile = new File(getClass().getResource("../testTools.csv").getFile());
        invalidToolFile = new File(getClass().getResource("../testToolsInvalid.csv").getFile());
        validPricingTableFile = new File(getClass().getResource("../pricingTables.csv").getFile());
        invalidPricingTableFile = new File(getClass().getResource("../pricingTablesInvalid.csv").getFile());
    }

    @Test
    public void assertSuccessfulToolDirectoryGeneration() throws IOException {
        toolDirectory = csvReader.generateToolDirectory(validToolFile);

        // Map contains four items
        assertThat(toolDirectory.size(), is(4));

        // Map contains expected tool codes as keys
        assertThat(toolDirectory, IsMapContaining.hasKey("LADW"));
        assertThat(toolDirectory, IsMapContaining.hasKey("CHNS"));
        assertThat(toolDirectory, IsMapContaining.hasKey("JAKR"));
        assertThat(toolDirectory, IsMapContaining.hasKey("JAKD"));
    }

    @Test
    public void assertInvalidToolFileWorksButIgnoresInvalidToolConfigs() throws IOException {
        toolDirectory = csvReader.generateToolDirectory(invalidToolFile);

        // Map contains two items
        assertThat(toolDirectory.size(), is(2));

        // Map contains expected tool codes as keys
        assertThat(toolDirectory, IsMapContaining.hasKey("LADW"));
        assertThat(toolDirectory, IsMapContaining.hasKey("HUSK"));
        assertThat(toolDirectory, not(IsMapContaining.hasKey("CHNS")));
    }

    @Test
    public void assertSuccessfulPricingDirectoryGeneration() throws IOException {
        pricingDirectory = csvReader.generatePricingDirectory(validPricingTableFile);

        // Map contains three items
        assertThat(pricingDirectory.size(), is(3));

        // Map contains expected tool types as keys
        assertThat(pricingDirectory, IsMapContaining.hasKey("Ladder"));
        assertThat(pricingDirectory, IsMapContaining.hasKey("Jackhammer"));
        assertThat(pricingDirectory, IsMapContaining.hasKey("Chainsaw"));
    }

    @Test
    public void assertInvalidPricingFileWorksButIgnoresInvalidPricingConfigs() throws IOException {
        pricingDirectory = csvReader.generatePricingDirectory(invalidPricingTableFile);

        // Map contains one item
        assertThat(pricingDirectory.size(), is(1));

        // Map contains expected tool types as keys
        assertThat(pricingDirectory, not(IsMapContaining.hasKey("Ladder")));
        assertThat(pricingDirectory, IsMapContaining.hasKey("Shovel"));
    }

    @Test(expected = IOException.class)
    public void assertInvalidFilePathThrowsException() throws IOException {
        File invalidPath = new File("junkPath");
        pricingDirectory = csvReader.generatePricingDirectory(invalidPath);
    }


}
