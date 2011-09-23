package com.aimprosoft.wavilon.util;

public class FormatUtil {

    /**
     * This function format functions. I was created because of incorrect behaviour of standart Java formatters
     *
     * @param function - function to format
     * @param params - input params
     * @return formatted function
     */
    public static String formatFunction(String function, Object... params){

        if (params != null && params.length > 0){
            for (int i = 0; i < params.length; i++){
                function = function.replace("{" + i + "}", String.valueOf(params[i]));
            }
        }

        return function;
    }

}
