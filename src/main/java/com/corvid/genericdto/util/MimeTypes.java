package com.corvid.genericdto.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface MimeTypes {

    public final String SERVLET_HTML_CONTENT_TYPE = "text/html;charset=UTF-8";
    public final String DEFAULT_MIME_TYPE = "application/octet-stream";
    public final String RTF_MIME_TYPE = "application/rtf";
    public final String PDF_MIME_TYPE = "application/pdf";
    public final String WORD_MIME_TYPE = "application/msword";
    public final String EXCEL_MIME_TYPE1 = "application/x-msexcel";
    public final String EXCEL_MIME_TYPE2 = "application/vnd.ms-excel";
    public final String POWERPOINT_MIME_TYPE1 = "application/powerpoint";
    public final String POWERPOINT_MIME_TYPE2 = "application/vnd.ms-powerpoint";
    public final String SPINFIRE_MIME_TYPE = "application/xview3d-3d";
    public final String HTML_MIME_TYPE = "text/html";
    public final String XML_MIME_TYPE = "text/xml";
    public final String XHTML_MIME_TYPE = "application/xhtml+xml";
    public final String PLAIN_TEXT_MIME_TYPE = "text/plain";
    public final String ARCHIVE_MIME_TYPE = "application/x-zip-compressed";
    public final String SHORT_ARCHIVE_MIME_TYPE = "application/zip";
    public final String JAVA_ARCHIVE_MIME_TYPE = "application/java-archive";
    public final String JSP_MIME_TYPE = "text/x-jsp";
    public final String PHP_MIME_TYPE = "text/x-php";
    public static final String BZ2_ARCHIVE_MIME_TYPE = "application/x-bzip";
    public static final String GZ_ARCHIVE_MIME_TYPE = "application/x-gzip";
    public static final String GUNZIP_ARCHIVE_MIME_TYPE = "application/x-gunzip";
    public static final String TARGZ_ARCHIVE_MIME_TYPE = "application/x-tar-gz";
    public final String WORD_2007_MIME_TYPE
            = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    public final String WORD_2007_TEMPLATE_MIME_TYPE
            = "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
    public final String WORD_2007_EXTENSION = "application/vnd.ms-word";
    public final String EXCEL_2007_MIME_TYPE
            = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public final String EXCEL_2007_TEMPLATE_MIME_TYPE
            = "application/vnd.openxmlformats-officedocument.spreadsheetml.template";
    public final String EXCEL_2007_EXTENSION = "application/vnd.ms-excel";
    public final String POWERPOINT_2007_MIME_TYPE
            = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    public final String POWERPOINT_2007_TEMPLATE_MIME_TYPE
            = "application/vnd.openxmlformats-officedocument.presentationml.template";
    public final String POWERPOINT_2007_EXTENSION = "application/vnd.ms-powerpoint";
    public final String RSS_MIME_TYPE = "application/rss+xml";
    public final String JAR_EXTENSION = "jar";
    public final String WAR_EXTENSION = "war";
    public final String EAR_EXTENSION = "ear";
    public final String RTF_EXTENSION = "rtf";
    public final String JSP_EXTENSION = "jsp";
    public final String PHP_EXTENSION = "php";
    // Extension .odt (Texte)
    public final String MIME_TYPE_OO_FORMATTED_TEXT = "application/vnd.oasis.opendocument.text";
    // Extension .ods (Tableur)
    public final String MIME_TYPE_OO_SPREADSHEET = "application/vnd.oasis.opendocument.spreadsheet";
    // Extension .odp (Presentation)
    public final String MIME_TYPE_OO_PRESENTATION = "application/vnd.oasis.opendocument.presentation";
    // Extension .odg (Dessin)
    public final String MIME_TYPE_OO_GRAPHICS = "application/vnd.oasis.opendocument.graphics";
    // Extension .odc (Diagramme)
    public final String MIME_TYPE_OO_DIAGRAM = "application/vnd.oasis.opendocument.chart";
    // Extension .odf (Formule)
    public final String MIME_TYPE_OO_FORMULA = "application/vnd.oasis.opendocument.formula";
    // Extension .odb (Base de donnees )
    public final String MIME_TYPE_OO_DB = "application/vnd.oasis.opendocument.database";
    // Extension .odi (Image)
    public final String MIME_TYPE_OO_IMAGE = "application/vnd.oasis.opendocument.image";
    // Extension .odm (Document principal)
    public final String MIME_TYPE_OO_MASTER = "application/vnd.oasis.opendocument.text-master";
    public static final Set<String> MS_OFFICE_MIME_TYPES = new HashSet<>(Arrays
            .asList(
                    new String[]{WORD_MIME_TYPE, EXCEL_MIME_TYPE1, EXCEL_MIME_TYPE2, POWERPOINT_MIME_TYPE1,
                            POWERPOINT_MIME_TYPE2}));
    public static final Set<String> OPEN_OFFICE_MIME_TYPES = new HashSet<>(Arrays.asList(
            new String[]{WORD_MIME_TYPE, WORD_2007_MIME_TYPE, WORD_2007_TEMPLATE_MIME_TYPE,
                    EXCEL_MIME_TYPE1, EXCEL_MIME_TYPE2, EXCEL_2007_MIME_TYPE, EXCEL_2007_TEMPLATE_MIME_TYPE,
                    POWERPOINT_MIME_TYPE1, POWERPOINT_MIME_TYPE2, POWERPOINT_2007_MIME_TYPE,
                    POWERPOINT_2007_TEMPLATE_MIME_TYPE, MIME_TYPE_OO_FORMATTED_TEXT, MIME_TYPE_OO_SPREADSHEET,
                    MIME_TYPE_OO_PRESENTATION, MIME_TYPE_OO_GRAPHICS, MIME_TYPE_OO_DIAGRAM, MIME_TYPE_OO_FORMULA,
                    MIME_TYPE_OO_DB, MIME_TYPE_OO_IMAGE, MIME_TYPE_OO_MASTER}));
    public static final Set<String> ARCHIVE_MIME_TYPES = new HashSet<>(Arrays.asList(
            new String[]{BZ2_ARCHIVE_MIME_TYPE, GZ_ARCHIVE_MIME_TYPE, GUNZIP_ARCHIVE_MIME_TYPE,
                    TARGZ_ARCHIVE_MIME_TYPE, ARCHIVE_MIME_TYPE, SHORT_ARCHIVE_MIME_TYPE, JAVA_ARCHIVE_MIME_TYPE}));
}