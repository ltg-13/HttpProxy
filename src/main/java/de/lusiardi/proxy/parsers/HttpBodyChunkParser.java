package de.lusiardi.proxy.parsers;

import de.lusiardi.proxy.data.HttpBodyChunk;
import de.lusiardi.proxy.exceptions.ChunkParseException;
import de.lusiardi.proxy.exceptions.NotEnoughBytesAvailableException;
import de.lusiardi.proxy.stream.HttpInputStream;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Parser for chunks in chunked transfer encoding as defined in <a
 * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.6.1">RFC
 * 2616</a>.
 *
 * @author Joachim Lusiardi
 */
public class HttpBodyChunkParser {

    private static Logger logger = Logger.getLogger(HttpBodyChunkParser.class);

    /**
     * Tries to parse a chunk from the given input stream.
     *
     * @param inputStream the stream to read from
     * @return the parsed chunk
     * @throws ChunkParseException on any issue while parsing the chunk
     */
    public HttpBodyChunk parse(HttpInputStream inputStream) throws ChunkParseException {
        String chunksLength = "";
        try {
            HttpBodyChunk result = new HttpBodyChunk();

            String line = inputStream.readNonEmptyLine();
            logger.debug("line '" + line + "'");

            String[] parts = line.split(";");
            logger.debug("#parts '" + parts.length + "'");

            chunksLength = parts[0];
            int size = Integer.parseInt(chunksLength, 16);
            result.setSize(size);
            if (size == 0) {
                logger.debug("found last-chunk '" + line + "'");
                return result;
            }

            byte[] data = new byte[size];
            inputStream.readFixedNumberOfBytes(data, size);

            logger.debug("chunksize '" + size + "' ('0x" + parts[0] + ")'");
            result.setData(data);

            return result;
        } catch (IOException ex) {
            throw new ChunkParseException("Problem while reading data from input stream", ex);
        } catch (NotEnoughBytesAvailableException ex) {
            throw new ChunkParseException("Problem while reading the fixed amout of bytes from input stream", ex);
        } catch (NumberFormatException ex) {
            throw new ChunkParseException("Problem while parsing the chunks length from '" + chunksLength + "'", ex);
        }
    }
}
