package eu.clarussecure.dataviewer.model;

public class ColumnInfo {

    private String columnName;
    private String dataType;
    private String udtName;
    private boolean nullable;

    public String getColumnName() {
        return columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public String getUdtName() {
        return udtName;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setUdtName(String udtName) {
        this.udtName = udtName;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

}
