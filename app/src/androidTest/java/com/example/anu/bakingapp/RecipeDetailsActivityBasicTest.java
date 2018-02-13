package com.example.anu.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.anu.bakingapp.ui.activity.RecipeDetailsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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
