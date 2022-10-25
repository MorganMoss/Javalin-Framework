package wethinkcode.places;

import java.io.*;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.annotations.VisibleForTesting;
import wethinkcode.places.db.memory.PlacesDb;
import wethinkcode.places.model.Municipality;
import wethinkcode.places.model.Place;
import wethinkcode.places.model.Places;
import wethinkcode.places.model.Province;


/**
 * PlacesCsvParser : I parse a CSV file with each line containing the fields (in order):
 * <code>Name, Feature_Description, pklid, Latitude, Longitude, Date, MapInfo, Province,
 * fklFeatureSubTypeID, Previous_Name, fklMagisterialDistrictID, ProvinceID, fklLanguageID,
 * fklDisteral, Local Municipality, Sound, District Municipality, fklLocalMunic, Comments, Meaning</code>.
 * <p>
 * For the PlaceNameService we're only really interested in the <code>Name</code>,
 * <code>Feature_Description</code> and <code>Province</code> fields.
 * <code>Feature_Description</code> allows us to distinguish towns and urban areas from
 * (e.g.) rivers, mountains, etc. since our PlaceNameService is only concerned with occupied places.
 */
public class PlacesCsvParser
{
    @VisibleForTesting
    int name_column;
    @VisibleForTesting
    int province_column;
    @VisibleForTesting
    int type_column;
    @VisibleForTesting
    int municipality_column;
    @VisibleForTesting
    int columns;

    private static final List<String> types = List.of("town", "neighbourhood", "populated area", "settled place", "urban area");


    /**
     * Takes into account the type_column,
     * Filters out feature types that are irrelevant
     * @param s - the full line from the csv being parsed
     * @return true if the right type
     */
    boolean isCorrectType(String[] s){
        return types.contains(s[type_column].toLowerCase());
    }

    private Place convertToPlace(String[] data){
        return new Place(data[name_column], data[municipality_column]);
    }

    private Municipality convertToMunicipality(String[] data) {
        return new Municipality(data[municipality_column], data[province_column]);
    }

    private Province convertToProvince(String[] data) {
        return new Province(data[province_column]);
    }

    public Places parseCsvSource( File csvFile ) throws IOException {
        if (!csvFile.exists()){
            throw new FileNotFoundException();
        }

        FileReader fileReader = new FileReader(csvFile);

        LineNumberReader csvReader = new LineNumberReader(fileReader);

        return parseDataLines(csvReader);
    }

    private void parseHeader(List<String> header){
        columns = header.size();
        name_column = header.indexOf("Name");
        province_column = header.indexOf("Province");
        type_column = header.indexOf("Feature_Description");
        municipality_column = header.indexOf("Local Municipality");
    }

    @VisibleForTesting
    Places parseDataLines( final LineNumberReader in ){
        List<String> lines = in.lines().toList();

        parseHeader(List.of(lines.get(0).split(",", -1)));

        Stream<String[]> placeStream = lines
                .stream()
                .map((s) -> s.split(",", -1))
//                .filter((s) -> s.length >= columns)
                .filter(this::isCorrectType);

        List<String[]> placesRaw = placeStream.toList();

        List<Province> provinces = placesRaw
                .stream()
                .map(this::convertToProvince)
                .distinct()
                .toList();

        List<Municipality> municipalities = placesRaw
                .stream()
                .map(this::convertToMunicipality)
                .distinct()
                .toList();

        List<Place> places = placesRaw
                .stream()
                .map(this::convertToPlace)
                .toList();


        return new PlacesDb(provinces, municipalities, places);
    }


}