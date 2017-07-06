package com.devtonix.amerricard.utils.AmazonUtils;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class GetS3Object {

    private static final String REGION_NAME = "us-west-2";
    private static final String ACCESS_KEY = "AKIAIT4TMENYJGROYMCA";
    private static final String SECRET_KEY = "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";

    public static GlideUrl fromS3toUrl(String url) {
        URL endpointUrl;
        try {
            endpointUrl = new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("x-amz-content-sha256", AWS4SignerBase.EMPTY_BODY_SHA256);
        
        AWS4SignerForAuthorizationHeader signer = new AWS4SignerForAuthorizationHeader(
                endpointUrl, "GET", "s3", REGION_NAME);
        String authorization = signer.computeSignature(headers, 
                                                       null,
                                                       AWS4SignerBase.EMPTY_BODY_SHA256, 
                                                       ACCESS_KEY,
                                                       SECRET_KEY);
                
        headers.put("Authorization", authorization);

        LazyHeaders.Builder builder = new LazyHeaders.Builder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        return new GlideUrl(endpointUrl,  builder.build());
    }
}
