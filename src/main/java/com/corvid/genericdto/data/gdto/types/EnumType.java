package com.corvid.genericdto.data.gdto.types;

import com.corvid.genericdto.util.LoggingUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.WordUtils;
import org.jboss.logging.Logger.Level;

import java.io.IOException;
import java.util.Map;

/**
 * Created by mokua,kodero on 3/1/16.
 */
public class EnumType extends AbstractType<GenericEnum>{

    public EnumType(String regExp) {
        super(regExp);
    }

    public EnumType(String regExp, String contentAsString) {
        super(regExp);
        this.instantiateFromString(contentAsString);
    }

    public EnumType() {
        this(null);
    }

    protected GenericEnum construct(String content) {
        LoggingUtil.log(EnumType.class, Level.DEBUG, String.format("Construct enum instance from string [%s]", content));
        if (content == null) return null;
        if (content.matches("^\\{.*\\}")){//object representation of enum
            ObjectMapper mapper = new ObjectMapper();

            LoggingUtil.log(EnumType.class, Level.DEBUG, String.format("Found an object representation of enum, normalizing.....[%s]", content));
            Map<String,Object> enumMap = null;
            try {
                enumMap = mapper.readValue(content, Map.class);
                return new GenericEnum((String)enumMap.get("key"), (String)enumMap.get("name"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            //construct generic enum from simple content
            return new GenericEnum(content, WordUtils.capitalize(content.replace("_", " ")));
        }
        return null;
    }

    @Override
    public String toString() {
        return "{" +
                "EnumType ='" + getValue() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnumType)) return false;
        EnumType that = (EnumType) o;
        if (!this.t.equals(that.t)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }
}
