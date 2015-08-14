package de.lusiardi.proxy.writers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Converts a byte[] into the canonical hex+ASCII display of hexdump.
 *
 * @author Joachim Lusiardi
 */
public class HexDump {

    private static final int width = 16;

    /**
     * Converts a byte[] into the canonical hex+ASCII display of hexdump.
     *
     * @param in the byte array to print
     * @param indentation a string to prepend every line
     * @return a string holding the output
     */
    public String write(byte[] in, String indentation) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream(in.length);
        try {
            bao.write(in);
        } catch (IOException e) {
        }
        String result = "";
        int i = 0;
        if (in.length == 0) {
            return indentation + "\n";
        }
        do {
            result += indentation;
            result += String.format("%08x  ", i);
            byte[] line = new byte[width];
            do {
                result += String.format("%02x ", in[i]);
                byte b = in[i];
                if (b > 31 && b < 127) {
                    line[i % width] = in[i];
                } else {
                    line[i % width] = '.';
                }
                i++;
                if (i % 8 == 0) {
                    result += " ";
                }
                if (i >= in.length && i % 16 != 0) {
                    for (int fillup = i % width; fillup < width; fillup++) {
                        result += "   ";
                        if ((i + fillup) % 8 == 0) {
                            result += " ";
                        }
                    }
                    break;
                }
            } while (i % width != 0);
            result += "|";
            try {
                result += (new String(line, "ISO-8859-1"));
            } catch (UnsupportedEncodingException ex) {
                result += "unsupported charset";
            }
            result += "\n";
        } while (i < in.length);
        try {
            return bao.toString("UTF8");
        } catch (UnsupportedEncodingException e) {
            return result;
        }
    }
}
