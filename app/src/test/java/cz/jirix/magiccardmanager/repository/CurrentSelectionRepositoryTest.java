package cz.jirix.magiccardmanager.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import cz.jirix.magiccardmanager.BaseTest;
import cz.jirix.magiccardmanager.BuildConfig;
import cz.jirix.magiccardmanager.model.CardSearchCriteria;
import cz.jirix.magiccardmanager.model.MagicCard;
import cz.jirix.magiccardmanager.persistence.MagicCardDao;
import cz.jirix.magiccardmanager.persistence.MagicSetDao;
import cz.jirix.magiccardmanager.webservices.MagicCardApi;
import cz.jirix.magiccardmanager.webservices.MagicCardsResponse;
import okhttp3.Headers;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class CurrentSelectionRepositoryTest extends BaseTest{

    private MagicCardApi mCardApi;
    private MagicCardDao mCardDao;
    private MagicSetDao mSetDao;

    @Before
    public void before(){
        mCardApi = mock(MagicCardApi.class);
        mCardDao = mock(MagicCardDao.class);
        mSetDao = mock(MagicSetDao.class);

        makeRxSync();
    }


    @After
    public void after(){
        resetRxSync();
    }


    @Test
    public void testSelectingCard(){
        List<MagicCard> testCards = new ArrayList<>();
        testCards.add(new MagicCard("id1"));
        testCards.add(new MagicCard("id2"));

        CurrentSelectionRepository repository = new CurrentSelectionRepository(mCardApi, mCardDao, mSetDao);

        assertNull(repository.getCurrentCards().getValue());

        repository.setCurrentCards(testCards);

        assertNotNull(repository.getCurrentCards().getValue());

        assertNull(repository.getSelectedCard().getValue());
        repository.selectCard("id1");
        assertNotNull(repository.getSelectedCard().getValue());
    }

    @Test
    public void testLoadingCards(){
        List<MagicCard> testMagicCards = new ArrayList<>();
        testMagicCards.add(new MagicCard("id1"));
        testMagicCards.add(new MagicCard("id2"));

        CardSearchCriteria criteria = new CardSearchCriteria();
        int pageNo = 1;

        String[] headers = new String[]{MagicCardApi.HEADER_RESP_COUNT, "50", MagicCardApi.HEADER_RESP_TOTAL_COUNT, "120"};
        Response networkResponse = Response.success(new MagicCardsResponse(testMagicCards), Headers.of(headers));

        when(mCardApi.getCardsCall(criteria.getCardName(), criteria.getColor(), criteria.getSetName(), criteria.getType(), pageNo)).thenReturn(new Call<MagicCardsResponse>() {
            @Override
            public Response<MagicCardsResponse> execute() {
                return null;
            }

            @Override
            public void enqueue(Callback<MagicCardsResponse> callback) {
                callback.onResponse(this, networkResponse);
            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<MagicCardsResponse> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        });

        CurrentSelectionRepository repository = new CurrentSelectionRepository(mCardApi, mCardDao, mSetDao);

        repository.getCurrentCards().observeForever(magicCards -> {
            assertNotNull(magicCards);
            assertEquals(magicCards.size(), testMagicCards.size());
        });

        repository.loadCards(criteria, pageNo);
    }
}
