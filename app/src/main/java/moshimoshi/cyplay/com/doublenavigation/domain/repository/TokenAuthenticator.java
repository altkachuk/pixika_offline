package moshimoshi.cyplay.com.doublenavigation.domain.repository;

import android.util.Base64;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

import ninja.cyplay.com.apilibrary.domain.repositoryModel.requests.AccessTokenRequest;
import ninja.cyplay.com.apilibrary.models.business.OAuth2Credentials;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.responses.ResourceResponse;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.utils.ActivityHelper;
import ninja.cyplay.com.apilibrary.utils.ClientUtil;

/**
 * Created by andre on 28-Mar-18.
 */

public class TokenAuthenticator implements Authenticator {

    private APIValue apiValue;

    public TokenAuthenticator(APIValue apiValue) {
        this.apiValue = apiValue;
    }

    @Override
    public Request authenticate(Proxy proxy, Response response) throws IOException {
        // concatenate username and password with colon for authentication
        String credentials = apiValue.getUsername() + ":" + apiValue.getPassword();
        // create Base64 encodet string
        String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return response.request().newBuilder().
                    header("Authorization", basic).
                    build();
    }

    @Override
    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        // Null indicates no attempt to authenticate.
        return null;
    }
}
