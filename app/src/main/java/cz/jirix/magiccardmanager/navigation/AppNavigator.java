package cz.jirix.magiccardmanager.navigation;

import android.content.Context;
import android.content.Intent;

import cz.jirix.magiccardmanager.activity.CardDetailActivity;
import cz.jirix.magiccardmanager.activity.CardListActivity;

public class AppNavigator {

    public static void goToCardListDetailActivity(Context context) {
        Intent intent = new Intent(context, CardListActivity.class);
        context.startActivity(intent);
    }

    public static void goToCardDetailActivity(Context context){
        Intent intent = new Intent(context, CardDetailActivity.class);
        context.startActivity(intent);

    }

}
