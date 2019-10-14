package login;

import com.neopharma.datavault.activity.BaseActivity;
import com.neopharma.datavault.activity.InitialActivity;
import com.neopharma.datavault.data.remote.API;
import com.neopharma.datavault.fragments.viewmodel.LoginViewModel;
import com.neopharma.datavault.model.response.LoginResponse;
import com.neopharma.datavault.navigation.Navigator;
import com.neopharma.datavault.repository.Repository;
import com.neopharma.datavault.utility.Store;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.schedulers.TestScheduler;

import static org.mockito.BDDMockito.given;

//@RunWith(MockitoJUnitRunner.Silent.class)
@RunWith(MockitoJUnitRunner.class)
public class LoginTest {

    LoginViewModel loginViewModel;
    @Mock
    LoginResponse loginResponse;

    @Mock
    InitialActivity initialActivity;

    @Mock
    BaseActivity baseActivity;

    @Mock
    API api;
    Navigator navigator;
    @Mock
    private Repository repo;
    private TestScheduler testScheduler = new TestScheduler();

    @Before
    public void setup() {
        loginViewModel = new LoginViewModel(repo);
        navigator = new Navigator();
        navigator.init(baseActivity);
    }

    @Test
    public void testLogin() {
        Store.isNetworkAvailable = true;
        loginViewModel.setUsername("supervisor");
        loginViewModel.setPassword("supervisor@dv");
        loginViewModel.doLogin();
        given(baseActivity.validResponse(loginResponse)).willReturn(true);
        baseActivity.validResponse(loginResponse);
        //baseActivity.setNavigator(navigator);
        testScheduler.triggerActions();
        (initialActivity).actionToDataFetchFragment();

    }

    @Test
    public void loginFailureTest() {
        Store.isNetworkAvailable = false;
        loginViewModel.setUsername("supervisor");
        loginViewModel.setPassword("supervisor@dv");
        loginViewModel.doLogin();
        given(initialActivity.validResponse(loginResponse)).willReturn(false);
        initialActivity.validResponse(loginResponse);
        testScheduler.triggerActions();
        //then(baseActivity).should().hideProgressBar();
    }


    @Test
    public void testWithNullUserName() {
        Store.isNetworkAvailable = true;
        loginViewModel.doLogin();
    }
}
