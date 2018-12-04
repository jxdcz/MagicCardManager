package cz.jirix.magiccardmanager;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.jirix.magiccardmanager.activity.MainActivity;
import cz.jirix.magiccardmanager.model.CardSearchCriteria;
import cz.jirix.magiccardmanager.model.MagicColor;
import cz.jirix.magiccardmanager.model.MagicType;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasToString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BasicWalkthroughTest {

    private CardSearchCriteria mCriteria;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        mCriteria = new CardSearchCriteria();
        mCriteria.setSetName("Onslaught");
        mCriteria.setColor("Red");
        mCriteria.setCardName("Goblin");
        mCriteria.setType("Creature");
    }

    @Test
    public void testAppWalkthrough(){

        // search screen
        typeIntoEditText(R.id.edit_card_name, mCriteria.getCardName());

        selectValueFromDropdown(R.id.spinner_color, mCriteria.getColor(), MagicColor.class);
        selectValueFromDropdown(R.id.spinner_type, mCriteria.getType(), MagicType.class);

        typeIntoEditText(R.id.autocomplete_set, mCriteria.getSetName());

        clickButton(R.id.button_search);

        // results list
        checkRecyclerListHasAtLeastItems(R.id.card_list, 5);
        clickOnRecyclerListItem(R.id.card_list, 4);

        // detail screen
        onView(withId(R.id.image_card)).check(matches(isDisplayed()));
    }


    private void typeIntoEditText(int id, String text){
        onView(withId(id)).perform(typeText(text), closeSoftKeyboard());
    }

    private void clickButton(int id){
        onView(withId(id)).perform(click());
    }

    private void selectValueFromDropdown(int id, String value, Class cls){
        onView(withId(id)).perform(click());
        onData(hasToString(value)).perform(click());
        onView(withId(id)).check(matches(withSpinnerText(containsString(value))));

    }

    private void checkRecyclerListHasAtLeastItems(int id, int count){
        onView(withId(id)).check(matches(withRecyclerListSizeAtLeast(count)));
    }

    private void clickOnRecyclerListItem(int id, int position){
        onView(withId(id)).perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));
    }

    public static Matcher<View> withRecyclerListSizeAtLeast (final int size) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                return ((RecyclerView) view).getChildCount () >= size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("RecyclerView should have " + size + " items");
            }
        };
    }
}
