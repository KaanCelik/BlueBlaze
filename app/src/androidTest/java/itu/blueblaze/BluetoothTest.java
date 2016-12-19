package itu.blueblaze;

import android.app.Activity;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import itu.blueblaze.bluetooth.DeviceListActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by KaaN on 17-12-2016.
 * Test class for bluetooth functionality.
 */

@RunWith(AndroidJUnit4.class)
public class BluetoothTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private BluetoothServiceIdlingResource mBluetoothServiceIdlingResource;

    @Before
    public void registerBluetoothServiceIdlingResource(){

        mBluetoothServiceIdlingResource = new BluetoothServiceIdlingResource(mActivityTestRule.getActivity());
        Espresso.registerIdlingResources(mBluetoothServiceIdlingResource);
    }


    @After
    public void unregisterIdlingResource(){
        Espresso.unregisterIdlingResources(mBluetoothServiceIdlingResource);
    }



    @Test
    public void mainActivityTestSuccessfulConnection() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Connect a device - Secure"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction textView = onView(
                allOf(withText("DESKTOP-AJ3UGPJ\n00:DB:DF:E1:73:09"),
                        childAtPosition(
                                withId(R.id.paired_devices),
                                0),
                        isDisplayed()));
        textView.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withText("connected to DESKTOP-AJ3UGPJ"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("connected to DESKTOP-AJ3UGPJ")));

    }


    @Test
    public void mainActivityTestAddParameter() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.menu_item_new_word), withContentDescription("New Parameter"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withText("Add new parameter"),
                        childAtPosition(
                                allOf(withId(R.id.title_template),
                                        childAtPosition(
                                                withId(R.id.topPanel),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Add new parameter")));

        ViewInteraction editText = onView(
                allOf(withId(R.id.dialog_parameter_name), withText("Enter parameter name.."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.custom),
                                        0),
                                0),
                        isDisplayed()));
        editText.check(matches(withText("Enter parameter name..")));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.dialog_parameter_value), withText("Enter parameter value.."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.custom),
                                        0),
                                1),
                        isDisplayed()));
        editText2.check(matches(withText("Enter parameter value..")));

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.dialog_parameter_name), isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.dialog_parameter_name), isDisplayed()));
        appCompatEditText2.perform(replaceText("test param"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.dialog_parameter_value), isDisplayed()));
        appCompatEditText3.perform(replaceText("12"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.param_name_label), withText("test param"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("test param")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.param_value), withText("12"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_list_recycler_view),
                                        0),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("12")));

    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }


}
