package com.example.anu.bakingapp;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.anu.bakingapp.ui.activity.RecipeActivity;
import com.example.anu.bakingapp.ui.activity.RecipeDetailsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

//annotation to specify AndroidJUnitRunner as the default test runner
@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityBasicTest {

    //rule that provides functional testing of an activity
    @Rule public ActivityTestRule<RecipeDetailsActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeDetailsActivity.class);

    @Test
    public void clickOnRecipeViewItem_OpensRecipeDetailsActivity(){

        onView(withId(R.id.txt_ingredients_title)).check(matches(isDisplayed()));

    }
}
