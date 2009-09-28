#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import static org.junit.Assert.*;

import org.apache.camel.ProducerTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(locations = { "/context.xml" })
public class SampleRouteTest {

    @Autowired
    private ProducerTemplate producerTemplate;
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMultiply() throws Exception {
        assertEquals("abcabc", producerTemplate.requestBody("direct:input1", "abc"));
    }
    
    @Test
    public void testReverse() throws Exception {
        assertEquals("cba", producerTemplate.requestBody("direct:input2", "abc"));
    }
    
}
