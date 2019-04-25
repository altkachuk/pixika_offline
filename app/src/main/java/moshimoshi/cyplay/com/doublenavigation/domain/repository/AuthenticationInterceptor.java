package moshimoshi.cyplay.com.doublenavigation.domain.repository;

import android.util.Base64;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.utils.ClientUtil;

/**
 * Created by andre on 17-Apr-18.
 */

public class AuthenticationInterceptor implements Interceptor {

    private APIValue apiValue;

    public AuthenticationInterceptor(APIValue apiValue) {
        this.apiValue = apiValue;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // concatenate username and password with colon for authentication
        String credentials = apiValue.getUsername() + ":" + apiValue.getPassword();
        // create Base64 encodet string
        String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Request originalRequest = chain.request();

        Request authorisedRequest = originalRequest.newBuilder()
                .header("Authorization", basic)
                .build();
        return chain.proceed(authorisedRequest);
    }
}
