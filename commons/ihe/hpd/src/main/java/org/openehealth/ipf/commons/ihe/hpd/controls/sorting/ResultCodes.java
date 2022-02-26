/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hpd.controls.sorting;

/**
 * @author Dmytro Rud
 * @since 3.7.5
 */
public class ResultCodes {

    // results are sorted
    public static final int SUCCESS = 0;
    // server internal failure
    public static final int OPERATIONS_ERROR = 1;
    // timelimit reached before sorting was completed
    public static final int TIME_LIMIT_EXCEEDED = 3;
    // refused to return sorted results via insecure protocol
    public static final int STRONG_AUTH_REQUIRED = 8;
    // too many matching entries for the server to sort
    public static final int ADMIN_TIME_EXCEEDED = 11;
    // unrecognized attribute type in sort key
    public static final int NO_SUCH_ATTRIBUTE = 16;
    // unrecognized or inappropriate matching rule in sort key
    public static final int INAPPROPRIATE_MATCHING = 18;
    // refused to return sorted results to this client
    public static final int INSUFFICIENT_ACCESS_RIGHTS = 50;
    // too busy to process
    public static final int BUSY = 51;
    // unable to sort
    public static final int UNWILLING_TO_PERFORM = 53;
    // other
    public static final int OTHER = 80;

}
