package com.corvid.genericdto.util;

import java.io.IOException;
import java.util.Currency;

public class TestJavaClass {

    public static void main(String[] args)
        throws IOException {
        
        /*InputStreamReader a = new InputStreamReader(System.in);
        System.out.println("Kindly enter the first number:");
        BufferedReader ar = new BufferedReader(a);
        String input1 = ar.readLine();

        InputStreamReader b = new InputStreamReader(System.in);
        System.out.println("Kindly enter the second number:");
        BufferedReader br = new BufferedReader(b);
        String input2 = br.readLine();
        
        //String output = input1 + input2;
        //uncomment the following line to treat the inputs as integers
        int output = Integer.parseInt(input1) + Integer.parseInt(input2);
        System.out.println(output);*/
      Currency KSH = Currency.getInstance("KES");
      System.out.println("" + KSH.getCurrencyCode()+"  :: " + KSH.getDisplayName() +":: " + KSH.getSymbol());

     // System.out.println(String.format("'%s','%s'","test","2"));

    }
}