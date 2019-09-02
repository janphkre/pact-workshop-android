package au.com.dius.pactconsumer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import au.com.dius.pactconsumer.pact.AnimalPacts
import au.com.dius.pactconsumer.pact.PactProvider
import au.com.dius.pactconsumer.presentation.TestActivity
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(TestActivity::class.java, false, false)

    @Before
    fun setupPactServer() {
        PactProvider.setupServer()
    }

    @After
    fun teardownPactServer() {
        PactProvider.teardownServer()
    }

    @Test
    fun useAppContext() {
        PactProvider.setPact(AnimalPacts.animalCollectionPact)
        activityTestRule.launchActivity(null)

        onView(withId(R.id.txt_error)).check(matches(not(isDisplayed())))
    }
}
