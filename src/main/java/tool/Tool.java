package tool;

public class Tool {

    private String type;
    private String brand;
    private String toolCode;

    public Tool() {

    }

    public Tool(String type, String brand, String toolCode) {
        this.type = type;
        this.brand = brand;
        this.toolCode = toolCode;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public String getToolCode() {
        return toolCode;
    }
}
