package com.corvid.genericdto.data.gdto;

import com.corvid.genericdto.shared.time.CalendarDate;
import com.corvid.genericdto.util.LoggingUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.jboss.logging.Logger.Level;

/**
 * @author mokua,kodero
 *         {
 *         <p/>
 *         yyyy-MM-dd
 *         }
 */
public class CalendarDateDeserializer extends JsonDeserializer<CalendarDate> {

    @Override
    public CalendarDate deserialize(JsonParser jsonParser, DeserializationContext ctxt)
        throws IOException {
     JsonToken curr = jsonParser.getCurrentToken();
      // Usually should just get string value:
      CalendarDate returnValue = null;
      if (curr == JsonToken.VALUE_STRING) {
        // BEGIN NULL_STRING fix
        if (jsonParser.getText() == null) {
          return null;
        }
        // END NULL_STRING fix

        String dateString = jsonParser.getText();
        DateConverter dc = new DateConverter(false);
        try {
          Date date = dc.parse(dateString);
          returnValue = CalendarDate.fromDate(date);
        } catch (ParseException e) {
          e.printStackTrace();
          throw new IllegalStateException(e);
        }
      }
      LoggingUtil.log(CalendarDateDeserializer.class, Level.DEBUG, String.format("Converted date [%s]", returnValue));
      return returnValue;
    }
}
