package fossasia.valentina.bodyapp.models.test;

import com.google.android.gms.internal.ju;

import junit.framework.TestCase;

import fossasia.valentina.bodyapp.models.Person;

/**
 * Created by RAND on 6/18/2014.
 */
public class UnitTest extends TestCase {
    public void testName() throws Exception {
        Person p= new Person("asd","asd",1);
        assertNotNull(p);

    }
}
