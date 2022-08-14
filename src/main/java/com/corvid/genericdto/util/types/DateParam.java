package com.corvid.genericdto.util.types;

import com.corvid.genericdto.data.gdto.DateConverter;
import com.corvid.genericdto.util.Strings;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParam {
  private final Date date;

  public DateParam(String dateStr) throws WebApplicationException {
      if (Strings.isEmpty(dateStr)) {
          this.date = null;
          return;
      }
      /*String dateFormatString = "dd-MM-yyyy";//default
      if(dateStr.matches("^\\d{2}\\/\\d{2}\\/\\d{4}$")) dateFormatString = "dd/MM/yyyy";
      if(dateStr.matches("^\\d{4}\\/\\d{2}\\/\\d{2}$")) dateFormatString = "yyyy-MM-dd";*/
      //final DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
      if(dateStr.equalsIgnoreCase("today")) {
          this.date = new Date();
      }else {
          try {
              this.date = new DateConverter(true).parse(dateStr);
          } catch (ParseException e) {
              throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                  .entity("Couldn't parse date string: " + e.getMessage()).build());
          }
      }
  }

  public Date getDate() {
    return date;
  }

  public String toString(){
      if(date != null) return new SimpleDateFormat("dd-MM-yyyy").format(date);
      return null;
  }
}