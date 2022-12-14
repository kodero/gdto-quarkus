/**
 This file is part of javaee-patterns.

 javaee-patterns is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 javaee-patterns is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.opensource.org/licenses/gpl-2.0.php>.

 * Copyright (c) 23. September 2009 Adam Bien, blog.adam-bien.com
 * http://press.adam-bien.com
 */
package com.corvid.genericdto.data.gdto;

/**
 * @author Adam Bien, www.adam-bien.com
 */

public class ValidationException extends Exception {

    private String regExp = null;

    private String contentAsString = null;

    public ValidationException(String regExp, String contentAsString) {
        super("The content: " + contentAsString + " does not match with regex: " + regExp);
        this.regExp = regExp;
        this.contentAsString = contentAsString;
    }


    public String getRegexp() {
        return this.regExp;
    }

    public String getContentAsString() {
        return this.contentAsString;
    }
}
