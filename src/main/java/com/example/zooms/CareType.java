package com.example.zooms;

public class CareType {
    private int careTypeId;
    private String careTypeName;

    public CareType(int careTypeId, String careTypeName) {
        this.careTypeId = careTypeId;
        this.careTypeName = careTypeName;
    }

    public int getCareTypeId() {
        return careTypeId;
    }

    public void setCareTypeId(int careTypeId) {
        this.careTypeId = careTypeId;
    }

    public String getCareTypeName() {
        return careTypeName;
    }

    public void setCareTypeName(String careTypeName) {
        this.careTypeName = careTypeName;
    }
}
