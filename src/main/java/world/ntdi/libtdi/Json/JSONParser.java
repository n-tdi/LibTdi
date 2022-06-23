package world.ntdi.libtdi.Json;

public class JSONParser {
    public JSONParser() {
    }

    public static JSONList parseList(String json) {
        return (JSONList)parse(json);
    }

    public static JSONMap parseMap(String json) {
        return (JSONMap)parse(json);
    }

    private static JSONStorage parse(String json) {
        Type parentType;
        switch (json.charAt(0)) {
            case '[':
                parentType = JSONParser.Type.LIST;
                break;
            case '{':
                parentType = JSONParser.Type.MAP;
                break;
            default:
                throw new IllegalArgumentException("Invalid JSON input");
        }

        boolean quoted = false;
        JSONStorage currentParent = parentType == JSONParser.Type.LIST ? new JSONList() : new JSONMap();
        int cursor = 1;
        int lastChar = -1;
        boolean end = false;
        String key = null;
        Type type = JSONParser.Type.INT;
        char[] chars = json.toCharArray();

        for(int i = 1; i < json.length(); ++i) {
            Object prev;
            switch (chars[i]) {
                case '\t':
                case '\n':
                case '\r':
                case ' ':
                    if (!quoted && lastChar == -1) {
                        cursor = i + 1;
                    }
                    break;
                case '"':
                    quoted = !quoted;
                    lastChar = i;
                    type = JSONParser.Type.STRING;
                    break;
                case '.':
                    if (!quoted) {
                        type = JSONParser.Type.DOUBLE;
                    }

                    lastChar = i;
                    break;
                case ':':
                    if (!quoted) {
                        key = json.substring(cursor + 1, lastChar);
                        type = JSONParser.Type.INT;
                        cursor = i + 1;
                        lastChar = -1;
                    }
                    break;
                case '[':
                case '{':
                    if (!quoted) {
                        ((JSONStorage)currentParent).setTempKey(key == null ? "" : key);
                        key = null;
                        prev = chars[i] == '[' ? new JSONList() : new JSONMap();
                        ((JSONStorage)prev).setParent((JSONStorage)currentParent);
                        currentParent = (JSONStorage) prev;
                        cursor = i + 1;
                        lastChar = -1;
                    }
                    break;
                case '\\':
                    ++i;
                    lastChar = i;
                    break;
                case ']':
                case '}':
                    if (quoted) {
                        break;
                    }

                    end = true;
                case ',':
                    if (!quoted) {
                        if (lastChar != -1) {
                            prev = null;
                            switch (type) {
                                case STRING:
                                    prev = substring(chars, cursor + 1, lastChar);
                                    break;
                                case INT:
                                    prev = parseInteger(json.substring(cursor, lastChar + 1));
                                    break;
                                case DOUBLE:
                                    prev = parseDouble(json.substring(cursor, lastChar + 1));
                                    break;
                                case BOOLEAN:
                                    prev = chars[cursor] == 't';
                                    break;
                                case NULL:
                                    prev = null;
                            }

                            ((JSONStorage)currentParent).add(key, prev);
                            key = null;
                        } else {
                            switch (chars[i]) {
                                case ']':
                                case '}':
                                    end = true;
                                    break;
                                default:
                                    end = false;
                            }
                        }

                        type = JSONParser.Type.INT;
                        if (end) {
                            prev = currentParent;
                            currentParent = ((JSONStorage)currentParent).getParent();
                            parentType = currentParent instanceof JSONList ? JSONParser.Type.LIST : JSONParser.Type.MAP;
                            if (currentParent != null && ((JSONStorage)currentParent).getTempKey() != null) {
                                ((JSONStorage)currentParent).add(((JSONStorage)currentParent).getTempKey(), prev);
                                ((JSONStorage)currentParent).setTempKey((String)null);
                            }
                        }

                        lastChar = -1;
                        cursor = i + 1;
                        end = false;
                    }
                    break;
                case 'f':
                case 't':
                    if (!quoted) {
                        type = JSONParser.Type.BOOLEAN;
                    }
                    break;
                case 'n':
                    if (!quoted) {
                        type = JSONParser.Type.NULL;
                    }

                    lastChar = i;
                    break;
                default:
                    lastChar = i;
            }
        }

        return (JSONStorage)currentParent;
    }

    private static double parseDouble(String input) {
        int i = 0;
        boolean negative = false;
        if (input.charAt(0) == '-') {
            negative = true;
            ++i;
        }

        double output = 0.0;
        double after = 0.0;

        int decimal;
        for(decimal = -1; i < input.length(); ++i) {
            char c = input.charAt(i);
            if (c == '.') {
                if (decimal != -1) {
                    throw new NumberFormatException("Second period in double");
                }

                decimal = i;
            } else {
                if (c > '9' || c < '0') {
                    throw new NumberFormatException("Non-numeric character");
                }

                if (decimal != -1) {
                    after *= 10.0;
                    after += (double)(c - 48);
                } else {
                    output *= 10.0;
                    output += (double)(c - 48);
                }
            }
        }

        after /= Math.pow(10.0, (double)(input.length() - decimal - 1));
        return negative ? -output - after : output + after;
    }

    private static Object parseInteger(String input) {
        int i = 0;
        boolean negative = false;
        if (input.charAt(0) == '-') {
            negative = true;
            ++i;
        }

        long output;
        for(output = 0L; i < input.length(); ++i) {
            char c = input.charAt(i);
            if (c != 'L') {
                if (c > '9' || c < '0') {
                    throw new NumberFormatException("Non-numeric character");
                }

                output *= 10L;
                output += (long)(c - 48);
            }
        }

        output = negative ? -output : output;
        if (output <= 2147483647L && output >= -2147483648L) {
            return (int)output;
        } else {
            return output;
        }
    }

    private static String substring(char[] chars, int start, int end) {
        StringBuilder builder = new StringBuilder();

        for(int i = start; i < end; ++i) {
            char c = chars[i];
            if (c == '\\') {
                builder.append(chars[i + 1]);
                ++i;
            } else {
                builder.append(c);
            }
        }

        return builder.toString();
    }

    private static enum Type {
        LIST,
        MAP,
        STRING,
        BOOLEAN,
        DOUBLE,
        INT,
        NULL;

        private Type() {
        }
    }
}

