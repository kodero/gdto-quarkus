package com.corvid.genericdto.data.gdto;

import com.corvid.genericdto.shared.Money;
import com.corvid.genericdto.util.LoggingUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
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
 * @author mokua,kodero
 *         {
 *         <p/>
 *         "currency": "KES",
 *         "value": 4564
 *         }
 */
public class MoneyDeserializer extends JsonDeserializer<Money> {

    @Override
    public Money deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        LoggingUtil.log(this.getClass(), Level.DEBUG, String.format("All fields for money [%s]", fields));
        Money returnValue = null;
        Map.Entry<String, JsonNode> next = fields.next();
        String currencyCode = next.getValue().textValue();
        final Currency currency = Currency.getInstance(currencyCode);
        //get next
        next = fields.next();
        final BigDecimal value = next.getValue().decimalValue();
        LoggingUtil.log(this.getClass(), Level.DEBUG, String.format("Currency code [%s] ', value [%s]", currencyCode, value));

        returnValue = Money.valueOf(value, currency);
        return returnValue;
    }
}
