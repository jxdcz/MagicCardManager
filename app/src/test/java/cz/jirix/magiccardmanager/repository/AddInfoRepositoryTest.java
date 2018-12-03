package cz.jirix.magiccardmanager.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.jirix.magiccardmanager.BaseTest;
import cz.jirix.magiccardmanager.BuildConfig;
import cz.jirix.magiccardmanager.model.MagicColor;
import cz.jirix.magiccardmanager.model.MagicRarity;
import cz.jirix.magiccardmanager.persistence.MagicSetDao;
import cz.jirix.magiccardmanager.persistence.MagicTypeDao;
import cz.jirix.magiccardmanager.webservices.MagicCardApi;
import cz.jirix.magiccardmanager.webservices.MagicTypesResponse;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class AddInfoRepositoryTest extends BaseTest{

    private MagicCardApi mCardApi;
    private MagicSetDao mSetDao;
    private MagicTypeDao mTypeDao;

    @Before
    public void prepare() {
        mCardApi = mock(MagicCardApi.class);
        mSetDao = mock(MagicSetDao.class);
        mTypeDao = mock(MagicTypeDao.class);

        makeRxSync();
    }

    @After
    public void finish(){
        resetRxSync();
    }

    @Test
    public void testCreation() {
        AddInfoRepository repository = new AddInfoRepository(mCardApi, mSetDao, mTypeDao);

        List<MagicColor> colors = repository.getLiveColors().getValue();
        assertNotNull(colors);
        assertTrue(colors.size() > 4);

        List<MagicRarity> rarities = repository.getRarities().getValue();
        assertNotNull(rarities);
        assertTrue(rarities.size() > 4);
    }

    @Test
    public void testWebserviceLoad() {
        List<String> testMagicTypes = new ArrayList<>();
        testMagicTypes.add("test1");
        testMagicTypes.add("test2");

        when(mCardApi.getTypesCall()).thenReturn(new Call<MagicTypesResponse>() {
            @Override
            public Response<MagicTypesResponse> execute() {
                return null;
            }

            @Override
            public void enqueue(Callback<MagicTypesResponse> callback) {
                callback.onResponse(this, Response.success(200, new MagicTypesResponse(testMagicTypes)));
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
            public Call<MagicTypesResponse> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        });

        AddInfoRepository repository = new AddInfoRepository(mCardApi, mSetDao, mTypeDao);

        repository.getLiveTypes().observeForever(magicTypes -> {
            assertNotNull(magicTypes);
            assertEquals(magicTypes.size(), testMagicTypes.size());
        });

        repository.loadTypesFromWebservice();
    }

    private String loadResourceJSON(String fileName) throws IOException {
        //TODO wanted to use mockserver
        URL resource = getClass().getClassLoader().getResource(fileName);
        InputStream is = (BufferedInputStream) resource.getContent();
        StringBuilder SB = new StringBuilder();
        byte[] buffer = new byte[255];
        while (is.read(buffer) >= 0) {
            SB.append(new String(buffer));
        }
        return SB.toString();
    }
}
