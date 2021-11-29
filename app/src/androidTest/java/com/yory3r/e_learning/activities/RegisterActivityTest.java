package com.yory3r.e_learning.activities;


import static androidx.test.espresso.Espresso.onData;
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
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.RootMatchers;
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
public class RegisterActivityTest {

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityTestRule = new ActivityTestRule<>(RegisterActivity.class);

    @Test
    public void registerActivityTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton.perform(scrollTo(), click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.ivFoto),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.cardview.widget.CardView")),
                                        0),
                                0)));
        appCompatImageView.perform(scrollTo(), click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.btnGaleri), withText("Buka Galeri"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.cardview.widget.CardView")),
                                        0),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.etNama),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutNama),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText.perform(replaceText("Yolif Syebathanim"), closeSoftKeyboard());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton4.perform(scrollTo(), click());

        ViewInteraction materialAutoCompleteTextView = onView(
                allOf(withId(R.id.tvTanggalLahir),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutTanggalLahir),
                                        0),
                                1),
                        isDisplayed()));
        materialAutoCompleteTextView.perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton5.perform(scrollTo(), click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton6.perform(scrollTo(), click());

        ViewInteraction materialAutoCompleteTextView2 = onView(
                allOf(withId(R.id.tvJenisKelamin),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutJenisKelamin),
                                        0),
                                1),
                        isDisplayed()));
        materialAutoCompleteTextView2.perform(click());

//        DataInteraction materialTextView = onData(anything())
//                .inAdapterView(childAtPosition(
//                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
//                        0))
//                .atPosition(1);
//        materialTextView.perform(click());

        onView(withText("Perempuan"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton7.perform(scrollTo(), click());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.etNomorTelepon),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutNomorTelepon),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText2.perform(replaceText("123"), closeSoftKeyboard());

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton8.perform(scrollTo(), click());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.etNomorTelepon), withText("123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutNomorTelepon),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText3.perform(replaceText("0815"));

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.etNomorTelepon), withText("0815"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutNomorTelepon),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText4.perform(closeSoftKeyboard());

        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton9.perform(scrollTo(), click());

        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.etNomorTelepon), withText("0815"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutNomorTelepon),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText5.perform(replaceText("08154270121111"));

        ViewInteraction textInputEditText6 = onView(
                allOf(withId(R.id.etNomorTelepon), withText("08154270121111"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutNomorTelepon),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText6.perform(closeSoftKeyboard());

        ViewInteraction materialButton10 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton10.perform(scrollTo(), click());

        ViewInteraction textInputEditText7 = onView(
                allOf(withId(R.id.etNomorTelepon), withText("08154270121111"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutNomorTelepon),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText7.perform(replaceText("081542701211"));

        ViewInteraction textInputEditText8 = onView(
                allOf(withId(R.id.etNomorTelepon), withText("081542701211"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutNomorTelepon),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText8.perform(closeSoftKeyboard());

        ViewInteraction materialButton11 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton11.perform(scrollTo(), click());

        ViewInteraction textInputEditText9 = onView(
                allOf(withId(R.id.etEmail),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText9.perform(replaceText("123"), closeSoftKeyboard());

        ViewInteraction materialButton12 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton12.perform(scrollTo(), click());

        ViewInteraction textInputEditText10 = onView(
                allOf(withId(R.id.etEmail), withText("123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText10.perform(replaceText("123@gmail"));

        ViewInteraction textInputEditText11 = onView(
                allOf(withId(R.id.etEmail), withText("123@gmail"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText11.perform(closeSoftKeyboard());

        ViewInteraction materialButton13 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton13.perform(scrollTo(), click());

        ViewInteraction textInputEditText12 = onView(
                allOf(withId(R.id.etEmail), withText("123@gmail"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText12.perform(replaceText("123.com"));

        ViewInteraction textInputEditText13 = onView(
                allOf(withId(R.id.etEmail), withText("123.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText13.perform(closeSoftKeyboard());

        ViewInteraction materialButton14 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton14.perform(scrollTo(), click());

        ViewInteraction textInputEditText14 = onView(
                allOf(withId(R.id.etEmail), withText("123.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText14.perform(replaceText("yolifsyebathanim@yahoo.com"));

        ViewInteraction textInputEditText15 = onView(
                allOf(withId(R.id.etEmail), withText("yolifsyebathanim@yahoo.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText15.perform(closeSoftKeyboard());

        ViewInteraction materialButton15 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton15.perform(scrollTo(), click());

        ViewInteraction textInputEditText16 = onView(
                allOf(withId(R.id.etPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutPassword),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText16.perform(replaceText("123"), closeSoftKeyboard());

        ViewInteraction materialButton16 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton16.perform(scrollTo(), click());

        ViewInteraction textInputEditText17 = onView(
                allOf(withId(R.id.etPassword), withText("123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutPassword),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText17.perform(click());

        ViewInteraction textInputEditText18 = onView(
                allOf(withId(R.id.etPassword), withText("123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutPassword),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText18.perform(replaceText("123123"));

        ViewInteraction textInputEditText19 = onView(
                allOf(withId(R.id.etPassword), withText("123123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutPassword),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText19.perform(closeSoftKeyboard());

        ViewInteraction materialButton17 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton17.perform(scrollTo(), click());

        ViewInteraction textInputEditText20 = onView(
                allOf(withId(R.id.etKonfirmasiPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutKonfirmasiPassword),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText20.perform(replaceText("123"), closeSoftKeyboard());

        ViewInteraction materialButton18 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton18.perform(scrollTo(), click());

        ViewInteraction textInputEditText21 = onView(
                allOf(withId(R.id.etKonfirmasiPassword), withText("123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutKonfirmasiPassword),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText21.perform(replaceText("123123"));

        ViewInteraction textInputEditText22 = onView(
                allOf(withId(R.id.etKonfirmasiPassword), withText("123123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutKonfirmasiPassword),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText22.perform(closeSoftKeyboard());

        ViewInteraction materialButton19 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton19.perform(scrollTo(), click());

        ViewInteraction textInputEditText23 = onView(
                allOf(withId(R.id.etEmail), withText("yolifsyebathanim@yahoo.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText23.perform(click());

        ViewInteraction textInputEditText24 = onView(
                allOf(withId(R.id.etEmail), withText("yolifsyebathanim@yahoo.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText24.perform(replaceText("123@yahoo.com"));

        ViewInteraction textInputEditText25 = onView(
                allOf(withId(R.id.etEmail), withText("123@yahoo.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutEmail),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText25.perform(closeSoftKeyboard());

        ViewInteraction materialButton20 = onView(
                allOf(withId(R.id.btnRegister), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton20.perform(scrollTo(), click());
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
