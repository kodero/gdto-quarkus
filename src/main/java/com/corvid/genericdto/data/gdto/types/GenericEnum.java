package com.corvid.genericdto.data.gdto.types;

import com.corvid.genericdto.data.gdto.GenericEnumTypeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by mokua,kodero on 3/1/16.
 */
@JsonSerialize(using = GenericEnumTypeSerializer.class)
public class GenericEnum {
    private String enumKey;

    private String enumName;

    public GenericEnum(){

    }

    public GenericEnum(String enumKey, String enumName){
        setEnumKey(enumKey);
        setEnumName(enumName);
    }

    public String getEnumKey() {
        return enumKey;
    }

    public void setEnumKey(String enumKey) {
        this.enumKey = enumKey;
    }

    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }
}
