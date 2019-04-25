package moshimoshi.cyplay.com.doublenavigation.utils;

import android.util.Base64;
import android.util.Log;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;

import java.io.UnsupportedEncodingException;

/**
 * Created by andre on 24-Mar-19.
 */

public class PBKDF2Hash {

    private String algorithm = "pbkdf2_sha256";
    private int iterations = 10000;
    private String salt;
    private String strHash;

    public PBKDF2Hash(String djangoHashString) {
        String[] s = djangoHashString.split("\\$");
        algorithm = s[0];
        iterations = Integer.parseInt(s[1]);
        salt = s[2];
        strHash = s[3];
    }

    public String getEncodedHash(String password) {
        // Returns only the last part of whole encoded password
        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA256Digest());
        try {
            gen.init(password.getBytes("UTF-8"), salt.getBytes(), iterations);
        } catch (UnsupportedEncodingException ex) {
            Log.d("Error", ex.getMessage());
        }
        byte[] dk = ((KeyParameter) gen.generateDerivedParameters(256)).getKey();

        byte[] hashBase64 = Base64.encode(dk, Base64.NO_WRAP);
        return new String(hashBase64);
    }

    public boolean checkString(String text) {
        String hash = getEncodedHash(text);

        return hash.equals(strHash);
    }
}
