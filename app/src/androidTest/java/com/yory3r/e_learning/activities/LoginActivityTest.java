package com.yory3r.e_learning.activities;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.yory3r.e_learning.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginActivityTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnLogin), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton.perform(scrollTo(), click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.etEmail),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText.perform(replaceText("123"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.btnLogin), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton2.perform(scrollTo(), click());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.etEmail), withText("123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText2.perform(click());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.etEmail), withText("123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText3.perform(replaceText("123@gmail"));

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.etEmail), withText("123@gmail"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText4.perform(closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.btnLogin), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.etEmail), withText("123@gmail"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText5.perform(replaceText("123@gmail.com"));

        ViewInteraction textInputEditText6 = onView(
                allOf(withId(R.id.etEmail), withText("123@gmail.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText6.perform(closeSoftKeyboard());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.btnLogin), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton4.perform(scrollTo(), click());

        ViewInteraction textInputEditText7 = onView(
                allOf(withId(R.id.etPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutPassword),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText7.perform(replaceText("123"), closeSoftKeyboard());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.btnLogin), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton5.perform(scrollTo(), click());

        ViewInteraction textInputEditText8 = onView(
                allOf(withId(R.id.etPassword), withText("123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutPassword),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText8.perform(replaceText("123123"));

        ViewInteraction textInputEditText9 = onView(
                allOf(withId(R.id.etPassword), withText("123123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutPassword),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText9.perform(closeSoftKeyboard());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.btnLogin), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton6.perform(scrollTo(), click());

        ViewInteraction textInputEditText10 = onView(
                allOf(withId(R.id.etEmail), withText("123@gmail.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText10.perform(replaceText("yolifsyebathanim@yahoo.com"));

        ViewInteraction textInputEditText11 = onView(
                allOf(withId(R.id.etEmail), withText("yolifsyebathanim@yahoo.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText11.perform(closeSoftKeyboard());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.btnLogin), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton7.perform(scrollTo(), click());

        ViewInteraction textInputEditText12 = onView(
                allOf(withId(R.id.etPassword), withText("123123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutPassword),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText12.perform(replaceText("123123"));

        ViewInteraction textInputEditText13 = onView(
                allOf(withId(R.id.etPassword), withText("123123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutPassword),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText13.perform(closeSoftKeyboard());

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.btnLogin), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton8.perform(scrollTo(), click());

        ViewInteraction textInputEditText14 = onView(
                allOf(withId(R.id.etPassword), withText("123123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutPassword),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText14.perform(replaceText("123123123"));

        ViewInteraction textInputEditText15 = onView(
                allOf(withId(R.id.etPassword), withText("123123123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutPassword),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText15.perform(closeSoftKeyboard());

        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.btnLogin), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton9.perform(scrollTo(), click());
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
