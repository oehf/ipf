package org.openehealth.ipf.commons.map;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Fails rather than reads on past the limit, so that the parser downstream never sees more
 * than "limit" bytes however much the other end is willing to send.
 * <p>
 * Replacement of Commons IO's BoundedInputStream: that one ends the stream at the limit by
 * default, handing the parser a truncated document that may still parse. It would be
 * this module's only runtime dependency.
 */
final class BoundedInputStream extends FilterInputStream {

    private final long limit;
    private long read;

    BoundedInputStream(InputStream in, long limit) {
        super(in);
        this.limit = limit;
    }

    @Override
    public int read() throws IOException {
        var b = super.read();
        if (b >= 0) {
            count(1);
        }
        return b;
    }

    @Override
    public int read(byte[] buffer, int offset, int length) throws IOException {
        var n = super.read(buffer, offset, length);
        if (n > 0) {
            count(n);
        }
        return n;
    }

    private void count(int n) throws IOException {
        read += n;
        if (read > limit) {
            throw new IOException("mapping source is larger than the " + limit
                + " bytes a mapping file may be");
        }
    }
}
