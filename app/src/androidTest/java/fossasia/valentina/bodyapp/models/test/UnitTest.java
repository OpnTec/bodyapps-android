package fossasia.valentina.bodyapp.models.test;

import com.google.android.gms.internal.ju;

import junit.framework.TestCase;

import fossasia.valentina.bodyapp.models.Person;
import fossasia.valentina.bodyapp.models.User;

public class UnitTest extends TestCase {

    public void testPersorn() throws Exception {
        Person person= new Person("test1","test2",1);
        assertNotNull(person);
        assertEquals("test1", person.getEmail());
        assertEquals("test2",person.getName());
        assertEquals(1,person.getGender());

    }

    public void testUser() throws Exception {
        User user=new User("test1","test2","test3");
        assertNotNull(user);
        assertEquals("test1", user.getEmail());
        assertEquals("test2",user.getName());
        assertEquals(1,user.getId());
    }
}
