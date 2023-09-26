/*
 * Copyright (c) 2023. http://t.me/mibal_ua
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.mibal.minervaTest.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class StringUtils {

    /**
     * Returns a string that is a substring of this original string with
     * appended {@code toAppend} string or original string if its length
     * less than {@code max}.
     * <p>
     * Examples:
     * <blockquote><pre>
     * substringAppend("hamburger", ".." , 5) returns "ham.."
     * substringAppend("cheeseburger", ".." , 20) returns "cheeseburger"
     * substringAppend("cheeseburger", ".." , 12) returns "cheeseburger"
     * substringAppend("coca-cola is the best!", ".." , 12) returns "coca-cola.." not "coca-cola .."
     * </pre></blockquote>
     *
     * @param str      the original string to operate.
     * @param toAppend string that have to append.
     * @return the specified substring.
     */
    public static String substringAppend(final String str, final String toAppend, final int max) {
        if (str.length() <= max) return str;
        return str.substring(0, max - toAppend.length()).trim() + toAppend;
    }

    public static String min(String name, int n) {
        return substringAppend(name, "..", n);
    }

    /**
     * Returns boolean value if {@code str} matches regexp. Regexp {@code "^[0-9]+$"}
     * checks is this string a positive integer number.
     *
     * @param str the original string
     * @return {@code true} if string matches regexp,
     *         {@code false} if string doesn't match regexp
     */
    public static boolean isNumber(String str) {
        return str.trim().matches("^[0-9]+$");
    }

    public static List<String> divideStrToLines(String str, int lineLength) {
        if (lineLength < 2)
            throw new IllegalArgumentException("Parameter lineLength must be greater than 1. Actual is " + lineLength);

        if (str.contains("\n"))
            return Arrays.stream(str.split("\n"))
                    .flatMap(string -> divideStrToLines(string, lineLength).stream())
                    .toList();

        if (str.length() < lineLength)
            return List.of(str);

        return splitIntoLines(str, lineLength);
    }

    private static List<String> splitIntoLines(String str, int lineLength) {
        List<String> result = new ArrayList<>();

        int beginIndex = 0;
        int lastIndexOfSpace = -1;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                lastIndexOfSpace = i;
            }

            if ((i - beginIndex) / lineLength > 0 && (i - beginIndex) % lineLength == 0) {
                if (beginIndex > lastIndexOfSpace) {
                    int dividingIndex = beginIndex + lineLength - 1;
                    String operatedStr = str.substring(beginIndex, dividingIndex) + "-";
                    result.add(operatedStr);
                    beginIndex = dividingIndex;
                } else {
                    result.add(str.substring(beginIndex, lastIndexOfSpace));
                    beginIndex = lastIndexOfSpace + 1;
                }
            }
        }
        result.add(str.substring(beginIndex));
        return result;
    }

}
