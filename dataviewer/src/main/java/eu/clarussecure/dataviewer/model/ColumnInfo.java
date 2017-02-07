package eu.clarussecure.dataviewer.model;

public class ColumnInfo {

    private String name;
    private String type;
    private String udtName;
    private boolean nullable;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUdtName() {
        return udtName;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setName(String columnName) {
        this.name = columnName;
    }

    public void setType(String dataType) {
        this.type = dataType;
    }

    public void setUdtName(String udtName) {
        this.udtName = udtName;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

}
