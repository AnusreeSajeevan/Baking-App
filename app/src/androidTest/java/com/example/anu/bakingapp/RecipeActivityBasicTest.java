package com.example.anu.bakingapp;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.anu.bakingapp.ui.activity.RecipeActivity;
import com.example.anu.bakingapp.ui.viewmodel.RecipeViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

//annotation to specify AndroidJUnitRunner as the default test runner
@RunWith(AndroidJUnit4.class)
public class RecipeActivityBasicTest {

    private RecipeViewModel recipeViewModel;

    //rule that provides functional testing of an activity
    @Rule public ActivityTestRule<RecipeActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeActivity.class);

    @Test
    public void clickOnRecipeViewItem_OpensRecipeDetailsActivity(){



        // // Uses {@link Espresso#onData(org.hamcrest.Matcher)} to get a reference to a specific
        // gridview item and clicks it.
        onView(withId(R.id.recycler_view_recipes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        CharSequence title = InstrumentationRegistry.getTargetContext()
                .getString(R.string.sample_recipe_name);
        matchToolbarTitle(title);
    }


    private static void matchToolbarTitle(CharSequence title) {
        onView(
                allOf(
                        isAssignableFrom(TextView.class),
                        withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(title.toString())));
    }
}
