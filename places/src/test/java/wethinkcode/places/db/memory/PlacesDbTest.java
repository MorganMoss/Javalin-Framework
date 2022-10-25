package wethinkcode.places.db.memory;

import java.util.Set;

import org.junit.jupiter.api.*;
import wethinkcode.places.model.Municipality;


/**
 * Uncomment the body of the test methods. They can't compile until you add appropriate code
 * to the PlacesDb class. Once you make them compile, the tests should fail at first. Now
 * make the tests green.
 */
public class PlacesDbTest
{
    public static final Set<Municipality> MUNICIPALITIES = Set.of(
        new Municipality( "Cape Municipality", "Western Cape" ),
        new Municipality( "Worcester", "Western Cape" ),
        new Municipality( "Riversdale", "Western Cape" ),
        new Municipality( "Gqeberha", "Eastern Cape" ),
        new Municipality( "Queenstown", "Eastern Cape" ),
        new Municipality( "Sandton-East", "Gauteng" ),
        new Municipality( "Riversdale", "Gauteng" ),
        new Municipality( "Mabopane", "Gauteng" ),
        new Municipality( "Brakpan", "Gauteng" )
    );

    @Test
    public void testProvinces(){
//        final PlacesDb db = new PlacesDb( MUNICIPALITIES );
//        assertThat( db.provinces().size() ).isEqualTo( 3 );
    }

    @Test
    public void testTownsInProvince(){
//        final PlacesDb db = new PlacesDb( MUNICIPALITIES );
//        assertThat( db.townsIn( "Gauteng" ).size() ).isEqualTo( 4 );
//        assertThat( db.townsIn( "Eastern Cape" ).size() ).isEqualTo( 2 );
//        assertThat( db.townsIn( "Western Cape" ).size() ).isEqualTo( 3 );
//        assertThat( db.townsIn( "Northern Cape" ) ).isEmpty();
    }
}
