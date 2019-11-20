package com.example.gofit;





import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.core.util.Preconditions.checkNotNull;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegrationTesting  {
Context context ;
    private Instrumentation.ActivityResult mActivityResult;

    @Rule
    public final ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true);



// TEST FOLLOW SEQUENCE





    @Before
    public void setupImageUri() {

        Resources resources = activityRule.getActivity().getResources();
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources
                .getResourcePackageName(
                        R.drawable.viduyat) + '/' + resources.getResourceTypeName(
                R.drawable.viduyat) + '/' + resources.getResourceEntryName(
                R.drawable.viduyat));
        Intent resultData = new Intent();
        resultData.setData(imageUri);
        mActivityResult= new Instrumentation.ActivityResult(
                Activity.RESULT_OK, resultData);

    }

    @Test
    public void a_first_fragment_open()
    {
        Fragment fragment      = new FIrstOnBoarding();
        FragmentManager fm     = activityRule.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame,fragment);
        ft.addToBackStack(null);
        ft.commit();


    }

    @Test
    public void b_TestSlideOnboardingScreens()
    {
        onView(withId(R.id.nextbtn)).perform(click());
        onView(withId(R.id.nextbtn)).perform(click());
        onView(withId(R.id.nextbtn)).perform(click());
        onView(withId(R.id.askFrame)).check(matches(isDisplayed()));

    }


    @Test
    public void c_TestIsSignUpFragDispplayed()
    {
        onView(withId(R.id.nextbtn)).perform(click());
        onView(withId(R.id.nextbtn)).perform(click());
        onView(withId(R.id.nextbtn)).perform(click());
        onView(withId(R.id.normalSignup)).perform(click());

        onView(withId(R.id.username_editText)).check(matches(isDisplayed()));
    }



    @Test
    public void  d_TestSignFields_HasError() throws InterruptedException {
        onView(withId(R.id.nextbtn)).perform(click());
        onView(withId(R.id.nextbtn)).perform(click());
        onView(withId(R.id.nextbtn)).perform(click());
        onView(withId(R.id.normalSignup)).perform(click());

        onView(withId(R.id.username_editText)).perform(typeText(""));
        onView(withId(R.id.goButton)).perform(click());

        onView(withId(R.id.username_editText)).check(matches(hasErrorText("Username should'nt be empty")));
    }





    @Test
    public void e_TestSignFields_HasNoError() throws InterruptedException {

        onView(withId(R.id.nextbtn)).perform(click());
        onView(withId(R.id.nextbtn)).perform(click());
        onView(withId(R.id.nextbtn)).perform(click());
        onView(withId(R.id.normalSignup)).perform(click());

        onView(withId(R.id.username_editText)).perform(typeText("raman"));
        onView(withId(R.id.email_editText)).perform(typeText("0002sran@gmail.com"));
        onView(withId(R.id.psswrd_editText)).perform(typeText("123456"));
        onView(withId(R.id.C_psswrd_editText)).perform(typeText("123456"));

        closeSoftKeyboard();
        Thread.sleep(1);
        onView(withId(R.id.male_radio)).perform(click());

        onView(withId(R.id.goButton)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.button6)).check(matches(isDisplayed()));
        onView(withId(R.id.Demand_Profile_ImageView)).check(matches(not(hasDrawable())));


        Intents.init();
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_PICK),
                hasData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        intending(expectedIntent).respondWith(mActivityResult);


        onView(withId(R.id.button6)).perform(click());
        intended(expectedIntent);
        Intents.release();

        onView(withId(R.id.Demand_Profile_ImageView
        )).check(matches(hasDrawable()));

        onView(withId(R.id.nextbtn)).perform(click());
        Thread.sleep(2000);
    }








    @Test
    public void f_LogInFragmentIsDisplayed()
    {
       onView(withId(R.id.loginEditText)).check(matches(isDisplayed()));
    }

    @Test
    public void g_IfWrongInfoEnteredInLogInFields() throws InterruptedException {
        onView(withId(R.id.loginEditText)).perform(typeText("gg"));
        onView(withId(R.id.loginEditText2)).perform(typeText("ds"));
        closeSoftKeyboard();
        onView(withId(R.id.LogInBtn)).perform(click());

        Thread.sleep(1000);
        onView(withText("Authentication failed.")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));


    }

    @Test
    public void h_LogInSuccess() throws InterruptedException {
        onView(withId(R.id.loginEditText)).perform(typeText("0002sran@gmail.com"));
        onView(withId(R.id.loginEditText2)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.LogInBtn)).perform(click());

        Thread.sleep(1000);
        onView(withText("Authentication Success.")).inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));


    }


    @Test
    public void i_ExerciseListFragIsDisplayed() throws InterruptedException {
        onView(withId(R.id.loginEditText)).perform(typeText("0002sran@gmail.com"));
        onView(withId(R.id.loginEditText2)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.LogInBtn)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.grid_view)).check(matches(isDisplayed()));
    }

    @Test
    public void j_RelativePopup() throws InterruptedException {
        onView(withId(R.id.loginEditText)).perform(typeText("0002sran@gmail.com"));
        onView(withId(R.id.loginEditText2)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.LogInBtn)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.grid_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Thread.sleep(2000);

        onView(withId(R.id.text_for_cycling)).check(matches(withText("Please Select Hours And Week For Cycling")));

        Thread.sleep(1000);

    }

    @Test
    public void k_RelativePopup2() throws InterruptedException {
        onView(withId(R.id.loginEditText)).perform(typeText("0002sran@gmail.com"));
        onView(withId(R.id.loginEditText2)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.LogInBtn)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.grid_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        Thread.sleep(2000);

        onView(withId(R.id.text_for_cycling)).check(matches(withText("Please Select Hours And Week For Rope Jumping")));

        Thread.sleep(1000);

    }


    @Test
    public void l_isPickedDateAdded() throws InterruptedException {
        onView(withId(R.id.loginEditText)).perform(typeText("0002sran@gmail.com"));
        onView(withId(R.id.loginEditText2)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.LogInBtn)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.grid_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        ViewInteraction numPicker = onView(withClassName(Matchers.equalTo(NumberPicker.class.getName())));
        numPicker.perform(setNumber(7));
        Thread.sleep(1000);

        onView(withId(R.id.calenderbb)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 12, 29));
        onView(withText("OK")).perform(click());
        Thread.sleep(100);

        onView(withId(R.id.exercise_upload_button)).perform(click());
        Thread.sleep(100);
        onView(withId(R.id.Profile_screen)).perform(click());
        onView(withId(R.id.Completed_task_layout)).perform(click());
        onView(withId(R.id.weekList)).check(matches(atPosition(0,hasDescendant(withText(endsWith("29:12:2019"))))));
        Thread.sleep(2000);

    }




    @Test
    public void m_isHoursSplited() throws InterruptedException {
        onView(withId(R.id.loginEditText)).perform(typeText("0002sran@gmail.com"));
        onView(withId(R.id.loginEditText2)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.LogInBtn)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.grid_view)).perform(RecyclerViewActions.actionOnItemAtPosition(8, click()));
        ViewInteraction numPicker = onView(withClassName(Matchers.equalTo(NumberPicker.class.getName())));
        numPicker.perform(setNumber(7));
        Thread.sleep(1000);

        onView(withId(R.id.calenderbb)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 12, 29));
        onView(withText("OK")).perform(click());
        Thread.sleep(100);

        onView(withId(R.id.exercise_upload_button)).perform(click());
        Thread.sleep(100);

        onView(withId(R.id.Profile_screen)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.Completed_task_layout)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.weekList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(1000);
        onView(withText("jogging")).perform(click());

        onView(withId(R.id.Exercise_Splited_Hours)).check(matches(withText(endsWith("1 hours - 0 minutes"))));
        Thread.sleep(1000);
    }



    @Test
    public void n_isProgressBarFull() throws InterruptedException {

        onView(withId(R.id.loginEditText)).perform(typeText("0002sran@gmail.com"));
        onView(withId(R.id.loginEditText2)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.LogInBtn)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.grid_view)).perform(RecyclerViewActions.actionOnItemAtPosition(7, click()));

        ViewInteraction numPicker = onView(withClassName(Matchers.equalTo(NumberPicker.class.getName())));
        numPicker.perform(setNumber(7));
        Thread.sleep(1000);

        onView(withId(R.id.calenderbb)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 12, 29));
        onView(withText("OK")).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.exercise_upload_button)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.Profile_screen)).perform(click());
        onView(withId(R.id.Completed_task_layout)).perform(click());
        onView(withId(R.id.weekList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Thread.sleep(1000);
        onView(withText("Yoga")).perform(click());


        ViewInteraction numPicker1 = onView(allOf(withId(R.id.hoursPickerDay1)));
        numPicker1.perform(setNumber(1));
        ViewInteraction numPicker2 = onView(allOf(withId(R.id.minutesPickerDay1)));
        numPicker2.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay1)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker3 = onView(allOf(withId(R.id.hoursPickerDay2)));
        numPicker3.perform(setNumber(1));
        ViewInteraction numPicker4 = onView(allOf(withId(R.id.minutesPickerDay2)));
        numPicker4.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay2)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker5 = onView(allOf(withId(R.id.hoursPickerDay3)));
        numPicker5.perform(setNumber(1));
        ViewInteraction numPicker6 = onView(allOf(withId(R.id.minutesPickerDay3)));
        numPicker6.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay3)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker7 = onView(allOf(withId(R.id.hoursPickerDay4)));
        numPicker7.perform(setNumber(1));
        ViewInteraction numPicker8 = onView(allOf(withId(R.id.minutesPickerDay4)));
        numPicker8.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay4)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker9 = onView(allOf(withId(R.id.hoursPickerDay5)));
        numPicker9.perform(setNumber(1));
        ViewInteraction numPicker10 = onView(allOf(withId(R.id.minutesPickerDay5)));
        numPicker10.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay5)).perform(click());
        Thread.sleep(1000);

        ViewInteraction numPicker11 = onView(allOf(withId(R.id.hoursPickerDay6)));
        numPicker11.perform(setNumber(1));
        ViewInteraction numPicker12 = onView(allOf(withId(R.id.minutesPickerDay6)));
        numPicker12.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay6)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker13 = onView(allOf(withId(R.id.hoursPickerDay7)));
        numPicker13.perform(setNumber(1));
        ViewInteraction numPicker14 = onView(allOf(withId(R.id.minutesPickerDay7)));
        numPicker14.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay7)).perform(click());
        Thread.sleep(1000);

        ProgressBar progressBar = activityRule.getActivity().findViewById(R.id.progressSingle);
        assertThat(progressBar.getProgress(), equalTo(420));
    }


    @Test
    public void oisHistorySaved() throws InterruptedException {
        onView(withId(R.id.loginEditText)).perform(typeText("0002sran@gmail.com"));
        onView(withId(R.id.loginEditText2)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.LogInBtn)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.grid_view)).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));

        ViewInteraction numPicker = onView(withClassName(Matchers.equalTo(NumberPicker.class.getName())));
        numPicker.perform(setNumber(7));
        Thread.sleep(1000);

        onView(withId(R.id.calenderbb)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 12, 29));
        onView(withText("OK")).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.exercise_upload_button)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.Profile_screen)).perform(click());
        onView(withId(R.id.Completed_task_layout)).perform(click());
        onView(withId(R.id.weekList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Thread.sleep(1000);
        onView(withText("Swimming")).perform(click());


        ViewInteraction numPicker1 = onView(allOf(withId(R.id.hoursPickerDay1)));
        numPicker1.perform(setNumber(1));
        ViewInteraction numPicker2 = onView(allOf(withId(R.id.minutesPickerDay1)));
        numPicker2.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay1)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker3 = onView(allOf(withId(R.id.hoursPickerDay2)));
        numPicker3.perform(setNumber(1));
        ViewInteraction numPicker4 = onView(allOf(withId(R.id.minutesPickerDay2)));
        numPicker4.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay2)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker5 = onView(allOf(withId(R.id.hoursPickerDay3)));
        numPicker5.perform(setNumber(1));
        ViewInteraction numPicker6 = onView(allOf(withId(R.id.minutesPickerDay3)));
        numPicker6.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay3)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker7 = onView(allOf(withId(R.id.hoursPickerDay4)));
        numPicker7.perform(setNumber(1));
        ViewInteraction numPicker8 = onView(allOf(withId(R.id.minutesPickerDay4)));
        numPicker8.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay4)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker9 = onView(allOf(withId(R.id.hoursPickerDay5)));
        numPicker9.perform(setNumber(1));
        ViewInteraction numPicker10 = onView(allOf(withId(R.id.minutesPickerDay5)));
        numPicker10.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay5)).perform(click());
        Thread.sleep(1000);

        ViewInteraction numPicker11 = onView(allOf(withId(R.id.hoursPickerDay6)));
        numPicker11.perform(setNumber(1));
        ViewInteraction numPicker12 = onView(allOf(withId(R.id.minutesPickerDay6)));
        numPicker12.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay6)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker13 = onView(allOf(withId(R.id.hoursPickerDay7)));
        numPicker13.perform(setNumber(1));
        ViewInteraction numPicker14 = onView(allOf(withId(R.id.minutesPickerDay7)));
        numPicker14.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay7)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.closepopup)).perform(click());
        onView(withId(R.id.Profile_screen)).perform(click());
        onView(withId(R.id.history_Layout)).perform(click());

        RecyclerView recyclerView = activityRule.getActivity().findViewById(R.id.E_Select);
        int itemCount = recyclerView.getAdapter().getItemCount();

        onView(withId(R.id.E_Select))
                .perform(RecyclerViewActions.scrollToPosition(itemCount - 3));

        Thread.sleep(1000);

        onView(withText("Swimming")).perform(click());

        onView(withId(R.id.E_History)).check(matches(isDisplayed()));

        Thread.sleep(1000);
    }
















    @Test
    public void p_canHistoryShared() throws InterruptedException {
        onView(withId(R.id.loginEditText)).perform(typeText("0002sran@gmail.com"));
        onView(withId(R.id.loginEditText2)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.LogInBtn)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.grid_view)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        ViewInteraction numPicker = onView(withClassName(Matchers.equalTo(NumberPicker.class.getName())));
        numPicker.perform(setNumber(7));
        Thread.sleep(1000);

        onView(withId(R.id.calenderbb)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 12, 29));
        onView(withText("OK")).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.exercise_upload_button)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.Profile_screen)).perform(click());
        onView(withId(R.id.Completed_task_layout)).perform(click());
        onView(withId(R.id.weekList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Thread.sleep(1000);
        onView(withText("Meditate")).perform(click());


        ViewInteraction numPicker1 = onView(allOf(withId(R.id.hoursPickerDay1)));
        numPicker1.perform(setNumber(1));
        ViewInteraction numPicker2 = onView(allOf(withId(R.id.minutesPickerDay1)));
        numPicker2.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay1)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker3 = onView(allOf(withId(R.id.hoursPickerDay2)));
        numPicker3.perform(setNumber(1));
        ViewInteraction numPicker4 = onView(allOf(withId(R.id.minutesPickerDay2)));
        numPicker4.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay2)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker5 = onView(allOf(withId(R.id.hoursPickerDay3)));
        numPicker5.perform(setNumber(1));
        ViewInteraction numPicker6 = onView(allOf(withId(R.id.minutesPickerDay3)));
        numPicker6.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay3)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker7 = onView(allOf(withId(R.id.hoursPickerDay4)));
        numPicker7.perform(setNumber(1));
        ViewInteraction numPicker8 = onView(allOf(withId(R.id.minutesPickerDay4)));
        numPicker8.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay4)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker9 = onView(allOf(withId(R.id.hoursPickerDay5)));
        numPicker9.perform(setNumber(1));
        ViewInteraction numPicker10 = onView(allOf(withId(R.id.minutesPickerDay5)));
        numPicker10.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay5)).perform(click());
        Thread.sleep(1000);

        ViewInteraction numPicker11 = onView(allOf(withId(R.id.hoursPickerDay6)));
        numPicker11.perform(setNumber(1));
        ViewInteraction numPicker12 = onView(allOf(withId(R.id.minutesPickerDay6)));
        numPicker12.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay6)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker13 = onView(allOf(withId(R.id.hoursPickerDay7)));
        numPicker13.perform(setNumber(1));
        ViewInteraction numPicker14 = onView(allOf(withId(R.id.minutesPickerDay7)));
        numPicker14.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay7)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.closepopup)).perform(click());
        onView(withId(R.id.Profile_screen)).perform(click());
        onView(withId(R.id.history_Layout)).perform(click());

        RecyclerView recyclerView = activityRule.getActivity().findViewById(R.id.E_Select);
        int itemCount = recyclerView.getAdapter().getItemCount();

        onView(withId(R.id.E_Select))
                .perform(RecyclerViewActions.scrollToPosition(2));

        Thread.sleep(1000);

        onView(withText("Meditate")).perform(click());
        onView(withText("Share")).perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.trainers_screen)).perform(click());

        onView(withId(R.id.stories_recycler_view)).check(matches(isDisplayed()));

        Thread.sleep(1000);
    }







    @Test
    public void Q_commentCanBeAdded() throws InterruptedException {
        onView(withId(R.id.loginEditText)).perform(typeText("0002sran@gmail.com"));
        onView(withId(R.id.loginEditText2)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.LogInBtn)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.grid_view)).perform(RecyclerViewActions.actionOnItemAtPosition(4, click()));

        ViewInteraction numPicker = onView(withClassName(Matchers.equalTo(NumberPicker.class.getName())));
        numPicker.perform(setNumber(7));
        Thread.sleep(1000);

        onView(withId(R.id.calenderbb)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 12, 29));
        onView(withText("OK")).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.exercise_upload_button)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.Profile_screen)).perform(click());
        onView(withId(R.id.Completed_task_layout)).perform(click());
        onView(withId(R.id.weekList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Thread.sleep(1000);
        onView(withText("Running")).perform(click());


        ViewInteraction numPicker1 = onView(allOf(withId(R.id.hoursPickerDay1)));
        numPicker1.perform(setNumber(1));
        ViewInteraction numPicker2 = onView(allOf(withId(R.id.minutesPickerDay1)));
        numPicker2.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay1)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker3 = onView(allOf(withId(R.id.hoursPickerDay2)));
        numPicker3.perform(setNumber(1));
        ViewInteraction numPicker4 = onView(allOf(withId(R.id.minutesPickerDay2)));
        numPicker4.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay2)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker5 = onView(allOf(withId(R.id.hoursPickerDay3)));
        numPicker5.perform(setNumber(1));
        ViewInteraction numPicker6 = onView(allOf(withId(R.id.minutesPickerDay3)));
        numPicker6.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay3)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker7 = onView(allOf(withId(R.id.hoursPickerDay4)));
        numPicker7.perform(setNumber(1));
        ViewInteraction numPicker8 = onView(allOf(withId(R.id.minutesPickerDay4)));
        numPicker8.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay4)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker9 = onView(allOf(withId(R.id.hoursPickerDay5)));
        numPicker9.perform(setNumber(1));
        ViewInteraction numPicker10 = onView(allOf(withId(R.id.minutesPickerDay5)));
        numPicker10.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay5)).perform(click());
        Thread.sleep(1000);

        ViewInteraction numPicker11 = onView(allOf(withId(R.id.hoursPickerDay6)));
        numPicker11.perform(setNumber(1));
        ViewInteraction numPicker12 = onView(allOf(withId(R.id.minutesPickerDay6)));
        numPicker12.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay6)).perform(click());
        Thread.sleep(1000);


        ViewInteraction numPicker13 = onView(allOf(withId(R.id.hoursPickerDay7)));
        numPicker13.perform(setNumber(1));
        ViewInteraction numPicker14 = onView(allOf(withId(R.id.minutesPickerDay7)));
        numPicker14.perform(setNumber(0));

        onView(withId(R.id.doneButtonDay7)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.closepopup)).perform(click());
        onView(withId(R.id.Profile_screen)).perform(click());
        onView(withId(R.id.history_Layout)).perform(click());

        RecyclerView recyclerView = activityRule.getActivity().findViewById(R.id.E_Select);
        int itemCount = recyclerView.getAdapter().getItemCount();

        onView(withId(R.id.E_Select))
                .perform(RecyclerViewActions.scrollToPosition(4));

        Thread.sleep(1000);

        onView(withText("Running")).perform(click());
        onView(withText("Share")).perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.trainers_screen)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.stories_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.comment_btn)));

        Thread.sleep(1000);

        onView(withId(R.id.comment_edit_text)).perform(typeText("great job"));
        closeSoftKeyboard();
        onView(withId(R.id.comment_add_btn)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.comment_recycler_View)).check(matches(atPosition(0,hasDescendant(withText(endsWith("great job"))))));
        Thread.sleep(1000);
    }





    public static ViewAction typeSearchViewText(final String text){
        return new ViewAction(){
            @Override
            public Matcher<View> getConstraints() {
                //Ensure that only apply if it is a SearchView and if it is visible.
                return allOf(isDisplayed(), isAssignableFrom(SearchView.class));
            }

            @Override
            public String getDescription() {
                return "Change view text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((SearchView) view).setQuery(text,false);
            }


        };
    }




    public static class MyViewAction {

        public static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    v.performClick();
                }
            };
        }

    }


    public static BoundedMatcher<View, ImageView> hasDrawable() {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("has drawable");
            }
            @Override
            public boolean matchesSafely(ImageView imageView) {
                return imageView.getDrawable() != null;
            }
        };
    }







    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }



    public static Matcher<View> withViewAtPosition(final int position, final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(RecyclerView recyclerView) {
                final RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    public static ViewAction setNumber(final int num) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                NumberPicker np = (NumberPicker) view;
                np.setValue(num);

            }

            @Override
            public String getDescription() {
                return "Set the passed number into the NumberPicker";
            }

            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(NumberPicker.class);
            }
        };
    }



}



