package za.co.bcx.velocity.promisingfuture;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;



import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("za.co.bcx.velocity.promisingfuture", appContext.getPackageName());

    }

//    @Test
//    public void we_have_a_api_client() {
//        StackOverflowApiClient apiClient = new StackOverflowApiClient();
//        assertNotNull("No API client object!. It's null!",apiClient);
//    }
//
//    @Test
//    public void api_client_can_call_a_api_function_to_list_active_tags() {
//        StackOverflowApiClient apiClient = new StackOverflowApiClient();
//        assertNotNull("No API client object!. It's null!",apiClient);
//
//        apiClient.makeActiveTagQuery(1,10,);
//
//    }
}
