package com.example.anu.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.anu.bakingapp.ui.activity.RecipeDetailsActivity;
import com.example.anu.bakingapp.utils.Constants;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.anu.bakingapp.TestUtils.withRecyclerView;
import static com.example.anu.bakingapp.utils.Constants.KEY_RECIPE;
import static com.example.anu.bakingapp.utils.Constants.KEY_RECIPE_ID;
import static org.hamcrest.Matchers.allOf;


//annotation to specify AndroidJUnitRunner as the default test runner
@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityBasicTest {

    private static final int SAMPLE_RECIPE_ID = 1;

    //rule that provides functional testing of an activity
    @Rule public ActivityTestRule<RecipeDetailsActivity> mActivityTestRule =
            new ActivityTestRule<RecipeDetailsActivity>(RecipeDetailsActivity.class){
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent i = new Intent(targetContext, RecipeDetailsActivity.class);
                    i.putExtra(KEY_RECIPE_ID, SAMPLE_RECIPE_ID);
                    return i;
                }
            };



    @Test
    public void checkIngredientsDiaplyed(){

        //check if ingredients title is displayed
        onView(withId(R.id.txt_ingredients_title)).check(matches(isDisplayed()));

        // Check item at position 3 has "salt"
        CharSequence ingredientName = InstrumentationRegistry.getTargetContext().getString(R.string.sample_ingredient_name);
        onView(withRecyclerView(R.id.recycler_view_ingredients)
                .atPositionOnView(3, R.id.txt_ingredient))
                .check(matches(withText(String.valueOf(ingredientName))));

        //check if clicking on step is redirecting to stepsDetails
        onView(withId(R.id.recycler_view_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

       CharSequence stepsTitle = InstrumentationRegistry.getTargetContext()
                .getString(R.string.sample_steps_title);
        matchToolbarTitle(stepsTitle);
    }

    private static void matchToolbarTitle(CharSequence title) {
        onView(
                allOf(
                        isAssignableFrom(TextView.class),
                        withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(title.toString())));
    }
}
