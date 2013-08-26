package de.lusiardi.soa.proxy.parsers;

import de.lusiardi.soa.proxy.data.HttpBodyChunk;
import de.lusiardi.soa.proxy.stream.HttpInputStream;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author shing19m
 */
public class HttpBodyChunkParser {

    private static Logger logger = Logger.getLogger(HttpBodyChunkParser.class);

    public HttpBodyChunk parse(HttpInputStream bufferedReader) throws IOException {
        HttpBodyChunk result = new HttpBodyChunk();

        String line = bufferedReader.readNonEmptyLine();
        logger.debug("line '" + line + "'");

        String[] parts = line.split(";");
        logger.debug("#parts '" + parts.length + "'");

        int size = Integer.parseInt(parts[0], 16);
        result.setSize(size);
        if (size == 0) {
            logger.debug("found last-chunk '" + line + "'");
            return result;
        }

        byte[] data = new byte[size];
        bufferedReader.readBytes(data, 0, size);

        logger.debug("chunksize '" + size + "'");
        logger.debug("data '" + new String(data) + "'");
        result.setData(data);

        return result;
    }
}
