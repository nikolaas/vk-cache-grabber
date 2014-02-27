package org.ns.vkcachegrabber.vk.convert.json;

import org.ns.vkcachegrabber.vk.convert.json.JSONUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author stupak
 */
public class JSONUtilsTest extends TestCase {
    
    public JSONUtilsTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private static final List<String> pojoProps = Arrays.asList(
            "id",
            "lastName"
    );

    private static final List<String> jsonProps = Arrays.asList(
            "id",
            "last_name"
    );
    /**
     * Test of toJsonNotation method, of class JSONUtils.
     */
    public void testToJsonNotation() {
        System.out.println("toJsonNotation");
        
        List<String> converted = new ArrayList<>();
        for ( String pojoProp : pojoProps ) {
            converted.add(JSONUtils.toJsonNotation(pojoProp));
        }
        
        System.out.println("pojoProps = " + pojoProps);
        System.out.println("jsonProps = " + jsonProps);
        System.out.println("converted = " + converted);
        
        assertEquals(jsonProps, converted);
    }
    
    /**
     * Test of toPojoNotation method, of class JSONUtils.
     */
    public void testToPojoNotation() {
        System.out.println("toPojoNotation");
        
        List<String> converted = new ArrayList<>();
        for ( String jsonProp : jsonProps ) {
            converted.add(JSONUtils.toPojoNotation(jsonProp));
        }
        
        System.out.println("jsonProps = " + jsonProps);
        System.out.println("pojoProps = " + pojoProps);
        System.out.println("converted = " + converted);
        
        assertEquals(pojoProps, converted);
    }
    
}
