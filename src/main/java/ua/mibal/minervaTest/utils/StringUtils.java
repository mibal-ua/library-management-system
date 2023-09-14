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

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class StringUtils {

    public static String substring(final String str, final int max) {
        if (str.length() < max) {
            return str;
        }
        return str.substring(0, max);
    }

    /**
     * Returns a string that is a substring of this original string with
     * appended {@code toAppend} string or original string if its length
     * less than {@code max}.
     * <p>
     * Examples:
     * <blockquote><pre>
     * substringAppend("hamburger", ".." , 5) returns "ham.."
     * substringAppend("cheeseburger", ".." , 20) returns "cheeseburger"
     * </pre></blockquote>
     *
     * @param str      the original string to operate.
     * @param toAppend string that have to append.
     * @return the specified substring.
     */
    public static String substringAppend(final String str, final String toAppend, final int max) {
        if (str.length() < max) {
            return str;
        }
        return str.substring(0, max - toAppend.length()) + toAppend;
    }

    public static String min(String name, int n) {
        // TODO
        return null;
    }
}
