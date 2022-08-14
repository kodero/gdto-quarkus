package com.corvid.genericdto.data.gdto;

import com.corvid.genericdto.shared.MonetaryAmount;
import com.corvid.genericdto.util.LoggingUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Iterator;
import java.util.Map;

import org.jboss.logging.Logger.Level;

/**
 * @author mokua,kodero {
 * <p/>
 * "currency": "KES", "value": 4564 }
 */
public class MonetaryAmountDeserializer extends JsonDeserializer<MonetaryAmount> {

    @Override
    public MonetaryAmount deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        LoggingUtil.log(this.getClass(), Level.DEBUG, String.format(" All fields for money [%s]", fields));
        MonetaryAmount returnValue = null;
        Map.Entry<String, JsonNode> next = fields.next();
        String currencyCode = next.getValue().textValue();
        final Currency currency = Currency.getInstance(currencyCode);
        //get next, some cases the amount is not specified, we only have the currency
        BigDecimal value = BigDecimal.ZERO;
        if (fields.hasNext()) {
            next = fields.next();
            value = next.getValue().decimalValue();
        }
        LoggingUtil.log(this.getClass(), Level.DEBUG, String.format("Currency code [%s], value [%s]", currencyCode, value));

        returnValue = MonetaryAmount.valueOf(value, currency, MonetaryAmount.DEFAULT_ROUNDING_MODE);
        return returnValue;
    }
}
