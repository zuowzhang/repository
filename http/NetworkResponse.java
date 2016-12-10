package com.meizu.push.http;

import java.util.List;
import java.util.Map;

/**
 * Created by zbin on 16-11-12.
 */
class NetworkResponse {
    /** The HTTP status code. */
    int mStatusCode = -1;
    /** Raw data from this response. */
    byte[] mRawData;
    /** Response headers. */
    Map<String, List<String>> mHeaderMap;

    Throwable mThrowable;
}
