package com.scaleset.geo.geonames;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.scaleset.geo.AbstractFeatureParser;
import com.scaleset.geo.Feature;
import com.scaleset.utils.IntegerUtils;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * Reader for geonames.org US data file.
 * 
 * @author topher
 * 
 */
public class GeoNamesParser extends AbstractFeatureParser {

    // fipscode (subject to change to iso code), isocode for the
    // us and ch, see file admin1Codes.txt for display names of this code;
    // varchar(20
    public final static int ADMIN1 = 10;
    // code for the second administrative division, a county in the US, see file
    // admin2Codes.txt;
    // varchar(80)
    public final static int ADMIN2 = 11;
    // code for third level administrative division, varchar(20)
    public final static int ADMIN3 = 12;
    // code for fourth level administrative division, varchar(20)
    public final static int ADMIN4 = 13;
    // comma separated varchar(4000)
    public final static int ALTERNATE_NAMES = 3;
    // name of geographical point in plain ascii characters, varchar(200)
    public final static int ASCIINAME = 2;
    // alternate country codes, comma separated, ISO-3166 2-letter country code,
    // 60 characters
    public final static int CC2 = 9;
    // ISO-3166 2-letter country code, 2 characters
    public final static int COUNTRY_CODE = 8;
    // elevation : in meters, integer
    public final static int ELEVATION = 15;
    // see http://www.geonames.org/export/codes.html, char(1)
    public final static int FEATURE_CLASS = 6;
    // see http://www.geonames.org/export/codes.html, varchar(10)
    public final static int FEATURE_CODE = 7;
    // integer id of record in geonames database
    public final static int GEONAMEID = 0;
    // average elevation of 30'x30' (ca 900mx900m) area in meters, integer
    public final static int GTOPO30 = 16;
    // latitude in decimal degrees (wgs84)
    public final static int LATITUDE = 4;
    // longitude in decimal degrees (wgs84)
    public final static int LONGITUDE = 5;
    // modification date : date of last modification in yyyy-MM-dd format
    public final static int MODIFICATION_DATE = 18;

    // name of geographical point (utf8) varchar(200)
    public final static int NAME = 1;

    // integer
    public final static int POPULATION = 14;

    // timezone : the timezone id (see file timeZone.txt)
    public final static int TIMEZONE = 17;

    private DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd");

    public GeoNamesParser() {
    }

    public void parse(InputStream in) throws Exception {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
        begin();
        String line = null;
        while ((line = buffer.readLine()) != null) {
            String[] values = line.split("\t");
            Feature feature = parsePlace(values);
            handle(feature);
        }
        in.close();
        end();
    }

    Date parseDate(String dateValue) {
        Date result = null;
        try {
            result = readFormat.parse(dateValue);
        } catch (ParseException e) {
        }
        return result;
    }

    Feature parsePlace(String[] fields) {
        Feature result = new Feature();

        result.setId(fields[GEONAMEID]);
        result.put("asciiName", fields[ASCIINAME]);
        result.put("alternateNames", fields[ALTERNATE_NAMES].split(","));
        result.geometry(new Coordinate(Float.valueOf(fields[LONGITUDE]), Float.valueOf(fields[LATITUDE]), 0));
        result.put("featureClass", fields[FEATURE_CLASS]);
        result.put("featureCode", fields[FEATURE_CODE]);
        result.put("countryCode", fields[COUNTRY_CODE]);
        result.put("countryCode2", fields[CC2]);
        result.put("admin1", fields[ADMIN1]);
        result.put("admin2", fields[ADMIN2]);
        result.put("admin3", fields[ADMIN3]);
        result.put("admin4", fields[ADMIN4]);
        result.put("population", IntegerUtils.valueOf(fields[POPULATION], 0));
        result.put("elevation", IntegerUtils.valueOf(fields[ELEVATION], 0));
        result.put("gtopo30", IntegerUtils.valueOf(fields[GTOPO30], 0));
        // TODO: check for null
        result.put("timeZone", IntegerUtils.valueOf(fields[TIMEZONE], 0));
        result.put("modified", parseDate(fields[MODIFICATION_DATE]));
        return result;
    }
}
