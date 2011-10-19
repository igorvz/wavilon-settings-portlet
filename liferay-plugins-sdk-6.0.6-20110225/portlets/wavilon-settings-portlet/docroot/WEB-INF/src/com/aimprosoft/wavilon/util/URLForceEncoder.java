package com.aimprosoft.wavilon.util;

import org.apache.catalina.util.URLEncoder;

import java.util.BitSet;

/**
 * Encoder, which similar to org.apache.catalina.util.URLEncoder, but it encodes all characters
 */
public class URLForceEncoder extends URLEncoder {
    public URLForceEncoder() {
        super();
        safeCharacters = new BitSet();
    }

    public BitSet getSafeCharacters(){
        return safeCharacters;
    }

    public void setSafeCharacters(BitSet safeCharacters){
        this.safeCharacters = safeCharacters;
    }
}
