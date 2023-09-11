package aug.laundry.enums.fileUpload;

public enum FileUploadType {
    DELIVERY("DELIVERY"),
    REPAIR("REPAIR"),
    INSPECTION("INSPECTION");

    private final String tableType;

    FileUploadType(String tableType) {
        this.tableType = tableType;
    }

    public String getTableType(){
        return tableType;
    }

}
